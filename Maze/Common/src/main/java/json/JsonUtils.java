package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import components.Board;
import components.Coordinate;
import components.PublicState;
import players.BadPlayer;
import players.BadPlayer2;
import players.IPlayer;
import players.PlayerImpl;

import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//TODO: test this with unit tests
public class JsonUtils {
  static Gson gson = new Gson();

  public static JsonElement readNextInputStrict(Reader input) throws IOException {
    Gson gson = new Gson();
    JsonElement nextElement = gson.getAdapter(JsonElement.class).read(gson.newJsonReader(input));
    return nextElement;
  }

  public static boolean isRefereeStateSingleGoalState(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    return jsonObject.get("goals") == null || jsonObject.get("goals").getAsJsonArray().size() == 0;
  }

  public static Coordinate getGoalFromSetupJson(JsonElement setupJson) {
    JsonArray jarr = setupJson.getAsJsonArray();
    JsonElement args = jarr.get(1);
    JsonArray argArr = args.getAsJsonArray();
    return getCoord(argArr.get(1));
  }

  public static Optional<PublicState> getOptionalPublicStateFromSetupJson(JsonElement setupJson) {
    JsonArray jarr = setupJson.getAsJsonArray();
    JsonElement args = jarr.get(1);
    JsonArray argArr = args.getAsJsonArray();
    return deserializeOptionalPublicState(argArr.get(0));
  }

  public static PublicState getPublicStateFromTakeTurnJson(JsonElement takeTurnJson) {
    JsonArray jarr = takeTurnJson.getAsJsonArray();
    JsonElement args = jarr.get(1);
    JsonArray argArr = args.getAsJsonArray();
    return deserializePublicState(argArr.get(0));
  }

