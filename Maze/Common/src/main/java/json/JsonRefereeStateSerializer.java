package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


import java.awt.*;
import java.lang.reflect.Type;
import java.util.Optional;
import components.Board;
import components.PlayerInfo;
import components.SingleGoalRefereeState;
import components.Tile;
import org.apache.commons.lang3.StringUtils;

public class JsonRefereeStateSerializer implements JsonSerializer<SingleGoalRefereeState> {
  @Override
  public JsonElement serialize(SingleGoalRefereeState singleGoalRefereeState, Type type,
                               JsonSerializationContext jsonSerializationContext) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Board.class, new JsonBoardSerializer());
    builder.registerTypeAdapter(Tile.class, new JsonTileSerializer());
    builder.registerTypeAdapter(Optional.class, new JsonActionSerializer());
    builder.serializeNulls();
    Gson gson = builder.create();
    JsonObject jObj = new JsonObject();
    jObj.add("board", gson.toJsonTree(singleGoalRefereeState.publicState.getBoard()));
    jObj.add("spare", gson.toJsonTree(singleGoalRefereeState.publicState.spare));
    jObj.add("plmt", getRefereePlayersAsJson(singleGoalRefereeState));
    jObj.add("last", gson.toJsonTree(singleGoalRefereeState.publicState.lastAction));
    return jObj;
  }

  private JsonElement getRefereePlayersAsJson(SingleGoalRefereeState singleGoalRefereeState) {
    JsonArray plmt = new JsonArray();
    for (PlayerInfo playerInfo: singleGoalRefereeState.publicState.getPlayers()) {
      JsonObject refereePlayer = new JsonObject();
      Color playerColor = playerInfo.avatar;
      refereePlayer.add("current", JsonUtils.coordinateToJsonElement(playerInfo.currentPos));
      refereePlayer.add("home", JsonUtils.coordinateToJsonElement(playerInfo.home));
      refereePlayer.add("goto",
              JsonUtils.coordinateToJsonElement(singleGoalRefereeState.infoMap.get(playerColor).getGoal()));
      StringBuilder colorString = new StringBuilder();
      colorString.append(StringUtils.leftPad(Integer.toHexString(playerColor.getRed()), 2, '0'));
      colorString.append(StringUtils.leftPad(Integer.toHexString(playerColor.getGreen()), 2, '0'));
      colorString.append(StringUtils.leftPad(Integer.toHexString(playerColor.getBlue()), 2, '0'));
      refereePlayer.addProperty("color", colorString.toString().toUpperCase());
      plmt.add(refereePlayer);
    }
    return plmt;
  }
}
