package deliverables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import components.SingleGoalRefereeState;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import json.JsonRefereeStateDeserializer;
import json.JsonUtils;
import players.IPlayer;
import players.Observer;
import players.ObserverView;
import players.PlayerImpl;
import referee.SingleGoalReferee;

public class XBad {


  static void xBad() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonRefereeStateDeserializer());
    Gson gson = gsonBuilder.create();
    JsonElement badPlayerSpecs = JsonParser.parseReader(reader);
    List<PlayerImpl> players = JsonUtils.deserializePSAndBadPS(badPlayerSpecs);
    List<IPlayer> properPlayerList = new ArrayList<>(players);
    SingleGoalReferee singleGoalReferee = new SingleGoalReferee(properPlayerList);

    SingleGoalRefereeState rs = gson.fromJson(reader, SingleGoalRefereeState.class);

    SimpleEntry<List<IPlayer>, List<IPlayer>> result = singleGoalReferee.playGame(rs);
    JsonElement output = JsonUtils.getOutputFromWinnersAndCheaters(result);
    System.out.println(gson.toJson(output));
  }

  static void xBadWithObserver() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonRefereeStateDeserializer());
    Gson gson = gsonBuilder.create();
    JsonElement badPlayerSpecs = JsonParser.parseReader(reader);
    List<PlayerImpl> players = JsonUtils.deserializePSAndBadPS(badPlayerSpecs);
    List<IPlayer> properPlayerList = new ArrayList<>(players);
    SingleGoalReferee singleGoalReferee = new SingleGoalReferee(properPlayerList);

    SingleGoalRefereeState rs = gson.fromJson(reader, SingleGoalRefereeState.class);

    Observer obs = new Observer();
    new ObserverView(obs);
    singleGoalReferee.addObserver(obs);

    SimpleEntry<List<IPlayer>, List<IPlayer>> result = singleGoalReferee.playGame(rs);
    JsonElement output = JsonUtils.getOutputFromWinnersAndCheaters(result);
    System.out.println(gson.toJson(output));
  }

}
