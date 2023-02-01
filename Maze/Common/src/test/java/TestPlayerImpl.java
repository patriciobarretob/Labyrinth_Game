import components.Board;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import players.EuclidStrategy;
import players.PlayerImpl;
import players.RiemannStrategy;

public class TestPlayerImpl extends ATestLabyrinth {
  PlayerImpl playerRiemann;
  PlayerImpl playerEuclid;
  @Before
  public void setup() {
    playerRiemann = new PlayerImpl(new RiemannStrategy(), "p1");
    playerEuclid = new PlayerImpl(new EuclidStrategy(), "p2");
  }

  @Test
  public void testName() {
    Assert.assertEquals(playerRiemann.name(), "p1");
    Assert.assertEquals(playerEuclid.name(), "p2");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testProposeBoard1x1() {
    Board board = playerRiemann.proposeBoard0(1,1);
    Assert.assertEquals(board.width, 1);
    Assert.assertEquals(board.height, 1);
  }

  @Test
  public void testProposeBoard3x4() {
    Board board = playerRiemann.proposeBoard0(3,4);
    Assert.assertEquals(board.width, 4);
    Assert.assertEquals(board.height, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testProposeBoard100x2() {
    Board board = playerRiemann.proposeBoard0(100,2);
    Assert.assertEquals(board.width, 2);
    Assert.assertEquals(board.height, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProposeBoardZeroRows() {
    playerRiemann.proposeBoard0(0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProposeBoardZeroCols() {
    playerRiemann.proposeBoard0(2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProposeBoardTooLargeForUnique() {
    playerRiemann.proposeBoard0(200, 200);
  }

}
