package players;

import components.Coordinate;
import components.Move;
import components.PublicState;

import java.util.Optional;

/**
 * Represents a player that has the ability to throw if set up to.
 */
public class BadPlayer extends PlayerImpl{

    private final boolean setupFail;
    private final boolean takeTurnFail;
    private final boolean winFail;

    /**
     * Sets up a bad player that may work or may throw based on boolean inputs.
     * @param setupFail - does this player fail when calls setup
     * @param takeTurnFail - does this player fail when calls takeTurn
     * @param winFail - does this player fail when calls won
     */
    public BadPlayer(IStrategy strategy, String name, boolean setupFail, boolean takeTurnFail, boolean winFail) {
        super(strategy, name);
        this.setupFail = setupFail;
        this.takeTurnFail = takeTurnFail;
        this.winFail = winFail;


    }



    @Override
    public void setup(Optional<PublicState> state0, Coordinate goal) {
        super.setup(state0, goal);
        if (setupFail) {
            int x = 1/0;
        }
    }

    @Override
    public void won(boolean w) {
        super.won(w);
        if (winFail) {
            int x = 1/0;
        }
    }

    @Override
    public Optional<Move> takeTurn(PublicState s) {
        if (takeTurnFail) {
            int x = 1 / 0;
        }
        return super.takeTurn(s);
    }
}
