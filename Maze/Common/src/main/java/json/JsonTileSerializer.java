package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import components.Connector;
import components.Gem;
import components.Tile;

public class JsonTileSerializer implements JsonSerializer<Tile> {
  @Override
  public JsonElement serialize(Tile tile, Type type, JsonSerializationContext jsonSerializationContext) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Gem.class, new JsonGemSerializer());
    builder.registerTypeAdapter(Connector.class, new JsonConnectorSerializer());
    Gson gson = builder.create();
    JsonObject jObj = new JsonObject();
    jObj.add("tilekey", gson.toJsonTree(tile.getConnector()));
    jObj.add("1-image", gson.toJsonTree(tile.gem1));
    jObj.add("2-image", gson.toJsonTree(tile.gem2));
    return jObj;
  }
}
