package components;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

//Represents a state which is only available to the referee, implements common functionality
//between single and multi goal referee state
abstract public class ARefereeState {

    public final Map<Color, IRefereePlayerInfo> infoMap;
    //todo: figure out how to deal with the disconnect between PlayerInfo and RefereePlayerInfo
    //todo: and between the PublicState and RefereeState. Every idea we have right now creates something in parallel.
    public final PublicState publicState;

    public ARefereeState(Board board, Tile spare, Queue<PlayerInfo> players,
                                  Map<Color, IRefereePlayerInfo> infoMap) {
        this.infoMap = infoMap;
        this.publicState = new PublicState(board, spare, players);
    }

    public ARefereeState(PublicState publicState,
                                  Map<Color, IRefereePlayerInfo> infoMap) {
        this.infoMap = infoMap;
        this.publicState = publicState;
    }

    public ARefereeState(ARefereeState otherState){
        this.publicState = new PublicState(otherState.publicState);
        Map<Color, IRefereePlayerInfo> newInfoMap = new HashMap<>();
        for (Map.Entry<Color, IRefereePlayerInfo> entry : otherState.infoMap.entrySet()) {
            newInfoMap.put(entry.getKey(), getPlayerInfoCopy(entry.getValue()));
        }
        this.infoMap = newInfoMap;
    }

    protected abstract IRefereePlayerInfo getPlayerInfoCopy(IRefereePlayerInfo original);

    /**
     * This method kicks a player that is not necessarily active using its avatar color.
     */
    public void kickNonActivePlayer(PlayerInfo pi) {

        infoMap.remove(pi.avatar);
        this.publicState.kickNonActivePlayer(pi);
    }


    // Returns true if the currently active player is on its goal tile.
    // Returns false otherwise.
    public boolean isActivePlayerOnGoalTile() {
        PlayerInfo activePlayer = this.publicState.getActivePlayer();
        return activePlayer.currentPos.equals(infoMap.get(activePlayer.avatar).getGoal());
    }

    //Kicks the currently active player. Removes it from infoMap and from the publicState queue.
    public void kickActivePlayer() {
        PlayerInfo activePlayer = this.publicState.getActivePlayer();
        infoMap.remove(activePlayer.avatar);
        this.publicState.kickActivePlayer();
    }
}
