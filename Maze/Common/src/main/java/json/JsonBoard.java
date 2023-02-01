package json;

import components.*;

public class JsonBoard implements JsonRepresentation<Board> {

  public String[][] connectors;
  public String[][][] treasures;

  @Override
  public Board getActual() {
    Tile[][] tiles = new Tile[connectors.length][connectors[0].length];
    for(int row = 0; row < connectors.length; row++) {
      for(int col = 0; col < connectors[0].length; col++) {
        Gem gem1 = new Gem(treasures[row][col][0]);
        Gem gem2 = new Gem(treasures[row][col][1]);
        Connector connector = Connector.getFromChar(connectors[row][col].charAt(0));
        tiles[row][col] = new Tile(gem1, gem2, connector);
      }
    }
    return new Board(tiles);
  }
}
