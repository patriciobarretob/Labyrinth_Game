package remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
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

  //Creates Json reader for method calls using the input stream and then keeps listening
  public void startListeningForMethodCalls() {
    JsonReader reader = new JsonReader(new InputStreamReader(this.in));
    keepListeningForMethodCalls(reader);

  }

  //keeps listening for method calls until given a won call (i.e. game over)
  private void keepListeningForMethodCalls(JsonReader reader) {
    boolean done = false;
    while (!done) {
      JsonElement methodCall;
      try {
        methodCall = JsonParser.parseReader(reader);
      } catch (JsonParseException e) {
        break;
      }
      if (methodCall == null) {
        break;
      }
      done = handleMethodCall(methodCall);
    }
  }

  //Based on method call name, handles that method call
  //method call name is one of: "setup", "take-turn", "win"
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

  //Given a "setup" method call, gets the Json state and goal coordinate, passes it to the player,
  //and gives back a void result to the output stream
  private void handleSetup(JsonElement setupMethodCall) {
    Optional<PublicState> fState = JsonUtils.getOptionalPublicStateFromSetupJson(setupMethodCall);
    Coordinate goal = JsonUtils.getGoalFromSetupJson(setupMethodCall);
    this.player.setup(fState, goal);
    giveBackVoidResult();
  }

  //Sends a void result as a Json string to the output stream
  private void giveBackVoidResult() {
    JsonElement voidResult = new JsonPrimitive(VOID_RESULT_STRING);
    Gson gson = new Gson();
    try {
      out.write(gson.toJson(voidResult).getBytes(StandardCharsets.UTF_8));
      out.flush();
    } catch (IOException e) {
      throw new IllegalStateException("Something bad happened while trying to write void to " +
              "output!");
    }
  }

  //Given a "take-turn" method call, gets the Json state, passes it to the player,
  //and returns a move result to the output stream
  private void handleTakeTurn(JsonElement takeTurnMethodCall) {
    PublicState state = JsonUtils.getPublicStateFromTakeTurnJson(takeTurnMethodCall);
    Optional<Move> move = this.player.takeTurn(state);
    giveBackMoveResult(move);
  }

  //Sends a Choice Json to the output stream
  //a choice is either a move or a pass
  private void giveBackMoveResult(Optional<Move> move) {
    Gson gson =
            new GsonBuilder().registerTypeAdapter(Optional.class, new JsonMoveSerializer()).create();
    try {
      out.write(gson.toJson(move).getBytes(StandardCharsets.UTF_8));
      out.flush();
    } catch (IOException e) {
      throw new IllegalStateException("Something bad happened while trying to write move to " +
              "output!");
    }
  }

  //Given a "win" method call, tells a player if its a winner and return void result to output stream
  private void handleWin(JsonElement winMethodCall) {
    boolean winBoolean = JsonUtils.getBooleanFromWonJson(winMethodCall);
    this.player.won(winBoolean);
    giveBackVoidResult();
  }

}
