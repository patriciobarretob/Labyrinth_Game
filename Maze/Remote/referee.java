package remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import components.Coordinate;
import components.Move;
import components.PublicState;

import json.JsonMoveSerializer;
import json.JsonUtils;
import players.IPlayer;

/**
 * This class represents a remote listener which will receive method calls from a remote server,
 * and parses requests as referee calls to the local player.
 * The name is slightly a misnomer as it is not a referee.
 */
public class RemoteReferee {
  private final IPlayer player;
  private final InputStream in;
  private final OutputStream out;
  private static final String VOID_RESULT_STRING = "void";

  public RemoteReferee(IPlayer player, InputStream in, OutputStream out) {
    this.player = player;
    this.in = in;
    this.out = out;
  }

  public void startListening() {
    JsonReader reader = new JsonReader(new InputStreamReader(this.in));
    try {
      keepListening(reader);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Something bad happened while trying to read input!");
    }

  }

  //keeps listening until given a won call, when the game is done
  private void keepListening (JsonReader reader) throws IOException {
    boolean done = false;
    while (!done) {
      JsonElement methodCall = JsonParser.parseReader(reader);
      done = handleMethodCall(methodCall);
    }
  }

  //This method returns true when receiving a won method -- essentially a !done flag
  private boolean handleMethodCall(JsonElement methodCall) {
    JsonArray methodCallArr = methodCall.getAsJsonArray();
    String methodName = methodCallArr.get(0).getAsString();
    switch (methodName) {
      case "setup":
        handleSetup(methodCall);
        break;
      case "take-turn":
        handleTakeTurn(methodCall);
        break;
      case "win":
        handleWin(methodCall);
        return true;
      default:
        throw new IllegalArgumentException("Invalid method call");
    }
    return false;
  }

  private void handleSetup(JsonElement setupMethodCall) {
    Optional<PublicState> fState = JsonUtils.getOptionalPublicStateFromSetupJson(setupMethodCall);
    Coordinate goal = JsonUtils.getGoalFromSetupJson(setupMethodCall);
    this.player.setup(fState, goal);
    giveBackVoidResult();
  }

  private void giveBackVoidResult() {
    JsonElement voidResult = new JsonPrimitive(VOID_RESULT_STRING);
    Gson gson = new Gson();
    try {
      out.write(gson.toJson(voidResult).getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("Something bad happened while trying to write void to " +
              "output!");
    }
  }

  private void handleTakeTurn(JsonElement takeTurnMethodCall) {
    PublicState state = JsonUtils.getPublicStateFromTakeTurnJson(takeTurnMethodCall);
    Optional<Move> move = this.player.takeTurn(state);
    giveBackMoveResult(move);
  }

  private void giveBackMoveResult(Optional<Move> move) {
    Gson gson =
            new GsonBuilder().registerTypeAdapter(Optional.class, new JsonMoveSerializer()).create();
    try {
      out.write(gson.toJson(move).getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("Something bad happened while trying to write move to " +
              "output!");
    }
  }

  private void handleWin(JsonElement winMethodCall) {
    boolean winBoolean = JsonUtils.getBooleanFromWonJson(winMethodCall);
    this.player.won(winBoolean);
    giveBackVoidResult();
  }


}
