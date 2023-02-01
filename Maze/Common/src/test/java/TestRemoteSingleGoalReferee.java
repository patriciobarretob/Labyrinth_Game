import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.Coordinate;
import components.Direction;
import components.Move;
import json.JsonMoveSerializer;
import json.JsonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import players.IPlayer;
import remote.RemoteReferee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class TestRemoteSingleGoalReferee extends ATestLabyrinth {


    RemoteReferee rr1;
    InputStream is1;
    OutputStream os1;
    IPlayer mock;
    IPlayer mock2;
    StringBuilder sb;
    Queue<Optional<Move>> qOfMoves;
    Queue<Optional<Move>> qOfMoves2;
    String setupInput1;
    String setupInputEmpty;
    String wonInput1;

    String takeTurnInput1;
    String takeTurnInput2;
    Coordinate gol;
    Move actualMove;

    String moveSerialized;

    @Before
    public void init() {
        gol = new Coordinate(1 , 1);
        qOfMoves = new ArrayDeque<>();
        qOfMoves.add(Optional.empty());
        qOfMoves.add(Optional.empty());
        qOfMoves.add(Optional.empty());
        qOfMoves2 = new ArrayDeque<>();
        actualMove = new Move(new SimpleEntry<>(0, Direction.LEFT), 1, gol);
        qOfMoves2.add(Optional.of(actualMove));



        //THREE EMPTY MOVES -- PASSES
        sb = new StringBuilder();
        mock = new MockPlayer(sb, p1Name, publicGameState.getBoard(),qOfMoves);
        mock2 = new MockPlayer(sb, p2Name, publicGameState.getBoard(), qOfMoves2);
        Gson gson = new GsonBuilder().registerTypeAdapter(Optional.class, new JsonMoveSerializer()).serializeNulls().create();
        setupInput1 = gson.toJson(JsonUtils.serializeSetup("setup", Optional.of(publicGameState), gol));
        setupInputEmpty = gson.toJson(JsonUtils.serializeSetup("setup", Optional.empty(), gol));


        wonInput1 = gson.toJson(JsonUtils.serializeWon("win",true));

        takeTurnInput1 = gson.toJson(JsonUtils.serializeTakeTurn("take-turn", publicGameState));
        takeTurnInput2 = gson.toJson(JsonUtils.serializeTakeTurn("take-turn", publicGameState));

        moveSerialized = gson.toJson(Optional.of(actualMove));

    }

    @Test
    public void testGetSetUp() {
        String input = setupInput1;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rr1 = new RemoteReferee(mock, is1, os1);
        try {
            rr1.startListeningForMethodCalls();
        } catch (IllegalStateException e) {
            //as expected - end document not given
        }
        String[] split = sb.toString().split("\n");
        Assert.assertEquals(MockPlayer.SETUP_CALLED_STRING,
                split[0]);
        Assert.assertEquals(gol.toString(),
                split[2]);

        Assert.assertEquals("\"void\"", os1.toString());
    }

    @Test
    public void testGetWon() {
        String input = wonInput1;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rr1 = new RemoteReferee(mock, is1, os1);
        rr1.startListeningForMethodCalls();
        String[] split = sb.toString().split("\n");
        Assert.assertEquals(MockPlayer.WON_CALLED_STRING,
                split[0]);
        Assert.assertEquals("true",
                split[1]);

        Assert.assertEquals("\"void\"", os1.toString());
    }

    @Test
    public void testGetTakeTurn() {
        String input = takeTurnInput1;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rr1 = new RemoteReferee(mock, is1, os1);
        try {
            rr1.startListeningForMethodCalls();
        } catch (IllegalStateException e) {
            //as expected - end document not given
        }

        String[] split = sb.toString().split("\n");
        Assert.assertEquals(MockPlayer.TAKE_TURN_CALLED_STRING,
                split[0]);

        Assert.assertEquals("\"PASS\"", os1.toString());
    }

    @Test
    public void testGetTakeTurnWithMove() {
        String input = takeTurnInput2;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rr1 = new RemoteReferee(mock2, is1, os1);
        try {
            rr1.startListeningForMethodCalls();
        } catch (IllegalStateException e) {
            //as expected - end document not given
        }

        String[] split = sb.toString().split("\n");
        Assert.assertEquals(MockPlayer.TAKE_TURN_CALLED_STRING,
                split[0]);


        Assert.assertEquals(moveSerialized, os1.toString());
    }

    @Test
    public void testGetSetUpEmpty() {
        String input = setupInputEmpty;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rr1 = new RemoteReferee(mock, is1, os1);
        try {
            rr1.startListeningForMethodCalls();
        } catch (IllegalStateException e) {
            //as expected - end document not given
        }
        String[] split = sb.toString().split("\n");
        Assert.assertEquals(MockPlayer.SETUP_CALLED_STRING,
                split[0]);
        Assert.assertEquals(Optional.empty().toString(),
                split[1]);

        Assert.assertEquals(gol.toString(),
                split[2]);

        Assert.assertEquals("\"void\"", os1.toString());
    }




}
