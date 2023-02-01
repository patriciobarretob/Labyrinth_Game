package json;

import com.google.gson.*;
import components.Board;
import components.PublicState;

import java.lang.reflect.Type;
import java.util.Optional;

public class JsonOptionalPublicStateSerializer implements JsonSerializer<Optional<PublicState>> {


    @Override
    public JsonElement serialize(Optional<PublicState> publicState, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonElement result;
        if (publicState.isPresent()) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(PublicState.class, new JsonPublicStateSerializer());
            builder.serializeNulls();
            Gson gson = builder.create();
            result = gson.toJsonTree(publicState.get());
        }
        else {
            result = new JsonPrimitive(false);
        }
        return result;
    }
}
