package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import components.Connector;

public class JsonConnectorSerializer implements JsonSerializer<Connector> {
  @Override
  public JsonElement serialize(Connector connector, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(connector.connectorChar);
  }
}
