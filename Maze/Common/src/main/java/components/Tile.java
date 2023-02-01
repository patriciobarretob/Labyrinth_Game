package components;

// A tile has four possible open directions, and 2 unordered gems.
// TODO: The unordered nature of the gems is currently not enforced by the class.
public class Tile{
  public final Gem gem1;
  public final Gem gem2;
  private final Connector connector;

  public Tile(Gem gem1, Gem gem2, Connector connector) {
    this.gem1 = gem1;
    this.gem2 = gem2;
    this.connector = connector;
  }

  //Return the connector after rotating the given connector 90 degrees clockwise.
  public Tile rotate() {
    return new Tile(gem1, gem2, connector.getNext());
  }

  public Connector getConnector() {
    return this.connector;
  }
}
