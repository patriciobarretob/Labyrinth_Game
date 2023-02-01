package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import players.IPlayer;
import players.IStrategy;
import players.PlayerImpl;

public class JsonPlayerImplListDeserializer implements JsonDeserializer<List<PlayerImpl>>  {

  @Override
  public List<PlayerImpl> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(PlayerImpl.class, new JsonPlayerImplDeserializer());
    Gson gson = gsonBuilder.create();
    JsonArray array = json.getAsJsonArray();
    List<PlayerImpl> playerList = new ArrayList<>();
    for(JsonElement elem : array) {
      PlayerImpl player = gson.fromJson(elem, PlayerImpl.class);
      playerList.add(player);
    }
    return playerList;
  }
}
