package json;

import com.google.gson.annotations.SerializedName;
import components.Connector;
import components.Gem;
import components.Tile;

public class JsonTile implements JsonRepresentation<Tile>{
  String tilekey;
  @SerializedName("1-image") String gem1;
  @SerializedName("2-image") String gem2;
  @Override

  public Tile getActual() {
    Gem g1 = new Gem(gem1);
    Gem g2 = new Gem(gem2);
    Connector c = Connector.getFromChar(tilekey.charAt(0));
    return new Tile(g1, g2, c);
  }
}
