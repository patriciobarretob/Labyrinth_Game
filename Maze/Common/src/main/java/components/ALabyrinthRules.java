package components;

import components.Board.RowColumnOrder;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

//Represents the game rules that do not vary between single and multi goal games
public abstract class ALabyrinthRules{

  // Given the width/height of the a board, is the given column/row slidable?
  static boolean isBoardIndexSlidable(int index, int length) {
    return (index >= 0 && index < length) && index % 2 == 0;
  }

  //Gets a List of all of the coordinates on a board that cannot be slid vertically or horizontally.
  public static List<Coordinate> getAllImmovableCoordinates(Board b) {
    List<Coordinate> out = new ArrayList<>();
    for(int row = 0; row < b.height; row++)  {
      for(int col = 0; col < b.width; col++) {
        if(!b.validSlideRow(row) && !b.validSlideCol(col)) {
          out.add(new Coordinate(row, col));
        }
      }
    }
    return out;
  }
  // Determines if the given board is legal,
  // A board is not legal if there are two tiles with the same pair of gems,
  public static boolean isLegalBoard(Board board) {
    if(!isValidDimensions(board.height) || !isValidDimensions(board.width)) {
      return false;
    }

    List<HashSet<Gem>> seenGemSets = new ArrayList<>();
    Tile[][] tiles = board.getBoardTiles();

    //checks to see gems are unique unordered pairs
    for(int row = 0; row < board.height; row++) {
      for(int col = 0; col < board.width; col++) {
        Tile t = tiles[row][col];
        HashSet<Gem> gemSet = new HashSet<>();
        gemSet.add(t.gem1);
        gemSet.add(t.gem2);
        if(seenGemSets.contains(gemSet)) {
          return false;
        }
        seenGemSets.add(gemSet);
      }
    }
    return true;
  }

  //This method does not currently follow the spec of allowing even dimensions
  // Checks to make sure that the board dimensions are greater than 1 and odd.
  private static boolean isValidDimensions(int dimension) {
    return dimension > 1 && dimension % 2 == 1;
  }

  // Determines if the given game state is over without an active terminating player.
  // A game ends when:
  // - All players that survive a round opt to pass
  // - The game has run 1000 rounds
  public static boolean isGameOverWithoutTerminator(ARefereeState state) {
    return allPlayersPassed(state) || gameRan1kRounds(state);
  }

  //checks if all players in a round have passed
  protected static boolean allPlayersPassed(ARefereeState state) {
    for (PlayerInfo player: state.publicState.getPlayers()) {
      if (!player.skippedThisRound) {return false;}
    }
    return true;
  }

  protected static boolean gameRan1kRounds(ARefereeState state) {
    return state.publicState.getRoundNum() >= 1000;
  }

  // Has the active player just ended the game?
  public abstract boolean activePlayerEndedGame(ARefereeState state);

  // Determines if the given move is valid.
  // A move is invalid if:
  // - The shift index is out of bounds
  // - The shift column or row is odd
  // - The target coordinate is out of bounds
  // - The target coordinate is where the player is currently at
  // - The target coordinate is unreachable from the player's position
  // - The shift undoes the shift of the last player
  public static boolean canMove(Move move, PublicState state) {
    PublicState stateCopy = new PublicState(state);
    if(!canShift(move, stateCopy)) {
      return false;
    }
    if(stateCopy.getActivePlayer().currentPos.equals(move.target)) {
      return false;
    }
    return checkReachable(move, stateCopy);
  }

  //Attempts to rotate the spare tile and call shiftAndInsert on the state.
  //Returns false if the shift is invalid. Returns true otherwise.
  private static boolean canShift(Move move, PublicState stateCopy) {
    if(undoesLastAction(move, stateCopy)) {
      return false;
    }
    stateCopy.rotateSpare(move.rotations);
    try{
      stateCopy.shiftAndInsert(move.slide.getKey(), move.slide.getValue());
    } catch(IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  //Returns true if the given move undoes the last action. Returns true otherwise.
  private static boolean undoesLastAction(Move move, PublicState stateCopy) {
    Optional<SimpleEntry<Integer, Direction>> lastAction = stateCopy.lastAction;
    if(!lastAction.isPresent()) {
      return false;
    }
    SimpleEntry<Integer, Direction> lastActionNonEmpty = lastAction.get();
    SimpleEntry<Integer, Direction> currAction = move.slide;
    if(currAction.getKey().equals(lastActionNonEmpty.getKey()) &&
        currAction.getValue() == lastActionNonEmpty.getValue().getOpposite()) {
      return true;
    }
    return false;
  }


  //Determines whether a player is allowed to move to a target coordinate.
  //Returns false if the target coordinate is equal to the player's current position,
  //or if the target coordinate is unreachable. Returns true otherwise.
  private static boolean checkReachable(Move move, PublicState stateCopy) {
    if(stateCopy.getActivePlayer().currentPos.equals(move.target)) {
      return false;
    }
    return stateCopy.canActivePlayerReach(move.target);
  }

  //sorts given list of coordinates by row-column order
  public static List<Coordinate> sortCoordinates(List<Coordinate> coords){
    List<Coordinate> sortedList = new ArrayList<>(coords);
    sortedList.sort(new RowColumnOrder());
    return sortedList;
  }

}
