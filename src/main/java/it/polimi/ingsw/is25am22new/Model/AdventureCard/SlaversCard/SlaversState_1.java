package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SlaversState_1 represents the initial state of a SlaversCard interaction.
 * It handles the decision-making process for using battery tokens or taking other
 * actions to counter the effects of the SlaversCard. This state implements specific
 * game logic based on the player's choices and the current state of the game.
 *
 * Responsibilities:
 * - Determines if the player wishes to use a battery token to enhance their cannon strength.
 * - Transitions to appropriate states depending on whether the player wins, loses, or ties
 *   the cannon strength comparison against the slavers.
 * - Updates the game state, including shipboard components and player turns, as needed.
 *
 * State Transitions:
 * - Moves to SlaversState_2 if the player uses a battery token.
 * - Moves to SlaversState_3 if the player wins the cannon strength comparison.
 * - Moves to SlaversState_4 if the player loses the cannon strength comparison.
 * - Retains SlaversState_1 or performs a game reset if the current turn ties or additional
 *   game conditions are met.
 */
public class SlaversState_1 extends SlaversState implements Serializable {

    /**
     * Initializes a new SlaversState_1 object with the given SlaversCard.
     * @param slaversCard
     */
    public SlaversState_1(SlaversCard slaversCard) {
        super(slaversCard);
    }

    /**
     * Executes the effect of the SlaversState_1 card based on the player's decision
     * and the current game state. Manages the activation of batteries, evaluates
     * the outcome of the confrontation with the slavers, and transitions to the appropriate state.
     *
     * @param inputCommand the input command containing the player's choice and related parameters.
     *                     The command is used to determine whether the player chooses to use a battery
     *                     or stop using batteries, and provides grid coordinates for specific actions.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        if(inputCommand.getChoice()) { // want to use battery?
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            AtomicInteger numOfBatteries = new AtomicInteger(0);
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            // if player clicks on empty batteryComponent -> systems asks to confirm but cannot activate effect
            // because the flag setBatteryUsed is still false
            ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
            if(numOfBatteries.get() > 0) {
                ctOptional.ifPresent(ComponentTile::removeBatteryToken);
                slaversCard.setBatteryUsed(true);
                slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            }
            transition(new SlaversState_2(slaversCard));
        }
        else { // choose to stop using batteries
            if(shipboard.getCannonStrength() > slaversCard.getCannonStrength()) { // win case
                transition(new SlaversState_3(slaversCard)); // decide to lose daysOnFlight and take credits or not
            }
            else if (shipboard.getCannonStrength() < slaversCard.getCannonStrength()) { // lose case
                slaversCard.resetSelectedMembers();
                transition(new SlaversState_4(slaversCard));
            }
            else {// in case of tie nothing happens and the slavers attack next player
                // deactivates all components
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 7; j++){
                        game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                    }
                }
                slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    game.manageInvalidPlayers();
                    game.setCurrPlayerToLeader();
                    game.setCurrCard(null);
                    slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    slaversCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                    slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                }
                else {
                    game.setCurrPlayerToNext();
                    slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    transition(new SlaversState_1(slaversCard));
                }
            }
        }
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    @Override
    public String getStateName() {
        return "SlaversState_1";
    }
}
