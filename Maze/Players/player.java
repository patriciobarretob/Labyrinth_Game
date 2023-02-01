package players;

import components.Board;
import components.Connector;
import components.Coordinate;
import components.Gem;
import components.Move;
import components.PublicState;
import components.Tile;
import java.util.Optional;
import java.util.Random;

// Represents a basic AI player that proposes random boards.
// Turns are abstracted over a strategy that is passed to this player when it's constructed.
public class PlayerImpl implements IPlayer {

  private final IStrategy strategy;
  private final String name;
  private Coordinate goal;

  public PlayerImpl(IStrategy strategy, String name) {
    this.strategy = strategy;
    this.name = name;
    this.goal = new Coordinate(0,0);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Board proposeBoard0(int rows, int columns) {
    if(rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Number of rows and columns must be greater than 0");
    }
    int gemListLength = Gem.ALL_VALID_GEM_NAMES.length;
    if(rows * columns > gemListLength * gemListLength) {
      throw new IllegalArgumentException("Unable to create unique tiles for a board this large");
    }
    return createRandomBoard(rows, columns);
  }

  // Creates a board with "random" connectors and unique pairs of gems.
  private Board createRandomBoard(int rows, int columns) {
    String[] gemList = Gem.ALL_VALID_GEM_NAMES;
    Tile[][] tiles = new Tile[rows][columns];
    for (int i = 0; i < rows*columns; i += 1) {
      Connector connector = Connector.getRandomConnector(new Random());
      Tile curTile = new Tile(new Gem(gemList[i / gemList.length]),
          new Gem(gemList[i % gemList.length]), connector);
      tiles[i / columns][i % columns] = curTile;
    }
    return new Board(tiles);
  }

  @Override
  public void setup(Optional<PublicState> state0, Coordinate goal) {
    this.goal = goal;
  }

  @Override
  public Optional<Move> takeTurn(PublicState s) {
    return strategy.makeMove(s, goal);
  }

  //Nothing to do with this method currently.
  // This AI player doesn't have anything to do after it.
  @Override
  public void won(boolean w) {
  }
}
