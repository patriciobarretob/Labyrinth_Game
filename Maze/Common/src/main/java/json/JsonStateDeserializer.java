package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import components.PublicState;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import components.Board;
import components.Direction;
import components.PlayerInfo;
import components.Tile;

public class JsonStateDeserializer implements JsonDeserializer<PublicState> {
  private static Gson gson = new Gson();
  @Override
  public PublicState deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jObject = jsonElement.getAsJsonObject();
    Board board = getBoardFromElement(jObject.get("board"));
    Tile tile = getTileFromElement(jObject.get("spare"));
    Queue<PlayerInfo> players = getPlayersFromElement(jObject.get("plmt"));
    Optional<SimpleEntry<Integer, Direction>> lastAction = getLastActionFromElement(jObject.get(
            "last"));
    PublicState ret = new PublicState(board, tile, players);
    ret.lastAction = lastAction;
    return ret;
  }

  private Optional<SimpleEntry<Integer, Direction>> getLastActionFromElement(JsonElement elem) {
    Optional<SimpleEntry<Integer, Direction>> lastAction;
    if (elem.isJsonNull()) {
      lastAction = Optional.empty();
    } else {
      JsonArray jLastActionArr = elem.getAsJsonArray();
      int index = jLastActionArr.get(0).getAsInt();
      String directionString = jLastActionArr.get(1).getAsString();
      Direction direction = Direction.valueOf(directionString);
      lastAction = Optional.of(new SimpleEntry<>(index, direction));
    }
    return lastAction;
  }

  private Queue<PlayerInfo> getPlayersFromElement(JsonElement elem) {
    JsonPlayer[] jPlayers = gson.fromJson(elem, JsonPlayer[].class);
    Queue<PlayerInfo> players = new ArrayDeque<>();
    for(JsonPlayer player : jPlayers) {
      players.add(player.getActual());
    }
    return players;
  }

  static Tile getTileFromElement(JsonElement elem) {
    JsonTile jTile = gson.fromJson(elem, JsonTile.class);
    return jTile.getActual();
  }

  static Board getBoardFromElement(JsonElement elem) {
    JsonBoard jBoard = gson.fromJson(elem, JsonBoard.class);
    return jBoard.getActual();
  }
}
