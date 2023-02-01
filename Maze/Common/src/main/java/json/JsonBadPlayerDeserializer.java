package json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import players.BadPlayer;
import players.IStrategy;

public class JsonBadPlayerDeserializer implements JsonDeserializer<BadPlayer> {
  @Override
  public BadPlayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonArray array = jsonElement.getAsJsonArray();
    String name = array.get(0).getAsString();
    JsonElement strategyJson = array.get(1);
    String badMethod = array.get(2).getAsString();
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(IStrategy.class, new JsonStrategyDeserializer());
    IStrategy strategy = gson.create().fromJson(strategyJson, IStrategy.class);
    return new BadPlayer(strategy, name, badMethod.equals("setUp"), badMethod.equals("takeTurn"),
            badMethod.equals("win"));
  }


}
