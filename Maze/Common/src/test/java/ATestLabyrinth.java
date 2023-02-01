import components.*;
import players.IStrategy;
import players.RiemannStrategy;
import java.util.AbstractMap.SimpleEntry;
import java.awt.Color;
import java.util.*;

import org.junit.Before;

public abstract class ATestLabyrinth {
  //------------------------------ FIELDS FOR TESTING BOARD ---------------------------------
  public static final String[] LIST_OF_GEM_NAMES = Gem.ALL_VALID_GEM_NAMES;

  public static final int RANDOM_SEED = 1;


  Gem alexandrite;
  Gem aplite;
  Gem beryl;
  Gem amethyst;

  Gem[] gemList;

  Tile tile1;
  Tile tile2;
  Tile tile3;
  Tile tile4;
  Tile emptyTile;

  Tile[][] boardTiles;

  Board board1;

  Board board3x3;

  Board board7x10;

  //------------------------------ FIELDS FOR TESTING STATE ---------------------------------

  PlayerInfo player1;
  PlayerInfo player2;
  PlayerInfo player3;
  PlayerInfo player4;
  Queue<PlayerInfo> players;
  Map<Color, SingleGoalRefereePlayerInfo> infoMap;
  PublicState publicGameState;
  SingleGoalRefereeState singleGoalRefereeState;

  //------------------------------ FIELDS FOR TESTING PLAYER ---------------------------------

  IStrategy riemann1;

  //------------------------------ FIELDS FOR TESTING REFEREE --------------------------------

  char[][] proposedBoard;

  char[][] startBoard;
  Connector spareConnector;
  List<String> expectedWinnerNames;
  List<String> expectedCheaterNames;

  String p1Name;
  boolean testP1Kicked;
  Optional<Move>[] p1Moves;
  boolean p1HasReachedGoal;
  StringBuilder p1SB;
  Coordinate p1Home;
  Coordinate p1Goal;
  Coordinate p1CurPos;
  Color p1Color;
  List<String> p1ExpectedOutput;
  PlayerInfo p1PlayerInfo;

  String p2Name;
  boolean testP2Kicked;
  Optional<Move>[] p2Moves;
  boolean p2HasReachedGoal;
  StringBuilder p2SB;
  Coordinate p2Home;
  Coordinate p2Goal;
  Coordinate p2CurPos;
  Color p2Color;
  List<String> p2ExpectedOutput;
  PlayerInfo p2PlayerInfo;

  String p3Name;
  boolean testP3Kicked;
  Optional<Move>[] p3Moves;
  boolean p3HasReachedGoal;
  StringBuilder p3SB;
  Coordinate p3Home;
  Coordinate p3Goal;
  Coordinate p3CurPos;
  Color p3Color;
  List<String> p3ExpectedOutput;
  PlayerInfo p3PlayerInfo;

  String p4Name;
  boolean testP4Kicked;
  Optional<Move>[] p4Moves;
  boolean p4HasReachedGoal;
  StringBuilder p4SB;
  Coordinate p4Home;
  Coordinate p4Goal;
  Coordinate p4CurPos;
  Color p4Color;
  List<String> p4ExpectedOutput;
  PlayerInfo p4PlayerInfo;


  // --------------------------------- FIELDS FOR MULTIPLE GOAL STUFF --------------------------

  Map<Color, MultiGoalRefereePlayerInfo> multiGoalRefereePlayerInfoMap;

  MultiGoalRefereeState multiGoalRefereeState;

  Queue<Coordinate> multiGoalGoals;

  int p1NumGoals;
  int p2NumGoals;
  int p3NumGoals;
  int p4NumGoals;

  ALabyrinthRules singleRules;
  ALabyrinthRules multiRules;

  @Before
  public void setup() {
    setUpBoard();

    setUpState();

    setUpPlayer();

    this.singleGoalRefereeState = new SingleGoalRefereeState(publicGameState, infoMap);

    setUpReferee();

    setUpMultiGoalRefInfoMap();

    this.multiGoalRefereeState = new MultiGoalRefereeState(publicGameState,
            multiGoalRefereePlayerInfoMap);

    setUpMultiGoalReferee();
  }

