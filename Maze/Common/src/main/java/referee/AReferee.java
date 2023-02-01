package referee;

import components.ALabyrinthRules;
import components.ARefereeState;
import components.Board;
import components.Connector;
import components.Coordinate;
import components.Gem;
import components.Move;
import components.PlayerInfo;
import components.PublicState;
import components.Tile;
import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import players.IPlayer;
import players.Observer;

// Abstract class to include common functionality between single and multi goal referee.

// This class represents the referee if a Labyrinth game. It takes in a list of IPlayers,
// and either runs the game to completion, or, for testing purposes, runs the game to
// completion from a starting state. That said, the method that does not take in an existing state
// will create a state and call the method to run it from this state.

// When a player gets removed, the referee kicks the player from the gameState and terminates
// all connection with it. This means that once a player gives an invalid move,
// they will no longer receive any information from the referee.
// If every player gets removed, the game ends.

public abstract class AReferee {

  //This global variable carries players from beginning of the game
  //this list is only used in the set up of the game and players do not get kicked from this list
  List<IPlayer> players;
  //maps a unique color to an IPlayer
  protected Map<Color, IPlayer> avatarToPlayerMap;
  List<IPlayer> winnerList;
  List<IPlayer> cheaterList;
  List<players.Observer> observer;
  //This represents how long in ms a referee is willing to wait for a players move
  private final int delayTolerance;
  //Represents the rules of this labyrinth game.
  protected final ALabyrinthRules rules;

  // This constructor throws an error if there are no players in the list.
  //This ctor sets tolerance to 5s
  public AReferee(List<IPlayer> players, ALabyrinthRules rules) throws IllegalArgumentException {
    this(players, 5000, rules);
  }

  // This constructor throws an error if there are no players in the list.
  public AReferee(List<IPlayer> players, int delayTolerance, ALabyrinthRules rules) throws IllegalArgumentException {
    if(players.size() == 0) {
      throw new IllegalArgumentException("List of players must be greater than 0");
    }
    this.players = players;
    this.winnerList = new ArrayList<>();
    this.cheaterList = new ArrayList<>();
    this.avatarToPlayerMap = new HashMap<>();
    this.observer = new ArrayList<>();
    this.delayTolerance = delayTolerance;
    this.rules = rules;
  }


  // Adds an observer to this referee.
  public void addObserver(players.Observer o) {
    this.observer.add(o);
  }

  // Plays a game to completion and returns a pair of lists of players. The first item in the
  // pair is the list of winners, while the second item in the pair is the list of players that
  // misbehaved.
  public SimpleEntry<List<IPlayer>, List<IPlayer>> playGame() {
    this.winnerList = new ArrayList<>();
    this.cheaterList = new ArrayList<>();
    SimpleEntry<Board, Tile> boardAndSpare = createRandomBoard(7, 7);
    Board board = boardAndSpare.getKey();
    Tile spare = boardAndSpare.getValue();
    ARefereeState singleGoalRefereeState = initializeRefereeState(board, spare);
    return playGame(singleGoalRefereeState);
  }





  // Initializes the RefereeState and the avatarToPlayerMap.
  // Determines home and goal coordinates for every IPlayer.
  abstract protected ARefereeState initializeRefereeState(Board board, Tile spare);


  //Right now this only supports 9 colors. We will add more colors as necessary.
  protected Queue<Color> getPossibleColors() {
    Color purple = Color.decode("#850A95");
    Queue<Color> colors = new ArrayDeque<>();
    colors.add(purple);
    colors.add(Color.BLUE);
    colors.add(Color.YELLOW);
    colors.add(Color.GREEN);
    colors.add(Color.WHITE);
    colors.add(Color.BLACK);
    colors.add(Color.RED);
    colors.add(Color.PINK);
    colors.add(Color.ORANGE);
    return colors;
  }

