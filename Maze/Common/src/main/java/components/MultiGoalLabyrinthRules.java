package components;

//Represents game rules that are unique for a multi goal game
public class MultiGoalLabyrinthRules extends ALabyrinthRules {

    //Checks if the active player is game terminating player
    //Active player is terminator when there are no more available goals and it reaches its home
    //which is equal to its goal
    @Override
    public boolean activePlayerEndedGame(ARefereeState state) {
        MultiGoalRefereeState rState = (MultiGoalRefereeState) state;
        Coordinate curGoal = rState.getCurrentPlayerAsMultiGoalRefPlayer().getGoal();
        Coordinate curHome = rState.publicState.getActivePlayer().home;
        Coordinate curPos = rState.publicState.getActivePlayer().currentPos;
        return !rState.hasRemainingGoals() && curPos.equals(curGoal) && curGoal.equals(curHome);
    }
}
