import org.junit.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.ARefereeState;
import components.Board;
import components.Coordinate;
import components.MultiGoalRefereePlayerInfo;
import components.MultiGoalRefereeState;
import components.PlayerInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestMultiGoalRefereeState extends ATestRefereeState {


    Map<Color, MultiGoalRefereePlayerInfo>
    generateMultiGoalRefPlayerInfoMap(List<PlayerInfo> publicInfo, List<Coordinate> initialGoals) {
        Map<Color, MultiGoalRefereePlayerInfo> infoMap = new HashMap<>();
        if (publicInfo.size() != initialGoals.size()) {
            throw new IllegalArgumentException("Goal list & public info list are not same size!");
        }
        for (int i = 0; i < publicInfo.size(); i += 1) {
            infoMap.put(publicInfo.get(i).avatar,
                    new MultiGoalRefereePlayerInfo(initialGoals.get(i)));
        }
        return infoMap;
    }

    @Test
    public void testInitializedGoals() {
        Board b = new Board(createBoard(4, 4));
        this.multiGoalRefereeState = new MultiGoalRefereeState(b, this.tile1, players,
                multiGoalRefereePlayerInfoMap);
        assertEquals(4, this.multiGoalRefereeState.getPotentialGoals().size());
        List<Coordinate> goals = new ArrayList<>(this.multiGoalRefereeState.getPotentialGoals());
        assertEquals(new Coordinate(1, 1), goals.get(0));
        assertEquals(new Coordinate(3, 1), goals.get(1));
        assertEquals(new Coordinate(1, 3), goals.get(2));
        assertEquals(new Coordinate(3, 3), goals.get(3));
    }

    @Test
    public void testGivePlayerNextGoal() {
        this.multiGoalGoals.add(new Coordinate(1, 1));
        this.multiGoalRefereeState.setPotentialGoals(this.multiGoalGoals);
        this.multiGoalRefereeState
                .getCurrentPlayerAsMultiGoalRefPlayer()
                .setGoal(new Coordinate(3, 5));
        assertTrue(this.multiGoalRefereeState.hasRemainingGoals());
        assertNotEquals(new Coordinate(1, 1),
                this.multiGoalRefereeState.getCurrentPlayerAsMultiGoalRefPlayer().getGoal());
        this.multiGoalRefereeState.assignNextGoalToActivePlayer();
        assertFalse(this.multiGoalRefereeState.hasRemainingGoals());
        assertEquals(new Coordinate(1, 1),
                this.multiGoalRefereeState.getCurrentPlayerAsMultiGoalRefPlayer().getGoal());
    }

    @Test (expected = IllegalStateException.class)
    public void testGivePlayerNextGoalWhenNoGoals() {
        this.multiGoalRefereeState.setPotentialGoals(this.multiGoalGoals);
        this.multiGoalRefereeState.assignNextGoalToActivePlayer();
    }

    @Override
    protected ARefereeState getRefereeState() {
        return this.multiGoalRefereeState;
    }
}
