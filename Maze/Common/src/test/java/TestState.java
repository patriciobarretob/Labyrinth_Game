import components.Connector;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PlayerInfo;
import components.PublicState;
import components.Tile;

import java.util.AbstractMap.SimpleEntry;

import java.util.Optional;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

//todo: Write test ensuring invalid move doesn't update lastAction
public class TestState extends ATestLabyrinth {

  @Test (expected = IllegalArgumentException.class)
  public void testConstructStrictWithTooManyPlayers() {
    PublicState.constructStrictPublicState(this.board3x3, this.tile2, this.players);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructStrictWithPlayersWithSameHome() {
    this.p1Home = new Coordinate(1, 1);
    this.p2Home = new Coordinate(1, 1);
    Queue<PlayerInfo> players = createPlayerInfoList();
    PublicState.constructStrictPublicState(this.board7x10, this.tile2, players);
  }

  @Test
  public void testRotateOnce() {
    publicGameState.rotateSpare(1);
    Assert.assertEquals(publicGameState.getSpare().getConnector(), tile2.getConnector().getNext());
  }

  @Test
  public void testRotateThrice() {
    publicGameState.rotateSpare(3);
    Assert.assertEquals(publicGameState.getSpare().getConnector(),
            tile2.getConnector().getNext().getNext().getNext());
  }

  @Test
  public void testRotateOneThousand() {
    publicGameState.rotateSpare(1000);
    Assert.assertEquals(publicGameState.getSpare().getConnector(), tile2.getConnector());
  }

  @Test
  public void testRotateNegative() {
    publicGameState.rotateSpare(-1);
    Assert.assertEquals(publicGameState.getSpare().getConnector(),
            tile2.getConnector().getNext().getNext().getNext());
  }

  @Test
  public void testKickPlayer() {
    Assert.assertEquals(publicGameState.getFirstPlayer(), player1);
    Assert.assertEquals(publicGameState.getPlayers().size(), 4);
    Assert.assertTrue(publicGameState.getPlayers().contains(player1));

    publicGameState.kickActivePlayer();

    Assert.assertEquals(publicGameState.getPlayers().size(), 3);
    Assert.assertFalse(publicGameState.getPlayers().contains(player1));
    Assert.assertEquals(publicGameState.getFirstPlayer(), player2);
  }

  @Test
  public void testRoundChange() {
    publicGameState.incrementRound();
    Assert.assertEquals(publicGameState.getRoundNum(), 1);
  }

  @Test
  public void testGetBoard() {
    Assert.assertEquals(publicGameState.getBoard(), board1);
  }

  @Test
  public void testGetSpare() {
    Assert.assertTrue(tileEquals(publicGameState.getSpare(), tile2));
  }

  @Test
  public void testGetPlayers() {
    Assert.assertEquals(publicGameState.getPlayers(), players);
  }

  @Test
  public void testGoToNextActivePlayer() {
    Assert.assertEquals(publicGameState.getRoundNum(), 0);
    Assert.assertEquals(publicGameState.getActivePlayer(), player1);
    publicGameState.goToNextActivePlayer();
    Assert.assertFalse(publicGameState.hasRoundJustEnded());

    Assert.assertEquals(publicGameState.getRoundNum(), 0);
    Assert.assertEquals(publicGameState.getActivePlayer(), player2);
    publicGameState.goToNextActivePlayer();
    Assert.assertFalse(publicGameState.hasRoundJustEnded());

    Assert.assertEquals(publicGameState.getRoundNum(), 0);
    Assert.assertEquals(publicGameState.getActivePlayer(), player3);
    publicGameState.goToNextActivePlayer();
    Assert.assertFalse(publicGameState.hasRoundJustEnded());

    Assert.assertEquals(publicGameState.getRoundNum(), 0);
    Assert.assertEquals(publicGameState.getActivePlayer(), player4);
    publicGameState.goToNextActivePlayer();
    Assert.assertTrue(publicGameState.hasRoundJustEnded());

    Assert.assertEquals(publicGameState.getActivePlayer(), player1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveActivePlayerToNegativeX() {
    publicGameState.moveActivePlayerTo(new Coordinate(-1, 0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveActivePlayerToNegativeY() {
    publicGameState.moveActivePlayerTo(new Coordinate(0, -1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveActivePlayerToOutOfBoardX() {
    publicGameState.moveActivePlayerTo(new Coordinate(7, 0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveActivePlayerToOutOfBoardY() {
    publicGameState.moveActivePlayerTo(new Coordinate(0, 7));
  }

  @Test
  public void testMoveActivePlayer() {
    Assert.assertNotEquals(player1.currentPos, new Coordinate(0, 0));
    publicGameState.moveActivePlayerTo(new Coordinate(0, 0));
    Assert.assertEquals(player1.currentPos, new Coordinate(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNegativeXTarget() {
    Move move = new Move(new SimpleEntry<>(0, Direction.UP), 0, new Coordinate(-1, 0));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTooLargeXTarget() {
    Move move = new Move(new SimpleEntry<>(0, Direction.UP), 0,
        new Coordinate(publicGameState.getBoard().height, 0));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNegativeYTarget() {
    Move move = new Move(new SimpleEntry<>(0, Direction.UP), 0, new Coordinate(0, -1));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTooLargeYTarget() {
    Move move = new Move(new SimpleEntry<>(0, Direction.UP), 0,
        new Coordinate(0, publicGameState.getBoard().width));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNegativeColumnSlideIndex() {
    Move move = new Move(new SimpleEntry<>(-1, Direction.UP), 0, new Coordinate(0, 0));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveOddColumnSlideIndex() {
    Move move = new Move(new SimpleEntry<>(1, Direction.UP), 0, new Coordinate(0, 0));
    publicGameState.doTurn(Optional.of(move));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testMoveTooLargeColumnSlideIndex() {
    Move move = new Move(new SimpleEntry<>(7, Direction.UP), 0, new Coordinate(0, 0));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNegativeRowSlideIndex() {
    Move move = new Move(new SimpleEntry<>(-1, Direction.LEFT), 0, new Coordinate(0, 0));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveOddRowSlideIndex() {
    Move move = new Move(new SimpleEntry<>(1, Direction.LEFT), 0, new Coordinate(0, 0));
    publicGameState.doTurn(Optional.of(move));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testMoveTooLargeRowSlideIndex() {
    Move move = new Move(new SimpleEntry<>(7, Direction.LEFT), 0, new Coordinate(0, 0));
    publicGameState.doTurn(Optional.of(move));
  }

  @Test
  public void testMoveCanReachTargetSimple() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[0][1] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[2][0] = new Tile(gemList[1], gemList[1], Connector.UDLR);

    player1.currentPos = new Coordinate(0, 0);
    Move move = new Move(new SimpleEntry<>(2, Direction.LEFT), 0, new Coordinate(1, 0));
    publicGameState.doTurn(Optional.of(move));
    Assert.assertEquals(publicGameState.lastAction, Optional.of(new SimpleEntry<>(2, Direction.LEFT)));
    Assert.assertTrue(tileEquals(publicGameState.getSpare(), new Tile(gemList[1], gemList[1], Connector.UDLR)));
  }

  @Test
  public void testMoveCanReachTargetAfterRotate() {
    publicGameState.getBoard().getBoardTiles()[0][1] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UD);
    publicGameState.getBoard().getBoardTiles()[6][0] = new Tile(gemList[1], gemList[1], Connector.UDLR);

    player1.currentPos = new Coordinate(0, 0);
    publicGameState.spare = new Tile(publicGameState.spare.gem1, publicGameState.spare.gem2, Connector.UL);
    Move move = new Move(new SimpleEntry<>(0, Direction.DOWN), 2, new Coordinate(1, 0));
    publicGameState.doTurn(Optional.of(move));
    Assert.assertEquals(publicGameState.lastAction, Optional.of(new SimpleEntry<>(0, Direction.DOWN)));
    Assert.assertTrue(tileEquals(publicGameState.getSpare(), new Tile(gemList[1], gemList[1], Connector.UDLR)));
  }

  @Test
  public void testIsActivePlayerAtHome() {
    Assert.assertTrue(publicGameState.isActivePlayerAtHome());
    publicGameState.moveActivePlayerTo(new Coordinate(0, 5));
    Assert.assertFalse(publicGameState.isActivePlayerAtHome());
    publicGameState.moveActivePlayerTo(new Coordinate(1, 1));
    Assert.assertTrue(publicGameState.isActivePlayerAtHome());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShiftOdd() {
    publicGameState.shiftAndInsert(1, Direction.LEFT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShiftOutOfBounds() {
    publicGameState.shiftAndInsert(-1, Direction.LEFT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShiftNullDirection() {
    publicGameState.shiftAndInsert(4, null);
  }


  @Test
  public void testShiftLeft() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[0][0];
    publicGameState.shiftAndInsert(0, Direction.LEFT);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[0][6], tile2));
  }

  @Test
  public void testShiftRight() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[2][6];
    publicGameState.shiftAndInsert(2, Direction.RIGHT);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[2][0], tile2));
  }

  @Test
  public void testShiftUp() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[0][0];
    publicGameState.shiftAndInsert(0, Direction.UP);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[6][0], tile2));
  }

  @Test
  public void testShiftDown() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[6][2];
    publicGameState.shiftAndInsert(2, Direction.DOWN);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[0][2], tile2));
  }

  @Test
  public void testShiftPlayerLeft() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[0][0];
    player1.currentPos = new Coordinate(4, 0);
    publicGameState.shiftAndInsert(0, Direction.LEFT);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[0][6], tile2));
    Assert.assertEquals(player1.currentPos, new Coordinate(3, 0));
  }

  @Test
  public void testShiftPlayerUp() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[0][0];
    player1.currentPos = new Coordinate(0, 6);
    publicGameState.shiftAndInsert(0, Direction.UP);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[6][0], tile2));
    Assert.assertEquals(player1.currentPos, new Coordinate(0, 5));
  }

  @Test
  public void testShiftPlayerLeftOffBoard() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[0][0];
    player1.currentPos = new Coordinate(0, 0);
    publicGameState.shiftAndInsert(0, Direction.LEFT);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[0][6], tile2));
    Assert.assertEquals(player1.currentPos, new Coordinate(6, 0));
  }

  @Test
  public void testShiftPlayerUpOffBoard() {
    Tile t1 = publicGameState.getBoard().getBoardTiles()[0][0];
    player1.currentPos = new Coordinate(0, 0);
    publicGameState.shiftAndInsert(0, Direction.UP);
    Assert.assertTrue(tileEquals(t1, publicGameState.getSpare()));
    Assert.assertTrue(tileEquals(publicGameState.getBoard().getBoardTiles()[6][0], tile2));
    Assert.assertEquals(player1.currentPos, new Coordinate(0, 6));
  }

  @Test
  public void testCanPlayerReachNegativeXCoordinate() {
    Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(-1, 0)));
  }

  @Test
  public void testCanPlayerReachNegativeYCoordinate() {
    Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(0, -1)));
  }

  @Test
  public void testCanPlayerReachTooLargeXCoordinate() {
    Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(7, 0)));
  }

