import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import components.ARefereeState;
import components.Board;
import components.Connector;
import components.Coordinate;
import components.Direction;
import components.IRefereePlayerInfo;
import components.Move;
import components.PublicState;
import components.SingleGoalRefereeState;
import components.Tile;
import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import players.BadPlayer;
import players.BadPlayer2;
import players.DelayedPlayer;
import players.EuclidStrategy;
import players.IPlayer;
import players.PlayerImpl;
import players.RiemannStrategy;
import referee.AReferee;
import referee.SingleGoalReferee;

public abstract class ATestReferee extends ATestLabyrinth{
  Optional<Move> defaultInvalidMove = Optional.of(new Move(new SimpleEntry<>(1, Direction.UP),
          0, new Coordinate(1, 1)));

  @Test
  public void TestGameWithRealStrategies2() {
    startBoard = new char[][]{
        {'│', '│', '│', '│', '│', '│', '│'},
        {'│', '─', '│', '┐', '─', '─', '│'},
        {'│', '│', '│', '┤', '│', '│', '│'},
        {'┐', '┼', '┬', '└', '│', '┘', '┬'},
        {'┤', '┴', '┐', '├', '┌', '┐', '┐'},
        {'└', '├', '┘', '┬', '┬', '┴', '┐'},
        {'┼', '│', '└', '┐', '└', '┌', '│'}
    };
    PlayerImpl player1 = new PlayerImpl(new RiemannStrategy(), "John");
    PlayerImpl player2 = new PlayerImpl(new EuclidStrategy(), "605");
    List<IPlayer> l = new ArrayList<>(Arrays.asList(player1, player2));

    p1CurPos = new Coordinate(1,1);
    p1Home = new Coordinate(1,1);
    p1Goal = new Coordinate(1,1);

    p2CurPos = new Coordinate(5,1);
    p2Home = new Coordinate(5,1);
    p2Goal = new Coordinate(5,1);

    expectedWinnerNames = new ArrayList<>(Arrays.asList(player2.name()));
    createAndRun2('│', l);

  }

  @Test
  public void testPlayerReachedHomeAfterReachingGoal() {
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
    p1CurPos = new Coordinate(3, 1);
    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("true");

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("false");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    expectedWinnerNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testAllPlayersPassStartingFromPlayer2() {

    startBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'┴', '─', '─', '─', '─', '┐', '└'},
            {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
            {'│', '─', '┐', '└', '┌', '┘', '┬'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'─', '─', '─', '─', '─', '─', '─'}
    };

    Move p1Move = new Move(new SimpleEntry<>(6, Direction.RIGHT), 0,
            new Coordinate(0, 6));

    spareConnector = Connector.LR;
    p1Moves = new Optional[] { Optional.of(p1Move), Optional.empty()};
    p2Moves = new Optional[] { Optional.empty(), Optional.empty() };
    p3Moves = new Optional[] { Optional.empty(), Optional.empty() };
    p4Moves = new Optional[] { Optional.empty(), Optional.empty() };

    p1CurPos = new Coordinate(5,6);
    p1Goal = new Coordinate(1, 1);

    p2CurPos = new Coordinate(4,4);
    p2Goal = new Coordinate(3,3);

    p3CurPos = new Coordinate(5,6);
    p3Goal = new Coordinate(5, 5);

    p4CurPos = new Coordinate(5,6);
    p4Goal = new Coordinate(1,5);

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("false");

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;

    p4ExpectedOutput = p1ExpectedOutput;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    createAndRun();
  }

  @Test
  public void testAllPlayersPassPlayer2Closest() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1CurPos = new Coordinate(0,0);
    p1Goal = new Coordinate(5, 5);

    p2CurPos = new Coordinate(2,1);
    p2Goal = new Coordinate(1,1);

    p3CurPos = new Coordinate(3,3);
    p3Goal = new Coordinate(1, 1);

