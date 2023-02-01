import components.Coordinate;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCoordinate {

  @Test
  public void testEquals() {
    Coordinate c1 = new Coordinate(1, 1);
    assertEquals(c1, c1);
    String s = "";
    assertNotEquals(c1, s);
  }
}