  @Test
  public void testCanPlayerReachTooLargeYCoordinate() {
    Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(0, 7)));
  }

  @Test
  public void testCanPlayerReachCorner() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UL);
    player1.currentPos = new Coordinate(0, 0);
    for (int row = 0; row < 7; row += 1) {
      for (int col = 0; col < 7; col += 1) {
        if(col == 0 && row == 0) {
          Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(col, row)));
        } else {
          Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(col, row)));
        }
      }
    }
  }

  @Test
  public void testCanPlayerReachEdge() {
    publicGameState.getBoard().getBoardTiles()[2][0] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[2][1] = new Tile(gemList[2], gemList[2], Connector.UD);
    player1.currentPos = new Coordinate(2, 0);
    for (int row = 0; row < 7; row += 1) {
      for (int col = 0; col < 7; col += 1) {
        if(col == 2 && row == 0) {
          Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(col, row)));
        } else {
          Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(col, row)));
        }
      }
    }
  }

  @Test
  public void testCanPlayerReachCycle() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    publicGameState.getBoard().getBoardTiles()[0][1] = new Tile(gemList[2], gemList[2], Connector.DL);
    publicGameState.getBoard().getBoardTiles()[1][0] = new Tile(gemList[3], gemList[3], Connector.UR);
    publicGameState.getBoard().getBoardTiles()[1][1] = new Tile(gemList[4], gemList[4], Connector.UL);
    player1.currentPos = new Coordinate(0, 0);
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(0, 0)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(1, 0)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(0, 1)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(1, 1)));
    Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(2, 0)));
  }

  @Test
  public void testCanPlayerReachComplex() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    publicGameState.getBoard().getBoardTiles()[0][1] = new Tile(gemList[1], gemList[2], Connector.DL);
    publicGameState.getBoard().getBoardTiles()[1][0] = new Tile(gemList[1], gemList[3], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[1][1] = new Tile(gemList[1], gemList[4], Connector.UDLR);
    publicGameState.getBoard().getBoardTiles()[1][2] = new Tile(gemList[1], gemList[5], Connector.UD);
    publicGameState.getBoard().getBoardTiles()[2][0] = new Tile(gemList[1], gemList[6], Connector.UDL);
    publicGameState.getBoard().getBoardTiles()[2][1] = new Tile(gemList[1], gemList[7], Connector.UDL);
    publicGameState.getBoard().getBoardTiles()[3][1] = new Tile(gemList[1], gemList[8], Connector.DR);

    player1.currentPos = new Coordinate(0, 0);
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(0,0)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(0,1)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(1,1)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(1,0)));
    Assert.assertTrue(publicGameState.canActivePlayerReach(new Coordinate(1,2)));
    Assert.assertFalse(publicGameState.canActivePlayerReach(new Coordinate(0,2)));
  }

  @Test
  public void testDefaultLastAction() {
    Optional<SimpleEntry<Integer, Direction>> defaultLastAction = publicGameState.lastAction;
    Assert.assertFalse(defaultLastAction.isPresent());
  }

}
