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

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Board proposeBoard0(int rows, int columns) {
        //LOL
        return null;
    }

    @Override
    public void setup(Optional<PublicState> state0, Coordinate goal) {
        //translate state0 -> json
        //translate goal -> json
        JsonElement outputJson = JsonUtils.serializeSetup("setup", state0, goal);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //send jsons thru output stream
        try {
            this.outStream.write(gson.toJson(outputJson).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Wasn't able to write to RemotePlayer output.");
        }
        //wait and then receive void response (STILL REPSONSE)
        JsonReader reader = new JsonReader(new InputStreamReader(this.inStream));
        JsonElement result = JsonParser.parseReader(reader);
        if (!result.getAsString().equals("void")) {
            throw new IllegalStateException("Remote Player did not properly respond to setup.");
        }
        //throw if invalid or nonvoid response
    }

    @Override
    public Optional<Move> takeTurn(PublicState s) {
        JsonElement outputJson = JsonUtils.serializeTakeTurn("take-turn", s);

        Gson gson = new GsonBuilder().registerTypeAdapter(Optional.class,
                new JsonMoveDeserializer()).serializeNulls().create();
        //send jsons thru output stream
        try {
            this.outStream.write(gson.toJson(outputJson).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Wasn't able to write to RemotePlayer output.");
        }
        //wait and then receive CHOICE response (STILL REPSONSE)
        JsonReader reader = new JsonReader(new InputStreamReader(this.inStream));
        JsonElement result = JsonParser.parseReader(reader);
        //deserialize Choice



        Optional<Move> choice = gson.fromJson(result, Optional.class);

        return choice;
    }

    @Override
    public void won(boolean w) {
        //translate state0 -> json
        //translate goal -> json
        JsonElement outputJson = JsonUtils.serializeWon("win", w);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //send jsons thru output stream
        try {
            this.outStream.write(gson.toJson(outputJson).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Wasn't able to write to RemotePlayer output.");
        }
        //wait and then receive void response (STILL REPSONSE)
        JsonReader reader = new JsonReader(new InputStreamReader(this.inStream));
        JsonElement result = JsonParser.parseReader(reader);
        if (!result.getAsString().equals("void")) {
            throw new IllegalStateException("Remote Player did not properly respond to setup.");
        }
        //throw if invalid or nonvoid response
    }
}
