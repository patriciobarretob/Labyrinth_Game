package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Optional;

import components.Direction;

public class JsonActionSerializer implements JsonSerializer<Optional<AbstractMap.SimpleEntry<Integer, Direction>>> {
  @Override
  public JsonElement serialize(
          Optional<AbstractMap.SimpleEntry<Integer, Direction>> optionalIntegerDirectionSimpleEntry,
          Type type, JsonSerializationContext jsonSerializationContext) {
    if (optionalIntegerDirectionSimpleEntry.isPresent()) {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Direction.class, new JsonDirectionSerializer());
      Gson gson = builder.create();
      AbstractMap.SimpleEntry<Integer, Direction> action =
              optionalIntegerDirectionSimpleEntry.get();
      JsonArray jArr = new JsonArray();
      jArr.add(action.getKey());
      jArr.add(gson.toJsonTree(action.getValue()));
      return jArr;
    } else {
      return JsonNull.INSTANCE;
    }
  }
}
