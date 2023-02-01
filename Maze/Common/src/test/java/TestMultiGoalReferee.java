import org.junit.Ignore;
import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import components.ARefereeState;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.MultiGoalRefereeState;
import components.PublicState;
import players.IPlayer;
import referee.AReferee;
import referee.MultiGoalReferee;

public class TestMultiGoalReferee extends ATestReferee {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructRefereeNoPlayers() {
        new MultiGoalReferee(new ArrayList<>());
    }

    // Player 1 wins by going to 2 goals; its initial goal, and then the goal it gets assigned.
    // All the other players have 2 treasures, but are further away from their goals, so Player 1
    // wins, as it is closer to its home, which is now where it needs to go.
    @Test
    public void testPlayer1Gets2TreasuresAndWins() {
        startBoard = new char[][]{
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼', '┼', '┼', '┼'}
        };
        p1Moves = new Optional[] {
                Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT), 0, new Coordinate(1,
                        1))),
                Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT), 0, new Coordinate(1,
                        5))),
                Optional.empty()
        };
        p2Moves = new Optional[] {
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        };
        p3Moves = new Optional[] {
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        };
        p4Moves = new Optional[] {
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        };

        this.multiGoalGoals.add(new Coordinate(1, 5));

        p1Home = new Coordinate(1, 6);
        p1Goal = new Coordinate(1, 1);
        p1CurPos = new Coordinate(3, 1);

        p2Goal = new Coordinate(3, 1);
        p2CurPos = new Coordinate(3, 3);

        p3Goal = new Coordinate(3, 3);
        p3CurPos = new Coordinate(3, 5);

        p4Goal = new Coordinate(3, 5);
        p4CurPos = new Coordinate(3, 3);


        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("true");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("false");

        p1NumGoals = 0;
        p2NumGoals = 2;
        p3NumGoals = 2;
        p4NumGoals = 2;

        p3ExpectedOutput = p2ExpectedOutput;
        p4ExpectedOutput = p2ExpectedOutput;

        expectedWinnerNames.add(this.p1Name);
        createAndRun();
    }

    // Player 1 2 3 4 all start with 1 treasure and end the same distance away from their goals,
    // but player 1 reaches their goal, so they get 1 treasure, and win.
    @Test
    public void testPlayer1GetsTreasureAndWins() {
        startBoard = new char[][]{
                {'─', '┐', '└', '┌', '┘', '┬', '├'},
                {'┴', '─', '─', '─', '─', '┐', '└'},
                {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
                {'│', '─', '┐', '└', '┌', '┘', '┬'},
                {'├', '┴', '┤', '┼', '│', '─', '┐'},
                {'└', '┌', '┘', '┬', '├', '┴', '┤'},
                {'┼', '│', '─', '┐', '└', '┌', '┘'}
        };
        p1Moves = new Optional[] {
                Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT), 0, new Coordinate(1,
                        1))),
                Optional.empty()
        };
        p2Moves = new Optional[] {
                Optional.empty(),
                Optional.empty()
        };
        p3Moves = new Optional[] {
                Optional.empty(),
                Optional.empty()
        };
        p4Moves = new Optional[] {
                Optional.empty(),
                Optional.empty()
        };
        p1HasReachedGoal = false;
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        p1CurPos = new Coordinate(3, 1);

        p2Goal = new Coordinate(3, 1);
        p2CurPos = new Coordinate(3, 1);

        p3Goal = new Coordinate(3, 3);
        p3CurPos = new Coordinate(3, 3);

        p4Goal = new Coordinate(3, 5);
        p4CurPos = new Coordinate(3, 5);


        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("true");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("false");

        p1NumGoals = 1;
        p2NumGoals = 1;
        p3NumGoals = 1;
        p4NumGoals = 1;

        p3ExpectedOutput = p2ExpectedOutput;
        p4ExpectedOutput = p2ExpectedOutput;

        expectedWinnerNames.add(this.p1Name);
        createAndRun();
    }

    // Player 1, 2, 3, 4 have same treasures, but player 1 ended the game, so player 1 wins.
    @Test
    public void testPlayer1ReachedHomeAfterReachingGoalSameTreasure() {
        startBoard = new char[][]{
                {'─', '┐', '└', '┌', '┘', '┬', '├'},
                {'┴', '─', '─', '─', '─', '┐', '└'},
                {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
                {'│', '─', '┐', '└', '┌', '┘', '┬'},
                {'├', '┴', '┤', '┼', '│', '─', '┐'},
                {'└', '┌', '┘', '┬', '├', '┴', '┤'},
                {'┼', '│', '─', '┐', '└', '┌', '┘'}
        };
        p1Moves = new Optional[] {
                Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT), 0, new Coordinate(1, 1)))
        };
        p2Moves = new Optional[] {
        };
        p3Moves = new Optional[] {
        };
        p4Moves = new Optional[] {
        };
        p1HasReachedGoal = true;
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        p1CurPos = new Coordinate(3, 1);

        p2Goal = new Coordinate(3, 1);
        p2CurPos = new Coordinate(4, 2);

        p3Goal = new Coordinate(3, 3);
        p3CurPos = new Coordinate(4, 4);

        p4Goal = new Coordinate(3, 5);
        p4CurPos = new Coordinate(4, 6);


        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("true");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("false");

        p1NumGoals = 1;
        p2NumGoals = 1;
        p3NumGoals = 1;
        p4NumGoals = 1;

        p3ExpectedOutput = p2ExpectedOutput;
        p4ExpectedOutput = p2ExpectedOutput;

        expectedWinnerNames.add(this.p1Name);
        createAndRun();
    }

    // Player 1 does not get a treasure as going home isn't a treasure: thus, player 2 3 and 4 win
    @Test
    public void testPlayer1ReachedHomeAfterReachingGoalNoTreasure() {
        startBoard = new char[][]{
                {'─', '┐', '└', '┌', '┘', '┬', '├'},
                {'┴', '─', '─', '─', '─', '┐', '└'},
                {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
                {'│', '─', '┐', '└', '┌', '┘', '┬'},
                {'├', '┴', '┤', '┼', '│', '─', '┐'},
                {'└', '┌', '┘', '┬', '├', '┴', '┤'},
                {'┼', '│', '─', '┐', '└', '┌', '┘'}
        };
        p1Moves = new Optional[] {
                Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT), 0, new Coordinate(1, 1)))
        };
        p2Moves = new Optional[] {
        };
        p3Moves = new Optional[] {
        };
        p4Moves = new Optional[] {
        };
        p1HasReachedGoal = true;
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        p1CurPos = new Coordinate(3, 1);

        p2Goal = new Coordinate(3, 1);
        p2CurPos = new Coordinate(4, 2);

        p3Goal = new Coordinate(3, 3);
        p3CurPos = new Coordinate(4, 4);

        p4Goal = new Coordinate(3, 5);
        p4CurPos = new Coordinate(4, 6);


        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("false");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("true");

        p1NumGoals = 0;
        p2NumGoals = 1;
        p3NumGoals = 1;
        p4NumGoals = 1;

        p3ExpectedOutput = p2ExpectedOutput;
        p4ExpectedOutput = p2ExpectedOutput;

        expectedWinnerNames.add(this.p2Name);
        expectedWinnerNames.add(this.p3Name);
        expectedWinnerNames.add(this.p4Name);
        createAndRun();
    }

    // player 1 wins with 1 treasure, all other players have none, player 1 is furthest from goal
    @Test
    public void testPlayer1MostTreasuresFurthestFromGoal() {
        p1Moves = new Optional[] { Optional.empty() };
        p2Moves = new Optional[] { Optional.empty() };
        p3Moves = new Optional[] { Optional.empty() };
        p4Moves = new Optional[] { Optional.empty() };

        p1CurPos = new Coordinate(0,0);
        p1Goal = new Coordinate(5, 5);
        p1NumGoals = 1;

        p2CurPos = new Coordinate(4,4);
        p2Goal = new Coordinate(3,3);
        p2NumGoals = 0;

        p3CurPos = new Coordinate(6,6);
        p3Goal = new Coordinate(5, 5);
        p3NumGoals = 0;

        p4CurPos = new Coordinate(0,6);
        p4Goal = new Coordinate(1,5);
        p4NumGoals = 0;

        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("true");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("false");

        p3ExpectedOutput = p2ExpectedOutput;
        p4ExpectedOutput = p2ExpectedOutput;

        expectedWinnerNames.add(this.p1Name);
        createAndRun();
    }

    // player 2 and 3 have the most treasures, but player 2 is closer, so player 2 wins.
    @Test
    public void testPlayer23MostTreasures2Closer() {
        p1Moves = new Optional[] { Optional.empty() };
        p2Moves = new Optional[] { Optional.empty() };
        p3Moves = new Optional[] { Optional.empty() };
        p4Moves = new Optional[] { Optional.empty() };

        p1CurPos = new Coordinate(5,5);
        p1Goal = new Coordinate(5, 5);
        p1NumGoals = 0;

        p2CurPos = new Coordinate(3,4);
        p2Goal = new Coordinate(3,3);
        p2NumGoals = 1;

        p3CurPos = new Coordinate(6,6);
        p3Goal = new Coordinate(5, 5);
        p3NumGoals = 1;

        p4CurPos = new Coordinate(0,6);
        p4Goal = new Coordinate(1,5);
        p4NumGoals = 0;

        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("false");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("true");

        p3ExpectedOutput = p1ExpectedOutput;
        p4ExpectedOutput = p1ExpectedOutput;

        expectedWinnerNames.add(this.p2Name);
        createAndRun();
    }

    // Player 1 home coincides with the last goal in sequence and he is told ot go home
    @Test
    public void testPlayer1HeadsHomeSameAsLastTreasure() {
        startBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'┴', '─', '─', '─', '─', '┐', '└'},
            {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
            {'│', '─', '┐', '└', '┌', '┘', '┬'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
        };
        p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT), 0, new Coordinate(1, 1)))
        };
        p2Moves = new Optional[] {
        };
        p3Moves = new Optional[] {
        };
        p4Moves = new Optional[] {
        };
        p1Home = new Coordinate(1, 1);
        p1Goal = new Coordinate(1, 1);
        p1CurPos = new Coordinate(3, 1);

        p2Goal = new Coordinate(3, 1);
        p2CurPos = new Coordinate(4, 2);

        p3Goal = new Coordinate(3, 3);
        p3CurPos = new Coordinate(4, 4);

        p4Goal = new Coordinate(3, 5);
        p4CurPos = new Coordinate(4, 6);


        p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
        p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p1ExpectedOutput.add("false");

        p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p2ExpectedOutput.add("true");

        p3ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
        p3ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
        p3ExpectedOutput.add("false");

        p1NumGoals = 1;
        p2NumGoals = 2;
        p3NumGoals = 1;
        p4NumGoals = 1;

        p4ExpectedOutput = p3ExpectedOutput;

        expectedWinnerNames.add(this.p2Name);
        createAndRun();
    }

    @Override
    protected SimpleEntry<List<IPlayer>, List<IPlayer>> playGameFromStateUsingReferee(ARefereeState s, AReferee ref) {
        return ((MultiGoalReferee) ref).playGame((MultiGoalRefereeState) s);
    }

    @Override
    protected AReferee getRefereeFromPlayers(List<IPlayer> playerList) {
        return new MultiGoalReferee(playerList);
    }

    @Override
    protected ARefereeState getRefereeStateFromPublicState(PublicState ps) {
        MultiGoalRefereeState state = new MultiGoalRefereeState(ps, createMultiGoalInfoMap());
        state.setPotentialGoals(this.multiGoalGoals);
        return state;
    }

    @Override
    protected ARefereeState getRefereeStateFromPublicState2(PublicState ps) {
        MultiGoalRefereeState state = new MultiGoalRefereeState(ps, createMultiGoalInfoMap2());
        state.setPotentialGoals(this.multiGoalGoals);
        return state;
    }
}
