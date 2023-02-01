package players;

import components.SingleGoalRefereeState;

import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;

// Represents an omniscient observer of a Labyrinth game.
// Provides methods to allow the Referee to send it information,
// to get the first state in the queue of states,
// and to save a JSON representation of the state in a file.
public class Observer {
  private final ArrayDeque<SimpleEntry<SingleGoalRefereeState, Map<Color, String>>> states;
  private final Stack<SimpleEntry<SingleGoalRefereeState, Map<Color, String>>> previousStates;
  public boolean isGameOver = true;
  public Observer() {
    this.states = new ArrayDeque<>();
    this.previousStates = new Stack<>();
  }

  // Receives the current state of the game.
  public void receiveState(SingleGoalRefereeState s, Map<Color, String> nameMap) {
    states.add(new SimpleEntry<>(s, nameMap));
  }

  // Informs this observer that the game has ended.
  public void informGameOver() {
    this.isGameOver = true;
  }

  //Attempt to get the next state from the queue.
  //If the queue is empty, return Optional.empty()
  //If the queue is not empty, return an optional of the first entry in the queue.
  public Optional<SimpleEntry<SingleGoalRefereeState, Map<Color, String>>> getNextState() {
    if (this.states.isEmpty()) {
      return Optional.empty();
    } else {
      SimpleEntry<SingleGoalRefereeState, Map<Color, String>> currState = this.states.poll();
      this.previousStates.add(currState);
      return Optional.of(currState);
    }
  }

  //Attempt to get the previous state from the queue.
  //If the queue is empty, return Optional.empty()
  //If the queue is not empty, return an optional of the first entry in the queue.
  public Optional<SimpleEntry<SingleGoalRefereeState, Map<Color, String>>> getPreviousState() {
    if (this.previousStates.isEmpty()) {
      return Optional.empty();
    } else {
      SimpleEntry<SingleGoalRefereeState, Map<Color, String>> currState = this.previousStates.pop();
      this.states.addFirst(currState);
      return Optional.of(currState);
    }
  }
}
