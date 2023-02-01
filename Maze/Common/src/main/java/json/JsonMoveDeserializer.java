package json;

import com.google.gson.*;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PublicState;

import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

public class JsonMoveDeserializer implements JsonDeserializer<Optional<Move>> {


    @Override
    public Optional<Move> deserialize(JsonElement jsonElement, Type type,
                                      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        //is string pass ->
        if (jsonElement.isJsonPrimitive()) { //todo DOES NOT GUARANTEE GOOD VALUES
            return Optional.empty();
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        int idx = jsonArray.get(0).getAsInt();
        Direction dir = deserializeDirection(jsonArray.get(1));
        int rotationBy90 = deserializeRotation(jsonArray.get(2));
        Coordinate coord = deserializeCoordinate(jsonArray.get(3));



        Move move = new Move(new SimpleEntry<Integer, Direction >(idx,dir),rotationBy90, coord);
        return Optional.of(move);


    }

    public Direction deserializeDirection(JsonElement elem) {
        //"LEFT", "RIGHT", "UP", "DOWN"
        return Direction.valueOf(elem.getAsString());
    }

    public int deserializeRotation(JsonElement elem) {
        int cwRotation = elem.getAsInt() / -90;
        return cwRotation;
    }

    public Coordinate deserializeCoordinate(JsonElement elem) {
        return JsonUtils.getCoord(elem);
    }




}
