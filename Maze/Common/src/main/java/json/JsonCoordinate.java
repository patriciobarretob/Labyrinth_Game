package json;

import com.google.gson.annotations.SerializedName;
import components.Coordinate;

public class JsonCoordinate implements JsonRepresentation<Coordinate> {
  @SerializedName("row#") int row;
  @SerializedName("column#") int col;

  public JsonCoordinate(int row, int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public Coordinate getActual() {
    return new Coordinate(col, row);
  }
}
