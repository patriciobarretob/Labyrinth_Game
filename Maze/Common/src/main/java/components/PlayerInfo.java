package components;

import java.awt.Color;
import java.util.Objects;

// Represents the information about a player available to everyone in the game.
public class PlayerInfo {

  public final Color avatar;
  public final Coordinate home;
  public Coordinate currentPos;
  public boolean skippedThisRound;

  public PlayerInfo(Color avatar, Coordinate home, Coordinate currentPos) {
    this.avatar = avatar;
    this.home = home;
    this.currentPos = currentPos;
    this.skippedThisRound = false;
  }

  public PlayerInfo(Color avatar, Coordinate home) {
    this(avatar, home, home);
  }

  public PlayerInfo(PlayerInfo oldPlayerInfo) {
    this.avatar = oldPlayerInfo.avatar;
    this.home = oldPlayerInfo.home;
    this.currentPos = oldPlayerInfo.currentPos;
    this.skippedThisRound = oldPlayerInfo.skippedThisRound;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerInfo that = (PlayerInfo) o;
    return Objects.equals(avatar, that.avatar) && Objects.equals(home, that.home)
        && Objects.equals(currentPos, that.currentPos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(avatar, home, currentPos);
  }
}
