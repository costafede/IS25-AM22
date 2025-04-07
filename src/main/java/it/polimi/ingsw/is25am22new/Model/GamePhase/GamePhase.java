package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public abstract class GamePhase {
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
