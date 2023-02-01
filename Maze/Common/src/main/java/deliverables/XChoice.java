package deliverables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import components.Coordinate;
import components.Move;
import components.PublicState;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import json.JsonMoveSerializer;
import json.JsonStateDeserializer;
import json.JsonStrategyDeserializer;
import json.JsonUtils;
import players.IStrategy;

public class XChoice {

  static void xChoice() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(PublicState.class, new JsonStateDeserializer());
    gsonBuilder.registerTypeAdapter(IStrategy.class, new JsonStrategyDeserializer());
    gsonBuilder.registerTypeAdapter(Optional.class, new JsonMoveSerializer());
    Gson gson = gsonBuilder.create();

    IStrategy strategy = gson.fromJson(reader, IStrategy.class);
    PublicState s = gson.fromJson(reader, PublicState.class);
    JsonElement jsonCoord = JsonParser.parseReader(reader);
    Coordinate coord = JsonUtils.getCoord(jsonCoord);

    Optional<Move> move = strategy.makeMove(s, coord);
    String output = gson.toJson(move);
    System.out.println(output);
  }

}
