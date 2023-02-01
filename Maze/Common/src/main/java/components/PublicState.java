package components;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

// Represents the public information of a game
// A "round" represents every player taking a turn, starting from the first player
// and ending after the last player according to the initial ordering of players.
// This state represents that a round ends either when a player is kicked or we move on
// to the next player.

// When it comes to board mutating methods, only doTurn should be called. All other methods are
// public for testing reasons.
// (This means that rotateSpare, shiftAndInsert, and moveActivePlayerTo are not safe methods 
// on their own to call for a game to run smoothly.)

// Recommended use of this class:
// - Get a move from a player.
//    - If the move is valid, call doTurn on it, check if the current player has won, and if not
//    then call getNextActivePlayer
//    - If the move is invalid, call kickActivePlayer
// - After either of these, it should be checked if a round has just ended.
//    - If it has,
//       1. Increment rount counter
//       2. Check if the game has ended (if it has, terminate)
//       3. Reset player skips
//    - IN THAT ORDER
// - Repeat the above.
// TODO: turn the above into methods that probably make more sense, and remove dangerous ones

// By the way, both the referee class & the referee state depend on player avatars in their
// respective PlayerInfo being unique, as we use that as an identifier. That's probably bad, but
// we don't know a better design.
// TODO: ensure that avatars are unique when constructing a game state
public class PublicState {
  protected final Board board;
  public Tile spare;
  // The queue of players is in turn order,
  // with the frontmost player in the queue being the active player.
  protected final Queue<PlayerInfo> players;
  public Optional<SimpleEntry<Integer, Direction>> lastAction;

  //Represents the number of rounds played so far. For example, once every player has played once,
  //then roundNum will be equal to 1 after calling goToNextActivePlayer() when the final player is
  //the active player.
  private int roundNum;
  private PlayerInfo firstPlayer;

  public PublicState(Board board, Tile spare, Queue<PlayerInfo> players) {
    this.board = board;
    this.spare = spare;
    this.players = players;
    this.lastAction = Optional.empty();
    this.roundNum = 0;
    this.firstPlayer = players.peek();
  }

  /**
   * Constructs a PublicState that does not accept players with duplicate homes. This naturally
   * means that the number of players is restricted by the number of possible homes.
   */
  public static PublicState constructStrictPublicState(Board board, Tile spare,
                                                Queue<PlayerInfo> players) {
    validatePlayers(players);
    validateBoardWithPlayers(board, players);
    return new PublicState(board, spare, players);
  }

  private static void validateBoardWithPlayers(Board b, Queue<PlayerInfo> players) {
    List<Coordinate> validHomeCoords = ALabyrinthRules.getAllImmovableCoordinates(b);
    if (validHomeCoords.size() < players.size()) {
      throw new IllegalArgumentException(String.format("There cannot be more players than homes! " +
              "There are %d players and %d homes!", players.size(), validHomeCoords.size()));
    }
  }


  /**
   * Throws if players have duplicate homes.
   */
  private static void validatePlayers(Queue<PlayerInfo> players) {
    Set<Coordinate> homeCoords = new HashSet<>();
    for (PlayerInfo p : players) {
      if (homeCoords.contains(p.home)) {
        throw new IllegalArgumentException("Public State Constructor: Players should not have duplicate homes.");
      }
      homeCoords.add(p.home);
    }
  }


  //Creates a copy of a given PublicState. This creates a copy of all mutable fields.
  public PublicState(PublicState originalState) {
    this.board = new Board(originalState.board);
    this.spare = originalState.spare;

    this.players = new ArrayDeque<>();
    for(PlayerInfo player : originalState.players) {
      this.players.add(new PlayerInfo(player));
    }

    Optional<SimpleEntry<Integer, Direction>> oldLastAction  = originalState.lastAction;
    this.lastAction = oldLastAction.isPresent() ?
        Optional.of(new SimpleEntry<>(oldLastAction.get().getKey(), oldLastAction.get().getValue())) :
        oldLastAction;

    this.roundNum = originalState.roundNum;
    this.firstPlayer = originalState.firstPlayer;
  }





  //1 turn represents a 90 degree turn clockwise.
  //-1 turns represents a 90 degree turn counter-clockwise.
  public void rotateSpare(int turns) {
    for(int i = 0; i < Math.floorMod(turns, 4); i++) {
      this.spare = this.spare.rotate();
    }
  }

  // Handles a given turn. If the move is empty, indicates the current player has passed.
  // Otherwise, makes the move.
  public void doTurn(Optional<Move> move) {
    if (!move.isPresent()) {
      handlePass();
    } else {
      handleMove(move.get());
    }
  }

