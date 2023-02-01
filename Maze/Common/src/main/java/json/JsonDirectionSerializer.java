package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import components.Direction;

public class JsonDirectionSerializer implements JsonSerializer<Direction> {
  @Override
  public JsonElement serialize(Direction direction, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(direction.name());
  }
}
