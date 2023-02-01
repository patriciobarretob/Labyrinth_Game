package players;

import components.Connector;
import components.Coordinate;
import components.Gem;
import components.RefereePlayerInfo;
import components.Tile;
import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import components.Board;
import components.LabyrinthRules;
import components.Move;
import components.PlayerInfo;
import components.PublicState;
import components.RefereeState;
import java.util.Queue;
import java.util.Random;

// This class represents the referee of a Labyrinth game. It takes in a list of IPlayers,
// and either runs the game to completion, or, for testing purposes, runs the game to
// completion from a starting state. That said, the method that does not take in an existing state
// will create a state and call the method to run it from this state.

// For now, the referee has functionality for removing the current player when they give an
// invalid move. In the future, they may have functionality for removing players when it's not
// their turn.
// When a player gets removed, the referee kicks the player from the gameState and terminates
// all connection with it. This means that once a player gives an invalid move,
// they will no longer receive any information from the referee.
// If every player gets removed, the game ends.

// A game is completed if:
// - A player reaches its home after visiting its designated goal tile
// - All players that survive a round opt to pass
// - the referee has run 1000 rounds : when gameState.roundNum is equal to 1000, the game ends.

public class Referee {
  List<IPlayer> players;
  private Map<Color, IPlayer> avatarToPlayerMap;
  List<IPlayer> winnerList;
  List<IPlayer> cheaterList;

  // This constructor throws an error if there are no players in the list.
  public Referee(List<IPlayer> players) throws IllegalArgumentException {
    if(players.size() == 0) {
      throw new IllegalArgumentException("List of players must be greater than 0");
    }
    this.players = players;
    this.winnerList = new ArrayList<>();
    this.cheaterList = new ArrayList<>();
    this.avatarToPlayerMap = new HashMap<>();
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
    RefereeState refereeState = getRefereeState(board, spare);
    sendPlayersSetup(refereeState);
    return playGame(refereeState);
  }

  // Calls setup() on every IPlayer, giving the initial state and each player's goal.
  private void sendPlayersSetup(RefereeState refereeState) {
    for(PlayerInfo playerInfo : refereeState.publicState.getPlayers()) {
      IPlayer player = avatarToPlayerMap.get(playerInfo.avatar);
      Coordinate goal = refereeState.infoMap.get(playerInfo.avatar).goal;
      player.setup(Optional.of(new PublicState(refereeState.publicState)), goal);
    }
  }

  // Initializes the RefereeState and the avatarToPlayerMap.
  // Determines home and goal coordinates for every IPlayer.
  private RefereeState getRefereeState(Board board, Tile spare) {
    Queue<PlayerInfo> playerInfoQueue = new ArrayDeque<>();
    Map<Color, RefereePlayerInfo> infoMap = new HashMap<>();
    List<Coordinate> validCoordinates = LabyrinthRules.getAllImmovableCoordinates(board);

    Queue<Color> possibleColors = getPossibleColors();
    for(IPlayer player : players) {
      Random rand = new Random();
      PlayerInfo pInfo = new PlayerInfo(possibleColors.poll(),
          validCoordinates.get(rand.nextInt(validCoordinates.size())));
      playerInfoQueue.add(pInfo);
      this.avatarToPlayerMap.put(pInfo.avatar, player);
      RefereePlayerInfo refereePlayerInfo =
          new RefereePlayerInfo(validCoordinates.get(rand.nextInt(validCoordinates.size())));
      infoMap.put(pInfo.avatar, refereePlayerInfo);
    }
    return new RefereeState(board, spare, playerInfoQueue, infoMap);
  }


