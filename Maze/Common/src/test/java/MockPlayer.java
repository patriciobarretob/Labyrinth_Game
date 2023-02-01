import components.Board;
import components.Coordinate;
import components.Move;
import components.PublicState;
import java.util.Optional;
import java.util.Queue;

import players.IPlayer;

public class MockPlayer implements IPlayer {
  private StringBuilder sb;
  private String name;
  private Board board;
  private Queue<Optional<Move>> moves;

  public static String NAME_CALLED_STRING = "name() was called!";
  public static String PROPOSE_BOARD_CALLED_STRING = "proposeBoard0() was called with rows and " +
          "columns:";
  public static String SETUP_CALLED_STRING = "setup() was called with state0 and goal:";
  public static String TAKE_TURN_CALLED_STRING = "takeTurn() was called with state:";
  public static String WON_CALLED_STRING = "won() was called with win boolean:";


  public MockPlayer(StringBuilder sb, String name, Board board, Queue<Optional<Move>> moves) {
    this.sb = sb;
    this.name = name;
    this.board = board;
    this.moves = moves;
  }

  @Override
  public String name() {
    sb.append(NAME_CALLED_STRING);
    sb.append("\n");
    return name;
  }

  @Override
  public Board proposeBoard0(int rows, int columns) {
    sb.append(PROPOSE_BOARD_CALLED_STRING);
    sb.append("\n");
    sb.append(rows);
    sb.append("\n");
    sb.append(columns);
    sb.append("\n");
    return board;
  }

  @Override
  public void setup(Optional<PublicState> state0, Coordinate goal) {
    sb.append(SETUP_CALLED_STRING);
    sb.append("\n");
    sb.append(state0);
    sb.append("\n");
    sb.append(goal);
    sb.append("\n");
  }

  @Override
  public Optional<Move> takeTurn(PublicState s) {
    sb.append(TAKE_TURN_CALLED_STRING);
    sb.append("\n");
    sb.append(s);
    sb.append("\n");
    return moves.remove();
  }

  @Override
  public void won(boolean w) {
    sb.append(WON_CALLED_STRING);
    sb.append("\n");
    sb.append(w);
    sb.append("\n");
  }
}
