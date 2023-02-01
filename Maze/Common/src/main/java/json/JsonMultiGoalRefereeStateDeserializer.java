package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import components.Board;
import components.Coordinate;
import components.MultiGoalRefereePlayerInfo;
import components.MultiGoalRefereeState;
import components.PlayerInfo;
import components.PublicState;
import components.Tile;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class JsonMultiGoalRefereeStateDeserializer implements
    JsonDeserializer<MultiGoalRefereeState> {

  static final Type INFO_TYPE = new TypeToken<SimpleEntry<Color, MultiGoalRefereePlayerInfo>>(){}.getType();

  @Override
  public MultiGoalRefereeState deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(INFO_TYPE, new JsonMultiGoalRefereePlayerDeserializer());
    gsonBuilder.registerTypeAdapter(Coordinate[].class, new JsonCoordArrayDeserializer());
    Gson gson = gsonBuilder.create();

    JsonObject jObject = json.getAsJsonObject();
    Board board = JsonStateDeserializer.getBoardFromElement(jObject.get("board"));
    Tile tile = JsonStateDeserializer.getTileFromElement(jObject.get("spare"));

    Queue<Coordinate> potentialGoals = getGoalsFromJson(jObject, gson);

    JsonArray array = jObject.get("plmt").getAsJsonArray();

    Queue<PlayerInfo> playerInfoQueue = new ArrayDeque<>();
    Map<Color, MultiGoalRefereePlayerInfo> infoMap = new HashMap<>();
    processPlayerInfo(gson,array, playerInfoQueue, infoMap);

    PublicState publicState = new PublicState(board, tile, playerInfoQueue); //NON STRICT

    MultiGoalRefereeState refState = new MultiGoalRefereeState(publicState, infoMap);

    refState.setPotentialGoals(potentialGoals);

    return refState;
  }
  private Queue<Coordinate> getGoalsFromJson(JsonObject jObject, Gson gson){

    JsonElement goalsElement = jObject.get("goals");
    if(goalsElement == null){
      return new ArrayDeque<Coordinate>();
    }
    Coordinate[] goalsArray = gson.fromJson(goalsElement, Coordinate[].class);
    Queue<Coordinate> potentialGoals = new ArrayDeque<>(Arrays.asList(goalsArray));

    return potentialGoals;
  }

  private void processPlayerInfo(Gson gson, JsonArray array, Queue<PlayerInfo> playerInfoQueue,
      Map<Color, MultiGoalRefereePlayerInfo> infoMap)  {
    for(JsonElement elem : array) {
      SimpleEntry<PlayerInfo, MultiGoalRefereePlayerInfo> player = gson.fromJson(elem, INFO_TYPE);
      playerInfoQueue.add(player.getKey());
      infoMap.put(player.getKey().avatar, player.getValue());
    }
  }

}
