package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * Represents the "Card Phase" of the game, which is a specific type of {@link GamePhase}.
 * During this phase, the game evaluates whether it can proceed to the next phase based on
 * the availability of cards, the state of the game's deck, and the rockets on the flight board.
 */
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
