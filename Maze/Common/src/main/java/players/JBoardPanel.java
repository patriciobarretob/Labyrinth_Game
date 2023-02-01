package players;

import components.Board;
import components.Coordinate;
import components.Tile;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.swing.JPanel;

//Represents a visualization of a Board.
//Generates a grid of JTileComponents based on each Tile's gems and connector.
//Populates the corners of each tile with any players, goals, and homes that may be at the same
// position as the tile.
public class JBoardPanel extends JPanel {

  Board board;
  private final Map<Color, Coordinate> players;
  private final Map<Color, Coordinate> goals;
  private final Map<Color, Coordinate> homes;

  public JBoardPanel(Board board, Map<Color, Coordinate> players,
      Map<Color, Coordinate> goals, Map<Color, Coordinate> homes) {
    super();
    this.board = board;
    this.players = players;
    this.goals = goals;
    this.homes = homes;

    setupGrid();
    populate(this.players, (Color c, JTileComponent component) -> component.addPlayer(c));
    populate(this.goals, (Color c, JTileComponent component) -> component.addGoal(c));
    populate(this.homes, (Color c, JTileComponent component) -> component.addHome(c));
  }

  //Iterates through every row and column on the board and creates JTileComponents to the grid.
  private void setupGrid() {
    this.setLayout(new GridLayout(board.height, board.width, 0,0));
    Tile[][] tiles = board.getBoardTiles();
    for(int row = 0; row < board.height; row++) {
      for(int col = 0; col < board.width; col++) {
        this.add(new JTileComponent(tiles[row][col]), row*board.width + col);
      }
    }
  }


  private void populate(Map<Color, Coordinate> map,
      BiFunction<Color, JTileComponent, JTileComponent> colorToComponent) {
    for (Map.Entry<Color, Coordinate> entry : map.entrySet()) {
      Color color = entry.getKey();
      Coordinate coordinate = entry.getValue();
      int index = coordinate.y * board.width + coordinate.x;
      JTileComponent component = (JTileComponent) this.getComponent(index);
      this.remove(index);
      this.add(colorToComponent.apply(color, component), index);
    }
  }
}
