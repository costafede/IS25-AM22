package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

/**
 * Abstract class representing the state of an Abandoned Station card in the game.
 * Implements the State pattern to manage different possible states of the card.
 * Implements Serializable to enable object serialization.
 */
public abstract class AbandonedStationState implements Serializable {

    /** The Abandoned Station card associated with this state */
    protected AbandonedStationCard abandonedStationCard;

    /** The current game */
    protected Game game;

    /**
     * Constructs a new state for the Abandoned Station card.
     * 
     * @param abandonedStationCard the Abandoned Station card to associate with this state
     */
    public AbandonedStationState(AbandonedStationCard abandonedStationCard) {
        this.abandonedStationCard = abandonedStationCard;
        game = abandonedStationCard.getGame();
    }

    /**
     * Activates the specific effect of the current state.
     * 
     * @param inputCommand the input command specifying parameters for effect activation
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Performs the transition to a new state.
     * 
     * @param abandonedStationState the new state to transition to
     */
    public void transition(AbandonedStationState abandonedStationState) {
        abandonedStationCard.setAbandonedStationState(abandonedStationState);
    }

    /**
     * Returns the name of the current state.
     * 
     * @return a string representing the state name
     */
    public abstract String getStateName();
}