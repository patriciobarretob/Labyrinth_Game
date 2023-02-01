package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Optional;

import components.Coordinate;
import components.Direction;
import components.Move;
import json.JsonDirectionSerializer;

public class JsonMoveSerializer implements JsonSerializer<Optional<Move>> {
  @Override
  public JsonElement serialize(Optional<Move> move, Type type, JsonSerializationContext jsonSerializationContext) {
    if (!move.isPresent()) {
      return new JsonPrimitive("PASS");
    } else {
      Move moveNonEmpty = move.get();
      int index = moveNonEmpty.slide.getKey();
      Direction direction = moveNonEmpty.slide.getValue();
      int rotation = moveNonEmpty.rotations;
      Coordinate target = moveNonEmpty.target;
      return createChoiceJson(index, direction, rotation, target);
    }
  }

  private JsonElement createChoiceJson(int index, Direction direction, int rotation,
                                   Coordinate target) {
    JsonElement jsonIdx = new JsonPrimitive(index);
    JsonElement jsonDirection = serializeDirection(direction);
    JsonElement jsonDegree = serializeRotation(rotation);
    JsonElement jsonCoordinate = serializeCoordinate(target);
    JsonArray jArr = new JsonArray();
    jArr.add(jsonIdx);
    jArr.add(jsonDirection);
    jArr.add(jsonDegree);
    jArr.add(jsonCoordinate);
    return jArr;
  }

  private JsonElement serializeDirection(Direction direction) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Direction.class, new JsonDirectionSerializer());
    return gson.create().toJsonTree(direction);
  }

  private JsonElement serializeRotation(int rotation) {
    // yes this is a magic constant no we're not gonna fix it yet
    //TODO: fix it
    int counterClockwiseRotation = -rotation * 90;
    return new JsonPrimitive(counterClockwiseRotation);
  }

  private JsonElement serializeCoordinate(Coordinate target) {
    return JsonUtils.coordinateToJsonElement(target);
  }
}
