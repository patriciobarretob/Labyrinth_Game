package players;

import components.IRefereePlayerInfo;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import java.util.Map.Entry;
import javax.swing.*;

import components.Board;
import components.Coordinate;
import components.PlayerInfo;
import components.SingleGoalRefereePlayerInfo;
import components.SingleGoalRefereeState;

//This represents a visualization of a RefereeState.
//In the center is the board. The board is visualized by a JBoardPanel.
//To the right is
public class JRefereeStatePanel extends JPanel {
  private final SingleGoalRefereeState state;
  //todo: this is parallel
  private final Map<Color, String> colorToNameMap;

  public JRefereeStatePanel(SingleGoalRefereeState state, Map<Color, String> colorToNameMap) {
    this.state = state;
    this.colorToNameMap = colorToNameMap;
    this.setLayout(new BorderLayout());
    Map<Color, Coordinate> playerMap = getPlayerMap(state);
    Map<Color, Coordinate> homeMap = getHomeMap(state);
    Map<Color, Coordinate> goalMap = getGoalMap(state);
    Board board = state.publicState.getBoard();
    JBoardPanel boardPanel = new JBoardPanel(board, playerMap, goalMap, homeMap);
    boardPanel.setMinimumSize(new Dimension(500, 500));
    this.add(boardPanel, BorderLayout.CENTER);
    JPanel rightSideBar = generateRightSideBar();
    this.add(rightSideBar, BorderLayout.LINE_END);
    JPanel leftSideBar = generateLeftSideBar();
    this.add(leftSideBar, BorderLayout.LINE_START);
  }

