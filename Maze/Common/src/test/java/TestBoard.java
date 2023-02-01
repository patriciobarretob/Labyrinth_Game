import components.Board;
import components.Connector;
import components.Coordinate;
import components.Direction;
import components.Tile;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestBoard extends ATestLabyrinth {

  @Test (expected = IllegalArgumentException.class)
  public void testConstructBoardTooSmall() {
    Board b = new Board(createBoard(1, 1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructBoardTooSmall2() {
    Board b = new Board(createBoard(2, 2));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructBoardEmpty() {
    Board b = new Board(createBoard(0, 0));
  }

  @Test
  public void testGetBoard() {
    Tile[][] board1Tiles = board1.getBoardTiles();
    for (int row = 0; row < board1Tiles.length; row += 1) {
      for (int col = 0; col < board1Tiles[0].length; col += 1) {
        Assert.assertTrue(tileEquals(board1Tiles[row][col], boardTiles[row][col]));
      }
    }
  }

  @Test
  public void testSlide3x3() {
    Tile t1 = board3x3.getBoardTiles()[0][0];
    Tile t2 = board3x3.slideAndInsert(0, Direction.LEFT, tile1);
    Assert.assertTrue(tileEquals(t1, t2));
    Assert.assertTrue(tileEquals(board3x3.getBoardTiles()[0][2], tile1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlide3x3Odd() {
    board3x3.slideAndInsert(1, Direction.LEFT, tile1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlide3x3OutOfBounds() {
    board3x3.slideAndInsert(4, Direction.LEFT, tile1);
  }

  @Test
  public void testSlide7x10() {
    Tile t1 = board7x10.getBoardTiles()[0][0];
    Tile t2 = board7x10.slideAndInsert(0, Direction.UP, tile1);
    Assert.assertTrue(tileEquals(t1, t2));
    Assert.assertTrue(tileEquals(board7x10.getBoardTiles()[9][0], tile1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlide7x10Odd() {
    board7x10.slideAndInsert(9, Direction.UP, tile1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlide7x10OutOfBounds() {
    board7x10.slideAndInsert(7, Direction.LEFT, tile1);
  }

  @Test
  public void testSlideLeft() {
    Tile t1 = board1.getBoardTiles()[0][0];
    Tile t2 = board1.slideAndInsert(0, Direction.LEFT, tile1);
    Assert.assertTrue(tileEquals(t1, t2));
    Assert.assertTrue(tileEquals(board1.getBoardTiles()[0][6], tile1));
  }

  @Test
  public void testSlideRight() {
    Tile t1 = board1.getBoardTiles()[2][6];
    Tile t2 = board1.slideAndInsert(2, Direction.RIGHT, tile1);
    Assert.assertTrue(tileEquals(t1, t2));
    Assert.assertTrue(tileEquals(board1.getBoardTiles()[2][0], tile1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlideOdd() {
    board1.slideAndInsert(1, Direction.LEFT, tile1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlideOutOfBounds() {
    board1.slideAndInsert(-1, Direction.LEFT, tile1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSlideNullDirection() {
    board1.slideAndInsert(4, null, tile1);
  }

  @Test
  public void testSlideUp() {
    Tile t1 = board1.getBoardTiles()[0][0];
    Tile t2 = board1.slideAndInsert(0, Direction.UP, tile1);
    Assert.assertTrue(tileEquals(t1, t2));
    Assert.assertTrue(tileEquals(board1.getBoardTiles()[6][0], tile1));
  }

  @Test
  public void testSlideDown() {
    Tile t1 = board1.getBoardTiles()[6][2];
    Tile t2 = board1.slideAndInsert(2, Direction.DOWN, tile1);
    Assert.assertTrue(tileEquals(t1, t2));
    Assert.assertTrue(tileEquals(board1.getBoardTiles()[0][2], tile1));
  }

  @Test
  public void testIsInBounds() {
    Assert.assertFalse(board1.isInBounds(new Coordinate(-1, 0)));
    Assert.assertFalse(board1.isInBounds(new Coordinate(0, -1)));
    Assert.assertFalse(board1.isInBounds(new Coordinate(7, 0)));
    Assert.assertFalse(board1.isInBounds(new Coordinate(0, 7)));
    for (int row = 0; row < board1.getBoardTiles().length; row += 1) {
      for (int col = 0; col < board1.getBoardTiles()[0].length; col += 1) {
        Assert.assertTrue(board1.isInBounds(new Coordinate(col, row)));
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReachablePositionsNegativeXCoordinate() {
    board1.getReachablePositions(new Coordinate(-1, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReachablePositionsNegativeYCoordinate() {
    board1.getReachablePositions(new Coordinate(0, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReachablePositionsTooLargeXCoordinate() {
    board1.getReachablePositions(new Coordinate(7, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReachablePositionsTooLargeYCoordinate() {
    board1.getReachablePositions(new Coordinate(0, 7));
  }

  @Test
  public void testGetReachablePositionsCorner() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UL);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(0,0)).size(), 1);
  }

  @Test
  public void testGetReachablePositionsEdge() {
    board1.getBoardTiles()[2][0] = new Tile(gemList[1], gemList[1], Connector.LR);
    board1.getBoardTiles()[2][1] = new Tile(gemList[2], gemList[2], Connector.UD);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(2,0)).size(), 1);
  }

  @Test
  public void testGetReachablePositionCycle() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    board1.getBoardTiles()[0][1] = new Tile(gemList[2], gemList[2], Connector.DL);
    board1.getBoardTiles()[1][0] = new Tile(gemList[3], gemList[3], Connector.UR);
    board1.getBoardTiles()[1][1] = new Tile(gemList[4], gemList[4], Connector.UL);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(0,0)).size(), 4);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(0,1)).size(), 4);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(1,0)).size(), 4);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(1,1)).size(), 4);
  }

  @Test
  public void testGetReachablePositionComplex() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    board1.getBoardTiles()[0][1] = new Tile(gemList[1], gemList[2], Connector.DL);
    board1.getBoardTiles()[1][0] = new Tile(gemList[1], gemList[3], Connector.LR);
    board1.getBoardTiles()[1][1] = new Tile(gemList[1], gemList[4], Connector.UDLR);
    board1.getBoardTiles()[1][2] = new Tile(gemList[1], gemList[5], Connector.UD);
    board1.getBoardTiles()[2][0] = new Tile(gemList[1], gemList[6], Connector.UDL);
    board1.getBoardTiles()[2][1] = new Tile(gemList[1], gemList[7], Connector.UDL);
    board1.getBoardTiles()[3][1] = new Tile(gemList[1], gemList[8], Connector.DR);

    Set<Coordinate> expectedCoords1 = new HashSet<>();
    expectedCoords1.add(new Coordinate(0, 0));
    expectedCoords1.add(new Coordinate(0, 1));
    expectedCoords1.add(new Coordinate(1, 1));
    expectedCoords1.add(new Coordinate(1, 0));
    expectedCoords1.add(new Coordinate(1, 2));
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(0,0)).size(), 5);
    Assert.assertEquals(new HashSet<>(board1.getReachablePositions(new Coordinate(0,0))),
            expectedCoords1);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(0,1)).size(), 5);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(1,0)).size(), 5);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(1,1)).size(), 5);
    Assert.assertEquals(board1.getReachablePositions(new Coordinate(1,2)).size(), 5);

  }

  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableNegativeTargetXCoord() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(0, 0), new Coordinate(-1, 0)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableNegativeStartXCoord() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(-1, 0), new Coordinate(0, 0)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableNegativeTargetYCoord() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(0, 0), new Coordinate(0, -1)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableNegativeStartYCoord() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(0, -1), new Coordinate(0, 0)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableTargetXCoordTooBig() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(0, 0), new Coordinate(7, 0)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableStartXCoordTooBig() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(7, 0), new Coordinate(0, 0)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableTargetYCoordTooBig() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(0, 0), new Coordinate(0, 7)));
  }
  @Test (expected = IllegalArgumentException.class)
  public void testIsPositionReachableStartYCoordTooBig() {
    Assert.assertFalse(board1.isPositionReachable(new Coordinate(0, 7), new Coordinate(0, 0)));
  }

  @Test
  public void testIsPositionReachableCorner() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UL);
    player1.currentPos = new Coordinate(0, 0);
    for (int row = 0; row < 7; row += 1) {
      for (int col = 0; col < 7; col += 1) {
        if(col == 0 && row == 0) {
          Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(col,
                  row)));
        } else {
          Assert.assertFalse(board1.isPositionReachable(player1.currentPos, new Coordinate(col,
                  row)));
        }
      }
    }
  }

  @Test
  public void testIsPositionReachableEdge() {
    board1.getBoardTiles()[2][0] = new Tile(gemList[1], gemList[1], Connector.LR);
    board1.getBoardTiles()[2][1] = new Tile(gemList[2], gemList[2], Connector.UD);
    player1.currentPos = new Coordinate(2, 0);
    for (int row = 0; row < 7; row += 1) {
      for (int col = 0; col < 7; col += 1) {
        if(col == 2 && row == 0) {
          Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(col,
                  row)));
        } else {
          Assert.assertFalse(board1.isPositionReachable(player1.currentPos, new Coordinate(col,
                  row)));
        }
      }
    }
  }

  @Test
  public void testIsPositionReachableCycle() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    board1.getBoardTiles()[0][1] = new Tile(gemList[2], gemList[2], Connector.DL);
    board1.getBoardTiles()[1][0] = new Tile(gemList[3], gemList[3], Connector.UR);
    board1.getBoardTiles()[1][1] = new Tile(gemList[4], gemList[4], Connector.UL);
    player1.currentPos = new Coordinate(0, 0);
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(0, 0)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(1, 0)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(0, 1)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(1, 1)));
    Assert.assertFalse(board1.isPositionReachable(player1.currentPos, new Coordinate(2, 0)));
  }

  @Test
  public void testIsPositionReachableComplex() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    board1.getBoardTiles()[0][1] = new Tile(gemList[1], gemList[2], Connector.DL);
    board1.getBoardTiles()[1][0] = new Tile(gemList[1], gemList[3], Connector.LR);
    board1.getBoardTiles()[1][1] = new Tile(gemList[1], gemList[4], Connector.UDLR);
    board1.getBoardTiles()[1][2] = new Tile(gemList[1], gemList[5], Connector.UD);
    board1.getBoardTiles()[2][0] = new Tile(gemList[1], gemList[6], Connector.UDL);
    board1.getBoardTiles()[2][1] = new Tile(gemList[1], gemList[7], Connector.UDL);
    board1.getBoardTiles()[3][1] = new Tile(gemList[1], gemList[8], Connector.DR);

    player1.currentPos = new Coordinate(0, 0);
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(0,0)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(0,1)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(1,1)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(1,0)));
    Assert.assertTrue(board1.isPositionReachable(player1.currentPos, new Coordinate(1,2)));
    Assert.assertFalse(board1.isPositionReachable(player1.currentPos, new Coordinate(0,2)));
  }

  @Test
  public void testOrderedCoordinates() {
    board1.getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.DR);
    board1.getBoardTiles()[0][1] = new Tile(gemList[1], gemList[2], Connector.DL);
    board1.getBoardTiles()[1][0] = new Tile(gemList[1], gemList[3], Connector.LR);
    board1.getBoardTiles()[1][1] = new Tile(gemList[1], gemList[4], Connector.UDLR);
    board1.getBoardTiles()[1][2] = new Tile(gemList[1], gemList[5], Connector.UD);
    board1.getBoardTiles()[2][0] = new Tile(gemList[1], gemList[6], Connector.UDL);
    board1.getBoardTiles()[2][1] = new Tile(gemList[1], gemList[7], Connector.UDL);
    board1.getBoardTiles()[3][1] = new Tile(gemList[1], gemList[8], Connector.DR);

    List<Coordinate> expectedCoordOrder1 = new ArrayList<>();
    expectedCoordOrder1.add(new Coordinate(0, 0));
    expectedCoordOrder1.add(new Coordinate(1, 0));
    expectedCoordOrder1.add(new Coordinate(0, 1));
    expectedCoordOrder1.add(new Coordinate(1, 1));
    expectedCoordOrder1.add(new Coordinate(1, 2));

    Assert.assertEquals(board1.getReachablePositions(new Coordinate(0, 0)),
            expectedCoordOrder1);
  }
}
