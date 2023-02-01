package players;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import components.ALabyrinthRules;
import components.Board;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PublicState;

public abstract class AStrategy implements IStrategy {

  //todo: mixing atomic with a composition
  @Override
  public Optional<Move> makeMove(PublicState state, Coordinate target) {
    Queue<Coordinate> candidates = getCandidates(state, target);
    return tryAllCandidates(state, candidates);
  }

  // Get a queue of the candidates in order of priority according to the given target coordinate.
  abstract protected Queue<Coordinate> getCandidates(PublicState state, Coordinate target);

  //Iterates through the queue of candidates and either returns the first valid move
  //or passes. (Optional.empty())
  private Optional<Move> tryAllCandidates(PublicState state, Queue<Coordinate> candidates) {
    Optional<Move> finalMove = Optional.empty();
    for (Coordinate candidate: candidates) {
      finalMove = tryCandidate(candidate, state);
      if (finalMove.isPresent()) {
        break;
      }
    }
    return finalMove;
  }

  // Tries a move with the given candidate, returns empty if candidate is not reachable
  private Optional<Move> tryCandidate(Coordinate candidate, PublicState state) {
    Queue<SimpleEntry<Integer, Direction>> slides = getSlides(state);
    return tryAllSlides(candidate, state, slides);
  }

  // Gets all the possible slides in order of row slides -> column slides.
  // For each row slide, tries left, then right.
  // For each column slide, tries up, then down.
  private Queue<SimpleEntry<Integer, Direction>> getSlides(PublicState state) {
    Queue<SimpleEntry<Integer, Direction>> slides = new ArrayDeque<>();
    Board b = state.getBoard();
    //todo: for all moveable rows
    for (int row = 0; row < b.height; row += 1) {
      if (b.validSlideRow(row)) {
        slides.add(new SimpleEntry<>(row, Direction.LEFT));
        slides.add(new SimpleEntry<>(row, Direction.RIGHT));
      }
    }
    for (int col = 0; col < b.width; col += 1) {
      if (b.validSlideCol(col)) {
        slides.add(new SimpleEntry<>(col, Direction.UP));
        slides.add(new SimpleEntry<>(col, Direction.DOWN));
      }
    }
    return slides;
  }

  //Iterates through the queue of slides and either returns the first valid move
  //or passes. (Optional.empty())
  private Optional<Move> tryAllSlides(Coordinate candidate, PublicState state,
      Queue<SimpleEntry<Integer, Direction>> slides) {
    Optional<Move> attemptedMove = Optional.empty();
    for (SimpleEntry<Integer, Direction> slide: slides) {
      attemptedMove = trySlide(candidate, slide, state);
      if (attemptedMove.isPresent()) {
        break;
      }
    }
    return attemptedMove;
  }

  // Tries a move with the given candidate and slide, returns empty if the given candidate is not
  // reachable after the given slide
  private Optional<Move> trySlide(Coordinate candidate,
                                  SimpleEntry<Integer, Direction> slide, PublicState state) {
    Queue<Integer> rotations = getAllRotations();
    return tryAllRotations(candidate, slide, state, rotations);
  }

  //Gets a 0 degree, 90 degree, 180 degree, and 270 degree counter-clockwise rotation.
  private Queue<Integer> getAllRotations() {
    return new ArrayDeque<>(Arrays.asList(0, -1, -2, -3));
  }

  //Iterates through the queue of rotations and either returns the first valid move
  //or passes. (Optional.empty())
  private Optional<Move> tryAllRotations(Coordinate candidate,
      SimpleEntry<Integer, Direction> slide, PublicState state, Queue<Integer> rotations) {
    Optional<Move> move = Optional.empty();
    for(int rotation: rotations) {
      move = tryRotation(candidate, slide, state, rotation);
      if(move.isPresent()) {
        break;
      }
    }
    return move;
  }

  // Tries a move with the given candidate, slide, and rotation, returns empty if the given
  // candidate is not reachable after the given rotation and slide.
  private Optional<Move> tryRotation(Coordinate candidate, SimpleEntry<Integer, Direction> slide,
      PublicState state, int rotation) {
    Move move = new Move(slide, rotation, candidate);
    if(ALabyrinthRules.canMove(move, state)) {
      return Optional.of(move);
    }
    return Optional.empty();
  }


  // Get all the possible candidates from the board
  protected List<Coordinate> getAllCandidates(PublicState state) {
    List<Coordinate> candidates = new ArrayList<>();
    Board b = state.getBoard();
    //todo: Not a board method
    for (int row = 0; row < b.height; row += 1) {
      for (int col = 0; col < b.width; col += 1) {
        Coordinate curCoordinate = new Coordinate(col, row);
        candidates.add(curCoordinate);
      }
    }
    return candidates;
  }
}
