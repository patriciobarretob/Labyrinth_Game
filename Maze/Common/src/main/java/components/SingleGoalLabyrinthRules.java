package components;

//Represents the game rules that are unique for single goal games
public class SingleGoalLabyrinthRules extends ALabyrinthRules {

    //checks if the active player is game terminating player
    //active player is terminator when it has reached its home after collecting its treasure
    @Override
    public boolean activePlayerEndedGame(ARefereeState state) {
        SingleGoalRefereePlayerInfo activePlayerInfo =
                (SingleGoalRefereePlayerInfo) state.infoMap.get(state.publicState.getActivePlayer().avatar);
        return state.publicState.isActivePlayerAtHome() &&
                activePlayerInfo.isGoingHome();
    }
}
