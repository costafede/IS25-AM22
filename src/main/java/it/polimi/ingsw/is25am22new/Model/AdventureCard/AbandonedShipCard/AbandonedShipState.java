package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

/**
 * Abstract class representing the state of an {@link AbandonedShipCard}.
 * Each subclass defines specific behavior for the abandoned ship card when its effect is activated.
 * Implements the State design pattern to manage transitions between different states.
 */
public abstract class AbandonedShipState implements Serializable {

    /** The abandoned ship card associated with this state. */
    protected AbandonedShipCard abandonedShipCard;

    /** Reference to the current game instance. */
    protected Game game;

    /**
     * Constructs an AbandonedShipState with the associated card.
     *
     * @param abandonedShipCard the AbandonedShipCard this state is associated with
     */
    public AbandonedShipState(AbandonedShipCard abandonedShipCard) {
        this.abandonedShipCard = abandonedShipCard;
        this.game = abandonedShipCard.getGame();
    }

    /**
     * Activates the effect associated with this state.
     * This method must be implemented by concrete subclasses.
     *
     * @param inputCommand the input command from the player
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Transitions the card to a new state.
     *
     * @param abandonedShipState the new state to transition to
     */
    public void transition(AbandonedShipState abandonedShipState) {
        abandonedShipCard.setAbandonedShipState(abandonedShipState);
    }

    /**
     * Returns the name of the current state.
     * This method must be implemented by concrete subclasses.
     *
     * @return the name of the state
     */
    public abstract String getStateName();
}
