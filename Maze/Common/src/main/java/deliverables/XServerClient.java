package deliverables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import components.ARefereeState;
import components.MultiGoalRefereeState;
import components.SingleGoalRefereeState;

import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import json.JsonMultiGoalRefereeStateDeserializer;
import json.JsonStrictRefereeStateDeserializer;
import json.JsonUtils;
import players.IPlayer;
import players.PlayerImpl;
import remote.Client;
import remote.Server;

//Entry point from Main.java which receives Json input from standard in and starts clients or a
//server using the given input
public class XServerClient {

  public static String DEFAULT_HOST = "127.0.0.1";

  //order of arguments: identifier, port , IP (optional)
  //identifier: identifies if data goes to client("13") or server("12") (one of: "12", "13")
  //port: port number (positive integer as string)
  //IP: optional input for IP address (valid IP address) only present if identifier is "client"

  //Based on the given args, either creates and runs clients with a given IP or a default IP
  static void xClients(String[] args){
    if(args.length < 3){
      createAndRunClientsWithDefaultIP(args[1]);
    }
    else{
      createAndRunClientsWithIP(args[1], args[2]);
    }
  }

  //Using default IP (DEFAULT_HOST) creates and runs clients with a given port
  private static void createAndRunClientsWithDefaultIP(String port){
    createAndRunClientsWithIP(port, DEFAULT_HOST);
  }

  //Using a given IP and port string, creates and runs clients
  private static void createAndRunClientsWithIP(String port, String ip) {

    int portInt = Integer.parseInt(port);

    createAndRunClients(portInt, ip);
  }

  //Creates and runs clients using a given IP and port number
  private static void createAndRunClients(int portInt, String ip) {
    List<Client> clients = createClients(portInt, ip);
    runClients(clients);
  }

  //Creates a list of players from standard in Json and creates a client for each player
  private static List<Client> createClients(int portInt, String ip) {
    List<Client> clients = new ArrayList<>();
    List<IPlayer> players = getPlayersFromJson();
    for (IPlayer player: players) {
      clients.add(createClientFromPlayer(player, portInt, ip));
    }
    return clients;
  }

  //Parse the first JSON element from STDIN and turns it into a list of players
  private static List<IPlayer> getPlayersFromJson() {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in));
    JsonElement playersElement = JsonParser.parseReader(reader);
    return getPlayersFromJsonElement(playersElement);
  }

  //Deserializes the given Json element into IPlayers and returns the list of players
  private static List<IPlayer> getPlayersFromJsonElement(JsonElement playersElement) {

    List<PlayerImpl> players = JsonUtils.deserializerPsBadPSBadPS2(playersElement);
    List<IPlayer> properPlayerList = new ArrayList<>(players);

    return properPlayerList;
  }

  // - Note: this requires that clients do not immediately attempt to connect when constructed
  private static Client createClientFromPlayer(IPlayer player, int portInt, String ip) {

    Client client = new Client(player, ip, portInt);

    return client;
  }

  // Code referenced from https://crunchify.com/how-to-run-multiple-threads-concurrently-in-java-executorservice-approach/
  //In order of input,
  // - Attempt to connect to Server with Client in separate thread, using runnables
  // - Wait 3 seconds
  // - Repeat until all clients done
  private static void runClients(List<Client> clients) {
    int numThreads = clients.size();
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

    for (Client client: clients) {
      executor.execute(() -> runClient(client));
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new IllegalStateException("Thread.sleep got interrupted...? oh no");
      }
    }

    executor.shutdown();
    while(!executor.isTerminated()) {
      System.out.println("Doing stuff...");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      //idk
    }
    //done :D
  }

  //Starts remote connection for a given client
  private static void runClient(Client client) {
    client.startRemoteConnection();
  }

  //Creates and runs a sever with given command line args for port num
  //order of arguments: identifier, port
  //identifier: identifies if data goes to client("13") or server("12") (one of: "12", "13")
  //port: port number (positive integer as string)
  static void xServer(String[] args){
    int portInt = Integer.parseInt(args[1]);
    createAndStartServer(portInt);
  }

  //Creates a state from the Json in STDIN and starts a server with that state and the given port num
  private static void createAndStartServer(int portInt) {
    JsonReader reader = new JsonReader(new InputStreamReader(System.in));
    ARefereeState state = getStateFromNextJsonValue(reader);
    startServer(portInt, state);
  }
  //Parses the next Json value from STDIN representing the state and turns it into a ARefereeState
  private static ARefereeState getStateFromNextJsonValue(JsonReader reader) {
    JsonElement stateElement = JsonParser.parseReader(reader);
    return getRefereeStateFromJsonElement(stateElement);
  }

  //Deserializes Json element representing state into either a Multi or single goal referee state
  private static ARefereeState getRefereeStateFromJsonElement(JsonElement stateElement) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonStrictRefereeStateDeserializer());
    gsonBuilder.registerTypeAdapter(MultiGoalRefereeState.class,
        new JsonMultiGoalRefereeStateDeserializer());
    Gson gson = gsonBuilder.create();

    if (JsonUtils.isRefereeStateSingleGoalState(stateElement)) {
      SingleGoalRefereeState rs = gson.fromJson(stateElement, SingleGoalRefereeState.class);
      return rs;
    } else {
      MultiGoalRefereeState rs = gson.fromJson(stateElement, MultiGoalRefereeState.class);
      return rs;
    }
  }

  //Initializes a server for the given state and port num, then starts that server and prints the
  //results of the game
  private static void startServer(int portInt, ARefereeState state) {
    Gson gson = new Gson();
    Server server = new Server(portInt, 20, 2, 4000);
    SimpleEntry<List<IPlayer>, List<IPlayer>> output = startServerFromState(server, state);
    JsonElement outputJson = JsonUtils.getOutputFromWinnersAndCheaters(output);
    System.out.println(gson.toJson(outputJson));
  }

  //Starts a server from the given state (State is one of SingleGoalRefereeState, MultiGoalRefereeState)
  //Based on type state either runs a single or multi goal game
  private static SimpleEntry<List<IPlayer>, List<IPlayer>> startServerFromState(Server server,
                                                                         ARefereeState state) {
    if (state instanceof SingleGoalRefereeState) {
      return server.startSingleGoalServer((SingleGoalRefereeState)state);
    } else if (state instanceof MultiGoalRefereeState) {
      return server.startMultiGoalServer((MultiGoalRefereeState) state);
    } else {
      throw new IllegalStateException("State is neither single or multi goal referee state!");
    }
  }

  //TODO
  // This method tosses a json value by reading it with the parser. No questions asked.
  private static void tossFirstJsonValue(JsonReader reader) {
    JsonParser.parseReader(reader);
  }


}