  private void setUpMultiGoalRefInfoMap() {
    multiGoalRefereePlayerInfoMap = new HashMap<>();
    multiGoalRefereePlayerInfoMap.put(player1.avatar,
            new MultiGoalRefereePlayerInfo(new Coordinate(3, 3)));
    multiGoalRefereePlayerInfoMap.put(player2.avatar,
            new MultiGoalRefereePlayerInfo(new Coordinate(3,1)));
    multiGoalRefereePlayerInfoMap.put(player3.avatar,
            new MultiGoalRefereePlayerInfo(new Coordinate(5,5)));
    multiGoalRefereePlayerInfoMap.put(player4.avatar,
            new MultiGoalRefereePlayerInfo(new Coordinate(1,1)));
  }

  private void setUpPlayer() {
    riemann1 = new RiemannStrategy();
  }

  private void setUpState() {


    generatePlayerInfo();
    players = new ArrayDeque<>(Arrays.asList(player1, player2, player3, player4));

    generateInfoMap();

    publicGameState = new PublicState(board1, tile2, players);
  }

  private void setUpBoard() {
    alexandrite = new Gem("alexandrite");
    aplite = new Gem("aplite");
    beryl = new Gem("beryl");
    amethyst = new Gem("amethyst");
    tile1 = new Tile(alexandrite, aplite, Connector.UDLR);
    tile2 = new Tile(alexandrite, beryl, Connector.LR);
    tile3 = new Tile(amethyst, aplite, Connector.DLR);
    tile4 = new Tile(amethyst, beryl, Connector.DL);
    createGems();
    createBoard();
    board1 = new Board(boardTiles);
    board3x3 = new Board(createBoard(3, 3));
    board7x10 = new Board(createBoard(7, 10));
  }

  public void setUpReferee() {
    this.proposedBoard = new char[][]{
            {'─', '┐', '└', '┌', '┘', '┬', '├'},
            {'┴', '─', '─', '─', '─', '┐', '└'},
            {'┌', '┘', '┬', '├', '┴', '┤', '┼'},
            {'│', '─', '┐', '└', '┌', '┘', '┬'},
            {'├', '┴', '┤', '┼', '│', '─', '┐'},
            {'└', '┌', '┘', '┬', '├', '┴', '┤'},
            {'┼', '│', '─', '┐', '└', '┌', '┘'}
    };

    this.startBoard = proposedBoard;
    this.spareConnector = Connector.LR;
    this.expectedWinnerNames = new ArrayList<>();
    this.expectedCheaterNames = new ArrayList<>();

    this.p1Name = "p1";
    this.testP1Kicked = false;
    this.p1HasReachedGoal = false;
    this.p1SB = new StringBuilder();
    this.p1Home = new Coordinate(1, 1);
    this.p1Goal = new Coordinate(1, 1);
    this.p1CurPos = new Coordinate(0, 0);
    this.p1Color = colorList[0];
    this.p1ExpectedOutput = new ArrayList<>();

    this.p2Name = "p2";
    this.testP2Kicked = false;
    this.p2HasReachedGoal = false;
    this.p2SB = new StringBuilder();
    this.p2Home = new Coordinate(1, 3);
    this.p2Goal = new Coordinate(1, 1);
    this.p2CurPos = new Coordinate(0, 0);
    this.p2Color = colorList[1];
    this.p2ExpectedOutput = new ArrayList<>();

    this.p3Name = "p3";
    this.testP3Kicked = false;
    this.p3HasReachedGoal = false;
    this.p3SB = new StringBuilder();
    this.p3Home = new Coordinate(1, 5);
    this.p3Goal = new Coordinate(1, 1);
    this.p3CurPos = new Coordinate(0, 0);
    this.p3Color = colorList[2];
    this.p3ExpectedOutput = new ArrayList<>();

    this.p4Name = "p4";
    this.testP4Kicked = false;
    this.p4HasReachedGoal = false;
    this.p4SB = new StringBuilder();
    this.p4Home = new Coordinate(3, 3);
    this.p4Goal = new Coordinate(1, 1);
    this.p4CurPos = new Coordinate(0, 0);
    this.p4Color = colorList[3];
    this.p4ExpectedOutput = new ArrayList<>();
  }

