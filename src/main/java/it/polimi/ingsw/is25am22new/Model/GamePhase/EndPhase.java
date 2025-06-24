package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * Represents the final stage of a game in which the game logic concludes and prepares
 * for any cleanup or transition to a new game session.
 *
 * The EndPhase class is a concrete implementation of the abstract GamePhase
 * class. It defines the specific behavior for transitioning from the end phase
 * to the potential next phase in the game lifecycle.
 */
public class EndPhase extends GamePhase {
    /**
     * Constructs an EndPhase instance, setting the phase type to END
     * and initializing it with the specified game.
     *
     * @param game the Game instance associated with this phase
     */
    public EndPhase(Game game) {
        super(game);
        phaseType = PhaseType.END;
    }

    /**
     * Attempts to transition the current phase of the game to the next phase.
     *
     * This method delegates the phase transition logic to the {@code transition} method
     * of the {@link GamePhase} base class. The transition is initiated using the current phase
     * as the target for the transition. It is the responsibility of the concrete phase implementations
     * to determine if and when such a transition is appropriate.
     */
    public void trySwitchToNextPhase(){
        transition(this);
    }
}
