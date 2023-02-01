import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import components.Board;
import components.Connector;
import components.Coordinate;
import components.Direction;
import components.Move;
import components.PlayerInfo;
import components.PublicState;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import components.Tile;
import json.JsonMoveSerializer;
import json.JsonUtils;
import remote.RemotePlayer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class TestRemotePlayer extends ATestLabyrinth {
    RemotePlayer rp1;
    InputStream is1;
    OutputStream os1;
    String name1;

    @Before
    public void init() {
        name1 = "605";
    }


    @Test
    public void testSetUp() {
        String input = "\"void\"";
        String output;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        Coordinate goal = new Coordinate(1,1);
        rp1.setup(Optional.of(publicGameState), goal);
        output = os1.toString();
        JsonElement outputJson = JsonParser.parseString(output);
        Optional<PublicState> returnedState =
                JsonUtils.getOptionalPublicStateFromSetupJson(outputJson);
        Coordinate returnedGoal = JsonUtils.getGoalFromSetupJson(outputJson);
        Assert.assertTrue(stateEquals(publicGameState, returnedState.get()));
        Assert.assertEquals(goal, returnedGoal);
    }

    @Test
    public void testSetUpFalse() {
        String input = "\"void\"";
        String output;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        Coordinate goal = new Coordinate(1,1);
        rp1.setup(Optional.empty(), goal);
        output = os1.toString();
        JsonElement outputJson = JsonParser.parseString(output);
        Optional<PublicState> returnedState =
                JsonUtils.getOptionalPublicStateFromSetupJson(outputJson);
        Coordinate returnedGoal = JsonUtils.getGoalFromSetupJson(outputJson);
        Assert.assertEquals(Optional.empty(), returnedState);
        Assert.assertEquals(goal, returnedGoal);
    }

    @Test
    public void testTakeTurnPass() {
        String input = "\"PASS\"";
        String output;
        startBoard = new char[][]{
                {'┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼'}
        };
        p1Home = new Coordinate(1, 1);
        p2Home = new Coordinate(3, 1);
        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[1], gemList[1], Connector.getFromChar('┼'));
        Queue<PlayerInfo> players = new ArrayDeque<>();
        PlayerInfo p1 = new PlayerInfo(p1Color, p1Home);
        PlayerInfo p2 = new PlayerInfo(p1Color, p1Home);
        players.add(p1);
        players.add(p2);
        PublicState s = new PublicState(b, spare, players);
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        Optional<Move> returnedMove = rp1.takeTurn(s);
        output = os1.toString();
        JsonElement outputJson = JsonParser.parseString(output);
        PublicState returnedState =
                JsonUtils.getPublicStateFromTakeTurnJson(outputJson);
        Assert.assertTrue(stateEquals(s, returnedState));
        Assert.assertEquals(Optional.empty(), returnedMove);
    }

    @Test
    public void testTakeTurnMove() {
        Move move = new Move(new SimpleEntry<>(1, Direction.DOWN), 0, new Coordinate(2, 2));
        Gson gson = new GsonBuilder().registerTypeAdapter(Optional.class,
                new JsonMoveSerializer()).create();
        String input = gson.toJson(Optional.of(move));
        String output;
        startBoard = new char[][]{
                {'┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼'},
                {'┼', '┼', '┼', '┼'}
        };
        p1CurPos = new Coordinate(1, 1);
        p1Home = new Coordinate(1, 1);
        p2Home = new Coordinate(3, 1);
        Board b = new Board(createBoard(startBoard));
        Tile spare = new Tile(gemList[1], gemList[1], Connector.getFromChar('┼'));
        Queue<PlayerInfo> players = new ArrayDeque<>();
        PlayerInfo p1 = new PlayerInfo(p1Color, p1Home);
        p1.currentPos = p1CurPos;
        PlayerInfo p2 = new PlayerInfo(p1Color, p1Home);
        players.add(p1);
        players.add(p2);
        PublicState s = new PublicState(b, spare, players);
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        Optional<Move> returnedMove = rp1.takeTurn(s);
        output = os1.toString();
        JsonElement outputJson = JsonParser.parseString(output);
        PublicState returnedState =
                JsonUtils.getPublicStateFromTakeTurnJson(outputJson);
        Assert.assertTrue(stateEquals(s, returnedState));
        Assert.assertEquals(move, returnedMove.get());
    }

    @Test
    public void testWonTrue() {
        String input = "\"void\"";
        String output;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        rp1.won(true);
        output = os1.toString();
        JsonElement outputJson = JsonParser.parseString(output);
        boolean returnedBoolean = JsonUtils.getBooleanFromWonJson(outputJson);
        Assert.assertTrue(returnedBoolean);
    }

    @Test
    public void testWonFalse() {
        String input = "\"void\"";
        String output;
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        rp1.won(false);
        output = os1.toString();
        JsonElement outputJson = JsonParser.parseString(output);
        boolean returnedBoolean = JsonUtils.getBooleanFromWonJson(outputJson);
        Assert.assertFalse(returnedBoolean);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidStringSetUpInput() {
        String input = "void";
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        Coordinate goal = new Coordinate(1,1);
        rp1.setup(Optional.of(publicGameState), goal);
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongStringSetUpInput() {
        String input = "\"four\"";
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        Coordinate goal = new Coordinate(1,1);
        rp1.setup(Optional.of(publicGameState), goal);
    }

    @Test(expected = IllegalStateException.class)
    public void testTakeTurnInvalidStringInput() {
        String input = "PASS";
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        rp1.takeTurn(this.publicGameState);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testTakeTurnObjectInput() {
        String input = "{}";
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        rp1.takeTurn(this.publicGameState);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidStringWinInput() {
        String input = "void";
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        rp1.won(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongStringWinInput() {
        String input = "\"four\"";
        is1 = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        os1 = new ByteArrayOutputStream();
        rp1 = new RemotePlayer(is1, os1, name1);
        rp1.won(true);
    }

}
