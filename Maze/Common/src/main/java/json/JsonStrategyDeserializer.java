package json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import players.EuclidStrategy;
import players.IStrategy;
import players.RiemannStrategy;

// Deserializes a Strategy Designation JSON value
public class JsonStrategyDeserializer implements JsonDeserializer<IStrategy> {
  @Override
  public IStrategy deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    String stratString = jsonElement.getAsString();
    return getStrategyFromString(stratString);
  }

  private IStrategy getStrategyFromString(String stratString) {
    switch(stratString) {
      case "Euclid":
        return new EuclidStrategy();
      case "Riemann":
        return new RiemannStrategy();
      default:
        throw new IllegalArgumentException("Given jsonElement is not a valid strategy string!");
    }
  }
}
