package deliverables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import components.SingleGoalRefereeState;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import json.JsonPlayerImplListDeserializer;
import json.JsonRefereeStateDeserializer;
import players.IPlayer;
import players.Observer;
import players.ObserverView;
import referee.SingleGoalReferee;

public class XGame {


  static void xGame() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonRefereeStateDeserializer());
    gsonBuilder.registerTypeAdapter(List.class, new JsonPlayerImplListDeserializer());
    Gson gson = gsonBuilder.create();
    List<IPlayer> players = gson.fromJson(reader, List.class);
    SingleGoalReferee ref = new SingleGoalReferee(players);
    SingleGoalRefereeState state = gson.fromJson(reader, SingleGoalRefereeState.class);
    SimpleEntry<List<IPlayer>, List<IPlayer>> result = ref.playGame(state);
    List<IPlayer> winners = result.getKey();
    JsonArray output = new JsonArray();
    for(IPlayer player : winners) {
      output.add(player.name());
    }
    System.out.println(gson.toJson(output));
  }

  static void xGameWithObserver() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonRefereeStateDeserializer());
    gsonBuilder.registerTypeAdapter(List.class, new JsonPlayerImplListDeserializer());
    Gson gson = gsonBuilder.create();
    List<IPlayer> players = gson.fromJson(reader, List.class);
    SingleGoalReferee ref = new SingleGoalReferee(players);
    SingleGoalRefereeState state = gson.fromJson(reader, SingleGoalRefereeState.class);

    Observer obs = new Observer();
    new ObserverView(obs);
    ref.addObserver(obs);

    SimpleEntry<List<IPlayer>, List<IPlayer>> result = ref.playGame(state);
    List<IPlayer> winners = result.getKey();
    JsonArray output = new JsonArray();
    for(IPlayer player : winners) {
      output.add(player.name());
    }
    System.out.println(gson.toJson(output));
  }


}
