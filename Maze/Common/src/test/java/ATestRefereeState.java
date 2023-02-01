import org.junit.Assert;
import org.junit.Test;

import components.ARefereeState;
import components.PlayerInfo;

public abstract class ATestRefereeState extends ATestLabyrinth {
    protected abstract ARefereeState getRefereeState();

    @Test
    public void testIsOnGoalTile() {
        Assert.assertFalse(getRefereeState().isActivePlayerOnGoalTile());
        player1.currentPos = infoMap.get(player1.avatar).getGoal();
        Assert.assertTrue(getRefereeState().isActivePlayerOnGoalTile());
    }

    @Test
    public void testKickActivePlayer() {
        PlayerInfo currentPlayer = getRefereeState().publicState.getActivePlayer();
        getRefereeState().kickActivePlayer();
        Assert.assertFalse(getRefereeState().infoMap.containsKey(currentPlayer.avatar));
        Assert.assertNotEquals(currentPlayer, getRefereeState().publicState.getActivePlayer());
        Assert.assertFalse(getRefereeState().publicState.getPlayers().contains(currentPlayer));
    }
}
