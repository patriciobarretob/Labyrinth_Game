package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import components.Board;
import components.Connector;
import components.Gem;
import components.Tile;

public class JsonBoardSerializer implements JsonSerializer<Board> {
  @Override
  public JsonElement serialize(Board board, Type type, JsonSerializationContext jsonSerializationContext) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Connector.class, new JsonConnectorSerializer());
    builder.registerTypeAdapter(Gem.class, new JsonGemSerializer());
    Gson gson = builder.create();
    Tile[][] tiles = board.getBoardTiles();
    JsonArray arrayOfConnectorRows = new JsonArray();
    JsonArray arrayOfTreasureRows = new JsonArray();
    for (Tile[] row: tiles) {
      JsonArray rowOfConnectors = new JsonArray();
      JsonArray rowOfTreasures = new JsonArray();
      for (Tile tile: row) {
        rowOfConnectors.add(gson.toJsonTree(tile.getConnector()));
        JsonArray treasure = new JsonArray();
        treasure.add(gson.toJsonTree(tile.gem1));
        treasure.add(gson.toJsonTree(tile.gem2));
        rowOfTreasures.add(treasure);
      }
      arrayOfConnectorRows.add(rowOfConnectors);
      arrayOfTreasureRows.add(rowOfTreasures);
    }
    JsonObject jObj = new JsonObject();
    jObj.add("connectors", arrayOfConnectorRows);
    jObj.add("treasures", arrayOfTreasureRows);
    return jObj;
  }
}
