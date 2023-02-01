package components;

import java.util.Random;

/**
 * A connector represents the directions a tile connects to. "U" represents Up, "D" represents Down,
 * "L" represents Left, and "R" represents Right.
 * The name of each connector is represented by those characters in the order "U" -> "D" -> "L" -> "R".
 */
public enum Connector {

  UD('│',true, true, false, false), // │
  LR('─', false, false, true, true), // ─
  DL('┐', false,true,true,false), // ┐
  UR('└',true,false,false,true), // └
  UL('┘', true,false,true,false), // ┘
  DR('┌',false,true,false,true), // ┌
  DLR('┬',false,true,true,true), // ┬
  UDR('├', true,true,false,true), // ├
  ULR('┴', true,false,true,true), // ┴
  UDL('┤', true,true,true,false), // ┤
  UDLR('┼',true,true,true,true);  // ┼

  public final char connectorChar;
  public final boolean connectsUp;
  public final boolean connectsDown;
  public final boolean connectsLeft;
  public final boolean connectsRight;

  Connector(char connectorChar, boolean connectsUp, boolean connectsDown,
      boolean connectsLeft, boolean connectsRight) {
    this.connectorChar = connectorChar;
    this.connectsUp = connectsUp;
    this.connectsDown = connectsDown;
    this.connectsLeft = connectsLeft;
    this.connectsRight = connectsRight;
  }

  public static Connector getFromChar(char c) {
    for(Connector conn : values()) {
      if(c == conn.connectorChar) {
        return conn;
      }
    }
    throw new IllegalArgumentException("Connector char not found");
  }

  //Return the connector after rotating the given connector 90 degrees clockwise.
  public Connector getNext() {
    switch(this) {
      case UD:
        return LR;
      case LR:
        return UD;
      case DL:
        return UL;
      case UR:
        return DR;
      case UL:
        return UR;
      case DR:
        return DL;
      case DLR:
        return UDL;
      case UDR:
        return DLR;
      case ULR:
        return UDR;
      case UDL:
        return ULR;
      case UDLR:
        return UDLR;
    }
    throw new IllegalStateException("Invalid Connector");
  }

  public static Connector getRandomConnector(Random rand) {
    Connector[] allConnectors = Connector.values();
    int randInt = rand.nextInt(allConnectors.length);
    return allConnectors[randInt];
  }


}