  private void setUpMultiGoalReferee() {
    this.multiGoalGoals = new ArrayDeque<>();

    this.p1NumGoals = 0;
    this.p2NumGoals = 0;
    this.p3NumGoals = 0;
    this.p4NumGoals = 0;

    this.singleRules = new SingleGoalLabyrinthRules();
    this.multiRules = new MultiGoalLabyrinthRules();
  }

  Map<Color, SingleGoalRefereePlayerInfo> createInfoMap() {
    Map<Color, SingleGoalRefereePlayerInfo> map = new HashMap<>();

    SingleGoalRefereePlayerInfo p1RefPlayerInfo = new SingleGoalRefereePlayerInfo(p1Goal);
    p1RefPlayerInfo.setHasReachedGoal(p1HasReachedGoal);

    SingleGoalRefereePlayerInfo p2RefPlayerInfo = new SingleGoalRefereePlayerInfo(p2Goal);
    p2RefPlayerInfo.setHasReachedGoal(p2HasReachedGoal);

    SingleGoalRefereePlayerInfo p3RefPlayerInfo = new SingleGoalRefereePlayerInfo(p3Goal);
    p3RefPlayerInfo.setHasReachedGoal(p3HasReachedGoal);

    SingleGoalRefereePlayerInfo p4RefPlayerInfo = new SingleGoalRefereePlayerInfo(p4Goal);
    p4RefPlayerInfo.setHasReachedGoal(p4HasReachedGoal);

    map.put(p1Color, p1RefPlayerInfo);
    map.put(p2Color, p2RefPlayerInfo);
    map.put(p3Color, p3RefPlayerInfo);
    map.put(p4Color, p4RefPlayerInfo);
    return map;
  }

  Map<Color, SingleGoalRefereePlayerInfo> createInfoMap2() {
    Map<Color, SingleGoalRefereePlayerInfo> map = new HashMap<>();

    SingleGoalRefereePlayerInfo p1RefPlayerInfo = new SingleGoalRefereePlayerInfo(p1Goal);
    p1RefPlayerInfo.setHasReachedGoal(p1HasReachedGoal);

    SingleGoalRefereePlayerInfo p2RefPlayerInfo = new SingleGoalRefereePlayerInfo(p2Goal);
    p2RefPlayerInfo.setHasReachedGoal(p2HasReachedGoal);

    map.put(p1Color, p1RefPlayerInfo);
    map.put(p2Color, p2RefPlayerInfo);
    return map;
  }

  Map<Color, MultiGoalRefereePlayerInfo> createMultiGoalInfoMap() {
    Map<Color, MultiGoalRefereePlayerInfo> map = new HashMap<>();

    MultiGoalRefereePlayerInfo p1RefPlayerInfo = new MultiGoalRefereePlayerInfo(p1Goal);
    p1RefPlayerInfo.setNumGoalsReached(p1NumGoals);

    MultiGoalRefereePlayerInfo p2RefPlayerInfo = new MultiGoalRefereePlayerInfo(p2Goal);
    p2RefPlayerInfo.setNumGoalsReached(p2NumGoals);

    MultiGoalRefereePlayerInfo p3RefPlayerInfo = new MultiGoalRefereePlayerInfo(p3Goal);
    p3RefPlayerInfo.setNumGoalsReached(p3NumGoals);

    MultiGoalRefereePlayerInfo p4RefPlayerInfo = new MultiGoalRefereePlayerInfo(p4Goal);
    p4RefPlayerInfo.setNumGoalsReached(p4NumGoals);

    map.put(p1Color, p1RefPlayerInfo);
    map.put(p2Color, p2RefPlayerInfo);
    map.put(p3Color, p3RefPlayerInfo);
    map.put(p4Color, p4RefPlayerInfo);
    return map;
  }

