package remote;

import players.IPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This represents a client to the Maze Game. This class will tcp connect to a remote server which will serve information
 * about the game to this client, which will then serve responses about how it will play the game.
 *  Receives method calls and return necessary results.
 */
public class Client {

    //opens connection with server proactively - given place to connect - port and String host - NO REPLY
    //when connection made, create player (OR PLAYER LIST) and send names. Then delegate to RemoteReferee for listening

    //one player one connection - 1 socket

    private final IPlayer myPlayer;
    private final int portNum;
    private final String hostName;

    //Constructor will setup connection with socket and send name then delegate to referee.
    public Client(IPlayer myPlayer, String hostName, int portNum) {
        //todo check valid inputs
        this.myPlayer=myPlayer;
        this.hostName = hostName;
        this.portNum=portNum;
    }

    /**
     * This method starts a remote connection to the server as given in constructor, will then play game
     */
    public void startRemoteConnection() {
        try (Socket socket = new Socket(hostName,portNum);
             OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream();) {
            //send name
            String jsonName = "\"" + myPlayer.name() + "\"";
            os.write(jsonName.getBytes(StandardCharsets.UTF_8));
            RemoteReferee rr = new RemoteReferee(myPlayer, is, os);
            rr.startListening();
        } catch (IOException e) {
            //Nothing specified
            //Gah
        }
        //nothing specified
    }



}
