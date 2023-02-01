import components.Connector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestConnector {

  Connector c1;
  Connector c2;
  Connector c3;
  Connector c4;

  @Before
  public void setup() {
    c1 = Connector.DL;
    c2 = Connector.UD;
    c3 = Connector.UDL;
    c4 = Connector.UDLR;
  }

  @Test
  public void getFromChar() {
    Assert.assertEquals(c1, Connector.getFromChar('┐'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFromCharInvalid() {
    Assert.assertEquals(c1, Connector.getFromChar('h'));
  }

  @Test
  public void getNext() {
    Assert.assertEquals(c1.getNext(), Connector.UL);
    Assert.assertEquals(c2.getNext(), Connector.LR);
    Assert.assertEquals(c3.getNext(), Connector.ULR);
    Assert.assertEquals(c4.getNext(), Connector.UDLR);
  }

  @Test
  public void getNextRepeated() {
    Assert.assertEquals(c1.getNext().getNext().getNext().getNext(), Connector.DL);
    Assert.assertEquals(c2.getNext().getNext(), Connector.UD);
    Assert.assertEquals(c3.getNext().getNext().getNext().getNext(), Connector.UDL);
    Assert.assertEquals(c4.getNext(), Connector.UDLR);
  }
}