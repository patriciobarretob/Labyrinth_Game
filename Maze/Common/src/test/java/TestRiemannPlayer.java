import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import components.Board;
import components.Connector;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PlayerInfo;
import components.PublicState;
import players.RiemannStrategy;
import components.Tile;

public class TestRiemannPlayer extends ATestLabyrinth {

  // No playable moves
  @Test
  public void testRiemannStrategyNoPlayableMoves() {
    char[][] boardTiles = new char[][]{
        {'─', '┐', '└', '┌', '┘', '┬', '├'},
        {'┴', '─', '─', '─', '─', '┐', '└'},
        {'┌', '┘', '┘', '─', '└', '┤', '┼'},
        {'│', '─', '│', '└', '│', '┘', '┬'},
        {'├', '┴', '┐', '─', '┌', '─', '┐'},
        {'└', '┌', '┘', '┬', '├', '┴', '┤'},
        {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(3, 3);
    Coordinate playerGoal = new Coordinate(5, 5);

    testGeneralExpectEmpty(boardTiles, spareConnector, playerStart, playerGoal);
  }

  @Test
  public void testOptimalMoveUndoesLast() {
    char[][] boardTiles = new char[][]{
        {'┬', '┬', '┬', '┬', '┬', '┬', '┬'},
        {'┴', '┴', '─', '─', '└', '┘', '│'},
        {'┌', '┘', '┘', '─', '│', '─', '│'},
        {'│', '─', '│', '└', '│', '┘', '│'},
        {'├', '┴', '─', '│', '─', '─', '│'},
        {'─', '┌', '│', '┬', '│', '┌', '┘'},
        {'─', '─', '─', '─', '─', '┘', '┌'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(3, 0);
    Coordinate playerGoal = new Coordinate(1, 1);
    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.RIGHT);
    int expectedRotations = 0;
    Coordinate expectedTarget = new Coordinate(1,1);
    SimpleEntry<Integer, Direction> lastAction = new SimpleEntry<>(0, Direction.RIGHT);
    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
        expectedRotations, expectedTarget, lastAction);
  }

  // Goal is reachable currently but not after any slide
  @Test
  public void testRiemannStrategyGoalReachableButNotAfterSlide() {
    char[][] boardTiles = new char[][]{
        {'─', '─', '│', '│', '┌', '─', '┐'},
        {'┴', '─', '─', '─', '└', '┘', '│'},
        {'┌', '┘', '┘', '─', '│', '─', '│'},
        {'│', '─', '│', '└', '│', '┘', '│'},
        {'├', '┴', '─', '│', '─', '─', '│'},
        {'─', '┌', '│', '┬', '│', '┌', '┘'},
        {'─', '─', '─', '─', '─', '┘', '┌'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(0, 6);
    Coordinate playerGoal = new Coordinate(5, 1);
    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.DOWN);
    int expectedRotations = 0;
    Coordinate expectedTarget = new Coordinate(1,0);

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
        expectedRotations, expectedTarget);
  }

  // literally everything except for 6, 6 is unreachable
  @Test
  public void testRiemannStrategyLastAlternativeGoal() {
    char[][] boardTiles = new char[][]{
        {'─', '─', '─', '─', '─', '│', '─'},
        {'─', '┘', '│', '┴', '│', '│', '─'},
        {'─', '│', '│', '├', '│', '│', '┼'},
        {'│', '│', '│', '└', '│', '│', '┬'},
        {'├', '┴', '│', '┼', '│', '│', '─'},
        {'└', '│', '│', '│', '│', '│', '─'},
        {'│', '│', '─', '│', '│', '│', '─'}
    };
    Connector spareConnector = Connector.getFromChar('┘');
    Coordinate playerStart = new Coordinate(6, 6);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(6, Direction.LEFT);
    int expectedRotations = 0;
    Coordinate expectedTarget = new Coordinate(6,6);

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
        expectedRotations, expectedTarget);
  }

  // goal & first row isn't reachable, goes to 0, 1
  @Test
  public void testRiemannStrategySecondRowAlternativeGoal() {
    char[][] boardTiles = new char[][]{
        {'─', '─', '─', '─', '─', '─', '─'},
        {'─', '┘', '│', '┴', '─', '┐', '└'},
        {'─', '│', '│', '├', '┴', '┤', '┼'},
        {'│', '│', '│', '└', '┌', '┘', '┬'},
        {'├', '┴', '│', '┼', '│', '─', '┐'},
        {'└', '│', '│', '│', '├', '┴', '┤'},
        {'┼', '│', '─', '│', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┘');
    Coordinate playerStart = new Coordinate(1, 1);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.LEFT);
    int expectedRotations = 0;
    Coordinate expectedTarget = new Coordinate(0,1);

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
        expectedRotations, expectedTarget);
  }

  // goal & 0, 0 not reachable, goes to 1, 0
  @Test
  public void testRiemannStrategySecondAlternativeGoal() {
    char[][] boardTiles = new char[][]{
        {'─', '─', '│', '┐', '┘', '┬', '├'},
        {'─', '┘', '│', '┴', '─', '┐', '└'},
        {'─', '│', '─', '├', '┴', '┤', '┼'},
        {'│', '│', '│', '└', '┌', '┘', '┬'},
        {'├', '┴', '│', '┼', '│', '─', '┐'},
        {'└', '│', '│', '│', '├', '┴', '┤'},
        {'┼', '│', '─', '│', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┘');
    Coordinate playerStart = new Coordinate(0, 1);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.LEFT);
    int expectedRotations = 0;
    Coordinate expectedTarget = new Coordinate(1,0);

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
        expectedRotations, expectedTarget);
  }

  //Goal is not reachable, so it goes to 0, 0
  @Test
  public void testRiemannStrategyFirstAlternativeGoal() {
    char[][] boardTiles = new char[][]{
        {'─', '│', '│', '┐', '┘', '┬', '├'},
        {'┴', '│', '│', '┴', '─', '┐', '└'},
        {'─', '│', '─', '├', '┴', '┤', '┼'},
        {'│', '│', '│', '└', '┌', '┘', '┬'},
        {'├', '┴', '│', '┼', '│', '─', '┐'},
        {'└', '│', '│', '│', '├', '┴', '┤'},
        {'┼', '│', '─', '│', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┘');
    Coordinate playerStart = new Coordinate(0, 1);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.LEFT);
    int expectedRotations = 0;
    Coordinate expectedTarget = new Coordinate(0,0);

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
        expectedRotations, expectedTarget);
  }


  // Goal is reachable after a specific tile rotation & slide
  @Test
  public void testRiemannStrategyReachableAfterSlideAndRotate() {
    char[][] boardTiles = new char[][]{
            {'─', '┐', '│', '┐', '┘', '┬', '├'},
            {'┴', '─', '│', '┴', '─', '┐', '└'},
            {'┌', '┘', '─', '├', '┴', '┤', '┼'},
            {'│', '─', '│', '└', '┌', '┘', '┬'},
            {'├', '┴', '│', '┼', '│', '─', '┐'},
            {'└', '│', '│', '│', '├', '┴', '┤'},
            {'┼', '│', '─', '│', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┘');
    Coordinate playerStart = new Coordinate(2, 6);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(2, Direction.DOWN);
    int expectedRotations = -2;

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
            expectedRotations, playerGoal);
  }

  // Goal is reachable after getting slid off
  @Test
  public void testRiemannStrategyReachableAfterGettingSlidOff() {
    char[][] boardTiles = new char[][]{
            {'─', '┐', '│', '┐', '┘', '┬', '├'},
            {'┴', '─', '│', '┴', '─', '┐', '└'},
            {'┌', '┘', '─', '├', '┴', '┤', '┼'},
            {'│', '─', '│', '└', '┌', '┘', '┬'},
            {'├', '┴', '│', '┼', '│', '─', '┐'},
            {'└', '│', '│', '│', '├', '┴', '┤'},
            {'┼', '│', '─', '│', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(2, 6);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(2, Direction.DOWN);
    int expectedRotations = 0;

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
            expectedRotations, playerGoal);
  }

  // Goal is reachable after getting slid
  @Test
  public void testRiemannStrategyReachableAfterGettingSlid() {
    char[][] boardTiles = new char[][]{
            {'─', '┐', '│', '┌', '┘', '┬', '├'},
            {'┴', '─', '│', '─', '─', '┐', '└'},
            {'┌', '┘', '─', '├', '┴', '┤', '┼'},
            {'│', '─', '│', '└', '┌', '┘', '┬'},
            {'├', '┴', '│', '┼', '│', '─', '┐'},
            {'└', '┌', '│', '┬', '├', '┴', '┤'},
            {'┼', '│', '│', '┐', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(2, 2);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(2, Direction.UP);
    int expectedRotations = 0;

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
            expectedRotations, playerGoal);
  }

  // Goal is reachable after a slide
  @Test
  public void testRiemannStrategyReachableAfterSlide() {
    char[][] boardTiles = new char[][]{
            {'─', '┐', '│', '┌', '┘', '┬', '├'},
            {'┴', '─', '│', '─', '─', '┐', '└'},
            {'┌', '┘', '─', '├', '┴', '┤', '┼'},
            {'│', '─', '│', '└', '┌', '┘', '┬'},
            {'├', '┴', '│', '┼', '│', '─', '┐'},
            {'└', '┌', '│', '┬', '├', '┴', '┤'},
            {'┼', '│', '│', '┐', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(1, 1);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(2, Direction.UP);
    int expectedRotations = 0;

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
            expectedRotations, playerGoal);
  }

  // Goal is a bit far away but still easily reachable
  @Test
  public void testRiemannStrategyABitFarAway() {
    char[][] boardTiles = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'┴', '─', '─', '─', '─', '┐', '└'},
            {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
            {'│', '─', '┐', '└', '┌', '┘', '┬'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(1, 1);
    Coordinate playerGoal = new Coordinate(5, 5);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.LEFT);
    int expectedRotations = 0;

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
            expectedRotations, playerGoal);
  }

  // Goal is easily reachable
  @Test
  public void testRiemannStrategyEasilyReachable() {
    char[][] boardTiles = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'┴', '─', '─', '─', '─', '┐', '└'},
            {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
            {'│', '─', '┐', '└', '┌', '┘', '┬'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };
    Connector spareConnector = Connector.getFromChar('┌');
    Coordinate playerStart = new Coordinate(1, 1);
    Coordinate playerGoal = new Coordinate(3, 1);

    SimpleEntry<Integer, Direction> expectedSlide = new SimpleEntry<>(0, Direction.LEFT);
    int expectedRotations = 0;

    testGeneral(boardTiles, spareConnector, playerStart, playerGoal, expectedSlide,
            expectedRotations, playerGoal);
  }

  private void testGeneralExpectEmpty(char[][] boardTiles, Connector spareConnector,
                                      Coordinate playerStarting, Coordinate playerGoal) {
    PublicState s = generateState(boardTiles, spareConnector, playerStarting);
    RiemannStrategy riemannPlayer = new RiemannStrategy();
    Optional<Move> move = riemannPlayer.makeMove(s, playerGoal);
    Optional<Move> expectedMove = Optional.empty();
    Assert.assertEquals(expectedMove, move);
  }

  private void testGeneral(char[][] boardTiles, Connector spareConnector, Coordinate playerStarting,
                           Coordinate playerGoal, SimpleEntry<Integer, Direction> expectedSlide,
                           int expectedRotations, Coordinate expectedTarget) {
    PublicState s = generateState(boardTiles, spareConnector, playerStarting);
    RiemannStrategy riemannPlayer = new RiemannStrategy();
    Optional<Move> move = riemannPlayer.makeMove(s, playerGoal);
    Optional<Move> expectedMove = Optional.of(new Move(expectedSlide, expectedRotations,
        expectedTarget));
    Assert.assertEquals(expectedMove, move);
  }

  private void testGeneral(char[][] boardTiles, Connector spareConnector, Coordinate playerStarting,
      Coordinate playerGoal, SimpleEntry<Integer, Direction> expectedSlide,
      int expectedRotations, Coordinate expectedTarget, SimpleEntry<Integer, Direction> lastAction) {
    PublicState s = generateState(boardTiles, spareConnector, playerStarting);
    s.lastAction = Optional.of(lastAction);
    RiemannStrategy riemannPlayer = new RiemannStrategy();
    Optional<Move> move = riemannPlayer.makeMove(s, playerGoal);
    Optional<Move> expectedMove = Optional.of(new Move(expectedSlide, expectedRotations,
        expectedTarget));
    Assert.assertEquals(expectedMove, move);
  }

  private PublicState generateState(char[][] boardTiles, Connector spareConnector,
                                    Coordinate start) {
    Tile spare = new Tile(gemList[0], gemList[49], spareConnector);
    Board board = createBoard(boardTiles);
    PlayerInfo player1 = new PlayerInfo(Color.BLUE, new Coordinate(1, 1));
    player1.currentPos = start;
    Queue<PlayerInfo> playerInfoQueue = new ArrayDeque<>();
    playerInfoQueue.add(player1);
    return new PublicState(board, spare, playerInfoQueue);
  }
}
