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
    public EndPhase(Game game) {
        super(game);
        phaseType = PhaseType.END;
    }

    public void trySwitchToNextPhase(){
        transition(this);
    }
}
