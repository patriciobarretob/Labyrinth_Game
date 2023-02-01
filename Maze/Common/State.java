package components;

import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

// Keeps track of the current state of the game, i.e, all the information necessary
// for the referee to tell what is going on in the game at any given moment.
// This class is not in charge of rules.
public class RefereeState extends PublicState {
  public final Map<Color, Coordinate> goalMap;
  public RefereeState(Board board, Tile spare, Queue<PlayerInfo> players, Map<Color, Coordinate> goalMap) {
    super(board, spare, players);
    this.goalMap = goalMap;
  }

  public boolean isActivePlayerOnGoalTile() {
    PlayerInfo activePlayer = this.getActivePlayer();
    return activePlayer.currentPos.equals(goalMap.get(activePlayer.avatar));
  }

  @Override
  public void kickActivePlayer() {
    PlayerInfo activePlayer = this.getActivePlayer();
    goalMap.remove(activePlayer.avatar);
    super.kickActivePlayer();
  }
}
