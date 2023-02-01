package deliverables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import components.MultiGoalRefereeState;
import components.SingleGoalRefereeState;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import json.JsonMultiGoalRefereeStateDeserializer;
import json.JsonStrictRefereeStateDeserializer;
import json.JsonUtils;
import players.IPlayer;
import players.Observer;
import players.ObserverView;
import players.PlayerImpl;
import referee.MultiGoalReferee;
import referee.SingleGoalReferee;

public class XBad2 {

  static void xBad2WithMultiGoal() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(MultiGoalRefereeState.class,
            new JsonMultiGoalRefereeStateDeserializer());
    Gson gson = gsonBuilder.create();
    JsonElement badPlayerSpecs = JsonParser.parseReader(reader);
    List<PlayerImpl> players = JsonUtils.deserializerPsBadPSBadPS2(badPlayerSpecs);
    List<IPlayer> properPlayerList = new ArrayList<>(players);
    Collections.reverse(properPlayerList);
    MultiGoalReferee multiGoalReferee = new MultiGoalReferee(properPlayerList, 200);

    MultiGoalRefereeState rs = gson.fromJson(reader, MultiGoalRefereeState.class);

    SimpleEntry<List<IPlayer>, List<IPlayer>> result = multiGoalReferee.playGame(rs);
    JsonElement output = JsonUtils.getOutputFromWinnersAndCheaters(result);
    System.out.println(gson.toJson(output));
  }

  static void xBad2() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonStrictRefereeStateDeserializer());
    Gson gson = gsonBuilder.create();
    JsonElement badPlayerSpecs = JsonParser.parseReader(reader);
    List<PlayerImpl> players = JsonUtils.deserializerPsBadPSBadPS2(badPlayerSpecs);
    List<IPlayer> properPlayerList = new ArrayList<>(players);
    SingleGoalReferee singleGoalReferee = new SingleGoalReferee(properPlayerList, 200);

    SingleGoalRefereeState rs = gson.fromJson(reader, SingleGoalRefereeState.class);

    SimpleEntry<List<IPlayer>, List<IPlayer>> result = singleGoalReferee.playGame(rs);
    JsonElement output = JsonUtils.getOutputFromWinnersAndCheaters(result);
    System.out.println(gson.toJson(output));
  }

  static void xBad2WithObserver() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonStrictRefereeStateDeserializer());
    Gson gson = gsonBuilder.create();
    JsonElement badPlayerSpecs = JsonParser.parseReader(reader);
    List<PlayerImpl> players = JsonUtils.deserializerPsBadPSBadPS2(badPlayerSpecs);
    List<IPlayer> properPlayerList = new ArrayList<>(players);
    SingleGoalReferee singleGoalReferee = new SingleGoalReferee(properPlayerList, 200);

    SingleGoalRefereeState rs = gson.fromJson(reader, SingleGoalRefereeState.class);

    Observer obs = new Observer();
    new ObserverView(obs);
    singleGoalReferee.addObserver(obs);

    SimpleEntry<List<IPlayer>, List<IPlayer>> result = singleGoalReferee.playGame(rs);
    JsonElement output = JsonUtils.getOutputFromWinnersAndCheaters(result);
    System.out.println(gson.toJson(output));
  }
}