  public static PublicState deserializePublicState(JsonElement elem) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PublicState.class, new JsonStateDeserializer());
    Gson gson = builder.create();
    return gson.fromJson(elem, PublicState.class);
  }


  public static Optional<PublicState> deserializeOptionalPublicState(JsonElement elem) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Optional.class, new JsonOptionalPublicStateDeserializer());
    Gson gson = builder.create();
    return gson.fromJson(elem, Optional.class);
  }

  public static JsonElement serializeSetup(String setupMethodName, Optional<PublicState> fState,
                                  Coordinate goal) {
    JsonArray jarr = new JsonArray();
    jarr.add(new JsonPrimitive(setupMethodName));
    JsonArray args = new JsonArray();
    JsonElement serializedState = serializeOptionalPublicState(fState);
    JsonElement serializedGoal = coordinateToJsonElement(goal);
    args.add(serializedState);
    args.add(serializedGoal);
    jarr.add(args);
    return jarr;
  }

  public static JsonElement serializeWon(String wonMethodName, boolean won) {
    JsonArray jarr = new JsonArray();
    jarr.add(new JsonPrimitive(wonMethodName));
    JsonArray args = new JsonArray();
    JsonElement serializedBool = new JsonPrimitive(won);

    args.add(serializedBool);
    jarr.add(args);
    return jarr;
  }

  public static boolean getBooleanFromWonJson(JsonElement boolJson) {
    JsonArray jarr = boolJson.getAsJsonArray();
    JsonElement args = jarr.get(1);
    JsonArray argArr = args.getAsJsonArray();
    return argArr.get(0).getAsBoolean();
  }


  public static JsonElement serializeTakeTurn(String takeTurnMethodName, PublicState fState) {
    JsonArray jarr = new JsonArray();
    jarr.add(new JsonPrimitive(takeTurnMethodName));
    JsonArray args = new JsonArray();
    JsonElement serializedState = serializePublicState(fState);
    args.add(serializedState);
    jarr.add(args);
    return jarr;
  }

  public static JsonElement serializePublicState(PublicState state) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PublicState.class, new JsonPublicStateSerializer());
    builder.serializeNulls();
    Gson gson = builder.create();
    return gson.toJsonTree(state);
  }

  public static JsonElement serializeOptionalPublicState(Optional<PublicState> oPState) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Optional.class, new JsonOptionalPublicStateSerializer());
    builder.serializeNulls();
    Gson gson = builder.create();
    return gson.toJsonTree(oPState);
  }

  public static List<PlayerImpl> deserializerPsBadPSBadPS2(JsonElement badPlayerSpec2) {
    return deserializePSGeneral(badPlayerSpec2, JsonUtils::getSpecAsPlayerImpl2);
  }

  public static PlayerImpl getSpecAsPlayerImpl2(JsonElement spec) {
    JsonArray playerSpec = spec.getAsJsonArray();
    if (playerSpec.size() == 2) {
      return getGoodPlayer(playerSpec);
    } else if (playerSpec.size() == 3){
      return getBadPlayer(playerSpec);
    } else {
      return getBadPlayer2(playerSpec);
    }
  }

  public static PlayerImpl getBadPlayer2(JsonElement goodSpec) {
    Gson gson = new GsonBuilder().registerTypeAdapter(BadPlayer2.class,
            new JsonBadPlayer2Deserializer()).create();
    return gson.fromJson(goodSpec, BadPlayer2.class);
  }

  public static JsonElement getOutputFromWinnersAndCheaters(AbstractMap.SimpleEntry<List<IPlayer>,
          List<IPlayer>> result) {
    List<IPlayer> winners = result.getKey();
    List<IPlayer> cheaters = result.getValue();
    JsonArray winnerArray = new JsonArray();
    JsonArray cheaterArray = new JsonArray();
    for(IPlayer player : winners) {
      winnerArray.add(player.name());
    }
    for(IPlayer player: cheaters) {
      cheaterArray.add(player.name());
    }
    JsonArray winnersAndCheaters = new JsonArray();
    winnersAndCheaters.add(winnerArray);
    winnersAndCheaters.add(cheaterArray);
    return winnersAndCheaters;
  }

  public static List<PlayerImpl> deserializePSAndBadPS(JsonElement badPlayerSpec) {
    return deserializePSGeneral(badPlayerSpec, JsonUtils::getSpecAsPlayerImpl);
  }

  private static List<PlayerImpl> deserializePSGeneral(JsonElement badPlayerSpec,
                                                       Function<JsonElement, PlayerImpl> jsonToPlayerImplFunc) {
    JsonArray playerSpecs = badPlayerSpec.getAsJsonArray();
    List<PlayerImpl> players = new ArrayList<>();
    for (JsonElement spec: playerSpecs) {
      PlayerImpl player = jsonToPlayerImplFunc.apply(spec);
      players.add(player);
    }
    return players;
  }

  public static PlayerImpl getSpecAsPlayerImpl(JsonElement spec) {
    JsonArray playerSpec = spec.getAsJsonArray();
    if (playerSpec.size() == 2) {
      return getGoodPlayer(playerSpec);
    } else {
      return getBadPlayer(playerSpec);
    }
  }

  public static PlayerImpl getGoodPlayer(JsonElement goodSpec) {
    Gson gson = new GsonBuilder().registerTypeAdapter(PlayerImpl.class,
            new JsonPlayerImplDeserializer()).create();
    return gson.fromJson(goodSpec, PlayerImpl.class);
  }

  public static PlayerImpl getBadPlayer(JsonElement goodSpec) {
    Gson gson = new GsonBuilder().registerTypeAdapter(BadPlayer.class,
            new JsonBadPlayerDeserializer()).create();
    return gson.fromJson(goodSpec, BadPlayer.class);
  }

  public static String listOfCoordinatesAsJsonOutput(List<Coordinate> coords) {
    JsonArray coordArray = coordinatesAsJsonArray(coords);
    return gson.toJson(coordArray);
  }

  public static JsonElement coordinateToJsonElement(Coordinate coord) {
    return gson.toJsonTree(new JsonCoordinate(coord.y, coord.x));
  }

  public static JsonArray coordinatesAsJsonArray(List<Coordinate> coords) {
    JsonArray jarr = new JsonArray();
    for (Coordinate c: coords) {
      jarr.add(coordinateToJsonElement(c));
    }
    return jarr;
  }

  public static Board getBoard(JsonElement board) {
    Gson gson = new Gson();
    JsonBoard b = gson.fromJson(board, JsonBoard.class);
    return b.getActual();
  }

  public static Coordinate getCoord(JsonElement coord) {
    Gson gson = new Gson();
    JsonCoordinate c = gson.fromJson(coord, JsonCoordinate.class);
    return c.getActual();
  }
}
