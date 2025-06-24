package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * Represents the setup phase of the game where initial configurations and
 * preparations are performed before gameplay begins. This phase is responsible
 * for setting the game state to the setup phase and transitioning to the
 * building phase once the setup is complete.
 */
public class SetUpPhase extends GamePhase {

    /**
     * Initializes the setup phase of the game. This phase includes initial
     * configurations and prepares the game for subsequent phases by setting
     * the game state to the setup phase.
     *
     * @param game the current game instance to associate with this setup phase
     */
    public SetUpPhase(Game game) {
        super(game);
        phaseType = PhaseType.SETUP;
    }

    /**
     * Attempts to progress the game from the setup phase to the next phase.
     *
     * This method initiates a transition from the SetupPhase to the BuildingPhase,
     * where players begin constructing their shipboards. The transition logic is
     * executed based on the current state of the game.
     */
    @Override
    public void trySwitchToNextPhase() {
        transition(new BuildingPhase(game));
    }
}
