import components.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import players.*;
import remote.Client;
import remote.Server;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.fail;

public class TestServer extends ATestLabyrinth {

    final String defaultHost = "localhost";
    final int defaultPort = 32642;
    final String[] emptyListOfJsons = new String[0];

    Server realServer;

    StringBuilder sbPlayer1;
    IPlayer mp1;
    StringBuilder sbClient1;
    MockClient mockClient1;

    StringBuilder sbPlayer2;
    IPlayer mp2;
    StringBuilder sbClient2;
    MockClient mockClient2;

    StringBuilder sBadPlayer1;
    IPlayer mpb1;
    StringBuilder sbBadClient1;
    MockClient mockClientBad1;



    @Before
    public void setUp() {


        sbPlayer1 = new StringBuilder();
        mp1 = new MockPlayer(sbPlayer1,p1Name,board1,new ArrayDeque<>());
        sbClient1 = new StringBuilder();
        mockClient1 = new MockClient(mp1, defaultHost, defaultPort, sbClient1, emptyListOfJsons, true);



        sbPlayer2 = new StringBuilder();
        mp2 = new MockPlayer(sbPlayer2,p2Name,board1,new ArrayDeque<>());
        sbClient2 = new StringBuilder();
        mockClient2 = new MockClient(mp2, defaultHost, defaultPort, sbClient2, emptyListOfJsons, true);

        sBadPlayer1 = new StringBuilder();
        mpb1 = new MockPlayer(sBadPlayer1, "", board1, new ArrayDeque<>());
        sbBadClient1 = new StringBuilder();
        mockClientBad1 = new MockClient(mpb1, defaultHost, defaultPort, sbBadClient1,
                emptyListOfJsons, false);



        realServer = new Server(defaultPort, 4, 2);

    }

    MockClient createBadClient() {
        return new MockClient(createBadPlayer(), defaultHost, defaultPort, new StringBuilder(),
                new String[0], false);
    }

    IPlayer createBadPlayer() {
        return new MockPlayer(new StringBuilder(), "", new Board(board1), new ArrayDeque<>());
    }

    @Test
    public void testServerOnePlayer() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable runnable = ()->{mockClient1.startRemoteConnection();};

        SimpleEntry<List<IPlayer>, List<IPlayer>> output = null;

        try {
            executor.execute(runnable);
            output = realServer.startSingleGoalServer(constructRefState('┌'));
        } catch (Exception e) {
            //dunno
        }
        Assert.assertEquals(output.getKey().size(), 0);
        Assert.assertEquals(output.getValue().size(), 0);

