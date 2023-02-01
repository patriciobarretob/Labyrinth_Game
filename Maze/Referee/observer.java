package players;

import components.RefereeState;

import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

// Represents an omniscient observer of a Labyrinth game. Provides methods to allow the Referee
// to send it information.
public class Observer {
  private final Queue<SimpleEntry<RefereeState, Map<Color, String>>> states;

  public Observer() {
    this.states = new ArrayDeque<>();
  }

  public Observer(Queue<SimpleEntry<RefereeState, Map<Color, String>>> states) {
    this.states = states;
  }


  // Receives the current state of the game.
  public void receiveState(RefereeState s, Map<Color, String> nameMap) {
    states.add(new SimpleEntry<>(s, nameMap));
  }

  // Informs this observer that the game has ended.
  public void informGameOver() {

  }

  public Optional<SimpleEntry<RefereeState, Map<Color, String>>> getNextState() {
    if (this.states.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(this.states.poll());
    }
  }

  public void toFile(Optional<RefereeState> s) {

  }


}
