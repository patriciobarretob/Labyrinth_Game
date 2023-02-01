package components;

//Represents information about a player in a multi goal game only available to the referee
public class MultiGoalRefereePlayerInfo implements IRefereePlayerInfo{

  private int numGoalsReached;
  private Coordinate goal;

  public MultiGoalRefereePlayerInfo(Coordinate goal) {
    this.numGoalsReached = 0;
    this.goal = goal;
  }

  public MultiGoalRefereePlayerInfo(MultiGoalRefereePlayerInfo oldPlayerInfo) {
    this.numGoalsReached = oldPlayerInfo.numGoalsReached;
    this.goal = oldPlayerInfo.goal;
  }

  @Override
  public Coordinate getGoal() {
    return this.goal;
  }

  public void setGoal(Coordinate newGoal) {this.goal = newGoal;}

  /**
   * Convenience method for testing.
   */
  public void setNumGoalsReached(int numGoalsReached) {
    this.numGoalsReached = numGoalsReached;
  }

  public void increaseNumGoalsReached(){
    this.numGoalsReached++;
  }

  public int getNumGoalsReached(){
    return this.numGoalsReached;
  }

}
