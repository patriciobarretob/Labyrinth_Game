package json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import components.Direction;
import java.lang.reflect.Type;
import players.IPlayer;
import players.IStrategy;
import players.PlayerImpl;

public class JsonPlayerImplDeserializer implements JsonDeserializer<PlayerImpl> {

  @Override
  public PlayerImpl deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonArray array = json.getAsJsonArray();
    String name = array.get(0).getAsString();
    JsonElement strategyJson = array.get(1);
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(IStrategy.class, new JsonStrategyDeserializer());
    IStrategy strategy = gson.create().fromJson(strategyJson, IStrategy.class);
    return new PlayerImpl(strategy, name);
  }
}
