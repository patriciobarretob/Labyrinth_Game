package components;

import java.util.Optional;

/**
 * Represents the information and functionality a referee needs to play a game with a player.
 */
public interface IPlayer {
  //Get the player's move at the given state of the game. Will return Optional.empty() if
  //the player has elected to pass.
  Optional<Move> makeMove(PublicState state);
}
