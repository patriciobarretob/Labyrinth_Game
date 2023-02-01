package components;

import java.util.AbstractMap.SimpleEntry;
import java.util.Objects;

/**
 * This class represents all three steps of a valid move that a player makes in a Labyrinth game,
 * including rotating the spare tile, sliding a row or column in a direction, and moving to a
 * target coordinate.
 */
public class Move {
  //Represents sliding a row or column on the board in the given direction.
  public final SimpleEntry<Integer, Direction> slide;

  //Number of rotations 90 degrees clockwise, for example,
  // 1 will rotate 90 degrees clockwise, -1 will rotate 90 degrees counterclockwise.
  public final int rotations;

  //The Coordinate of the tile to move to.
  public final Coordinate target;

  public Move(SimpleEntry<Integer, Direction> slide, int rotations, Coordinate target) {
    this.slide = slide;
    this.rotations = rotations;
    this.target = target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Move move = (Move) o;
    return rotations == move.rotations && Objects.equals(slide, move.slide) && Objects.equals(target, move.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slide, rotations, target);
  }

  @Override
  public String toString() {
    return "Move{" +
        "slide=" + slide +
        ", rotations=" + rotations +
        ", target=" + target +
        '}';
  }
}
