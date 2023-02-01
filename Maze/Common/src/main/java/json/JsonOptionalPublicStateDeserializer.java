package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.OptionalInt;

import components.PublicState;

public class JsonOptionalPublicStateDeserializer implements JsonDeserializer<Optional<PublicState>> {
  @Override
  public Optional<PublicState> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    if (jsonElement.isJsonPrimitive()) {
      return Optional.empty();
    } else {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(PublicState.class, new JsonStateDeserializer());
      Gson gson = builder.create();
      return Optional.of(gson.fromJson(jsonElement, PublicState.class));
    }
  }
}