  Map<Color, MultiGoalRefereePlayerInfo> createMultiGoalInfoMap2() {
    Map<Color, MultiGoalRefereePlayerInfo> map = new HashMap<>();

    MultiGoalRefereePlayerInfo p1RefPlayerInfo = new MultiGoalRefereePlayerInfo(p1Goal);
    p1RefPlayerInfo.setNumGoalsReached(p1NumGoals);

    MultiGoalRefereePlayerInfo p2RefPlayerInfo = new MultiGoalRefereePlayerInfo(p2Goal);
    p2RefPlayerInfo.setNumGoalsReached(p2NumGoals);

    map.put(p1Color, p1RefPlayerInfo);
    map.put(p2Color, p2RefPlayerInfo);
    return map;
  }

  Queue<PlayerInfo> createPlayerInfoList() {
    Queue<PlayerInfo> queue = new ArrayDeque<>();

    p1PlayerInfo = new PlayerInfo(colorList[0], p1Home);
    p1PlayerInfo.currentPos = p1CurPos;

    p2PlayerInfo = new PlayerInfo(colorList[1], p2Home);
    p2PlayerInfo.currentPos = p2CurPos;

    p3PlayerInfo = new PlayerInfo(colorList[2], p3Home);
    p3PlayerInfo.currentPos = p3CurPos;

    p4PlayerInfo = new PlayerInfo(colorList[3], p4Home);
    p4PlayerInfo.currentPos = p4CurPos;

    queue.add(p1PlayerInfo);
    queue.add(p2PlayerInfo);
    queue.add(p3PlayerInfo);
    queue.add(p4PlayerInfo);
    return queue;
  }

  void refreshPlayerInfo() {
    p1PlayerInfo = new PlayerInfo(colorList[0], p1Home);
    p1PlayerInfo.currentPos = p1CurPos;

    p2PlayerInfo = new PlayerInfo(colorList[1], p2Home);
    p2PlayerInfo.currentPos = p2CurPos;

    p3PlayerInfo = new PlayerInfo(colorList[2], p3Home);
    p3PlayerInfo.currentPos = p3CurPos;

    p4PlayerInfo = new PlayerInfo(colorList[3], p4Home);
    p4PlayerInfo.currentPos = p4CurPos;
  }

  Queue<PlayerInfo> createPlayerInfoList2() {
    Queue<PlayerInfo> queue = new ArrayDeque<>();

    p1PlayerInfo = new PlayerInfo(colorList[0], p1Home);
    p1PlayerInfo.currentPos = p1CurPos;

    p2PlayerInfo = new PlayerInfo(colorList[1], p2Home);
    p2PlayerInfo.currentPos = p2CurPos;

    queue.add(p1PlayerInfo);
    queue.add(p2PlayerInfo);
    return queue;
  }

  private void generatePlayerInfo() {
    player1 = new PlayerInfo(Color.BLUE, new Coordinate(1, 1));
    player2 = new PlayerInfo(Color.BLACK, new Coordinate(1, 3));
    player3 = new PlayerInfo(Color.ORANGE, new Coordinate(5, 3));
    player4 = new PlayerInfo(Color.YELLOW, new Coordinate(5, 1));
  }

  private void generateInfoMap() {
    infoMap = new HashMap<>();
    infoMap.put(Color.BLUE, new SingleGoalRefereePlayerInfo(new Coordinate(3,3)));
    infoMap.put(Color.BLACK, new SingleGoalRefereePlayerInfo(new Coordinate(3,1)));
    infoMap.put(Color.ORANGE, new SingleGoalRefereePlayerInfo(new Coordinate(5,5)));
    infoMap.put(Color.YELLOW, new SingleGoalRefereePlayerInfo(new Coordinate(1,1)));
  }

  public String[] getAllGemNames() {
    //TODO: Get gem names from file rather than initializing them all in first 100 lines
    return null;
  }

