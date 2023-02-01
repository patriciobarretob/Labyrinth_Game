package components;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

//Represents a referee state information which is unique to a multi goal referee
public class MultiGoalRefereeState extends ARefereeState{

  private Queue<Coordinate> potentialGoals;

  public MultiGoalRefereeState(Board board, Tile spare, Queue<PlayerInfo> players,
      Map<Color, MultiGoalRefereePlayerInfo> infoMap) {
    super(board, spare, players, new HashMap<>(infoMap));
    this.potentialGoals = generatePotentialGoals(board);
  }

  public MultiGoalRefereeState(PublicState publicState,
      Map<Color, MultiGoalRefereePlayerInfo> infoMap) {
    super(publicState, new HashMap<>(infoMap));
    this.potentialGoals = generatePotentialGoals(publicState.getBoard());
  }

  public MultiGoalRefereeState(MultiGoalRefereeState otherState) {
    super(otherState);
    this.potentialGoals = new ArrayDeque<>();
    for (Coordinate coord : otherState.potentialGoals){
      this.potentialGoals.add(coord);
    }
  }

  @Override
  protected IRefereePlayerInfo getPlayerInfoCopy(IRefereePlayerInfo original) {
    return new MultiGoalRefereePlayerInfo((MultiGoalRefereePlayerInfo) original);
  }

  //Determines all potential goals and returns a sorted (by row column order) Q of goals
  private static Queue<Coordinate> generatePotentialGoals(Board board){
    List<Coordinate> coords = ALabyrinthRules.getAllImmovableCoordinates(board);
    List<Coordinate> sortedCoords = ALabyrinthRules.sortCoordinates(coords);
    Queue<Coordinate> sortedCoordsQ = new ArrayDeque<>(sortedCoords);
    return sortedCoordsQ;
  }

  public Queue<Coordinate> getPotentialGoals() {
    return this.potentialGoals;
  }

  public Coordinate getNextGoal(){return this.potentialGoals.peek();}

  // This is mostly a testing method.
  public void setPotentialGoals(Queue<Coordinate> goals) {
    this.potentialGoals = goals;
  }

  public boolean hasRemainingGoals() {
    return !this.potentialGoals.isEmpty();
  }

  /**
   * EFFECT: Removes the next goal in the list and assigns it to the active player
   * This method does not update number of goals reached.
   * @throws IllegalStateException if there are no goals in the list.
   */
  public void assignNextGoalToActivePlayer() throws IllegalStateException {
    if (!hasRemainingGoals()) {
      throw new IllegalStateException("No goals in list!");
    }
    MultiGoalRefereePlayerInfo curPlayer =
            (MultiGoalRefereePlayerInfo)
                    this.infoMap.get(this.publicState.getActivePlayer().avatar);
    curPlayer.setGoal(this.potentialGoals.poll());
  }

  //Convinience method that casts current player to multi goal player info
  public MultiGoalRefereePlayerInfo getCurrentPlayerAsMultiGoalRefPlayer() {
    return (MultiGoalRefereePlayerInfo) this.infoMap.get(this.publicState.getActivePlayer().avatar);
  }
}
