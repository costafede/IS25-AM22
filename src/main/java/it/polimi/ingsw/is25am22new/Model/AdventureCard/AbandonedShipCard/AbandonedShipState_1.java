package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * First state of the {@link AbandonedShipCard} effect logic.
 * Handles the initial player decision and manages transitions based on their choice.
 */
public class AbandonedShipState_1 extends AbandonedShipState implements Serializable {

    /**
     * Constructs the first state of the Abandoned Ship card.
     *
     * @param abandonedShipCard the card this state is associated with
     */
    public AbandonedShipState_1(AbandonedShipCard abandonedShipCard) {
        super(abandonedShipCard);
    }

    /**
     * Activates the effect for this state.
     * <p>
     * If the current player chooses to board the abandoned ship:
     * - The ship is delayed (flight days are added)
     * - Astronauts are lost if there are enough available
     * - The state transitions to {@link AbandonedShipState_2}
     * </p>
     * <p>
     * If the player declines and is not the last player:
     * - Turn passes to the next player
     * </p>
     * <p>
     * If the last player declines:
     * - The turn ends, game state is updated accordingly
     * </p>
     *
     * @param inputCommand the player's input choice (true = board, false = skip)
     * @throws IllegalStateException if the player lacks enough astronauts to board
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if (inputCommand.getChoice()) {
            if (game.getShipboards().get(game.getCurrPlayer()).getCrewNumber() < abandonedShipCard.getLostAstronauts()) {
                throw new IllegalStateException("Not Enough Astronauts");
            }

            game.getFlightboard().shiftRocket(game.getCurrPlayer(), abandonedShipCard.getFlightDaysLost());
            transition(new AbandonedShipState_2(abandonedShipCard));

            abandonedShipCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            abandonedShipCard.getObservableModel().updateAllFlightboard(game.getFlightboard());

        } else if (!game.getCurrPlayer().equals(game.getLastPlayer())) {
            game.setCurrPlayerToNext();
            abandonedShipCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());

        } else {
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);

            abandonedShipCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            abandonedShipCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedShipCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    /**
     * Returns the name of this state.
     *
     * @return the string "AbandonedShipState_1"
     */
    @Override
    public String getStateName() {
        return "AbandonedShipState_1";
    }
}