  //Right now this only supports 9 colors. We will add more colors as necessary.
  private Queue<Color> getPossibleColors() {
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
  public SimpleEntry<List<IPlayer>, List<IPlayer>> playGame(RefereeState s) {
    this.winnerList = new ArrayList<>();
    this.cheaterList = new ArrayList<>();
    setUpAvatarToPlayerMap(s);
    return runFromInitialState(s);
  }

  // Sets up a mapping from the info of the players in the game to the actual players. This
  // method assumes that the players in this class and the information of the players from the
  // state are in the same order and are the same length.
  private void setUpAvatarToPlayerMap(RefereeState s) {
    List<PlayerInfo> playerInfoList = new ArrayList<>(s.publicState.getPlayers());
    for (int i = 0; i < this.players.size(); i += 1) {
      PlayerInfo curPlayerInfo = playerInfoList.get(i);
      avatarToPlayerMap.put(curPlayerInfo.avatar, this.players.get(i));
    }
  }

  //Runs a game to completion given an initial state.
  //This method does not call any setup initially and will
  // immediately start from the first player's turn.
  private SimpleEntry<List<IPlayer>, List<IPlayer>> runFromInitialState(RefereeState s) {
    List<PlayerInfo> winners = new ArrayList<>();
    while(true) {
      Color curAvatar = s.publicState.getActivePlayer().avatar;
      IPlayer curPlayer = this.avatarToPlayerMap.get(curAvatar);
      Optional<Move> move = curPlayer.takeTurn(new PublicState(s.publicState));
      if (!ensureNotNull(move)) {
        kickPlayer(s, curAvatar, curPlayer);
      } else if (move.isPresent()) {
        winners = handleMove(s, move, curAvatar, curPlayer);
      } else {
        handlePass(s, move);
      }
      if(winners.size() > 0) { break; }
      if(s.publicState.hasRoundJustEnded()) {
        s.publicState.incrementRound();
        if(LabyrinthRules.isGameOver(s)) {
          winners = updateWinners(s);
          break;
        }
      }
    }
    List<PlayerInfo> allPlayers = new ArrayList<>(s.publicState.getPlayers());
    for(PlayerInfo playerInfo: allPlayers) {
      IPlayer player = avatarToPlayerMap.get(playerInfo.avatar);
      player.won(winners.contains(playerInfo));
    }
    return new SimpleEntry<>(winnerList, cheaterList);
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


  //Returns winners if there are any, otherwise returns an empty list.
  //If the move is valid, it will be executed. If this move causes the active player to win,
  //The player will be added to the winnerList. If this move causes the active player to
  //end up on their goal tile, it will call setup() with an empty state and their home Coordinate.
  //After this is completed, it will goToNextActivePlayer()
  private List<PlayerInfo> handleMove(RefereeState s, Optional<Move> move, Color curAvatar,
  IPlayer curPlayer) {
    List<PlayerInfo> winners = new ArrayList<>();
    if(!kickIfInvalidMove(s, move, curAvatar, curPlayer)) {
      return winners;
    }
    s.publicState.doTurn(move);
    if(LabyrinthRules.activePlayerWon(s)){
      winnerList.add(curPlayer);
      winners.add(s.publicState.getActivePlayer());
      return winners;
    }
    if(s.isActivePlayerOnGoalTile()) {
      s.updateActivePlayerHasReachedGoal();
      curPlayer.setup(Optional.empty(), s.publicState.getActivePlayer().home);
    }
    s.publicState.goToNextActivePlayer();
    return winners;
  }
  //Returns true if the move is valid. Returns false otherwise.
  private boolean kickIfInvalidMove(RefereeState s, Optional<Move> move, Color curAvatar,
                                    IPlayer curPlayer) {
    if(!validMove(s, move.get())) {
      kickPlayer(s, curAvatar, curPlayer);
      return false;
    }
    return true;
  }

  //Adds the active player to the cheaterList and removes it from the game.
  private void kickPlayer(RefereeState s, Color curAvatar, IPlayer curPlayer) {
    s.kickActivePlayer();
    cheaterList.add(curPlayer);
    this.avatarToPlayerMap.remove(curAvatar);
  }

  //Handles the case that a player passes.
  // If a player passes, it will not be checked if it reached its goal or home Coordinate.
  private void handlePass(RefereeState s, Optional<Move> move) {
    s.publicState.doTurn(move);
    s.publicState.goToNextActivePlayer();
  }

  //Returns the list of winners.
  //If any player has reached their goal tile, potential candidates are players who have
  //reached their goal tile. If no player has reached their goal tile,
  // all players are potential candidates.
  private List<PlayerInfo> updateWinners(RefereeState s) {
    List<PlayerInfo> players = new ArrayList<>(s.publicState.getPlayers());
    List<PlayerInfo> playersReachedGoal = new ArrayList<>();
    for(PlayerInfo player : players) {
      if(s.infoMap.get(player.avatar).hasPlayerReachedGoal) {
        playersReachedGoal.add(player);
      }
    }
    List<PlayerInfo> winners;
    if(playersReachedGoal.size() == 0) {
      winners = updateWinnersByGoal(players, s);
    } else {
      winners = updateWinnersByHome(playersReachedGoal);
    }
    return winners;
  }

  //Returns a list of winners judged by how close they are to its goal,
  // with the winner(s) being the closest.
  // In the case of ties, all players are winners.
  private List<PlayerInfo> updateWinnersByGoal(List<PlayerInfo> players, RefereeState s) {
    double minDist = Double.MAX_VALUE;
    List<PlayerInfo> minPlayers = new ArrayList<>();
    for(PlayerInfo player : players) {
      Coordinate goal = s.infoMap.get(player.avatar).goal;
      Double distance = player.currentPos.distanceBetween(goal);
      if(Math.abs(distance - minDist) < 0.00001d) {
        minPlayers.add(player);
      } else if (distance < minDist) {
        minPlayers.clear();
        minPlayers.add(player);
        minDist = distance;
      }
    }
    for(PlayerInfo minPlayer : minPlayers) {
      winnerList.add(avatarToPlayerMap.get(minPlayer.avatar));
    }
    return minPlayers;
  }

  //Returns a list of winners judged by how close they are to its home,
  // with the winner(s) being the closest.
  // In the case of ties, all players are winners.
  private List<PlayerInfo> updateWinnersByHome(List<PlayerInfo> players) {
    double minDist = Double.MAX_VALUE;
    List<PlayerInfo> minPlayers = new ArrayList<>();
    for(PlayerInfo player : players) {
      Double distance = player.currentPos.distanceBetween(player.home);
      if(Math.abs(distance - minDist) < 0.00001d) {
        minPlayers.add(player);
      } else if (distance < minDist) {
        minPlayers.clear();
        minPlayers.add(player);
        minDist = distance;
      }
    }
    for(PlayerInfo minPlayer : minPlayers) {
      winnerList.add(avatarToPlayerMap.get(minPlayer.avatar));
    }
    return minPlayers;
  }

  //Returns true if the given move is a valid move in the given state. Return false otherwise.
  private boolean validMove(RefereeState s, Move move) {
    return LabyrinthRules.canMove(move, new PublicState(s.publicState));
  }
}
