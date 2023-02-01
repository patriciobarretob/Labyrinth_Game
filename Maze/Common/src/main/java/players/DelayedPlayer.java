package players;

import components.Coordinate;
import components.Move;
import components.PublicState;

import java.util.Optional;

/**
 * This player returns a method after a certain delay.
 */
public class DelayedPlayer extends PlayerImpl{

    private final int setupDelay;
    private final int turnDelay;
    private final int wonDelay;

    //Delays in milliseconds
    public DelayedPlayer(IStrategy strategy, String name, int setupDelay,  int turnDelay, int wonDelay) {
        super(strategy, name);
        this.setupDelay = setupDelay;
        this.turnDelay = turnDelay;
        this.wonDelay = wonDelay;

    }

    @Override
    public void setup(Optional<PublicState> state0, Coordinate goal) {
        try {
            Thread.sleep(setupDelay);
        } catch (Exception e) {
           throw new IllegalStateException("Interrupted during setup");
        }
        super.setup(state0, goal);
    }

    @Override
    public Optional<Move> takeTurn(PublicState s) {
        try {
            Thread.sleep(turnDelay);
        } catch (Exception e) {
            throw new IllegalStateException("Interrupted during taketurn");
        }
        return super.takeTurn(s);
    }

    @Override
    public void won(boolean w) {
        try {
            Thread.sleep(wonDelay);
        } catch (Exception e) {
            throw new IllegalStateException("Interrupted during won");
        }
        super.won(w);
    }
}
