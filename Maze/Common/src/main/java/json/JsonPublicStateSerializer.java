package json;

import com.google.gson.*;
import components.*;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.Optional;

public class JsonPublicStateSerializer implements JsonSerializer<PublicState> {


    @Override
    public JsonElement serialize(PublicState publicState, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Board.class, new JsonBoardSerializer());
        builder.registerTypeAdapter(Tile.class, new JsonTileSerializer());
        builder.registerTypeAdapter(Optional.class, new JsonActionSerializer());
        builder.serializeNulls();
        Gson gson = builder.create();
        JsonObject jObj = new JsonObject();
        jObj.add("board", gson.toJsonTree(publicState.getBoard()));
        jObj.add("spare", gson.toJsonTree(publicState.spare));
        jObj.add("plmt", getPlayersAsJson(publicState));
        jObj.add("last", gson.toJsonTree(publicState.lastAction));
        return jObj;
    }


    private JsonElement getPlayersAsJson(PublicState publicState) {
        JsonArray plmt = new JsonArray();
        for (PlayerInfo playerInfo : publicState.getPlayers()) {
            JsonObject refereePlayer = new JsonObject();
            Color playerColor = playerInfo.avatar;
            refereePlayer.add("current", JsonUtils.coordinateToJsonElement(playerInfo.currentPos));
            refereePlayer.add("home", JsonUtils.coordinateToJsonElement(playerInfo.home));
            StringBuilder colorString = new StringBuilder();
            colorString.append(StringUtils.leftPad(Integer.toHexString(playerColor.getRed()), 2, '0'));
            colorString.append(StringUtils.leftPad(Integer.toHexString(playerColor.getGreen()), 2, '0'));
            colorString.append(StringUtils.leftPad(Integer.toHexString(playerColor.getBlue()), 2, '0'));
            refereePlayer.addProperty("color", colorString.toString().toUpperCase());
            plmt.add(refereePlayer);
        }
        return plmt;
    }

}
