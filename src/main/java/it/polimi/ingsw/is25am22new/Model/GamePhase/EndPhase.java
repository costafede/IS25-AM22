package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public class EndPhase extends GamePhase {
    public EndPhase(Game game) {
        super(game);
        phaseType = PhaseType.END;
    }

    public void trySwitchToNextPhase(){
        transition(this);
    }
}