  // Indicates the current player has passed
  private void handlePass() {
    getActivePlayer().skippedThisRound = true;
  }


  // Executes the given move.
  private void handleMove(Move move) {
    this.rotateSpare(move.rotations);
    this.shiftAndInsert(move.slide.getKey(), move.slide.getValue());
    this.moveActivePlayerTo(move.target);
    this.lastAction = Optional.of(move.slide);
  }


  //If the Direction is LEFT or RIGHT, the index represents the row to be shifted,
  // with 0 representing the top row and increasing going downwards.
  //If the Direction is UP or DOWN, the index represents the column to be shifted,
  // with 0 representing the leftmost row and increasing going right.
  // This method will throw an exception when trying to shift an unshiftable row/column
  public void shiftAndInsert(int index, Direction direction) {
    this.spare = this.board.slideAndInsert(index, direction, this.spare);
    shiftPlayers(index, direction);
  }

  //Shift all the players in the given column or row in the given direction
  private void shiftPlayers(int index, Direction direction) {
    if (direction == Direction.UP) {
      shiftPlayersVert(index, -1);
    } else if (direction == Direction.DOWN){
      shiftPlayersVert(index, 1);
    } else if (direction == Direction.LEFT) {
      shiftPlayersHoriz(index, -1);
    } else if (direction == Direction.RIGHT){
      shiftPlayersHoriz(index, 1);
    } else {
      throw new IllegalArgumentException("Direction is null!");
    }
  }

  //Delta is the amount players get shifted. For up it will be -1, for down it will be 1.
  private void shiftPlayersVert(int index, int delta) {
    for (PlayerInfo player: players) {
      if (player.currentPos.x == index) {
        int newY = Math.floorMod((player.currentPos.y + delta), Board.BOARD_DIMENSION);
        player.currentPos = new Coordinate(player.currentPos.x, newY);
      }
    }
  }

  //Delta is the amount players get shifted. For up it will be -1, for down it will be 1.
  private void shiftPlayersHoriz(int index, int delta) {
    for (PlayerInfo player: players) {
      if (player.currentPos.y == index) {
        int newX = Math.floorMod((player.currentPos.x + delta), Board.BOARD_DIMENSION);
        player.currentPos = new Coordinate(newX, player.currentPos.y);
      }
    }
  }

  public boolean canActivePlayerReach(Coordinate target) {
    if (!board.isInBounds(target)) {
      return false;
    }
    PlayerInfo activePlayer = this.getActivePlayer();
    return board.isPositionReachable(activePlayer.currentPos, target);
  }

  public boolean isActivePlayerAtHome() {
    PlayerInfo activePlayer = getActivePlayer();
    return activePlayer.currentPos.equals(activePlayer.home);
  }

  /**
   * Kicks a player using their information.
   */
  public void kickNonActivePlayer(PlayerInfo pi) {

    players.remove(pi);
    if(players.size() == 0) {
      return;
    }
    if(pi.equals(this.firstPlayer)) {
      this.firstPlayer = players.peek();
    }
  }

  //Kicks the currently active player. If this player was the first player in a round,
  // updates firstPlayer to start a round with the next player.
  public void kickActivePlayer() {
    PlayerInfo removedPlayer = players.remove();
    if(players.size() == 0) {
      return;
    }
    if(removedPlayer.equals(this.firstPlayer)) {
      this.firstPlayer = players.peek();
    }
  }

  // Removes the currently active player from the queue and appends it to the end of the queue.
  // Do not call this after kicking the active player, as it will skip the next player's turn.
  // This method updates the round number if the next player is the first player in the initial
  // order of players.
  public void goToNextActivePlayer() {
    players.add(players.remove());
  }

  public boolean hasRoundJustEnded() {
    return (this.players.size() == 0 || players.peek().equals(this.firstPlayer));
  }

  public void incrementRound() {
    this.roundNum += 1 ;
  }

  public void resetSkips() {
    for(PlayerInfo player : this.getPlayers()) {
      player.skippedThisRound = false;
    }
  }

  public void moveActivePlayerTo(Coordinate target) {
    if (!this.board.isInBounds(target)) {
      throw new IllegalArgumentException("Given target coordinate is not in bounds.");
    }
    getActivePlayer().currentPos = target;
  }

  public Board getBoard() {
    return this.board;
  }

  public Tile getSpare() {
    return this.spare;
  }

  public Queue<PlayerInfo> getPlayers() {
    return this.players;
  }

  public PlayerInfo getActivePlayer() {
    return this.players.peek();
  }

  public int getRoundNum() { return this.roundNum; }

  public PlayerInfo getFirstPlayer() { return this.firstPlayer; }

}
