package components;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

// Represents a 7 by 7 square board full of Tiles.
public class Board {
  //TODO: get rid of this v
  //TODO: method to return all tiles in a 1d array
  public final static int BOARD_DIMENSION = 7;
  // ... and uses these v
  public final int width;
  public final int height;

  // boardTiles is in row-column order.
  private Tile[][] boardTiles;

  public Board(Tile[][] boardTiles) {
    ensureValidBoard(boardTiles);
    this.boardTiles = boardTiles;
    this.height = boardTiles.length;
    this.width = boardTiles[0].length;
  }

  // Ensures that the board is valid.
  // An invalid board contains null tiles, zero-length dimensions, or is non-rectangular
  private static void ensureValidBoard(Tile[][] boardTiles) {
    if(boardTiles == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    if(boardTiles.length == 0) {
      throw new IllegalArgumentException("Board length cannot be zero");
    }
    int width = boardTiles[0].length;
    if(width <= 2 || boardTiles.length <= 2) {
      throw new IllegalArgumentException("Board is too small to support players");
    }
    for(int i = 0; i < boardTiles.length; i++) {
      Tile[] row = boardTiles[i];
      if(row == null) {
        throw new IllegalArgumentException("Row cannot be null");
      }
      if(row.length <= 0 || row.length != width) {
        throw new IllegalArgumentException("Every row length must be non-zero "
                + "and board must be rectangular");
      }
      for(Tile t : row) {
        if(t == null) {
          throw new IllegalArgumentException("Tile cannot be null");
        }
      }
    }
  }

  //Creates a copy of the given board. Tiles must be immutable.
  public Board(Board originalBoard) {
    Tile[][] oldTiles = originalBoard.boardTiles;
    this.boardTiles = new Tile[oldTiles.length][oldTiles[0].length];
    for(int row = 0; row < oldTiles.length; row++) {
      for(int col = 0; col < oldTiles[0].length; col++) {
        this.boardTiles[row][col] = oldTiles[row][col];
      }
    }
    this.height = originalBoard.height;
    this.width = originalBoard.width;
  }

  public Tile[][] getBoardTiles() {
    return this.boardTiles;
  }

  public boolean validSlideRow(int row) {
    return ALabyrinthRules.isBoardIndexSlidable(row, this.height);
  }

  public boolean validSlideCol(int col) {
    return ALabyrinthRules.isBoardIndexSlidable(col, this.width);
  }

  private boolean validSlideIndex(int index, Direction direction)  {
    if (direction == null) {
      return false;
    }
    if (direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT)) {
      return validSlideRow(index);
    } else {
      return validSlideCol(index);
    }
  }

  private boolean validRow(int row) {
    return row >= 0 && row < this.height;
  }

  private boolean validCol(int col) {
    return col >= 0 && col < this.width;
  }

  //Slides the tile at the given index in the given direction, and then inserts a spare into the
  // open slot created by that slide
  //The index is either a row or a column, counting from the top left.
  public Tile slideAndInsert(int index, Direction direction, Tile spare) {
    if (!validSlideIndex(index, direction)) {
      throw new IllegalArgumentException("Not a valid index to slide");
    }
    Tile spareTile;
    if (direction == Direction.UP) {
      spareTile = slideVert(spare, true, index);
    } else if (direction == Direction.DOWN){
      spareTile = slideVert(spare, false, index);
    } else if (direction == Direction.LEFT) {
      spareTile = slideHoriz(spare, true, index);
    } else if (direction == Direction.RIGHT){
      spareTile = slideHoriz(spare, false, index);
    } else {
      throw new IllegalArgumentException("Direction is null!");
    }
    return spareTile;
  }

  // Slides a row at the given index. Reversed indicates that the tile should be slid
  // starting from the end of the row.
  private Tile slideHoriz(Tile spare, boolean reversed, int index) {
    Tile[] rowToBeSlid = this.boardTiles[index];
    if (reversed) {
      rowToBeSlid = reverseTileArray(rowToBeSlid);
    }
    Tile newSpare = rowToBeSlid[rowToBeSlid.length - 1];
    Tile[] slidRow = slideTiles(spare, rowToBeSlid);
    if (reversed) {
      slidRow = reverseTileArray(slidRow);
    }
    this.boardTiles[index] = slidRow;
    return newSpare;
  }

  // Slides a column at the given index. Reversed indicates that the tile should be slid
  // starting from the end of the column.
  private Tile slideVert(Tile spare, boolean reversed, int index) {
    Tile[] columnToBeSlid = new Tile[this.boardTiles.length];
    for (int i = 0; i < this.boardTiles.length; i += 1) {
      columnToBeSlid[i] = this.boardTiles[i][index];
    }
    if (reversed) {
      columnToBeSlid = reverseTileArray(columnToBeSlid);
    }
    Tile newSpare = columnToBeSlid[columnToBeSlid.length - 1];
    Tile[] slidColumn = slideTiles(spare, columnToBeSlid);
    if (reversed) {
      slidColumn = reverseTileArray(slidColumn);
    }
    for (int i = 0; i < this.boardTiles.length; i += 1) {
      this.boardTiles[i][index] = slidColumn[i];
    }
    return newSpare;
  }

