package deliverables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import components.Coordinate;
import components.Direction;
import components.PublicState;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import json.JsonStateDeserializer;
import json.JsonUtils;

public class XState {

  //Todo: Refactor into different methods
  static void xState() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(PublicState.class, new JsonStateDeserializer());
    Gson gson = gsonBuilder.create();

    PublicState s = gson.fromJson(reader, PublicState.class);
    int index = gson.fromJson(reader, int.class);
    Direction direction = Direction.valueOf(gson.fromJson(reader, String.class));
    // Degrees counter-clockwise
    int degrees = gson.fromJson(reader, int.class);
    s.rotateSpare(-degrees / 90);
    s.shiftAndInsert(index, direction);
    List<Coordinate> reachable = s.getBoard().getReachablePositions(s.getActivePlayer().currentPos);
    String output = JsonUtils.listOfCoordinatesAsJsonOutput(reachable);
    System.out.println(output);
  }


}
