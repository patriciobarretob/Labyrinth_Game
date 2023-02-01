import components.Direction;
import org.junit.Assert;
import org.junit.Test;

public class TestDirection {

  @Test
  public void getOpposite() {
    Assert.assertEquals(Direction.UP.getOpposite(), Direction.DOWN);
    Assert.assertEquals(Direction.DOWN.getOpposite(), Direction.UP);
    Assert.assertEquals(Direction.LEFT.getOpposite(), Direction.RIGHT);
    Assert.assertEquals(Direction.RIGHT.getOpposite(), Direction.LEFT);

  }
}