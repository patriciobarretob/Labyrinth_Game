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
import components.PlayerInfo;
import components.PublicState;
import components.SingleGoalRefereePlayerInfo;
import components.SingleGoalRefereeState;
import components.Tile;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


/**
 * THIS MAKES NON STRICT REFEREE STATES
 */
public class JsonRefereeStateDeserializer implements JsonDeserializer<SingleGoalRefereeState> {

  static final Type INFO_TYPE = new TypeToken<SimpleEntry<Color, SingleGoalRefereePlayerInfo>>(){}.getType();

  @Override
  public SingleGoalRefereeState deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context) throws JsonParseException {
      JsonObject jObject = json.getAsJsonObject();
      Board board = JsonStateDeserializer.getBoardFromElement(jObject.get("board"));
      Tile tile = JsonStateDeserializer.getTileFromElement(jObject.get("spare"));

      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(INFO_TYPE, new JsonRefereePlayerDeserializer());
      Gson gson = gsonBuilder.create();
      JsonArray array = jObject.get("plmt").getAsJsonArray();

      Queue<PlayerInfo> playerInfoQueue = new ArrayDeque<>();
      Map<Color, SingleGoalRefereePlayerInfo> infoMap = new HashMap<>();
      processPlayerInfo(gson,array, playerInfoQueue, infoMap);

      PublicState publicState = new PublicState(board, tile, playerInfoQueue); //NON STRICT

      return new SingleGoalRefereeState(publicState, infoMap);
  }

  private void processPlayerInfo(Gson gson, JsonArray array, Queue<PlayerInfo> playerInfoQueue,
      Map<Color, SingleGoalRefereePlayerInfo> infoMap)  {
    for(JsonElement elem : array) {
      SimpleEntry<PlayerInfo, SingleGoalRefereePlayerInfo> player = gson.fromJson(elem, INFO_TYPE);
      playerInfoQueue.add(player.getKey());
      infoMap.put(player.getKey().avatar, player.getValue());
    }
  }
}
