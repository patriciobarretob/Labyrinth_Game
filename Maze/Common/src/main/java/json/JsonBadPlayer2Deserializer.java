package json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import players.BadPlayer;
import players.BadPlayer2;
import players.IStrategy;

public class JsonBadPlayer2Deserializer implements JsonDeserializer<BadPlayer2> {
  @Override
  public BadPlayer2 deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonArray array = jsonElement.getAsJsonArray();
    String name = array.get(0).getAsString();
    JsonElement strategyJson = array.get(1);
    String badMethod = array.get(2).getAsString();
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(IStrategy.class, new JsonStrategyDeserializer());
    IStrategy strategy = gson.create().fromJson(strategyJson, IStrategy.class);
    int count = array.get(3).getAsInt();
    int[] args = getCountArgs(badMethod, count);
    return new BadPlayer2(strategy, name, args[0], args[1], args[2]);
  }

  int[] getCountArgs(String method, int count) {
    int[] args = new int[3]; // initialize to 0
    switch(method) {
      case "setUp":
        args[0] = count;
        break;
      case "takeTurn":
        args[1] = count;
      case "win":
        args[2] = count;
    }
    return args;
  }
}
