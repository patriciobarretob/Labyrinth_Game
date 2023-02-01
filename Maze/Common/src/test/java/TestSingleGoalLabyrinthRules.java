import org.junit.Test;

import components.ALabyrinthRules;
import components.Board;
import components.Coordinate;
import components.PublicState;
import components.SingleGoalRefereeState;
import components.Tile;

import static org.junit.Assert.assertTrue;

public class TestSingleGoalLabyrinthRules extends ATestLabyrinthRules {

    @Test
    public void testActivePlayerWonCurrentPlayerReachedHomeAfterGoal() {
        p1CurPos = new Coordinate(1, 1);
        p1Home = new Coordinate(1, 1);
        p1HasReachedGoal = true;

        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
        PublicState ps = new PublicState(b, spare, createPlayerInfoList());
        SingleGoalRefereeState s = new SingleGoalRefereeState(ps, createInfoMap());

        assertTrue(singleRules.activePlayerEndedGame(s));
    }
}