  public void createGems() {
    gemList = new Gem[LIST_OF_GEM_NAMES.length];
    for (int i = 0; i < LIST_OF_GEM_NAMES.length; i += 1) {
      Gem newGem = new Gem(LIST_OF_GEM_NAMES[i]);
      gemList[i] = newGem;
    }
  }

  public void createBoard() {
    boardTiles = new Tile[7][7];
    for (int i = 0; i < 49; i += 1) {
      Connector connector = Connector.getRandomConnector(new Random(RANDOM_SEED));
      Tile curTile = new Tile(gemList[0], gemList[i], connector);
      boardTiles[i / 7][i % 7] = curTile;
    }
  }

  public Tile[][] createBoard(int width, int height) {
    Tile[][] boardTiles = new Tile[height][width];
    for (int i = 0; i < height * width; i += 1) {
      Connector connector = Connector.getRandomConnector(new Random(RANDOM_SEED));
      Tile curTile = new Tile(gemList[0], gemList[i], connector);
      boardTiles[i / width][i % width] = curTile;
    }
    return boardTiles;
  }

  public Board createBoard(char[][] connectors) {
    int rows = connectors.length;
    int columns = connectors[0].length;
    String[] gemList = Gem.ALL_VALID_GEM_NAMES;
    Tile[][] tiles = new Tile[rows][columns];
    for (int i = 0; i < rows*columns; i += 1) {
      int row = i / columns;
      int col = i % columns;
      Connector connector = Connector.getFromChar(connectors[row][col]);
      Tile curTile = new Tile(new Gem(gemList[row]),
              new Gem(gemList[col]), connector);
      tiles[i / columns][i % columns] = curTile;
    }
    return new Board(tiles);
  }

  public boolean stateEquals(PublicState st1, PublicState st2) {
    //board, the spare tile, the loplayers, last action
    return boardEquals(st1.getBoard(), st2.getBoard()) && tileEquals(st1.getSpare(), st2.getSpare())
            && playersEquals(st1.getPlayers(), st2.getPlayers()) && actionEquals(st1.lastAction,st2.lastAction);
  }

  public boolean boardEquals(Board b1, Board b2) {
    if (!(b1.width == b2.width && b1.height == b2.height)) {
      return false;
    }

    for (int row = 0; row < b1.height; row++) {
      for (int col = 0; col < b1.width; col++) {
        if (!(tileEquals(b1.getBoardTiles()[row][col], b2.getBoardTiles()[row][col]))) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean playersEquals(Queue<PlayerInfo> pl1, Queue<PlayerInfo> pl2) {
    List<PlayerInfo> p1arr =  new ArrayList<>(pl1);
    List<PlayerInfo> p2arr =  new ArrayList<>(pl2);
    if (p1arr.size() != p2arr.size()) {
      return false;
    }
    for (int index = 0; index < p1arr.size(); index++) {
      if (!p1arr.get(index).equals(p2arr.get(index))) {
        return false;
      }
    }
    return true;

  }

  public boolean actionEquals(Optional<SimpleEntry<Integer, Direction>> move1,
                              Optional<SimpleEntry<Integer, Direction>> move2) {
    if (move1.isPresent() && move2.isPresent()) {
      return move1.get().getKey().equals(move2.get().getKey())
              && move1.get().getValue() == move2.get().getValue();
    }
    return !move1.isPresent() && !move2.isPresent();
  }

  public boolean tileEquals(Tile t1, Tile t2) {
    return ((gemEquals(t1.gem1, t2.gem1) && gemEquals(t1.gem2, t2.gem2))
            || (gemEquals(t1.gem2, t2.gem1) && gemEquals(t1.gem1, t2.gem2)))
            && t1.getConnector() == t2.getConnector();
  }

  public boolean gemEquals (Gem g1, Gem g2) {
    return g1.name.equals(g2.name);
  }

  protected static Color[] colorList = new Color[] {
          Color.orange,
          Color.blue,
          Color.black,
          Color.white,
          Color.pink,
          Color.yellow,
          Color.green
  };
}
