import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import players.IPlayer;
import remote.RemoteReferee;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * A Class meant to test server. does NOT PASS INTO A REMOTE REF JUST BUILDS THE SB
 * When testing you may need to use threads if done on the same device - note.
 */
public class MockClient {

    int portNum;
    String hostName;
    IPlayer player;
    StringBuilder sb;
    String[] sendToServ;
    boolean sendName;

    public MockClient(IPlayer myPlayer, String hostName, int portNum,
                      StringBuilder sb, String[] whatToSend, boolean sendName) {
        this.hostName = hostName;
        this.portNum = portNum;
        this.player = myPlayer;
        this.sb = sb;
        this.sendToServ = whatToSend;
        this.sendName = sendName;
    }

    public void startRemoteConnection() {
        //DOES NOT SEND TO
        try (Socket socket = new Socket(hostName, portNum);
             OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream();) {
            //send name
            String jsonName = "\"" + player.name() + "\"";
            if (sendName) {
                os.write(jsonName.getBytes(StandardCharsets.UTF_8));
            }
            // now, prepare for any inputs and respond with outputs

            JsonReader reader = new JsonReader(new InputStreamReader(is));
            sb.append(JsonParser.parseReader(reader));
            sb.append("\n");


        } catch (IOException e) {
            //Nothing specified
            //Gah
        }
        //nothing specified
    }


}