  // Generates the information aside from the board itself that the observer needs to know.
  // This will consist of information that is mostly relating to player names and ordering.
  // This consists of:
  // A color to name diagram
  // Glossary for what each shape describes
  // FirstPlayer to indicate the start of a round
  private JPanel generateLeftSideBar() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(generateGlossary());
    panel.add(generateQueueOfPlayers());
    panel.add(generateNameToColors());
    return panel;
  }

  // Generates a JPanel that describes what each shape on a tile means, and the corner of the tile
  // that these shapes will appear at.
  private JPanel generateGlossary() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    JLabel glossary = new JLabel("Shape Representation");
    JLabel playerLabel = new JLabel("Player: Circle, Top Right");
    JLabel homeLabel = new JLabel("Home: Rectangle, Bottom Left");
    JLabel goalLabel = new JLabel("Goals: Oval, Bottom Right");
    panel.add(glossary);
    panel.add(playerLabel);
    panel.add(homeLabel);
    panel.add(goalLabel);
    return panel;
  }

  // Generates a JPanel that describes the current ordering of players. The first label
  // added represents the "FirstPlayer", which shows the first player to go for each round.
  private JPanel generateQueueOfPlayers() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(generateFirstPlayer());
    int idx = 0;
    for (PlayerInfo player : state.publicState.getPlayers()) {
      JLabel playerLabel = new JLabel(idx + ": at pos: (x:" +
          player.currentPos.x  + ", y:" + player.currentPos.y + ")");
      playerLabel.setForeground(player.avatar);
      playerLabel.setBackground(colorToBackground(player.avatar));
      playerLabel.setOpaque(true);
      panel.add(playerLabel);
      idx++;
    }
    return panel;
  }

  //Generates the first player. Denotes when a new round will start.
  private JLabel generateFirstPlayer() {
    JLabel firstPlayerLabel = new JLabel("First Player");
    Color firstAvatar = state.publicState.getFirstPlayer().avatar;
    firstPlayerLabel.setForeground(firstAvatar);
    firstPlayerLabel.setBackground(colorToBackground(firstAvatar));
    firstPlayerLabel.setOpaque(true);
    return firstPlayerLabel;
  }

  //Provides a visual mapping from player's names to the colors it is represented by on the board.
  private JPanel generateNameToColors() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    for (Entry<Color, String> colorToName : colorToNameMap.entrySet()) {
      Color playerColor = colorToName.getKey();
      JLabel playerLabel = new JLabel(colorToName.getValue());
      playerLabel.setForeground(playerColor);
      playerLabel.setBackground(colorToBackground(playerColor));
      playerLabel.setOpaque(true);
      panel.add(playerLabel);
    }
    return panel;
  }

  //Converts from RGB values to (Y)IQ, where Y determines the intensity.
  //We found this from https://stackoverflow.com/questions/4672271/reverse-opposing-colors
  private Color colorToBackground(Color color) {
    double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
    return y >= 128 ? Color.BLACK : Color.WHITE;
  }

  // Generates the information aside from the board itself that the observer needs to know.
  // This will consist of information that will change throughout the game.
  // (Mostly) Non-changing information will be in generateGlossary().
  // This consists of:
  // - the spare tile
  // - the previous action
  // - whether each player has reached their goal
  // - the round number
  // - whether each player has skipped this round
  private JPanel generateRightSideBar() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    JTileComponent spareTile = new JTileComponent(this.state.publicState.spare);
    spareTile.setMaximumSize(new Dimension(this.getMaximumSize().width, 100));
    JLabel previousActionLabel = new JLabel(this.state.publicState.lastAction.toString());
    JLabel roundNumberLabel = new JLabel("Round num: " + this.state.publicState.getRoundNum());
    JPanel reachedGoalPanel = generateReachedGoalPanel();
    JPanel skippedThisRoundPanel = generateSkippedThisRound();
    panel.add(roundNumberLabel);
    panel.add(spareTile);
    panel.add(previousActionLabel);
    panel.add(reachedGoalPanel);
    panel.add(skippedThisRoundPanel);
    return panel;
  }

  //Generates a JPanel which contains a list of JLabels.
  // Each JLabel represents one player (denoted by color) and will either be true or false.
  // True, if the player has reached its goal.
  // False, if the player has not.
  private JPanel generateReachedGoalPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(new JLabel("Has reached goal:"));
    for (Map.Entry<Color, IRefereePlayerInfo> entry: this.state.infoMap.entrySet()) {

      StringBuilder sb = new StringBuilder();
      SingleGoalRefereePlayerInfo playerInfo = (SingleGoalRefereePlayerInfo)entry.getValue();
      sb.append(playerInfo.getHasReachedGoal());
      JLabel label = new JLabel(sb.toString());
      label.setForeground(entry.getKey());
      label.setBackground(colorToBackground(entry.getKey()));
      label.setOpaque(true);

      panel.add(label);
    }
    return panel;
  }


  //Generates a JPanel which contains a list of JLabels.
  // Each JLabel represents one player (denoted by color) and will either be true or false.
  // True, if the player has skipped this round.
  // False, if the player has not.
  private JPanel generateSkippedThisRound() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(new JLabel("Has skipped round:"));
    for (PlayerInfo player : state.publicState.getPlayers()) {
      StringBuilder sb = new StringBuilder();
      sb.append(player.skippedThisRound);
      JLabel label = new JLabel(sb.toString());
      label.setForeground(player.avatar);
      label.setBackground(colorToBackground(player.avatar));
      label.setOpaque(true);
      panel.add(label);
    }
    return panel;
  }

  //Creates a HashMap from a player avatar (Color) to its current position (Coordinate)
  private Map<Color, Coordinate> getPlayerMap(SingleGoalRefereeState state) {
    Map<Color, Coordinate> playerMap = new HashMap<>();
    for (PlayerInfo playerInfo: state.publicState.getPlayers()) {
      playerMap.put(playerInfo.avatar, playerInfo.currentPos);
    }
    return playerMap;
  }

  //Creates a HashMap from a player avatar (Color) to its home (Coordinate)
  private Map<Color, Coordinate> getHomeMap(SingleGoalRefereeState state) {
    Map<Color, Coordinate> homeMap = new HashMap<>();
    for (PlayerInfo playerInfo: state.publicState.getPlayers()) {
      homeMap.put(playerInfo.avatar, playerInfo.home);
    }
    return homeMap;
  }

  //Creates a HashMap from a player avatar (Color) to its goal (Coordinate)
  private Map<Color, Coordinate> getGoalMap(SingleGoalRefereeState state) {
    Map<Color, Coordinate> goalMap = new HashMap<>();
    for (PlayerInfo playerInfo: state.publicState.getPlayers()) {
      Color playerColor = playerInfo.avatar;
      goalMap.put(playerColor, state.infoMap.get(playerColor).getGoal());
    }
    return goalMap;
  }
}
