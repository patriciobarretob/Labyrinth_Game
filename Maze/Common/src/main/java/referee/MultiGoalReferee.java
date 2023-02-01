package referee;

import components.ALabyrinthRules;
import components.ARefereeState;
import components.Board;
import components.Coordinate;
import components.Move;
import components.MultiGoalLabyrinthRules;
import components.MultiGoalRefereePlayerInfo;
import components.MultiGoalRefereeState;
import components.PlayerInfo;
import components.Tile;

import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import players.IPlayer;

// This class represents the referee of a multi goal Labyrinth game. Includes all functionality
// that is unique when running a multi goal game.

// A game is completed if:
// - A player reaches its home if its current destination and there are no more goals to visit
// - All players that survive a round opt to pass
// - the referee has run 1000 rounds : when gameState.roundNum is equal to 1000, the game ends.
public class MultiGoalReferee extends AReferee{

  public MultiGoalReferee(List<IPlayer> players) throws IllegalArgumentException {
    super(players, new MultiGoalLabyrinthRules());
  }

  public MultiGoalReferee(List<IPlayer> players, int delayTolerance)
      throws IllegalArgumentException {
    super(players, delayTolerance, new MultiGoalLabyrinthRules());
  }

  public SimpleEntry<List<IPlayer>, List<IPlayer>> playGame(MultiGoalRefereeState s) {
    return super.playGame(s);
  }

  @Override
  protected ARefereeState initializeRefereeState(Board board, Tile spare) {
    throw new UnsupportedOperationException("cannot create multi goal referee without initial state");
  }

  @Override
  protected void sendObserverState(ARefereeState refereeState) {
    //TODO: do this
  }


  @Override
  protected SimpleEntry<List<IPlayer>, List<IPlayer>> runFromInitialState(ARefereeState s) {
    Optional<PlayerInfo> maybeGameEnder = Optional.empty();
    while (!ALabyrinthRules.isGameOverWithoutTerminator(s)) {
      //play a round, sets meybeGmeEnder as the winner if there is one
      maybeGameEnder = playRound(s);
      //if game terminating player is present, end game
      if (maybeGameEnder.isPresent()) {
        break;
      }
      s.publicState.incrementRound();
      //if game finished due to other reason, end game
      if (ALabyrinthRules.isGameOverWithoutTerminator(s)) {
        break;
      }
      //reset skips after a round
      s.publicState.resetSkips();
    }
    //Game terminating actions
    List<PlayerInfo> winners = calculateWinners(s, maybeGameEnder);
    notifyAndFinalizeWinnersAndCheaters(s, winners);
    sendObserverGameOver();
    sortWinnerAndCheaterLists();
    return new SimpleEntry<>(winnerList, cheaterList);
  }

  @Override
  protected Optional<PlayerInfo> handleMove(ARefereeState s, Optional<Move> move, Color curAvatar,
                                            IPlayer curPlayer) {
    Optional<PlayerInfo> gameEnder = Optional.empty();
    //Checks if move is illegal, if it is kick active player
    if(!kickIfIllegalMove(s, move, curAvatar, curPlayer)) {
      return gameEnder;
    }
    //executes legal move
    s.publicState.doTurn(move);
    //checks if active player is game terminating
    if(this.rules.activePlayerEndedGame(s)){
      //sets active player as game ender and returns it
      gameEnder = Optional.of(s.publicState.getActivePlayer());
      return gameEnder;
    }
    //checks if current player reached it goal
    if(s.isActivePlayerOnGoalTile()) {
      handleActivePlayerReachingGoal(curAvatar, curPlayer, s);
    }
    s.publicState.goToNextActivePlayer();
    return gameEnder;
  }

  @Override
  protected ARefereeState createRefereeStateCopy(ARefereeState original) {
    return new MultiGoalRefereeState((MultiGoalRefereeState) original);
  }

  @Override
  protected void handleActivePlayerReachingGoal(Color curAvatar,
                                                IPlayer curPlayer, ARefereeState s) {
    MultiGoalRefereeState state = (MultiGoalRefereeState) s;
    Coordinate coordinateToSend;
    //checks if any goals remain in state
    if (state.hasRemainingGoals()) {
      coordinateToSend = state.getNextGoal();
      //assigns next goal in state to active player
      state.assignNextGoalToActivePlayer();
    } else {
      //since no goals are left in state, active player is given his home as next goal
      coordinateToSend = state.publicState.getActivePlayer().home;
      state.getCurrentPlayerAsMultiGoalRefPlayer().setGoal(coordinateToSend);
    }
    //increases active player goal count
    state.getCurrentPlayerAsMultiGoalRefPlayer().increaseNumGoalsReached();
    //sets up active player with new goal (either goal or home)
    boolean handledSetup = safeSetUp(curPlayer, Optional.empty(), coordinateToSend);
    //if setup is not handled properly, kick active player
    if (!handledSetup) {
      kickGivenActivePlayer(s, curAvatar, curPlayer); //will kick current player
    }
  }

  /**
   * Calculates the game winner(s) based on having the most treasures, if several players have same
   * number of treasures, winner is decided by closest to their next goal
   * In case of ties, multiple winners are selected
   * @param maybeEnder player which ended game by reaching his home, might not be game winner based
   * on treasures collected.
   * @return list of winner(s)
   */
  protected List<PlayerInfo> calculateWinners(ARefereeState s, Optional<PlayerInfo> maybeEnder) {
    List<PlayerInfo> winners = new ArrayList<>();
    List<PlayerInfo> potentialWinners = getPotentialWinnersByNumTreasures(s);
    if(maybeEnder.isPresent() && potentialWinners.contains(maybeEnder.get())) {
      winners.add(maybeEnder.get());
    } else {
      winners = getPlayersClosestToGoal(potentialWinners, s);
    }
    return winners;
  }

  /**
   * Calculates player(s) with most number of treasures
   * @return list of player(s) with most treasures
   */
  private List<PlayerInfo> getPotentialWinnersByNumTreasures(ARefereeState s) {
    int maxTreasures = 0;
    List<PlayerInfo> potentialWinners = new ArrayList<>();
    //get all players from state
    List<PlayerInfo> players = new ArrayList<>(s.publicState.getPlayers());
    //iterates through players to find ones with most goals
    for(PlayerInfo player : players) {
      MultiGoalRefereePlayerInfo refPlayerInfo =
              (MultiGoalRefereePlayerInfo)s.infoMap.get(player.avatar);
      //checks if current player's treasures > the max num of treasures checked
      if (refPlayerInfo.getNumGoalsReached() > maxTreasures) {
        //clears previous list and adds new potential winner
        potentialWinners = new ArrayList<>();
        potentialWinners.add(player);
        maxTreasures = refPlayerInfo.getNumGoalsReached();
        //checks if current player's treasure = the max num of treasures checked
      } else if (refPlayerInfo.getNumGoalsReached() == maxTreasures) {
        //player is added to existing winners list
        potentialWinners.add(player);
      }
    }
    return potentialWinners;
  }


}
