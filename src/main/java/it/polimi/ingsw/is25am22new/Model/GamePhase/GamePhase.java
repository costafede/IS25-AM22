package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

/**
 * Represents an abstract phase in the lifecycle of the game.
 * Each concrete implementation of this class corresponds to a specific phase,
 * with its own state and logic for transitioning to the next phase.
 *
 * This class acts as the base for all game phases, providing common functionality
 * such as transitioning between phases and retrieving the current phase type.
 * Phases are intended to encapsulate independent game logic for a particular
 * stage of the game flow.
 */
public abstract class GamePhase implements Serializable {
    protected Game game;
    protected PhaseType phaseType;

    public GamePhase(Game game) {
        this.game = game;
    }

    public abstract void trySwitchToNextPhase();

    public void transition(GamePhase nextPhase) {
        game.setGamePhase(nextPhase);
    }

    public PhaseType getPhaseType() {
        return phaseType;
    }
}
