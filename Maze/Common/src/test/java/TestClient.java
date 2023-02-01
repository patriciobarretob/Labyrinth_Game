import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import players.IPlayer;
import remote.Client;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.fail;

/**
 * Class dedicated for testing client
 */
public class TestClient extends ATestLabyrinth {

    MockServer ms;
    Client c1;
    Client c2;
    Client c3;
    Client c4;
    StringBuilder sbClient;
    StringBuilder sbPlayers;
    String[] jsonsToUse;

    IPlayer mp1;
    IPlayer mp2;
    IPlayer mp3;
    IPlayer mp4;


    @Before
    public void setUp() {
        sbClient = new StringBuilder();
        sbPlayers = new StringBuilder();
        mp1 = new MockPlayer(sbPlayers,p1Name,board1,new ArrayDeque<>());
        mp2 = new MockPlayer(sbPlayers,p2Name,board1,new ArrayDeque<>());
        mp3 = new MockPlayer(sbPlayers,p1Name,board1,new ArrayDeque<>());
        mp4 = new MockPlayer(sbPlayers,p2Name,board1,new ArrayDeque<>());
        ms = new MockServer(8080,jsonsToUse,sbClient, 1);
        c1 = new Client(mp1, "localhost", 8080);
        c2 = new Client(mp2, "localhost", 8080);
        c3 = new Client(mp3, "localhost", 8080);
        c4 = new Client(mp4, "localhost", 8080);

    }

    @Test
    public void testStartUps() {

        Runnable runnable = ()->{ms.startServer();};
        Thread t = new Thread(runnable);
        t.start();

        try {
            c1.startRemoteConnection();
            fail();

        } catch (IllegalStateException e) {
            //good
        }
        String[] sbCSplit = sbClient.toString().split("\n");
        String[] sbPSplit = sbPlayers.toString().split("\n");
        Assert.assertEquals(p1Name, sbCSplit[0]);
        Assert.assertEquals(MockPlayer.NAME_CALLED_STRING, sbPSplit[0]);

        t.interrupt();
    }






}