  // Creates a random board with unique tiles.
  // The spare tile is also guaranteed to be unique.
  // There is a limit to how big this can be before tiles stop being unique but this will work
  // for all 7x7 boards.
  // If called multiple times with the same dimensions, the gems will be the exact same but
  // the connectors will be randomized.
  // todo: This will create duplicates in terms of gems.
  private SimpleEntry<Board, Tile> createRandomBoard(int rows, int columns) {
    String[] gemList = Gem.ALL_VALID_GEM_NAMES;
    Tile[][] tiles = new Tile[rows][columns];
    for (int i = 0; i < rows*columns; i += 1) {
      Connector connector = Connector.getRandomConnector(new Random());
      Tile curTile = new Tile(new Gem(gemList[i / gemList.length]),
          new Gem(gemList[i % gemList.length]), connector);
      tiles[i / columns][i % columns] = curTile;
    }
    Tile t =  new Tile(new Gem(gemList[rows*columns / gemList.length]),
        new Gem(gemList[rows*columns % gemList.length]), Connector.getRandomConnector(new Random()));
    return new SimpleEntry<>(new Board(tiles), t);
  }

  // Plays a game to completion from the given RefereeState and returns a pair of lists of players.
  // The first item in the pair is the list of winners, while the second item in the pair is the
  // list of players that misbehaved.
  // This method is more of a testing method, so although there are various ways this can break,
  // we don't check for them. :)
  public SimpleEntry<List<IPlayer>, List<IPlayer>> playGame(ARefereeState s) {
    this.winnerList = new ArrayList<>();
    this.cheaterList = new ArrayList<>();
    setUpAvatarToPlayerMap(s);
    sendPlayersSetup(s);
    sendObserverState(s);
    return runFromInitialState(s);
  }

  // Sets up a mapping from the info of the players in the game to the actual players. This
  // method assumes that the players in this class and the information of the players from the
  // state are in the same order and are the same length.
  private void setUpAvatarToPlayerMap(ARefereeState s) {
    List<PlayerInfo> playerInfoList = new ArrayList<>(s.publicState.getPlayers());
    for (int i = 0; i < this.players.size(); i += 1) {
      PlayerInfo curPlayerInfo = playerInfoList.get(i);
      avatarToPlayerMap.put(curPlayerInfo.avatar, this.players.get(i));
    }
  }

  // Calls setup() on every IPlayer, giving the initial state and each player's goal.
  private void sendPlayersSetup(ARefereeState refereeState) {
    List<PlayerInfo> lsOfPInfo =  new ArrayList<>(refereeState.publicState.getPlayers());
    for(PlayerInfo playerInfo : lsOfPInfo) {

      IPlayer player = avatarToPlayerMap.get(playerInfo.avatar);
      Coordinate goal = refereeState.infoMap.get(playerInfo.avatar).getGoal();

      boolean acceptedSetup = safeSetUp(player, Optional.of(new PublicState(refereeState.publicState)), goal);
      if (!acceptedSetup) {
        kickNonActivePlayer(player, playerInfo, refereeState);
      }
    }
  }

  /**
   * Kicks given player that is not necessarily active using avatar.
   */
  private void kickNonActivePlayer(IPlayer p, PlayerInfo pi, ARefereeState s) {
    s.kickNonActivePlayer(pi);
    cheaterList.add(p);
    this.avatarToPlayerMap.remove(pi.avatar);
  }

  // Sends the observers the state of the game
  protected abstract void sendObserverState(ARefereeState refereeState);

  /**
   * Maps all players color to their name
   */
  protected Map<Color, String> getColorToNameMap() {
    Map<Color, String> colorToNameMap = new HashMap<>();
    for (Map.Entry<Color, IPlayer> entry: this.avatarToPlayerMap.entrySet()) {
      colorToNameMap.put(entry.getKey(), entry.getValue().name());
    }
    return colorToNameMap;
  }

  //Runs a game to completion given an initial state.
  //This method does not call any setup initially and will
  // immediately start from the first player's turn.
  protected abstract SimpleEntry<List<IPlayer>, List<IPlayer>> runFromInitialState(ARefereeState s);

