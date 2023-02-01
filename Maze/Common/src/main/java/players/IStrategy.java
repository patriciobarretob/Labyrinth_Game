package players;

import java.util.Optional;

import components.Coordinate;
import components.Move;
import components.PublicState;

/**
 * Represents a strategy that a player can use at a given state of the game.
 */
public interface IStrategy {
  // Get this strategy's move at the given state of the game to go to the given target. Will return
  // Optional.empty() if this strategy has elected to pass.
  Optional<Move> makeMove(PublicState state, Coordinate target);
}
