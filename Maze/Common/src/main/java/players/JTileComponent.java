package players;

import components.Connector;
import components.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class JTileComponent extends JComponent {

  private final Tile tile;
  private final List<Color> players;
  private final List<Color> goals;
  private final List<Color> homes;

  private static final Color BORDER_COLOR = Color.black;

  public JTileComponent(Tile tile) {
    this(tile, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public JTileComponent(Tile tile, List<Color> players, List<Color> goals,
      List<Color> homes) {
    super();
    this.tile = tile;
    this.players = players;
    this.goals = goals;
    this.homes = homes;
  }

  public JTileComponent addPlayer(Color player) {
    List<Color> newPlayerList = new ArrayList<>(this.players);
    newPlayerList.add(player);
    return new JTileComponent(this.tile, newPlayerList, this.goals, this.homes);
  }

  public JTileComponent addGoal(Color goal) {
    List<Color> newGoalList = new ArrayList<>(this.goals);
    newGoalList.add(goal);
    return new JTileComponent(this.tile, this.players, newGoalList, this.homes);
  }

  public JTileComponent addHome(Color home) {
    List<Color> newHomeList = new ArrayList<>(this.homes);
    newHomeList.add(home);
    return new JTileComponent(this.tile, this.players, this.goals, newHomeList);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawRect(0, 0, this.getWidth(), this.getHeight());
    drawImages(g);
    drawConnector(g);
    drawPlayers(g);
    drawHomes(g);
    drawGoals(g);
  }

  private void drawImages(Graphics g) {
    BufferedImage gemImage1 = getImageFromPath("/gems/" + tile.gem1.name + ".png");
    BufferedImage gemImage2 = getImageFromPath("/gems/" + tile.gem2.name + ".png");
    ImageIcon icon1 = new ImageIcon(gemImage1);
    ImageIcon icon2 = new ImageIcon(gemImage2);
    int imageWidth = this.getWidth() / 5;
    int imageHeight = this.getHeight() / 5;
    int icon2X = this.getWidth()/4;
    int icon2Y = this.getHeight()/4;

    g.drawImage(icon1.getImage(), 0, 0, imageWidth, imageHeight, icon1.getImageObserver());
    g.drawImage(icon2.getImage(), icon2X, icon2Y, imageWidth, imageHeight,
            icon2.getImageObserver());
  }

  private BufferedImage getImageFromPath(String path) {
    BufferedImage myPicture;
    try {
      myPicture = ImageIO.read(getClass().getResourceAsStream(path) );
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.out.println("Something went wrong reading the file for the image!");
      return null;
    }
    return myPicture;
  }

  private void drawConnector(Graphics g) {
    Connector c = this.tile.getConnector();
    if(c.connectsUp) { drawVertical(true, g); }
    if(c.connectsDown) { drawVertical(false, g); }
    if(c.connectsLeft) { drawHorizontal(true, g); }
    if(c.connectsRight) { drawHorizontal(false, g); }
  }

  //Draws one side of a connector. If up is true, draw up. If up is false, draw down.
  private void drawVertical(boolean up, Graphics g){
    int roadWidth = this.getWidth() / 10;
    int roadX = this.getWidth()*9/20;
    int roadHeight = this.getHeight()/2;
    int roadY = -1;
    if(up){
      roadY = 0;
    } else {
      roadY = this.getHeight()/2;
    }
    g.fillRect(roadX, roadY, roadWidth, roadHeight);
  }

  //Draws one side of a connector. If left is true, draw left. If left is false, draw right.
  private void drawHorizontal(boolean left, Graphics g){
    int roadWidth = this.getWidth()/2;
    int roadX = -1;
    int roadHeight = this.getHeight()/10;
    int roadY = this.getHeight()*9/20;
    if(left){
      roadX = 0;
    } else {
      roadX = this.getWidth()/2;
    }
    g.fillRect(roadX, roadY, roadWidth, roadHeight);
  }

  private void drawPlayers(Graphics g) {
    int playerWidth = this.getWidth() / 6;
    int playerHeight = this.getHeight() / 6;
    int initialY = 0;
    int initialX = this.getWidth() - playerWidth;
    int deltaY = playerHeight / 2;
    int deltaX = playerWidth / 2;
    for (int i = 0; i < this.players.size(); i += 1) {
      int x = initialX - deltaX * i;
      int y = initialY + deltaY * i;
      drawPlayer(x, y, playerWidth, playerHeight, this.players.get(i), g);
    }
  }

  private void drawPlayer(int x, int y, int width, int height, Color c, Graphics g) {
    Color prevColor = g.getColor();
    g.setColor(BORDER_COLOR);
    g.drawOval(x, y, width, height);

    g.setColor(c);
    g.fillOval(x, y, width, height);
    g.setColor(prevColor);
  }

  private void drawHomes(Graphics g) {
    int homeWidth = this.getWidth() / 6;
    int homeHeight = this.getHeight() / 8;
    int initialY = this.getHeight() - homeHeight;
    int initialX = 0;
    int deltaY = homeHeight / 2;
    int deltaX = homeWidth / 2;
    for (int i = 0; i < this.homes.size(); i += 1) {
      int x = initialX + deltaX * i;
      int y = initialY - deltaY * i;
      drawHome(x, y, homeWidth, homeHeight, this.homes.get(i), g);
    }
  }

  private void drawHome(int x, int y, int width, int height, Color c, Graphics g) {
    Color prevColor = g.getColor();
    g.setColor(BORDER_COLOR);
    g.drawRect(x, y, width, height);

    g.setColor(c);
    g.fillRect(x, y, width, height);
    g.setColor(prevColor);
  }

  private void drawGoals(Graphics g) {
    int goalWidth = this.getWidth() / 6;
    int goalHeight = this.getHeight() / 8;
    int initialY = this.getHeight() - goalHeight;
    int initialX = this.getWidth() - goalWidth;
    int deltaY = goalHeight / 2;
    int deltaX = goalWidth / 2;
    for (int i = 0; i < this.goals.size(); i += 1) {
      int x = initialX - deltaX * i;
      int y = initialY - deltaY * i;
      drawGoal(x, y, goalWidth, goalHeight, this.goals.get(i), g);
    }
  }

  private void drawGoal(int x, int y, int width, int height, Color c, Graphics g) {
    int arcWidth = 2 * width / 3;
    int arcHeight = 2 * height / 3;
    Color prevColor = g.getColor();
    g.setColor(BORDER_COLOR);
    g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    g.setColor(c);
    g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    g.setColor(prevColor);
  }
}
