package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * Represents the "Card Phase" of the game, which is a specific type of {@link GamePhase}.
 * During this phase, the game evaluates whether it can proceed to the next phase based on
 * the availability of cards, the state of the game's deck, and the rockets on the flight board.
 */
public class CardPhase extends GamePhase {
    /**
     * Initializes a new instance of the CardPhase class, representing the card phase of the game.
     *
     * @param game the current game instance on which the card phase operates
     */
    public CardPhase(Game game) {
        super(game);
        phaseType = PhaseType.CARD;
    }

    /**
     * Attempts to transition the game from the current CardPhase to the next phase.
     *
     * This method checks specific conditions to determine if the game should transition
     * to the EndPhase. The transition occurs if:
     * - The current adventure card is null and the deck is empty, or
     * - The list of ordered rockets on the flight board is empty.
     *
     * If one of these conditions is met, the method transitions the game to the EndPhase.
     * Otherwise, no action is taken, and the game remains in the current phase.
     */
    public void trySwitchToNextPhase(){
        if(game.getCurrCard() == null && game.getDeck().isEmpty() || game.getFlightboard().getOrderedRockets().isEmpty())
            transition(new EndPhase(game));
    }
}