  // This method plays 1 round of the Labyrinth game and returns the winners if there are any.
  // Otherwise, it returns an Optional.empty.
  // A round is defined as all players from the original first player (or the next player that
  // isn't kicked) to the original last player (or the previous player that isn't kicked) playing
  // one move.
  // This method executes a player turn, informs any observers of the state of the game after a
  // turn, and repeats until the round is over.
  protected Optional<PlayerInfo> playRound(ARefereeState s) {
    Optional<PlayerInfo> winner = Optional.empty();
    do {
      Optional<PlayerInfo> maybeWinner = playPlayerTurn(s);
      sendObserverState(s);
      if(maybeWinner.isPresent()) {
        winner = maybeWinner;
        break;
      }
    } while (!s.publicState.hasRoundJustEnded());
    return winner;
  }

  /**
   * Handles the active player taking a turn. If move is invalid, user is kicked from game and
   * added to the cheaters list, otherwise its move is handled
   * @param s - referee state
   * @return The active PlayerInfo if they are the winners or an Optional.empty if the active
   * player did not win in round
   */
  private Optional<PlayerInfo> playPlayerTurn(ARefereeState s) {
    Optional<PlayerInfo> winner = Optional.empty();
    //Gets IPlayer from current player avatar and asks current player for their move
    Color curAvatar = s.publicState.getActivePlayer().avatar;
    IPlayer curPlayer = this.avatarToPlayerMap.get(curAvatar);
    Optional<Optional<Move>> maybeMove = safeTakeTurn(curPlayer, createRefereeStateCopy(s));
    //kicks player if move is invalid
    if (!maybeMove.isPresent()) {
      kickGivenActivePlayer(s, curAvatar, curPlayer);
    } else {
      Optional<Move> move = maybeMove.get();
      if (!ensureNotNull(move)) {
        kickGivenActivePlayer(s, curAvatar, curPlayer);
        //if move is valid, handles move or pass
      } else if (move.isPresent()) {
        //move is handled
        winner = handleMove(s, move, curAvatar, curPlayer);
      } else {
        //pass is handled
        handlePass(s, move);
      }
    }
    return winner;
  }

  // Method for creating a copy of a RefereeState
  protected abstract ARefereeState createRefereeStateCopy(ARefereeState original);

  /**
   * Attempts to get the current player's move.
   * @return returns one of (Optional.empty, Optional&lt;Move&gt;)
   * An Optional.empty indicates that the player's move was invalid, and needs to be kicked.
   * On the other hand, an Optional&lt;Move&gt; indicates a player move or pass.
   * Remember that for a player move, Optional.empty means a pass.
   * In this way, we are slightly overloading the definition of Optional.empty, hence why we are
   * thoroughly documenting this method.
   */
  // This is a defensive method as we are not sure how well curPlayer implemented takeTurn.
  // For now, we just catch all exceptions.
  private Optional<Optional<Move>> safeTakeTurn(IPlayer curPlayer, ARefereeState s) {
    PublicState copiedState = new PublicState(s.publicState);
    Callable<Optional<Move>> takeTurnCall = ()->curPlayer.takeTurn(copiedState);
    return safeCallGeneric(takeTurnCall);
  }

  // Safely runs a given callable and returns the output.
  private <T> Optional<T> safeCallGeneric(Callable<T> method) {
    FutureTask<T> future = new FutureTask<>(method);
    Thread t = new Thread(future);
    T output;
    try {
      t.start();
      output = future.get(this.delayTolerance, TimeUnit.MILLISECONDS);
    } catch (Exception | Error e) {
      return Optional.empty();
    } finally {
      future.cancel(true);
      t.stop();
    }
    if (output == null) {
      return Optional.empty();
    }
    return Optional.of(output);
  }

  //Adds the active player to the cheaterList and removes it from the game.
  protected void kickGivenActivePlayer(ARefereeState s, Color curAvatar, IPlayer curPlayer) {
    s.kickActivePlayer();
    cheaterList.add(curPlayer);
    this.avatarToPlayerMap.remove(curAvatar);
  }

  //Handles the active player's move.
  //Currently this method has to handle illegal moves
  //EFFECT: This method should mutate the state if the move is valid.
  protected abstract Optional<PlayerInfo> handleMove(ARefereeState s, Optional<Move> move,
      Color curAvatar, IPlayer curPlayer);

  //Handles the case that a player passes.
  // If a player passes, it will not be checked if it reached its goal or home Coordinate.
  private void handlePass(ARefereeState s, Optional<Move> move) {
    s.publicState.doTurn(move);
    s.publicState.goToNextActivePlayer();
  }

