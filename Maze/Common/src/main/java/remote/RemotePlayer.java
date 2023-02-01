package remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import components.Board;
import components.Coordinate;
import components.Move;
import components.PublicState;
import java.io.Reader;
import json.JsonMoveDeserializer;
import json.JsonOptionalPublicStateSerializer;
import json.JsonPublicStateSerializer;
import json.JsonUtils;
import players.IPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * This represents a remote player which will use tcp connection to a remote player to send method calls and receive
 * responses (or failures).
 *
 */
public class RemotePlayer implements IPlayer  {

    private final InputStream inStream;
    private final OutputStream outStream;

    private final String name;


    //NOTE THROW OF IOEXCEPTION
    public RemotePlayer(InputStream is, OutputStream os, String name) {
        this.inStream = is;
        this.outStream = os;
        this.name = name;
    }

    //returns player's name as string
    @Override
    public String name() {
        return this.name;
    }

    //used for players to propose a board size
    @Override
    public Board proposeBoard0(int rows, int columns) {
        //LOL
        return null;
    }

    //Translates given state and goal into Json, attempts to send Json through output stream,
    //then waits indefinitely to receive void response
    //if unsuccessful, throws an ISE
    @Override
    public void setup(Optional<PublicState> state0, Coordinate goal) {
        //translate state0 -> json
        //translate goal -> json
        JsonElement outputJson = JsonUtils.serializeSetup("setup", state0, goal);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //send jsons thru output stream
        try {
            this.outStream.write(gson.toJson(outputJson).getBytes(StandardCharsets.UTF_8));
            this.outStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Wasn't able to write to RemotePlayer output.");
        }
        //wait and then receive void response (STILL RESPONSE)
        JsonElement result;
        try {
            result = JsonUtils.readNextInputStrict(new InputStreamReader(this.inStream));
        } catch (IOException ioe) {
            throw new IllegalStateException("Something went wrong when reading input");
        }
        if (!result.getAsString().equals("void")) {
            throw new IllegalStateException("Remote Player did not properly respond to setup.");
        }
        //throw if invalid or nonvoid response
    }

    //translates given public state into Json and then attempts to send it through output stream
    //After state is sent, it will wait indefinitely for a choice response and returns the choice
    //as an Optional<Move>. If unsuccessful, throws an ISE
    @Override
    public Optional<Move> takeTurn(PublicState s) {
        //Sets up serialization and deserialization
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Optional.class, new JsonMoveDeserializer())
            .serializeNulls()
            .create();
        //translate state -> json
        JsonElement outputJson = JsonUtils.serializeTakeTurn("take-turn", s);
        //send jsons thru output stream
        try {
            this.outStream.write(gson.toJson(outputJson).getBytes(StandardCharsets.UTF_8));
            this.outStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Wasn't able to write to RemotePlayer output.");
        }
        //wait and then receive CHOICE response (STILL REPSONSE)
        JsonElement result;
        try {
            result = JsonUtils.readNextInputStrict(new InputStreamReader(this.inStream));
        } catch (IOException ioe) {
            throw new IllegalStateException("Something went wrong when reading input");
        }
        //deserialize Choice
        Optional<Move> choice = gson.fromJson(result, Optional.class);

        return choice;
    }

    //Translates given boolean w into Json and attempts to send it through output stream
    //If message is sent, waits indefinitely for a void response
    //If unsuccessful, throws an ISE
    @Override
    public void won(boolean w) {
        //translate boolean -> json
        JsonElement outputJson = JsonUtils.serializeWon("win", w);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //send jsons thru output stream
        try {
            this.outStream.write(gson.toJson(outputJson).getBytes(StandardCharsets.UTF_8));
            this.outStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Wasn't able to write to RemotePlayer output.");
        }
        //wait and then receive void response (STILL REPSONSE)
        JsonElement result;
        try {
            result = JsonUtils.readNextInputStrict(new InputStreamReader(this.inStream));
        } catch (IOException ioe) {
            throw new IllegalStateException("Something went wrong when reading input");
        }
        if (!result.getAsString().equals("void")) {
            throw new IllegalStateException("Remote Player did not properly respond to setup.");
        }
        //throw if invalid or nonvoid response
    }
}
