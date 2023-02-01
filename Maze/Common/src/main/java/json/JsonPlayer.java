package json;

import components.Coordinate;
import components.PlayerInfo;
import java.awt.Color;

public class JsonPlayer implements JsonRepresentation<PlayerInfo> {
  JsonCoordinate current;
  JsonCoordinate home;
  String color;

  @Override
  public PlayerInfo getActual() {
    PlayerInfo player = new PlayerInfo(colorStringToColor(color), home.getActual());
    player.currentPos = current.getActual();
    return player;
  }

  static Color colorStringToColor(String colorString) {
    if(!colorString.matches("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$")) {
      colorString = mapToColor(colorString);
    }
    return Color.decode("#" + colorString);
  }

  static String mapToColor(String colorString) {
    switch(colorString) {
      case "purple":
        return "850A95";
      case "orange":
        return "FF5733";
      case "pink":
        return "FF00FF";
      case "red":
        return "FF0000";
      case "blue":
        return "0000FF";
      case "green":
        return "00FF00";
      case "yellow":
        return "FFFF00";
      case "white":
        return "FFFFFF";
      case "black":
        return "000000";
      default:
          throw new IllegalArgumentException("color does not match list of valid colors");
    }
  }
}