  //Assume the given array of tiles needs to be slide starting from index 0.
  private Tile[] slideTiles(Tile spare, Tile[] toBeSlid) {
    Tile prevTile = spare;
    for (int i = 0; i < toBeSlid.length; i += 1) {
      Tile nextTile = toBeSlid[i];
      toBeSlid[i] = prevTile;
      prevTile = nextTile;
    }
    return toBeSlid;
  }


  private Tile[] reverseTileArray(Tile[] toBeReversed) {
    Tile[] retArr = new Tile[toBeReversed.length];
    for (int i = toBeReversed.length - 1; i >= 0; i -= 1) {
      retArr[toBeReversed.length - i - 1] = toBeReversed[i];
    }
    return retArr;
  }

  // Returns the reachable positions from this current position in row-column order
  // without including the current position.
  public List<Coordinate> getReachablePositions(Coordinate location) {
    if (!validCol(location.x) || !validRow(location.y)) {
      throw new IllegalArgumentException("Not a valid index");
    }

    Set<Coordinate> visited = new HashSet<>();
    Queue<Coordinate> queue = new ArrayDeque<>();
    queue.add(location);
    while(!queue.isEmpty()) {
      Coordinate coord = queue.remove();

      if(visited.contains(coord)) { continue; }
      visited.add(coord);

      Set<Coordinate> neighbors = getConnectedNeighbors(coord);
      queue.addAll(neighbors);
    }

    List<Coordinate> orderedVisited = new ArrayList<>(visited);
    orderedVisited.sort(new RowColumnOrder());

    return orderedVisited;
  }

  // Determines if the given target coordinate is reachable from the given location.
  public boolean isPositionReachable(Coordinate start, Coordinate target) {
    if (!validCol(start.x) || !validRow(start.y)
            || !validCol(target.x) || !validRow(target.y)) {
      throw new IllegalArgumentException("Not a valid index");
    }

    Set<Coordinate> visited = new HashSet<>();
    Queue<Coordinate> queue = new ArrayDeque<>();
    queue.add(start);
    while(!queue.isEmpty()) {
      Coordinate coord = queue.remove();

      if(coord.equals(target)) { return true; }
      if(visited.contains(coord)) { continue; }
      visited.add(coord);

      Set<Coordinate> neighbors = getConnectedNeighbors(coord);
      queue.addAll(neighbors);
    }

    return false;
  }

  public static class RowColumnOrder implements Comparator<Coordinate> {

    @Override
    public int compare(Coordinate o1, Coordinate o2) {
      if (o1.y == o2.y) {
        return Integer.compare(o1.x, o2.x);
      } else {
        return Integer.compare(o1.y, o2.y);
      }
    }
  }

  private Set<Coordinate> getConnectedNeighbors(Coordinate location) {
    Tile currSlot = boardTiles[location.y][location.x];
    Set<Coordinate> out = new HashSet<>();
    if(currSlot.getConnector().connectsUp) {
      Coordinate upNeighbor = new Coordinate(location.x, location.y - 1);
      if(connectsDown(upNeighbor)) {
        out.add(upNeighbor);
      }
    }
    if(currSlot.getConnector().connectsDown) {
      Coordinate downNeighbor = new Coordinate(location.x, location.y + 1);
      if(connectsUp(downNeighbor)) {
        out.add(downNeighbor);
      }
    }
    if(currSlot.getConnector().connectsLeft) {
      Coordinate leftNeighbor = new Coordinate(location.x - 1, location.y);
      if(connectsRight(leftNeighbor)) {
        out.add(leftNeighbor);
      }
    }
    if(currSlot.getConnector().connectsRight) {
      Coordinate rightNeighbor = new Coordinate(location.x + 1, location.y);
      if(connectsLeft(rightNeighbor)) {
        out.add(rightNeighbor);
      }
    }
    return out;
  }

  private boolean connectsDown(Coordinate location) {
    if(!isInBounds(location)) { return false; }
    Tile currTile = boardTiles[location.y][location.x];
    return currTile.getConnector().connectsDown;
  }

  private boolean connectsUp(Coordinate location) {
    if(!isInBounds(location)) { return false; }
    Tile currTile = boardTiles[location.y][location.x];
    return currTile.getConnector().connectsUp;
  }

  private boolean connectsLeft(Coordinate location) {
    if(!isInBounds(location)) { return false; }
    Tile currTile = boardTiles[location.y][location.x];
    return currTile.getConnector().connectsLeft;
  }

  private boolean connectsRight(Coordinate location) {
    if(!isInBounds(location)) { return false; }
    Tile currTile = boardTiles[location.y][location.x];
    return currTile.getConnector().connectsRight;
  }

  public boolean isInBounds(Coordinate location) {
    return validCol(location.x) && validRow(location.y);
  }

}
