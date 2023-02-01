package remote;


import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import components.RefereeState;
import players.IPlayer;
import players.Referee;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Facilitates remote connection from clients and creates an environment for the Maze game to be run.
 * Will query remote players and request responses based on the state of the game and the referee's requests.
 * NOTE PLAYERS ARE AGED BASED ON WHEN THEY CONNECT, OLDEST TO YOUNGEST.
 */
public class Server {

    private final int allowedTimePerMoveInMilliseconds;
    private final int setupTimeInSeconds;
    private final int timeToWaitForNameInSeconds;
    private final int maxPlayers;

    private final int portNum;


    //TODO: turn these into static defaults.
    public Server(int portNum) {
        this(portNum, 20, 2);

    }

    public Server(int portNum, int setupTimeInSeconds, int timeToWaitForNameInSeconds) {
        this.portNum = portNum;
        this.setupTimeInSeconds = setupTimeInSeconds;
        this.timeToWaitForNameInSeconds = timeToWaitForNameInSeconds;
        this.maxPlayers = 6;
        this.allowedTimePerMoveInMilliseconds = 1000;
    }

    // Starts a server, consisting of:
    // - Signing up players
    // - Delegating a game to a Referee if there's enough (>1) players
    // - Returns the result of a ran game, or two empty lists if the game didn't play.
    public SimpleEntry<List<IPlayer>, List<IPlayer>> startServer() {
        List<IPlayer> players;

        try (ServerSocket ss = new ServerSocket(portNum);) {
            players = serverSetup(ss, setupTimeInSeconds);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (players == null || players.size() <= 1) {
            return new SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
        }
        Collections.reverse(players);
        Referee ref = new Referee(players, allowedTimePerMoveInMilliseconds);
        return ref.playGame();
    }


    //WARNING USED ONLY FOR TESTING, DONT TOUCH IF DONT KNOW WHAT YOU ARE DOING
    //PARALLEL DATA BETWEEN PLAYERS CONNECTED AND STATE
     public SimpleEntry<List<IPlayer>, List<IPlayer>> startServer(RefereeState rs) {
        List<IPlayer> players;

        try (ServerSocket ss = new ServerSocket(portNum);) {
            players = serverSetup(ss, setupTimeInSeconds);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (players == null || players.size() <= 1) {
            return new SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
        }
        Collections.reverse(players);
        Referee ref = new Referee(players, allowedTimePerMoveInMilliseconds);
        return ref.playGame(rs);
    }



    // Runs a signup, and if there are not enough players, runs a second one.
    // Note that this signup does not ensure uniqueness of player names.
    private List<IPlayer> serverSetup(ServerSocket ss, int setUpTimeInSeconds) {
        List<IPlayer> players = signUpOnce(ss, setUpTimeInSeconds, maxPlayers);
        int numPlayers = players.size();
        if (numPlayers < maxPlayers) {
            players.addAll(signUpOnce(ss, setUpTimeInSeconds,
                    maxPlayers - numPlayers));
        }
        return players;
    }

    // Runs a signup for a set amount of time.
    private List<IPlayer> signUpOnce(ServerSocket ss, int setUpTimeInSeconds, int numPlayers) {
        long curTime = System.currentTimeMillis();
        long endTime = curTime + setUpTimeInSeconds * 1000L;
        List<IPlayer> players = new ArrayList<>();
        while (players.size() <= numPlayers && System.currentTimeMillis() < endTime) {
            Optional<IPlayer> maybePlayer = acceptPlayer(ss, endTime);
            maybePlayer.ifPresent(players::add);
        }
        return players;
    }

    // Attempts to sign up a player until the given time limit. If unsuccessful, returns Optional
    // .empty.
    private Optional<IPlayer> acceptPlayer(ServerSocket ss, long timeLimitInMilliseconds) {
        try {
            ss.setSoTimeout((int) (timeLimitInMilliseconds - System.currentTimeMillis()));
            Socket socket = ss.accept();
            return createPlayerFromSocket(socket, timeToWaitForNameInSeconds);
        } catch (SocketTimeoutException e) {
            return Optional.empty();
        } catch (SocketException e) {
            e.printStackTrace();
            throw new IllegalStateException("Something went wrong setting up TCP!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Something went wrong accepting sockets!");
        }
    }

    // Attempts to sign up a player from a connection with them. If unsuccessful, returns
    // Optional.empty
    private Optional<IPlayer> createPlayerFromSocket(Socket s, int maxTimeToWaitForNameInSeconds) {
        try {
            s.setSoTimeout(maxTimeToWaitForNameInSeconds * 1000);
            Optional<String> name = getNameFromSocket(s);
            if (!name.isPresent()) {
                return Optional.empty();
            }
            return Optional.of(new RemotePlayer(s.getInputStream(), s.getOutputStream(),
                    name.get()));
        } catch (SocketTimeoutException e) {
            return Optional.empty();
        } catch (SocketException e) {
            e.printStackTrace();
            throw new IllegalStateException("Something went wrong setting up TCP!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to create remote player from input/output " +
                    "streams!");
        }
    }

    // Gets a name from the given socket.
    // This method throws an IOException if something goes wrong with the socket's input stream.
    // This method returns empty if:
    // - The given input is not a valid Json
    // - The given Json is not a Json String
    // - The given Json String is not a valid name.
    private Optional<String> getNameFromSocket(Socket s) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(s.getInputStream()));
        try {
            JsonToken nextToken = reader.peek();
            if (!(nextToken == JsonToken.STRING)) {
                return Optional.empty();
            }
            String name = reader.nextString();
            if (!isValidName(name)) {
                return Optional.empty();
            }
            return Optional.of(name);
        } catch (JsonParseException | IllegalStateException e) {
            return Optional.empty();
        }
    }

    // Determines if the given name is a valid name.
    private boolean isValidName(String name) {
        return name.length() >= 1 && name.length() <= 20 && name.matches("^[a-zA-Z0-9]+$");
    }


}
