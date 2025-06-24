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

    /**
     * Constructs a new GamePhase object with the specified game instance.
     *
     * @param game the game instance associated with this phase
     */
    public GamePhase(Game game) {
        this.game = game;
    }

    /**
     * Attempts to transition the game to the next phase in its lifecycle.
     *
     * This method defines the logic for progressing the game from its current phase
     * to the next phase based on the specific rules of the implementing subclass.
     * The exact transition behavior and conditions depend on the concrete subclass
     * implementation. If transitioning to the next phase is not possible, the method
     * will retain the current phase.
     *
     * Concrete implementations may determine the next phase by evaluating the state
     * of the game, such as the status of its components, properties, or conditions
     * relevant to the phase progression.
     *
     * It is the responsibility of the implementing phase to invoke the {@code transition}
     * method from the {@link GamePhase} base class when a transition to the next phase
     * should occur.
     */
    public abstract void trySwitchToNextPhase();

    /**
     * Handles the transition from the current phase to the specified next phase in the game's lifecycle.
     * This method updates the game's state to reflect the new phase.
     *
     * @param nextPhase the target phase to transition to; must be an instance of a {@link GamePhase}
     */
    public void transition(GamePhase nextPhase) {
        game.setGamePhase(nextPhase);
    }

    /**
     * Retrieves the phase type associated with the current game phase.
     *
     * @return the {@link PhaseType} representing the current phase of the game
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }
}
