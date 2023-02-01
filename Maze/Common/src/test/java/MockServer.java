import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that mocks a server by creating a connection on a port and hostname
 */
public class MockServer {

    int portNum;
    StringBuilder sb;
    String[] jsonsToSend;
    int numPlayers;

    public MockServer(int portNum, String[] jsonsToSend, StringBuilder sb, int numPlayers) {
        this.portNum = portNum;
        this.sb = sb;
        this.numPlayers = numPlayers;
    }


    public void startServer() {
        //create a connection and listen, write what i receive from connection, send jsons in order and write responses onto sb
        try (ServerSocket ss = new ServerSocket(portNum);) {
            List<Socket> los = new ArrayList<>();

            for (int i = 0; i < numPlayers; i++) {
                Socket singleSocket = ss.accept();
                //now must wait for name
                InputStream is = singleSocket.getInputStream();
                InputStreamReader isRead = new InputStreamReader(is);
                JsonReader jsonReader = new JsonReader(isRead);
                sb.append(jsonReader.nextString());
                sb.append('\n');
                singleSocket.close();
                los.add(singleSocket);

            }



        //todo expand for remote referees
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
