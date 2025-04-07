package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

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
