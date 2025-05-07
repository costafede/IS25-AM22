package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public class CardPhase extends GamePhase {
    public CardPhase(Game game) {
        super(game);
        phaseType = PhaseType.CARD;
    }

    public void trySwitchToNextPhase(){
        if(game.getCurrCard() == null && game.getDeck().isEmpty() || game.getFlightboard().getOrderedRockets().isEmpty())
            transition(new EndPhase(game));
    }
}
