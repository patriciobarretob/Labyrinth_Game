package components;

import java.util.Objects;

/**
 * Represents a Coordinate on a board. 0,0 represents the top left of the board.
 * Starting from 0, x increases as the coordinate goes right across the columns.
 * Starting from 0, y increases as the coordinate goes down across the rows.
 */
public class Coordinate {
  public final int x;
  public final int y;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public double distanceBetween(Coordinate other) {
    return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return x == that.x && y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Coordinate{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }
}
