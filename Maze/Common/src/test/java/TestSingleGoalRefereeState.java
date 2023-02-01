import components.ARefereeState;
import components.PlayerInfo;
import components.SingleGoalRefereePlayerInfo;
import org.junit.Assert;
import org.junit.Test;

public class TestSingleGoalRefereeState extends ATestRefereeState {


  @Test
  public void testUpdateActivePlayerHasReachedGoal() {
    PlayerInfo currentPlayer = singleGoalRefereeState.publicState.getActivePlayer();
    SingleGoalRefereePlayerInfo activePlayerInfo = (SingleGoalRefereePlayerInfo)singleGoalRefereeState.infoMap.get(currentPlayer.avatar);
    Assert.assertFalse(activePlayerInfo.getHasReachedGoal());
    singleGoalRefereeState.updateActivePlayerHasReachedGoal();
    Assert.assertTrue(activePlayerInfo.getHasReachedGoal());
  }

  @Override
  protected ARefereeState getRefereeState() {
    return this.singleGoalRefereeState;
  }
}
