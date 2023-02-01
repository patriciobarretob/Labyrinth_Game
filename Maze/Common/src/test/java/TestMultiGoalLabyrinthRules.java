import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import components.ALabyrinthRules;
import components.Board;
import components.Coordinate;
import components.MultiGoalRefereeState;
import components.PublicState;
import components.SingleGoalRefereeState;
import components.Tile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMultiGoalLabyrinthRules extends ATestLabyrinthRules {
    @Test
    public void testActiveEndsGame() {
        p1CurPos = new Coordinate(1, 1);
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        Queue<Coordinate> goals = new ArrayDeque<>();

        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
        PublicState ps = new PublicState(b, spare, createPlayerInfoList());
        MultiGoalRefereeState s = new MultiGoalRefereeState(ps, createMultiGoalInfoMap());
        s.setPotentialGoals(goals);

        assertTrue(multiRules.activePlayerEndedGame(s));
    }

    @Test
    public void testActiveDoesntEndGameGoalsRemainingInQueue() {
        p1CurPos = new Coordinate(1, 1);
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        Queue<Coordinate> goals = new ArrayDeque<>();
        goals.add(new Coordinate(3, 3));

        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
        PublicState ps = new PublicState(b, spare, createPlayerInfoList());
        MultiGoalRefereeState s = new MultiGoalRefereeState(ps, createMultiGoalInfoMap());
        s.setPotentialGoals(goals);

        assertFalse(multiRules.activePlayerEndedGame(s));
    }

    @Test
    public void testActiveDoesntEndGameHomeDifferentFromGoal() {
        p1CurPos = new Coordinate(1, 1);
        p1Home = new Coordinate(5, 5);
        p1Goal = new Coordinate(1, 1);
        Queue<Coordinate> goals = new ArrayDeque<>();

        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
        PublicState ps = new PublicState(b, spare, createPlayerInfoList());
        MultiGoalRefereeState s = new MultiGoalRefereeState(ps, createMultiGoalInfoMap());
        s.setPotentialGoals(goals);

        assertFalse(multiRules.activePlayerEndedGame(s));
    }

    @Test
    public void testActiveDoesntEndGameNotAtGoalHome() {
        p1CurPos = new Coordinate(2, 1);
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        Queue<Coordinate> goals = new ArrayDeque<>();

        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
        PublicState ps = new PublicState(b, spare, createPlayerInfoList());
        MultiGoalRefereeState s = new MultiGoalRefereeState(ps, createMultiGoalInfoMap());
        s.setPotentialGoals(goals);

        assertFalse(multiRules.activePlayerEndedGame(s));
    }
}
