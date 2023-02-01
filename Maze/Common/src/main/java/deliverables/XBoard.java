package deliverables;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import components.Board;
import components.Coordinate;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import json.JsonUtils;

public class XBoard {

  static void xBoard() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    JsonElement board = JsonParser.parseReader(reader);
    Board finalBoard = JsonUtils.getBoard(board);
    JsonElement coord = JsonParser.parseReader(reader);
    Coordinate finalCoord = JsonUtils.getCoord(coord);
    List<Coordinate> reachable = finalBoard.getReachablePositions(finalCoord);
    String output = JsonUtils.listOfCoordinatesAsJsonOutput(reachable);
    System.out.println(output);
  }


}
