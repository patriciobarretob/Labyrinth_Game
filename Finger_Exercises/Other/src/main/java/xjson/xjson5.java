package xjson;



import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class xjson5 {
  private static JsonReader reader;

  public static void main(String[] args) {
    JsonElement firstElem = readInput();
    int fives = countFives(firstElem);
    System.out.println(fives);
    readInputUntilZero(fives);
  }

  private static void readInputUntilZero(int fives) {

    if(fives == 0) { return; }
    int count = 0;
    for(int i = 0; i < fives; i++) {
      JsonElement firstElem = JsonParser.parseReader(reader);
      count += countFives(firstElem);
    }
    System.out.println(count);
    readInputUntilZero(count);
  }

  private static JsonElement readInput() {
    reader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    return JsonParser.parseReader(reader);
  }

  private static int countFives(JsonElement element) {
    int fives = 0;
    if (element.isJsonArray()) {
      fives += countFivesInJsonArray(element);
    } else if (element.isJsonObject()) {
      fives += countFivesInJsonObject(element);
    } else if (element.isJsonNull()) {
      fives += countFivesInJsonNull(element);
    } else if (element.isJsonPrimitive()) {
      fives += countFivesInJsonPrimitive(element);
    } else {
      throw new IllegalArgumentException("Element is not a json array, object, null, or " +
              "primitive!");
    }
    return fives;
  }

  private static int countFivesInJsonArray(JsonElement element) {
    JsonArray jarr = element.getAsJsonArray();
    int fives = 0;
    for (JsonElement arrElement: jarr) {
      fives += countFives(arrElement);
    }
    return fives;
  }

  private static int countFivesInJsonObject(JsonElement element) {
    JsonObject jobj = element.getAsJsonObject();
    int fives = 0;
    for (Map.Entry<String, JsonElement> entry: jobj.entrySet()) {
      fives += countFives(entry.getValue());
    }
    return fives;
  }

  private static int countFivesInJsonNull(JsonElement element) {
    return 0;
  }

  private static int countFivesInJsonPrimitive(JsonElement element) {
    JsonPrimitive jprim = element.getAsJsonPrimitive();
    if (!jprim.isNumber()) {
      return 0;
    } else {
      int num = jprim.getAsInt();
      return num == 5 ? 1 : 0;
    }
  }

}
