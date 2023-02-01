package players;

import java.util.ArrayDeque;
import java.util.Queue;

import components.Coordinate;
import components.PublicState;

public class RiemannStrategy extends AStrategy {

  // Get a queue of the candidates in order of goal first, then row-column order

  // Technically this should be sorted using row-column order but getAllCandidates already gets
  // them in that order, so we're fine on that aspect.
  @Override
  protected Queue<Coordinate> getCandidates(PublicState state, Coordinate target) {
    Queue<Coordinate> candidates = new ArrayDeque<>();
    candidates.add(target);
    for (Coordinate coord : getAllCandidates(state)) {
      if (!coord.equals(target)) {
        candidates.add(coord);
      }
    }
    return candidates;
  }
}
