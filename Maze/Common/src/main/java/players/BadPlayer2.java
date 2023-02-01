package players;

import java.util.Optional;

import components.Coordinate;
import components.Move;
import components.PublicState;

/**
 * Represents a player that can cause an infinite loop when told to after a specific number of
 * calls.
 */
public class BadPlayer2 extends PlayerImpl {
  private int callsBeforeBreakingSetup;
  private int callsBeforeBreakingTakeTurn;
  private int callsBeforeBreakingWin;

  /**
   * Constructor for BadPlayer2
   * If the number of calls < 1, the method will not break no matter how many times it's called.
   */
  public BadPlayer2(IStrategy strategy, String name, int setupCalls, int takeTurnCalls,
                    int winCalls) {
    super(strategy, name);
    this.callsBeforeBreakingSetup = setupCalls;
    this.callsBeforeBreakingTakeTurn = takeTurnCalls;
    this.callsBeforeBreakingWin = winCalls;
  }

  @Override
  public void setup(Optional<PublicState> state0, Coordinate goal) {
    super.setup(state0, goal);
    callsBeforeBreakingSetup -= 1;
    if (callsBeforeBreakingSetup == 0) {
      runInfiniteLoop();
    }
  }

  private void runInfiniteLoop() {
    while(true){
      //:D
    }
  }

  @Override
  public void won(boolean w) {
    super.won(w);
    callsBeforeBreakingWin -= 1;
    if (callsBeforeBreakingWin == 0) {
      runInfiniteLoop();
    }
  }

  @Override
  public Optional<Move> takeTurn(PublicState s) {
    callsBeforeBreakingTakeTurn -= 1;
    if (callsBeforeBreakingTakeTurn == 0) {
      runInfiniteLoop();
    }
    return super.takeTurn(s);
  }
}
