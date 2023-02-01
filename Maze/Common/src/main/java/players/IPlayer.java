package players;

import java.util.Optional;

import components.Board;
import components.Coordinate;
import components.Move;
import components.PublicState;

/**
 * Represents the functionality a player needs to provide to the referee in order for the referee
 * to play a game with this player.
 */
public interface IPlayer {

  // Returns the name of this player
  String name();

  // Return this player's proposed board of a minimum number of given rows and columns.
  Board proposeBoard0(int rows, int columns);

  // If state0 is not empty, informs this player of the initial state of the game and what the
  // player's goal is.
  // If state0 is empty, reminds this player of its home coordinate. This would be the case when
  // the player reaches their initial goal.
  void setup(Optional<PublicState> state0, Coordinate goal);

  // Get this player's move at the given state of the game. Will return Optional.empty() if
  // this player has elected to pass.
  Optional<Move> takeTurn(PublicState s);

  // Informs this player if it has won or not.
  void won(boolean w);
}
