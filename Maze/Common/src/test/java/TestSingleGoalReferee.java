import components.ARefereeState;
import components.IRefereePlayerInfo;
import java.awt.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import components.Board;
import components.Connector;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PublicState;
import components.SingleGoalRefereeState;
import components.Tile;
import players.*;
import referee.AReferee;
import referee.SingleGoalReferee;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class TestSingleGoalReferee extends ATestReferee {



  @Test
  public void TestGameWithRealStrategies() {
    startBoard = new char[][]{
        {'│', '┐', '└', '┌', '┘', '│', '├'},
        {'┴', '─', '┌', '┐', '┌', '┐', '└'},
        {'┌', '┘', '┘', '┤', '┴', '└', '┼'},
        {'┐', '┼', '┬', '└', '│', '┘', '┬'},
        {'┤', '┴', '┐', '├', '┌', '┐', '┐'},
        {'└', '├', '┘', '┬', '┬', '┴', '┐'},
        {'┼', '│', '└', '┐', '└', '┌', '│'}
    };
    PlayerImpl player1 = new PlayerImpl(new RiemannStrategy(), "John");
    PlayerImpl player2 = new PlayerImpl(new EuclidStrategy(), "605");
    List<IPlayer> l = new ArrayList<>(Arrays.asList(player1, player2));

    p1CurPos = new Coordinate(3,3);
    p1Home = new Coordinate(3,3);
    p1Goal = new Coordinate(1,5);

    p2CurPos = new Coordinate(5,3);
    p2Home = new Coordinate(5,3);
    p2Goal = new Coordinate(1,1);

    expectedWinnerNames = new ArrayList<>(Arrays.asList(player1.name()));
    createAndRun2('┌', l);

  }

  @Test
  public void testSimpleGame() {
    startBoard = new char[][]{
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼', '┼', '┼', '┼'}
    };
    spareConnector = Connector.UDLR;

    p1CurPos = new Coordinate(0, 0);
    p1Home = new Coordinate(1,3);
    p1Goal = new Coordinate(5,5);
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(5,5))),
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(1,3))),
            };
    p2Home = new Coordinate(1,1);
    p2Goal = new Coordinate(3,1);
    p2Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(3,1)))
    };

    p3Goal = new Coordinate(1,3);
    p3Moves = new Optional[] { Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
            0, new Coordinate(1,3))) };

    p4Goal = new Coordinate(5,1);
    p4Moves = new Optional[] { Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
            0, new Coordinate(5,1))) };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("true");

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("false");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;


    expectedWinnerNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testGameFromBeginningToEnd() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);

    p2ExpectedOutput = p1ExpectedOutput;
    p3ExpectedOutput = p1ExpectedOutput;
    p4ExpectedOutput = p1ExpectedOutput;

    createAndRunFromBeginning();
  }


  @Test
  public void testAllPlayersPassP1ReachedGoal() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1HasReachedGoal = true;
    p1CurPos = new Coordinate(0,0);
    p1Goal = new Coordinate(5, 5);

    p2CurPos = new Coordinate(4,4);
    p2Goal = new Coordinate(3,3);

    p3CurPos = new Coordinate(2,2);
    p3Goal = new Coordinate(1, 1);

    p4CurPos = new Coordinate(0,6);
    p4Goal = new Coordinate(1,5);

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

  @Test
  public void testAllPlayersPassAllP2P4ClosestToHome() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1HasReachedGoal = true;
    p1CurPos = new Coordinate(0,0);
    p1Home = new Coordinate(5, 5);

    p2HasReachedGoal = true;
    p2CurPos = new Coordinate(4,4);
    p2Home = new Coordinate(3,3);

    p3HasReachedGoal = true;
    p3CurPos = new Coordinate(6,6);
    p3Home = new Coordinate(1, 1);

    p4HasReachedGoal = true;
    p4CurPos = new Coordinate(0,6);
    p4Home = new Coordinate(1,5);

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("false");

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p1ExpectedOutput;

    p4ExpectedOutput = p2ExpectedOutput;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p4Name);
    createAndRun();
  }

  @Test
  public void testGameIntTestXBad2_0() {
    startBoard = new char[][]{
            {'┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼'},
            {'┼', '┼', '┼', '┼'}
    };
    PlayerImpl player1 = new BadPlayer2(new RiemannStrategy(), "Larry", 0,2,0);
    PlayerImpl player2 = new PlayerImpl(new EuclidStrategy(), "605");
    List<IPlayer> l = new ArrayList<>(Arrays.asList(player1, player2));

    p1CurPos = new Coordinate(1,1);
    p1Home = new Coordinate(3,1);
    p1Goal = new Coordinate(3,1);

    p2CurPos = new Coordinate(3,1);
    p2Home = new Coordinate(1,1);
    p2Goal = new Coordinate(1,1);

    expectedWinnerNames = new ArrayList<>(Arrays.asList(player2.name()));
    expectedCheaterNames = new ArrayList<>(Arrays.asList(player1.name()));
    createAndRun2('┼', l);

  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructRefereeNoPlayers() {
    new SingleGoalReferee(new ArrayList<>());
  }

  @Override
  protected SimpleEntry<List<IPlayer>, List<IPlayer>> playGameFromStateUsingReferee(ARefereeState s,
      AReferee ref) {
    return ((SingleGoalReferee)ref).playGame((SingleGoalRefereeState) s);
  }

  @Override
  protected AReferee getRefereeFromPlayers(List<IPlayer> playerList) {
    return new SingleGoalReferee(playerList);
  }

  @Override
  protected ARefereeState getRefereeStateFromPublicState(PublicState ps) {
    SingleGoalRefereeState s = new SingleGoalRefereeState(ps, createInfoMap());
    return s;
  }

  @Override
  protected ARefereeState getRefereeStateFromPublicState2(PublicState ps) {
    SingleGoalRefereeState s = new SingleGoalRefereeState(ps, createInfoMap2());
    return s;
  }
}
