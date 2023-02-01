package json;

import com.google.gson.*;
import components.*;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * THIS MAKES STRICT REFEREE STATES
 */
public class JsonStrictRefereeStateDeserializer implements JsonDeserializer<SingleGoalRefereeState> {


    @Override
    public SingleGoalRefereeState deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObject = json.getAsJsonObject();
        Board board = JsonStateDeserializer.getBoardFromElement(jObject.get("board"));
        Tile tile = JsonStateDeserializer.getTileFromElement(jObject.get("spare"));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AbstractMap.SimpleEntry.class, new JsonRefereePlayerDeserializer());
        Gson gson = gsonBuilder.create();
        JsonArray array = jObject.get("plmt").getAsJsonArray();

        Queue<PlayerInfo> playerInfoQueue = new ArrayDeque<>();
        Map<Color, SingleGoalRefereePlayerInfo> infoMap = new HashMap<>();
        processPlayerInfo(gson, array, playerInfoQueue, infoMap);

        PublicState publicState = PublicState.constructStrictPublicState(board, tile, playerInfoQueue); //YES STRICT

        return new SingleGoalRefereeState(publicState, infoMap);
    }

    private void processPlayerInfo(Gson gson, JsonArray array, Queue<PlayerInfo> playerInfoQueue,
                                   Map<Color, SingleGoalRefereePlayerInfo> infoMap) {
        for (JsonElement elem : array) {
            AbstractMap.SimpleEntry<PlayerInfo, SingleGoalRefereePlayerInfo> player = gson.fromJson(elem, AbstractMap.SimpleEntry.class);
            playerInfoQueue.add(player.getKey());
            infoMap.put(player.getKey().avatar, player.getValue());
        }
    }


}