  /**
   * Safely deal with calling Won, returns true if player properly accepts the won.
   */
  private boolean safeWon(IPlayer curPlayer, boolean won) {
    Callable<Integer> wonCall = ()->{curPlayer.won(won); return 1;};
    Optional<Integer> result = safeCallGeneric(wonCall);
    return result.isPresent();
  }

  /**
   * Safely deal with calling setup, returns true if player properly accepts the setup.
   */
  protected boolean safeSetUp(IPlayer curPlayer, Optional<PublicState> optState, Coordinate goal) {
    if (optState.isPresent()) {
      optState = Optional.of(new PublicState(optState.get()));
    }
    Optional<PublicState> safeOptState = optState;
    Callable<Integer> setUpCall = ()->{curPlayer.setup(safeOptState, goal); return 1;};
    Optional<Integer> result = safeCallGeneric(setUpCall);
    return result.isPresent();

  }

  //returns true if the move is not null and won't cause any null pointer exceptions
  private boolean ensureNotNull(Optional<Move> move) {
    if (move == null) {return false;}
    if (move.isPresent()) {
      Move m = move.get();
      //TODO: make these enforced by the move class
      if (m.slide == null) {return false;}
      if (m.slide.getValue() == null) {return false;}
      if (m.target == null) {return false;}
    }
    return true;
  }


  // Handle the necessary processes knowing the active player has reached their goal.
  protected abstract void handleActivePlayerReachingGoal(Color curAvatar,
                                                         IPlayer curPlayer, ARefereeState s);

  //Returns true if the move is legal. Returns false otherwise.
  protected boolean kickIfIllegalMove(ARefereeState s, Optional<Move> move, Color curAvatar,
                                      IPlayer curPlayer) {
    if(!legalMove(s, move.get())) {
      kickGivenActivePlayer(s, curAvatar, curPlayer);
      return false;
    }
    return true;
  }

  //Returns true if the given move is a legal move in the given state. Return false otherwise.
  private boolean legalMove(ARefereeState s, Move move) {
    return ALabyrinthRules.canMove(move, new PublicState(s.publicState));
  }

  //Returns a list of winners judged by how close they are to its goal,
  // with the winner(s) being the closest.
  // In the case of ties, all players are winners.
  protected List<PlayerInfo> getPlayersClosestToGoal(List<PlayerInfo> players, ARefereeState s) {
    double minDist = Double.MAX_VALUE;
    List<PlayerInfo> minPlayers = new ArrayList<>();
    for(PlayerInfo player : players) {
      Coordinate goal = s.infoMap.get(player.avatar).getGoal();
      Double distance = player.currentPos.distanceBetween(goal);
      if(Math.abs(distance - minDist) < 0.00001d) {
        minPlayers.add(player);
      } else if (distance < minDist) {
        minPlayers.clear();
        minPlayers.add(player);
        minDist = distance;
      }
    }
    return minPlayers;
  }

  //TODO: split into two methods
  //Notifies all the player whether they have won or lost and finalizes the global winner/cheater list
  protected void notifyAndFinalizeWinnersAndCheaters(ARefereeState s, List<PlayerInfo> winners){
    List<PlayerInfo> allPlayers = new ArrayList<>(s.publicState.getPlayers());
    for (PlayerInfo playerInfo : allPlayers) {
      IPlayer player = avatarToPlayerMap.get(playerInfo.avatar);
      boolean playerWon = winners.contains(playerInfo);
      //player.won(playerWon);
      boolean handlesWon = safeWon(player, playerWon);

      if(playerWon && handlesWon) {
        winnerList.add(player);
      }
      else if (!handlesWon) {
        cheaterList.add(player);
      }
      //continue
    }
  }

  // Sends the observers the state of the game
  protected void sendObserverGameOver() {
    this.observer.forEach((Observer::informGameOver));
  }

  protected void sortWinnerAndCheaterLists() {
    this.winnerList.sort(Comparator.comparing(IPlayer::name));
    this.cheaterList.sort(Comparator.comparing(IPlayer::name));
  }

}