    p4CurPos = new Coordinate(6,6);
    p4Goal = new Coordinate(1,3);

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

  @Test
  public void testAllPlayersPassAllClosestToGoal() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1CurPos = new Coordinate(0,0);
    p1Goal = new Coordinate(1, 1);

    p2CurPos = new Coordinate(4,4);
    p2Goal = new Coordinate(3,3);

    p3CurPos = new Coordinate(6,6);
    p3Goal = new Coordinate(5, 5);

    p4CurPos = new Coordinate(0,6);
    p4Goal = new Coordinate(1,5);

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("true");

    p2ExpectedOutput = p1ExpectedOutput;
    p3ExpectedOutput = p1ExpectedOutput;
    p4ExpectedOutput = p1ExpectedOutput;

    expectedWinnerNames.add(this.p1Name);
    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);
    createAndRun();
  }


  @Test
  public void testAllPlayersRun1000Rounds() {

    startBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'┴', '─', '─', '─', '─', '┐', '└'},
            {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
            {'│', '─', '┐', '└', '┌', '┘', '┬'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'─', '─', '─', '─', '─', '─', '─'}
    };

    Move p1Move = new Move(new SimpleEntry<>(6, Direction.RIGHT), 0,
            new Coordinate(0, 6));

    spareConnector = Connector.LR;

    p1Moves = repeat1000Times(Optional.of(p1Move));
    p2Moves = repeat1000Times(Optional.empty());
    p3Moves = repeat1000Times(Optional.empty());
    p4Moves = repeat1000Times(Optional.empty());

    p1CurPos = new Coordinate(0,6);
    p1Goal = new Coordinate(5, 5);

    p2CurPos = new Coordinate(2,1);
    p2Goal = new Coordinate(1,1);

    p3CurPos = new Coordinate(3,3);
    p3Goal = new Coordinate(1, 1);

    p4CurPos = new Coordinate(6,6);
    p4Goal = new Coordinate(1,3);

    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("false");

    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p3ExpectedOutput.add("false");

    p4ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p4ExpectedOutput.add("false");

    expectedWinnerNames.add(this.p2Name);
    createAndRun();
  }

  private Optional[] repeat1000Times(Optional<Move> move) {
    Optional[] out = new Optional[1000];
    for(int i = 0; i < 1000; i++) {
      out[i] = move;
    }
    return out;
  }

  @Test
  public void testKickAllPlayers() {
    p1Moves = new Optional[] { defaultInvalidMove };
    p2Moves = new Optional[] { defaultInvalidMove };
    p3Moves = new Optional[] { defaultInvalidMove };
    p4Moves = new Optional[] { defaultInvalidMove };
    expectedCheaterNames.add(this.p1Name);
    expectedCheaterNames.add(this.p2Name);
    expectedCheaterNames.add(this.p3Name);
    expectedCheaterNames.add(this.p4Name);
    createAndRun();
  }

  @Test
  public void testP1KickedOutOfBoundsSlide() {
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(400, Direction.LEFT),
                    0, new Coordinate(1, 1)))
    };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testLastPlayerInRoundKicked() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { defaultInvalidMove };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("true");

    p2ExpectedOutput = p1ExpectedOutput;
    p3ExpectedOutput = p1ExpectedOutput;

    p4ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);


    testP4Kicked = true;

    expectedWinnerNames.add(this.p1Name);
    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);

    expectedCheaterNames.add(this.p4Name);
    createAndRun();
  }

  @Test
  public void testP2AndP3Kicked() {
    p1Moves = new Optional[] { Optional.empty() };
    p2Moves = new Optional[] { defaultInvalidMove };
    p3Moves = new Optional[] { defaultInvalidMove };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("true");

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p3ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p3ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p4ExpectedOutput = p1ExpectedOutput;

    testP2Kicked = true;

    testP3Kicked = true;


    expectedWinnerNames.add(this.p1Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p2Name);
    expectedCheaterNames.add(this.p3Name);
    createAndRun();
  }

  @Test
  public void testP1KickedNullSlide() {
    p1Moves = new Optional[] {
            Optional.of(new Move(null,
                    0, new Coordinate(1, 1)))
    };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP1KickedInvalidSlide() {
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(1, Direction.UP),
                    0, new Coordinate(1, 1)))
    };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP1KickedNullDirection() {
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, null),
                    0, new Coordinate(1, 1)))
    };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP1KickedNullCoordinate() {
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, null))
    };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP1KickedOutOfBounds() {
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(-500,5)))
    };
    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP1KickedNotReachable() {

    startBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };
    p1CurPos = new Coordinate(1,1);
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(4,4)))
    };

    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP1KickedTryToReachCurrent() {

    startBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };
    p1CurPos = new Coordinate(1,1);
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(1,1)))
    };

    p2Moves = new Optional[] { Optional.empty() };
    p3Moves = new Optional[] { Optional.empty() };
    p4Moves = new Optional[] { Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p2ExpectedOutput.add("true");

    p3ExpectedOutput = p2ExpectedOutput;
    p4ExpectedOutput = p2ExpectedOutput;

    testP1Kicked = true;

    expectedWinnerNames.add(this.p2Name);
    expectedWinnerNames.add(this.p3Name);
    expectedWinnerNames.add(this.p4Name);

    expectedCheaterNames.add(this.p1Name);
    createAndRun();
  }

  @Test
  public void testP2KickedTryToUndoPrev() {

    startBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'─', '─', '─', '─', '─', '─', '─'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };
    p1CurPos = new Coordinate(1,1);
    p1Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.LEFT),
                    0, new Coordinate(2,1))),
            Optional.empty()
    };

    p2Moves = new Optional[] {
            Optional.of(new Move(new SimpleEntry<>(0, Direction.RIGHT),
                    90, new Coordinate(5,1)))
    };

    p3Moves = new Optional[] { Optional.empty(), Optional.empty() };
    p4Moves = new Optional[] { Optional.empty(), Optional.empty() };

    p1ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p1ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p1ExpectedOutput.add("true");

    p2ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p2ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);

    p3ExpectedOutput.add(MockPlayer.SETUP_CALLED_STRING);
    p3ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p3ExpectedOutput.add(MockPlayer.TAKE_TURN_CALLED_STRING);
    p3ExpectedOutput.add(MockPlayer.WON_CALLED_STRING);
    p3ExpectedOutput.add("false");

    p4ExpectedOutput = p3ExpectedOutput;

    testP2Kicked = true;

    expectedWinnerNames.add(this.p1Name);

    expectedCheaterNames.add(this.p2Name);
    createAndRun();
  }





  @Test
  public void testGameWithBadPlayers() {
    List<IPlayer> badPlayers = new ArrayList<>();
    badPlayers.add(new BadPlayer(new RiemannStrategy(), p1Name, true,false,false));
    badPlayers.add(new BadPlayer(new RiemannStrategy(), p2Name, false,true,false));
    badPlayers.add(new BadPlayer(new RiemannStrategy(), p3Name, false,false,true));
    badPlayers.add(new BadPlayer(new RiemannStrategy(), p4Name, false,false,false));
    SingleGoalReferee ref = new SingleGoalReferee(badPlayers);
    SimpleEntry<List<IPlayer>, List<IPlayer>> output = ref.playGame(this.singleGoalRefereeState);
    List<IPlayer> cheaterList = output.getValue();
    Set<String> cheaters = new HashSet<>(Arrays.asList(p1Name,p2Name,p3Name));
    Assert.assertEquals(3, cheaterList.size());
    Assert.assertTrue(cheaters.contains(cheaterList.get(0).name()));
    Assert.assertTrue(cheaters.contains(cheaterList.get(1).name()));
    Assert.assertTrue(cheaters.contains(cheaterList.get(2).name()));



  }


  @Test
  public void testGameWithDelayedPlayers() {
    List<IPlayer> badPlayers = new ArrayList<>();
    badPlayers.add(new DelayedPlayer(new RiemannStrategy(), p1Name, 1000,0,0));
    badPlayers.add(new DelayedPlayer(new RiemannStrategy(), p2Name, 0,1000,0));
    badPlayers.add(new DelayedPlayer(new RiemannStrategy(), p3Name, 0,0,1000));
    badPlayers.add(new DelayedPlayer(new RiemannStrategy(), p4Name, 0,0,0));
    SingleGoalReferee ref = new SingleGoalReferee(badPlayers, 200);
    SimpleEntry<List<IPlayer>, List<IPlayer>> output = ref.playGame(this.singleGoalRefereeState);
    List<IPlayer> cheaterList = output.getValue();
    Set<String> cheaters = new HashSet<>(Arrays.asList(p1Name,p2Name,p3Name));
    Assert.assertEquals(3, cheaterList.size());
    Assert.assertTrue(cheaters.contains(cheaterList.get(0).name()));
    Assert.assertTrue(cheaters.contains(cheaterList.get(1).name()));
    Assert.assertTrue(cheaters.contains(cheaterList.get(2).name()));



  }

  @Test
  public void testGameWithInfiniteLoopPlayers() {
    List<IPlayer> badPlayers = new ArrayList<>();
    badPlayers.add(new BadPlayer2(new RiemannStrategy(), p1Name, 0,0,1));
    badPlayers.add(new BadPlayer2(new RiemannStrategy(), p2Name, 0,1,0));
    badPlayers.add(new BadPlayer2(new RiemannStrategy(), p3Name, 1,0,0));
    badPlayers.add(new BadPlayer2(new RiemannStrategy(), p4Name, 0,0,0));
    SingleGoalReferee ref = new SingleGoalReferee(badPlayers, 200);
    SimpleEntry<List<IPlayer>, List<IPlayer>> output = ref.playGame(this.singleGoalRefereeState);
    List<IPlayer> cheaterList = output.getValue();
    Set<String> cheaters = new HashSet<>(Arrays.asList(p1Name,p2Name,p3Name));
    Assert.assertEquals(3, cheaterList.size());
    Assert.assertTrue(cheaters.contains(cheaterList.get(0).name()));
    Assert.assertTrue(cheaters.contains(cheaterList.get(1).name()));
    Assert.assertTrue(cheaters.contains(cheaterList.get(2).name()));



  }

  @Test
  public void runGameWithNoPlayersAfterSetup() {
    IPlayer b1 = new BadPlayer(new RiemannStrategy(), "b1", true, false, false);
    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(b1);
    SingleGoalReferee ref = new SingleGoalReferee(playerList);
    ref.playGame();
  }

  public void createAndRun() {
    Board b = new Board(createBoard(startBoard));
    Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
    PublicState ps = new PublicState(b, spare, createPlayerInfoList());
    ARefereeState s = getRefereeStateFromPublicState(ps);
    AReferee ref = getRefereeFromPlayers(createPlayerList());
    SimpleEntry<List<IPlayer>, List<IPlayer>> output = playGameFromStateUsingReferee(s, ref);
    checkAllPlayerExpectedOutput();
    checkWinnersAndCheaters(output);
  }

  protected abstract SimpleEntry<List<IPlayer>, List<IPlayer>> playGameFromStateUsingReferee(
      ARefereeState s, AReferee ref);

  protected abstract AReferee getRefereeFromPlayers(List<IPlayer> playerList);

  protected abstract ARefereeState getRefereeStateFromPublicState(PublicState ps);

  //TODO fix lines 910 - 911
  public void createAndRun2(Character spare, List<IPlayer> players) {
    Board b = new Board(createBoard(startBoard));
    Tile spareTile = new Tile(gemList[0], gemList[49], Connector.getFromChar(spare));
    PublicState ps = new PublicState(b, spareTile, createPlayerInfoList2());

    ARefereeState s = getRefereeStateFromPublicState2(ps);

    AReferee ref = getRefereeFromPlayers(players);
    players.get(0).setup(Optional.of(ps), p1Goal);
    players.get(1).setup(Optional.of(ps), p2Goal);
    SimpleEntry<List<IPlayer>, List<IPlayer>> output = playGameFromStateUsingReferee(s, ref);
    checkWinnersAndCheaters(output);
  }

  protected abstract ARefereeState getRefereeStateFromPublicState2(PublicState ps);

  public void createAndRunFromBeginning() {
    AReferee ref = getRefereeFromPlayers(createPlayerList());
    ref.playGame();

    checkAllPlayerExpectedOutput();
  }


  public void checkWinnersAndCheaters(SimpleEntry<List<IPlayer>, List<IPlayer>> output) {
    List<String> winnerNames = new ArrayList<>();
    List<String> cheaterNames = new ArrayList<>();
    for (IPlayer winner: output.getKey()) {
      winnerNames.add(winner.name());
    }
    for (IPlayer cheater: output.getValue()) {
      cheaterNames.add(cheater.name());
    }
    Assert.assertEquals(expectedWinnerNames, winnerNames);
    Assert.assertEquals(expectedCheaterNames, cheaterNames);
  }

  public void checkAllPlayerExpectedOutput() {
    checkStringBuilderOutput(testP1Kicked, p1SB, p1ExpectedOutput);
    checkStringBuilderOutput(testP2Kicked, p2SB, p2ExpectedOutput);
    checkStringBuilderOutput(testP3Kicked, p3SB, p3ExpectedOutput);
    checkStringBuilderOutput(testP4Kicked, p4SB, p4ExpectedOutput);
  }

  public void checkStringBuilderOutput(boolean testKicked,
      StringBuilder sb, List<String> expectedOutput) {
    String[] output = sb.toString().split("\n");
    if (testKicked) {
      for (String actualOutput: output) {
        assertNotEquals(MockPlayer.WON_CALLED_STRING, actualOutput);
      }
    }
    int counter = 0;
    for (int i = 0; i < output.length; i += 1) {
      if (counter >= expectedOutput.size()) {
        break;
      }
      if(output[i].equals(expectedOutput.get(counter))) {
        counter += 1;
      }
    }
    if (counter < expectedOutput.size()) {
      StringBuilder failed = new StringBuilder();
      failed.append("Test did not receive all expected commands! Missed Commands:\n");
      for (int i = counter; i < expectedOutput.size(); i += 1) {
        failed.append(expectedOutput.get(i));
        failed.append("\n");
      }
      fail(failed.toString());
    }
  }



  List<IPlayer> createPlayerList() {
    List<IPlayer> list = new ArrayList<>();
    IPlayer mockP1 = createPlayerFromInfo(p1Moves, p1SB, p1Name);
    IPlayer mockP2 = createPlayerFromInfo(p2Moves, p2SB, p2Name);
    IPlayer mockP3 = createPlayerFromInfo(p3Moves, p3SB, p3Name);
    IPlayer mockP4 = createPlayerFromInfo(p4Moves, p4SB, p4Name);

    list.add(mockP1);
    list.add(mockP2);
    list.add(mockP3);
    list.add(mockP4);

    return list;
  }



  IPlayer createPlayerFromInfo( Optional<Move>[] moves, StringBuilder sb,
      String name) {
    Queue<Optional<Move>> queueOfMoves = createQueueFromArr(moves);
    return new MockPlayer(sb, name, createBoard(proposedBoard), queueOfMoves);
  }

  public Queue<Optional<Move>> createQueueFromArr (Optional<Move>[] moves) {
    return new ArrayDeque<>(Arrays.asList(moves));
  }



}
