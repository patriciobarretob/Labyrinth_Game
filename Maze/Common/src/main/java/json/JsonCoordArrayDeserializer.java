package json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import components.Coordinate;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonCoordArrayDeserializer implements JsonDeserializer<Coordinate[]> {

  @Override
  public Coordinate[] deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

    JsonArray jsonArray = jsonElement.getAsJsonArray();
    Coordinate[] coordArray = new Coordinate[jsonArray.size()];

    for (int i = 0; i < jsonArray.size(); i++){
      coordArray[i] = JsonUtils.getCoord(jsonArray.get(i));
    }

    return coordArray;
  }
}
