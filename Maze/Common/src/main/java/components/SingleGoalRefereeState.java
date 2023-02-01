package components;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

// Keeps track of the current state of the game, i.e, all the information necessary
// for the referee to tell what is going on in the game at any given moment.
// This class is not in charge of rules.
public class SingleGoalRefereeState extends ARefereeState {
  public SingleGoalRefereeState(Board board, Tile spare, Queue<PlayerInfo> players, Map<Color, SingleGoalRefereePlayerInfo> infoMap) {
    super(board, spare, players,new HashMap<>(infoMap));
  }

  public SingleGoalRefereeState(PublicState publicState, Map<Color, SingleGoalRefereePlayerInfo> infoMap) {
    super(publicState, new HashMap<>(infoMap));
  }

  public SingleGoalRefereeState(SingleGoalRefereeState otherState) {
    super(otherState);
  }

  //Marks that the active player has reached its goal
  public void updateActivePlayerHasReachedGoal() {
    PlayerInfo activePlayer = this.publicState.getActivePlayer();
    SingleGoalRefereePlayerInfo activePlayerInfo = (SingleGoalRefereePlayerInfo)infoMap.get(activePlayer.avatar);
    activePlayerInfo.setHasReachedGoal(true);
  }

  @Override
  protected IRefereePlayerInfo getPlayerInfoCopy(IRefereePlayerInfo original) {
    return new SingleGoalRefereePlayerInfo((SingleGoalRefereePlayerInfo)original);
  }
}