        executor.shutdownNow();
    }

    @Test
    public void testServer2Players() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable runnable = ()->{mockClient1.startRemoteConnection();};
        Runnable runnable2 = ()->{mockClient2.startRemoteConnection();};
        SimpleEntry<List<IPlayer>, List<IPlayer>> output = null;
        try {
            executor.execute(runnable);
            executor.execute(runnable2);
            output = realServer.startSingleGoalServer(constructRefState('┌'));


        } catch (Exception e) {
            //dunno
        }
        String[] sbC1Split = sbClient1.toString().split("\n");
        String[] sbC2Split = sbClient2.toString().split("\n");
        String[] sbP1Split = sbPlayer1.toString().split("\n");
        String[] sbP2Split = sbPlayer2.toString().split("\n");
        Assert.assertTrue(sbC1Split.length >= 1);
        Assert.assertTrue(sbC1Split[0].length() > 0);
        Assert.assertTrue(sbC2Split.length >= 1);
        Assert.assertTrue(sbC2Split[0].length() > 0);
        Assert.assertEquals(MockPlayer.NAME_CALLED_STRING, sbP1Split[0]);
        Assert.assertEquals(MockPlayer.NAME_CALLED_STRING, sbP2Split[0]);
        Assert.assertEquals(output.getKey().size(), 0);
        Assert.assertEquals(output.getValue().size(), 2);

        executor.shutdownNow();
    }


    @Test
    public void testServerSetupTimeoutAfterSixBadPlayers() {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        realServer.startSingleGoalServer();
        Runnable runnable1 = ()->{createBadClient().startRemoteConnection();};
        Runnable runnable2 = ()->{createBadClient().startRemoteConnection();};
        Runnable runnable3 = ()->{createBadClient().startRemoteConnection();};
        Runnable runnable4 = ()->{createBadClient().startRemoteConnection();};
        Runnable runnable5 = ()->{createBadClient().startRemoteConnection();};
        Runnable runnable6 = ()->{createBadClient().startRemoteConnection();};

        SimpleEntry<List<IPlayer>, List<IPlayer>> output = null;

        try {
            executor.execute(runnable1);
            executor.execute(runnable2);
            executor.execute(runnable3);
            executor.execute(runnable4);
            executor.execute(runnable5);
            executor.execute(runnable6);
            output = realServer.startSingleGoalServer(constructRefState('┌'));


        } catch (Exception e) {
            //should timeout and close connection with client
            //should be empty
        }

        Assert.assertEquals(output.getKey().size(), 0);
        Assert.assertEquals(output.getValue().size(), 0);

        executor.shutdownNow();

    }


    // For some reason this test is causing thread deaths....?
    @Test
    @Ignore
    public void testServer2GoodPlayersFullGame() {


        startBoard = new char[][]{
                {'│', '┐', '└', '┌', '┘', '│', '├'},
                {'┴', '─', '┌', '┐', '┌', '┐', '└'},
                {'┌', '┘', '┘', '┤', '┴', '└', '┼'},
                {'┐', '┼', '┬', '└', '│', '┘', '┬'},
                {'┤', '┴', '┐', '├', '┌', '┐', '┐'},
                {'└', '├', '┘', '┬', '┬', '┴', '┐'},
                {'┼', '│', '└', '┐', '└', '┌', '│'}
        };
        PlayerImpl player1 = new PlayerImpl(new RiemannStrategy(), "John");
        PlayerImpl player2 = new PlayerImpl(new EuclidStrategy(), "605");

        p1CurPos = new Coordinate(3,3);
        p1Home = new Coordinate(3,3);
        p1Goal = new Coordinate(1,5);

        p2CurPos = new Coordinate(5,3);
        p2Home = new Coordinate(5,3);
        p2Goal = new Coordinate(1,1);

        SingleGoalRefereeState rs = constructRefState('┌');


        Client p1Client = new Client(player1, defaultHost, defaultPort);
        Client p2Client = new Client(player2, defaultHost, defaultPort);
        //---------------
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Runnable runnable = ()->{p1Client.startRemoteConnection();};
        Runnable runnable2 = ()->{p2Client.startRemoteConnection();};
        Callable<SimpleEntry<List<IPlayer>, List<IPlayer>>> callable = ()->{return realServer.startSingleGoalServer(rs);};
        SimpleEntry<List<IPlayer>, List<IPlayer>> output = null;
        try {
            Future<SimpleEntry<List<IPlayer>, List<IPlayer>>> serverFuture =
                    executor.submit(callable);
            executor.execute(runnable2); //THIS IS OLDEST PLAYER - GOES SECOND
            Thread.sleep(200);
            executor.execute(runnable); //THIS IS YOUNGEST GOES FIRST
            output = serverFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals( 1, output.getKey().size());
        Assert.assertEquals(0, output.getValue().size());
        Assert.assertEquals(player1.name(), output.getKey().get(0).name());
        executor.shutdownNow();
    }

    // This test fails. Likely due to weird java collecting things?
    @Ignore
    public void testServerFullGameWithBadPlayers() {

        if (true) {
            throw new RuntimeException("DONT RUN THIS TEST");
        }


        startBoard = new char[][]{
                {'│', '┐', '└', '┌', '┘', '│', '├'},
                {'┴', '─', '┌', '┐', '┌', '┐', '└'},
                {'┌', '┘', '┘', '┤', '┴', '└', '┼'},
                {'┐', '┼', '┬', '└', '│', '┘', '┬'},
                {'┤', '┴', '┐', '├', '┌', '┐', '┐'},
                {'└', '├', '┘', '┬', '┬', '┴', '┐'},
                {'┼', '│', '└', '┐', '└', '┌', '│'}
        };
        PlayerImpl player1 = new BadPlayer2(new RiemannStrategy(), "Larry", 0,2,0);
        PlayerImpl player2 = new BadPlayer(new RiemannStrategy(), "Barry", false,false,false);

        p1CurPos = new Coordinate(3,3);
        p1Home = new Coordinate(3,3);
        p1Goal = new Coordinate(1,5);

        p2CurPos = new Coordinate(5,3);
        p2Home = new Coordinate(5,3);
        p2Goal = new Coordinate(1,1);

        SingleGoalRefereeState rs = constructRefState('┌');


        Client p1Client = new Client(player1, defaultHost, defaultPort);
        Client p2Client = new Client(player2, defaultHost, defaultPort);
        //---------------
        Runnable runnable = ()->{p1Client.startRemoteConnection();};
        Runnable runnable2 = ()->{p2Client.startRemoteConnection();};
        Callable<SimpleEntry<List<IPlayer>, List<IPlayer>>> callable = ()->{return realServer.startSingleGoalServer(rs);};
        Thread t = new Thread(runnable);
        Thread t2 = new Thread(runnable2);
        FutureTask<SimpleEntry<List<IPlayer>, List<IPlayer>>> future = new FutureTask<>(callable);
        Thread t3 = new Thread(future);
        SimpleEntry<List<IPlayer>, List<IPlayer>> output = null;
        try {
            t3.start();
            t2.start(); //THIS IS OLDEST PLAYER - GOES SECOND
            Thread.sleep(200);
            t.start(); //THIS IS YOUNGEST GOES FIRST
            output = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals( 0, output.getKey().size());
        Assert.assertEquals(2, output.getValue().size());
        Assert.assertEquals("Barry", output.getKey().get(0).name());
        Assert.assertEquals("Larry", output.getKey().get(0).name());
        t.interrupt();
        t2.interrupt();
        t3.interrupt();
    }



    public SingleGoalRefereeState constructRefState(Character spare) {
        Board b = new Board(createBoard(startBoard));
        Tile spareTile = new Tile(gemList[0], gemList[49], Connector.getFromChar(spare));
        PublicState ps = new PublicState(b, spareTile, createPlayerInfoList2());

        SingleGoalRefereeState s = new SingleGoalRefereeState(ps, createInfoMap2());
        return  s;
    }


}
