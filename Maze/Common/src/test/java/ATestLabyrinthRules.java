import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import components.ALabyrinthRules;
import components.Board;
import components.Connector;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PlayerInfo;
import components.PublicState;
import components.SingleGoalRefereeState;
import components.Tile;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;
import java.util.Queue;
import org.junit.Assert;
import org.junit.Test;

public class ATestLabyrinthRules extends ATestLabyrinth {


  @Test
  public void testIsLegalBoard() {
    Board b = new Board(createBoard(startBoard));
    Assert.assertTrue(ALabyrinthRules.isLegalBoard(b));
    Tile[][] allTiles = b.getBoardTiles();
    allTiles[3][2] = allTiles[5][6];
    Assert.assertFalse(ALabyrinthRules.isLegalBoard(b));
  }

  @Test
  public void testIsLegalBoardIllegalEvenHeight() {
    startBoard = new char[][]{
        {'─', '┐', '└', '┌', '┘', '┬', '├'},
        {'┴', '─', '─', '─', '─', '┐', '└'},
        {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
        {'│', '─', '┐', '└', '┌', '┘', '┬'},
        {'├', '┴', '┤', '┼', '│', '─', '┐'},
        {'└', '┌', '┘', '┬', '├', '┴', '┤'}
    };
    Board b = new Board(createBoard(startBoard));
    Assert.assertFalse(ALabyrinthRules.isLegalBoard(b));
  }

  @Test
  public void testGameOver1kRounds() {
    Board b = new Board(createBoard(startBoard));
    Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
    PublicState ps = new PublicState(b, spare, createPlayerInfoList());
    SingleGoalRefereeState s = new SingleGoalRefereeState(ps, createInfoMap());

    for (int i = 0; i < 4000; i += 1) {
      assertFalse(ALabyrinthRules.isGameOverWithoutTerminator(s));
      s.publicState.goToNextActivePlayer();
      if(s.publicState.hasRoundJustEnded()) {
        s.publicState.incrementRound();
      }
    }
    assertTrue(ALabyrinthRules.isGameOverWithoutTerminator(s));
  }

  @Test
  public void testGameOverEveryPlayerPassed() {
    Queue<PlayerInfo> players = createPlayerInfoList();
    p1PlayerInfo.skippedThisRound = true;
    p2PlayerInfo.skippedThisRound = true;
    p3PlayerInfo.skippedThisRound = true;
    p4PlayerInfo.skippedThisRound = true;

    Board b = new Board(createBoard(startBoard));
    Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
    PublicState ps = new PublicState(b, spare, players);
    SingleGoalRefereeState s = new SingleGoalRefereeState(ps, createInfoMap());

    assertTrue(ALabyrinthRules.isGameOverWithoutTerminator(s));
  }


  @Test
  public void testMoveInvalidTarget() {
    Move move = new Move(new SimpleEntry<>(0, Direction.UP), 0, new Coordinate(-1, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(0, Direction.UP),
        0, new Coordinate(publicGameState.getBoard().height, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(0, Direction.UP),
        0,new Coordinate(0, -1));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(0, Direction.UP),
        0,new Coordinate(0, publicGameState.getBoard().width));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
  }

  @Test
  public void testMoveInvalidSlideIndex() {
    Move move = new Move(new SimpleEntry<>(-1, Direction.UP), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(1, Direction.UP), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(7, Direction.UP), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(-1, Direction.LEFT), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(1, Direction.LEFT), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
    move = new Move(new SimpleEntry<>(7, Direction.LEFT), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
  }

  @Test
  public void testMoveCantReachSelf() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UL);
    player1.currentPos = new Coordinate(0, 0);
    Move move = new Move(new SimpleEntry<>(2, Direction.LEFT), 0, new Coordinate(0, 0));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
  }

  @Test
  public void testMoveCantReachTarget() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UL);
    player1.currentPos = new Coordinate(0, 0);
    Move move = new Move(new SimpleEntry<>(2, Direction.LEFT), 0, new Coordinate(5, 3));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
  }

  @Test
  public void testMoveCantUndo() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UL);
    publicGameState.lastAction = Optional.of(new SimpleEntry<>(2, Direction.RIGHT));
    Move move = new Move(new SimpleEntry<>(2, Direction.LEFT), 0, new Coordinate(5, 3));
    assertFalse(ALabyrinthRules.canMove(move, publicGameState));
  }

  @Test
  public void testMoveCanReachTargetSimple() {
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[0][1] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[2][0] = new Tile(gemList[1], gemList[1], Connector.UDLR);

    player1.currentPos = new Coordinate(0, 0);
    Move move = new Move(new SimpleEntry<>(2, Direction.LEFT), 0, new Coordinate(1, 0));
    assertTrue(ALabyrinthRules.canMove(move, publicGameState));
  }

  @Test
  public void testMoveCanReachTargetAfterRotate() {
    publicGameState.getBoard().getBoardTiles()[0][1] = new Tile(gemList[1], gemList[1], Connector.LR);
    publicGameState.getBoard().getBoardTiles()[0][0] = new Tile(gemList[1], gemList[1], Connector.UD);
    publicGameState.getBoard().getBoardTiles()[6][0] = new Tile(gemList[1], gemList[1], Connector.UDLR);

    player1.currentPos = new Coordinate(0, 0);
    publicGameState.spare = new Tile(publicGameState.spare.gem1, publicGameState.spare.gem2, Connector.UL);
    Move move = new Move(new SimpleEntry<>(0, Direction.DOWN), 2, new Coordinate(1, 0));
    assertTrue(ALabyrinthRules.canMove(move, publicGameState));
  }


}
