package components;

// Represents the information about a player in a single goal game available only to the referee.
public class SingleGoalRefereePlayerInfo implements IRefereePlayerInfo {
  private boolean hasPlayerReachedGoal;
  private final Coordinate goal;

  public SingleGoalRefereePlayerInfo(Coordinate goal) {
    this.hasPlayerReachedGoal = false;
    this.goal = goal;
  }

  public SingleGoalRefereePlayerInfo(SingleGoalRefereePlayerInfo oldPlayerInfo) {
    this.hasPlayerReachedGoal = oldPlayerInfo.hasPlayerReachedGoal;
    this.goal = oldPlayerInfo.goal;
  }

  public boolean isGoingHome() {
    return hasPlayerReachedGoal;
  }

  @Override
  public Coordinate getGoal() {
    return this.goal;
  }

  public boolean getHasReachedGoal() {
    return this.hasPlayerReachedGoal;
  }

  public void setHasReachedGoal(boolean goalReached) {
    this.hasPlayerReachedGoal = goalReached;
  }
}
