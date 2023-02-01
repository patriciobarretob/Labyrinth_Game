package players;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import components.Coordinate;
import components.PublicState;

public class EuclidStrategy extends AStrategy {

  // Get a queue of the candidates in order of goal first, then proximity to goal.
  // In the event of a tie, chooses in row-column order
  @Override
  protected Queue<Coordinate> getCandidates(PublicState state, Coordinate target) {
    Queue<Coordinate> candidates = new ArrayDeque<>();
    candidates.add(target);
    List<Coordinate> allCandidates = getAllCandidates(state);
    allCandidates.remove(target);
    EuclidComparator comp = new EuclidComparator(target);
    allCandidates.sort(comp);
    candidates.addAll(allCandidates);
    return candidates;
  }

  private static class EuclidComparator implements Comparator<Coordinate> {
    private final Coordinate target;

    EuclidComparator(Coordinate target) {
      this.target = target;
    }

    // Priority in order of goal first, then proximity to goal.
    // In the event of a tie, chooses in row-column order
    @Override
    public int compare(Coordinate o1, Coordinate o2) {
      int comparedDistance = Double.compare(target.distanceBetween(o1), target.distanceBetween(o2));
      if (comparedDistance == 0) {
        if (o1.y == o2.y) {
          return Integer.compare(o1.x, o2.x);
        } else {
          return Integer.compare(o1.y, o2.y);
        }
      } else {
        return Double.compare(target.distanceBetween(o1), target.distanceBetween(o2));
      }
    }
  }
}
