package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * Represents the first state of an Abandoned Station card in the game.
 * This state handles the initial interaction with the Abandoned Station card,
 * including the validation of astronaut requirements and the management of player turns.
 * 
 * @see AbandonedStationState
 * @see AbandonedStationCard
 */
public class AbandonedStationState_1 extends AbandonedStationState implements Serializable {

    /**
     * Constructs a new AbandonedStationState_1 with the specified card.
     *
     * @param abandonedStationCard the Abandoned Station card associated with this state
     */
    public AbandonedStationState_1(AbandonedStationCard abandonedStationCard) {
        super(abandonedStationCard);
    }

    /**
     * Activates the effect of the Abandoned Station card based on the player's input.
     * If the player chooses to interact with the station:
     * - Validates if they have enough astronauts
     * - Applies the flight days penalty
     * - Updates the game state
     * - Transitions to the next state
     * If the player declines:
     * - Moves to the next player or ends the card interaction if all players have responded
     *
     * @param inputCommand the command containing the player's choice
     * @throws IllegalStateException if the player doesn't have enough astronauts when attempting to load the station
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){
            if(game.getShipboards().get(game.getCurrPlayer()).getCrewNumber() < abandonedStationCard.getAstronautsNumber())
                throw new IllegalStateException("Not Enough Astronauts");
            game.getFlightboard().shiftRocket(game.getCurrPlayer(), abandonedStationCard.getFlightDaysLost());
            abandonedStationCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            abandonedStationCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedStationCard.loadStation();
            abandonedStationCard.getObservableModel().updateAllBanks(game.getBank());
            transition(new AbandonedStationState_2(abandonedStationCard));
        }
        else if(!game.getCurrPlayer().equals(game.getLastPlayer())) {
            game.setCurrPlayerToNext();
            abandonedStationCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
        }
        else{
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
            abandonedStationCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            abandonedStationCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedStationCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    /**
     * Gets the name of the current state.
     *
     * @return the string "AbandonedStationState_1"
     */
    @Override
    public String getStateName() {
        return "AbandonedStationState_1";
    }
}