package referee;

import components.ALabyrinthRules;
import components.ARefereeState;
import components.Coordinate;
import components.Move;
import components.SingleGoalLabyrinthRules;
import components.SingleGoalRefereePlayerInfo;
import components.Tile;
import java.awt.Color;
import java.util.*;

import components.Board;
import components.PlayerInfo;
import components.SingleGoalRefereeState;
import java.util.AbstractMap.SimpleEntry;
import players.IPlayer;

// This class represents the referee of a single goal Labyrinth game. Includes all functionality
// that is unique when running a single goal game.

// A game is completed if:
// - A player reaches its home after visiting its designated goal tile
// - All players that survive a round opt to pass
// - the referee has run 1000 rounds : when gameState.roundNum is equal to 1000, the game ends.
public class SingleGoalReferee extends AReferee {


  public SingleGoalReferee(List<IPlayer> players) throws IllegalArgumentException {
    super(players, new SingleGoalLabyrinthRules());
  }

  public SingleGoalReferee(List<IPlayer> players, int delayTolerance) throws IllegalArgumentException {
    super(players, delayTolerance, new SingleGoalLabyrinthRules());
  }



  // Initializes the RefereeState and the avatarToPlayerMap.
  // Determines home and goal coordinates for every IPlayer.
  @Override
  protected ARefereeState initializeRefereeState(Board board, Tile spare) {
    Queue<PlayerInfo> playerInfoQueue = new ArrayDeque<>();
    Map<Color, SingleGoalRefereePlayerInfo> infoMap = new HashMap<>();
    List<Coordinate> validCoordinates = ALabyrinthRules.getAllImmovableCoordinates(board);

    Queue<Color> possibleColors = getPossibleColors();
    for(IPlayer player : players) {
      Random rand = new Random();
      int randomHomeCoordIdx = rand.nextInt(validCoordinates.size());
      Coordinate playerHome = validCoordinates.remove(randomHomeCoordIdx);
      PlayerInfo pInfo = new PlayerInfo(possibleColors.poll(), playerHome);
      playerInfoQueue.add(pInfo);
      this.avatarToPlayerMap.put(pInfo.avatar, player);
      SingleGoalRefereePlayerInfo singleGoalRefereePlayerInfo =
          new SingleGoalRefereePlayerInfo(validCoordinates.get(rand.nextInt(validCoordinates.size())));
      infoMap.put(pInfo.avatar, singleGoalRefereePlayerInfo);
    }
    return new SingleGoalRefereeState(board, spare, playerInfoQueue, infoMap);
  }

  //Ensures the game is played with a single goal referee state
  public SimpleEntry<List<IPlayer>, List<IPlayer>> playGame(SingleGoalRefereeState s) {
    return super.playGame(s);
  }

  @Override
  protected SimpleEntry<List<IPlayer>, List<IPlayer>> runFromInitialState(ARefereeState s) {
    List<PlayerInfo> winners = new ArrayList<>();
    while (!ALabyrinthRules.isGameOverWithoutTerminator(s)) {
      //plays round, returns winner if there is one
      Optional<PlayerInfo> maybeWinner = playRound(s);
      if (maybeWinner.isPresent()) {
        //if theres a winner, marks it as the only winner and ends the game
        winners.add(maybeWinner.get());
        break;
      }
      s.publicState.incrementRound();
      if (ALabyrinthRules.isGameOverWithoutTerminator(s)) {
        //if game is over, calculates winners from state
        winners = calculateWinners(s);
        break;
      }
      s.publicState.resetSkips();
    }
    //game terminating actions
    notifyAndFinalizeWinnersAndCheaters(s, winners);
    sendObserverGameOver();
    sortWinnerAndCheaterLists();
    return new SimpleEntry<>(winnerList, cheaterList);
  }


  @Override
  protected void sendObserverState(ARefereeState refereeState) {
    this.observer.forEach((observer1 -> observer1.receiveState(new SingleGoalRefereeState((SingleGoalRefereeState) refereeState),
            getColorToNameMap())));
  }

  @Override
  protected ARefereeState createRefereeStateCopy(ARefereeState original) {
    return new SingleGoalRefereeState((SingleGoalRefereeState) original);
  }

  //Returns winners if there are any, otherwise returns an Optional.empty.
  //If the move is legal, it will be executed. If this move causes the active player to win,
  //The player will be returned. If this move causes the active player to
  //end up on their goal tile, it will call setup() with an empty state and their home Coordinate.
  //After this is completed, it will goToNextActivePlayer()
  @Override
  protected Optional<PlayerInfo> handleMove(ARefereeState s, Optional<Move> move, Color curAvatar,
                                            IPlayer curPlayer) {
    Optional<PlayerInfo> winner = Optional.empty();
    //Kicks active player if their move was illegal, returns Optional.empty
    if(!kickIfIllegalMove(s, move, curAvatar, curPlayer)) {
      return winner;
    }
    //executes move
    s.publicState.doTurn(move);
    //checks if active player won game
    if(this.rules.activePlayerEndedGame(s)){
      //sets active player as winner and ends game
      winner = Optional.of(s.publicState.getActivePlayer());
      return winner;
    }
    //checks if active player reached their goal
    if(s.isActivePlayerOnGoalTile()) {
      handleActivePlayerReachingGoal(curAvatar, curPlayer, s);
    }
    s.publicState.goToNextActivePlayer();
    return winner;
  }

  @Override
  protected void handleActivePlayerReachingGoal(Color curAvatar,
                                                IPlayer curPlayer, ARefereeState s) {
    SingleGoalRefereeState state = (SingleGoalRefereeState) s;
    state.updateActivePlayerHasReachedGoal();
    //sets current player home to be their new goal
    boolean acceptedSetup = safeSetUp(curPlayer, Optional.empty(), s.publicState.getActivePlayer().home);
    //if setup is not accepted, kicks the active player
    if (!acceptedSetup) {
      kickGivenActivePlayer(s, curAvatar, curPlayer); //will kick current player
    }
  }

  //Returns the list of winners.
  //If any player has reached their goal tile, potential candidates are players who have
  //reached their goal tile. If no player has reached their goal tile,
  // all players are potential candidates.
  protected List<PlayerInfo> calculateWinners(ARefereeState s) {
    List<PlayerInfo> players = new ArrayList<>(s.publicState.getPlayers());
    List<PlayerInfo> playersReachedGoal = new ArrayList<>();
    //checks if player has reached their goal, if true adds them to list
    for(PlayerInfo player : players) {
      SingleGoalRefereePlayerInfo playerInfo = (SingleGoalRefereePlayerInfo)s.infoMap.get(player.avatar);
      if(playerInfo.getHasReachedGoal()) {
        playersReachedGoal.add(player);
      }
    }
    List<PlayerInfo> winners;
    if(playersReachedGoal.size() == 0) {
      //if no player has reached their goal, calculate winner by distance to their goal
      winners = getPlayersClosestToGoal(players, s);
    } else {
      //calculate winner by finding the closest player to their home who have already reached their goal
      winners = getPlayersClosestToHome(playersReachedGoal);
    }
    return winners;
  }

  //Returns a list of all players closest to their home
  private List<PlayerInfo> getPlayersClosestToHome(List<PlayerInfo> players) {
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
    return minPlayers;
  }
}
