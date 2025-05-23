package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Client.View.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * Represents an "Abandoned Ship" adventure card in the game.
 * This card can affect the number of flight days lost, credits gained, and astronauts lost.
 * Its behavior is delegated to an internal state {@link AbandonedShipState}.
 */
public class AbandonedShipCard extends AdventureCard implements Serializable, ViewableCard {

    /** Number of flight days lost when the card is activated. */
    private int flightdaysLost;

    /** Amount of credits gained. */
    private int credits;

    /** Number of astronauts lost. */
    private int lostAstronauts;

    /** Current state of the card, which controls its behavior. */
    private AbandonedShipState abandonedShipState;

    /**
     * Returns the number of flight days lost.
     *
     * @return the number of lost flight days
     */
    public int getFlightDaysLost() {
        return flightdaysLost;
    }

    /**
     * Returns the amount of credits.
     *
     * @return the number of credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the number of lost astronauts.
     *
     * @return the number of lost astronauts
     */
    public int getLostAstronauts() {
        return lostAstronauts;
    }

    /**
     * Constructs a new AbandonedShipCard.
     *
     * @param pngName the name of the associated PNG image
     * @param name the name of the card
     * @param game the current game instance
     * @param level the level of the card
     * @param tutorial true if the card is used in tutorial mode
     * @param flightdaysLost the number of flight days lost
     * @param credits the amount of credits gained
     * @param lostAstronauts the number of lost astronauts
     */
    public AbandonedShipCard(String pngName, String name, Game game, int level, boolean tutorial, int flightdaysLost, int credits, int lostAstronauts) {
        super(pngName, name, game, level, tutorial);
        this.credits = credits;
        this.flightdaysLost = flightdaysLost;
        this.lostAstronauts = lostAstronauts;
        this.abandonedShipState = new AbandonedShipState_1(this);
    }

    /**
     * Activates the effect of the card by delegating to the current card state.
     *
     * @param inputCommand the player's input command
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        abandonedShipState.activateEffect(inputCommand);
    }

    /**
     * Returns the name of the current state of the card.
     *
     * @return the name of the current card state
     */
    @Override
    public String getStateName() {
        return abandonedShipState.getStateName();
    }

    /**
     * Sets the current state of the card.
     *
     * @param abandonedShipState the new state to be assigned
     */
    public void setAbandonedShipState(AbandonedShipState abandonedShipState) {
        this.abandonedShipState = abandonedShipState;
    }

    /**
     * Returns the current state of the card.
     *
     * @return the current card state
     */
    public AbandonedShipState getAbandonedShipState() {
        return abandonedShipState;
    }

    /**
     * Displays the card in the user interface depending on the current game phase.
     *
     * @param view the view component responsible for displaying the card
     * @param model the client-side model containing the current game state
     */
    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showAbandonedShipCardInGame(this);
        }
        else{
            view.showAbandonedShipCard(this);
        }
    }

}
