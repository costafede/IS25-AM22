package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * Represents the setup phase of the game where initial configurations and
 * preparations are performed before gameplay begins. This phase is responsible
 * for setting the game state to the setup phase and transitioning to the
 * building phase once the setup is complete.
 */
public class SetUpPhase extends GamePhase {

    public SetUpPhase(Game game) {
        super(game);
        phaseType = PhaseType.SETUP;
    }

    @Override
    public void trySwitchToNextPhase() {
        transition(new BuildingPhase(game));
    }
}
