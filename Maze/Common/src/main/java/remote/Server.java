package remote;


import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import components.MultiGoalRefereeState;
import components.SingleGoalRefereeState;
import players.IPlayer;
import referee.MultiGoalReferee;
import referee.SingleGoalReferee;

import java.io.IOException;
import java.io.InputStream;
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
    private final int signUpTimeInSeconds;
    private final int timeToWaitForNameInSeconds;
    private final int maxPlayers;
    private final int minPlayers;

    private final int portNum;


    //TODO: turn these into static defaults.
    public Server(int portNum) {
        this(portNum, 20, 2);
    }

    public Server(int portNum, int signUpTimeInSeconds, int timeToWaitForNameInSeconds) {
        this(portNum, signUpTimeInSeconds, timeToWaitForNameInSeconds, 1000);
    }

    public Server(int portNum, int signUpTimeInSeconds, int timeToWaitForNameInSeconds,
                  int allowedTimePerMoveInMilliseconds) {
        this.portNum = portNum;
        this.signUpTimeInSeconds = signUpTimeInSeconds;
        this.timeToWaitForNameInSeconds = timeToWaitForNameInSeconds;
        this.maxPlayers = 6;
        this.minPlayers = 2;
        this.allowedTimePerMoveInMilliseconds = allowedTimePerMoveInMilliseconds;
    }

    // Sets up a referee and then either returns the result of playing a game with a
    // referee or an empty result if the referee was not able to be setup
    public SimpleEntry<List<IPlayer>, List<IPlayer>> startSingleGoalServer() {
        Optional<SingleGoalReferee> ref = setUpSingleGoalReferee();
        if (ref.isPresent()) {
            return ref.get().playGame();
        }
        return new SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
    }

    // Sets up a Single goal referee by
    // - Signing up players
    // - Returns a single goal Referee if there's enough (>= minPlayers) players
    // - Otherwise return an empty indicating there were not enough players
    private Optional<SingleGoalReferee> setUpSingleGoalReferee() {
        Optional<List<IPlayer>> players = getPlayers();
        if (!players.isPresent()) {
            return Optional.empty();
        }
        SingleGoalReferee ref = new SingleGoalReferee(players.get(),
                allowedTimePerMoveInMilliseconds);
        return Optional.of(ref);
    }

    //Signs up players from a tcp connection and returns a list of player if there's enough
    // (>= minPlayers) players, otherwise returns an empty
    private Optional<List<IPlayer>> getPlayers() {
        List<IPlayer> players;

        try (ServerSocket ss = new ServerSocket(portNum);) {
            players = signUpPlayers(ss, signUpTimeInSeconds);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (players == null || players.size() < minPlayers) {
            return Optional.empty();
        }
        Collections.reverse(players);
        return Optional.of(players);
    }

    //Runs a sign up with the given server socket and sign up time,
    //and if there are not enough players (< minPlayers), runs a second one.
    //returns the list of signed up players
    //Note that this sign up does not ensure uniqueness of player names.
    private List<IPlayer> signUpPlayers(ServerSocket ss, int signUpTimeInSeconds) {
        List<IPlayer> players = signUpOnce(ss, signUpTimeInSeconds, maxPlayers);
        int numPlayers = players.size();
        if (numPlayers < minPlayers) {
            players.addAll(signUpOnce(ss, signUpTimeInSeconds,
                maxPlayers - numPlayers));
        }
        return players;
    }


    //Given a server socket, sign up time and maximum number of players to sign up,
    //runs a sign up for the given amount of time.
    //Returns a list of signed up players
    private List<IPlayer> signUpOnce(ServerSocket ss, int signUpTimeInSeconds, int maxPlayers) {
        long curTime = System.currentTimeMillis();
        //end time represents time stamp of the end of the sign up period
        long endTime = curTime + signUpTimeInSeconds * 1000L;
        List<IPlayer> players = new ArrayList<>();
        //runs sign up period until max num of players is reached or sign up time ends
        while (players.size() <= maxPlayers && System.currentTimeMillis() < endTime) {
            //duration represents time left of sign up period
            long duration = endTime - System.currentTimeMillis();
            if (duration > 0) {
                Optional<IPlayer> maybePlayer = acceptPlayer(ss, duration);
                maybePlayer.ifPresent(players::add);
            }
        }
        return players;
    }

    // Attempts to sign up a player for the given duration. If unsuccessful, returns Optional
    // .empty.
    private Optional<IPlayer> acceptPlayer(ServerSocket ss, long signUpDurationInMilliseconds) {
        try {
            ss.setSoTimeout((int) (signUpDurationInMilliseconds));
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

    //Attempts to get the player name from a given socket within a specified time limit
    //If successful creates a remote player
    //If unsuccessful, returns Optional.empty
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

    //Gets a name from the given socket.
    private Optional<String> getNameFromSocket(Socket s) throws IOException {
        return getNameFromInputStream(s.getInputStream());
    }

    //Gets a name from the given input stream
    //This method throws an IOException if something goes wrong with the socket's input stream.
    //This method returns empty if:
    //- The given input is not a valid Json
    //- The given Json is not a Json String
    //- The given Json String is not a valid name.
    //public for testing purposes
    public Optional<String> getNameFromInputStream(InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is));
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

    //Determines if the given name is a valid name.
    private boolean isValidName(String name) {
        return name.length() >= 1 && name.length() <= 20 && name.matches("^[a-zA-Z0-9]+$");
    }

    //Given a single goal referee state, sets up and runs a single goal game
    //Returns the results of the game which is a SimpleEntry<winners, cheaters>
    //WARNING USED ONLY FOR TESTING, DONT TOUCH IF DONT KNOW WHAT YOU ARE DOING
    //PARALLEL DATA BETWEEN PLAYERS CONNECTED AND STATE
    public SimpleEntry<List<IPlayer>, List<IPlayer>> startSingleGoalServer(SingleGoalRefereeState rs) {
        Optional<SingleGoalReferee> ref = setUpSingleGoalReferee();
        if (ref.isPresent()) {
            return ref.get().playGame(rs);
        }
        return new SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
    }

    //Given a multi goal referee state, sets up and runs a multi goal game
    //Returns the results of the game which is a SimpleEntry<winners, cheaters>
    //WARNING USED ONLY FOR TESTING, DONT TOUCH IF DONT KNOW WHAT YOU ARE DOING
    //PARALLEL DATA BETWEEN PLAYERS CONNECTED AND STATE
    public SimpleEntry<List<IPlayer>, List<IPlayer>> startMultiGoalServer(MultiGoalRefereeState rs) {
        Optional<MultiGoalReferee> ref = setUpMultiGoalReferee();
        if (ref.isPresent()) {
            return ref.get().playGame(rs);
        }
        return new SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
    }



    // Sets up a Multi goal referee by
    // - Signing up players
    // - Returns a multi goal Referee if there's enough (>= minPlayers) players
    // - Otherwise return an empty indicating there were not enough players
    private Optional<MultiGoalReferee> setUpMultiGoalReferee() {
        Optional<List<IPlayer>> players = getPlayers();
        if (!players.isPresent()) {
            return Optional.empty();
        }
        MultiGoalReferee ref = new MultiGoalReferee(players.get(),
                allowedTimePerMoveInMilliseconds);
        return Optional.of(ref);
    }



}
