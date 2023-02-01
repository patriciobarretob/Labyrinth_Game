package json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import components.Coordinate;
import components.PlayerInfo;
import components.SingleGoalRefereePlayerInfo;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;

public class JsonRefereePlayerDeserializer
    implements JsonDeserializer<SimpleEntry<PlayerInfo, SingleGoalRefereePlayerInfo>> {

  @Override
  public SimpleEntry<PlayerInfo, SingleGoalRefereePlayerInfo> deserialize(JsonElement json, Type typeOfT,
                                                                          JsonDeserializationContext context) throws JsonParseException {
    JsonObject jObject = json.getAsJsonObject();
    Coordinate current = JsonUtils.getCoord(jObject.get("current"));
    Coordinate home = JsonUtils.getCoord(jObject.get("home"));
    Coordinate target = JsonUtils.getCoord(jObject.get("goto"));
    Color avatar = JsonPlayer.colorStringToColor(jObject.get("color").getAsString());
    PlayerInfo playerInfo = new PlayerInfo(avatar, home, current);
    SingleGoalRefereePlayerInfo singleGoalRefereePlayerInfo = new SingleGoalRefereePlayerInfo(target);
    return new SimpleEntry<>(playerInfo, singleGoalRefereePlayerInfo);
  }
}
