package json;

import com.google.gson.JsonElement;

public interface JsonRepresentation<T> {
  T getActual();
}
