package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import components.Gem;

public class JsonGemSerializer implements JsonSerializer<Gem> {
  @Override
  public JsonElement serialize(Gem gem, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(gem.name);
  }
}
