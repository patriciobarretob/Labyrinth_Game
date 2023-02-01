package remote;

import players.IPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This represents a client to the Maze Game. This class will tcp connect to a remote server which will serve information
 * about the game to this client, which will then serve responses about how it will play the game.
 *  Receives method calls and return necessary results.
 */
public class Client {

    private final IPlayer myPlayer;
    private final int portNum;
    private final String hostName;

    public Client(IPlayer myPlayer, String hostName, int portNum) {
        //todo check valid inputs
        this.myPlayer = myPlayer;
        this.hostName = hostName;
        this.portNum = portNum;
    }

    // TODO: Ensure that the Client shuts down even if a bad player gets kicked.
    /**
     * This method starts a remote connection to the server as given in constructor, will then play game
     */
    public void startRemoteConnection() {
        try {
            //create connection
            Socket socket = getConnection();
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            //send name
            String jsonName = "\"" + myPlayer.name() + "\"";
            os.write(jsonName.getBytes(StandardCharsets.UTF_8));
            os.flush();
            //start playing
            RemoteReferee rr = new RemoteReferee(myPlayer, is, os);
            rr.startListeningForMethodCalls();
        } catch (IOException e) {
            //Nothing specified
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //nothing specified
    }

    //EFFECT: Blocks until the socket is connected.
    private Socket getConnection() throws IOException, InterruptedException {
        Socket socket;
        while(true) {
            try {
                socket = new Socket(hostName, portNum);
            } catch (ConnectException e){
                System.out.println("Connecting...");
                Thread.sleep(500);
                continue;
            }
            if(socket.isConnected()) {
                break;
            }
        }
        return socket;
    }


}
