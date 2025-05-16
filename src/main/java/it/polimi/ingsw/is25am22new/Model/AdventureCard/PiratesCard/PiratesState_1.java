package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversState_2;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger ;

/**
 * Represents the first state of the {@link PiratesCard} during its activation in the game.
 *
 * This state, part of the state pattern implementation for the PiratesCard, handles effects
 * and transitions based on the player's choices and the state of their shipboard. Upon
 * activation, the player is prompted to use their ship's battery or opt-out of using it.
 * The outcome depends on the strength of the ship's cannon relative to the PiratesCard.
 *
 * Key functionalities include:
 * - Evaluating player decisions regarding the battery's usage.
 * - Updating the shipboard and game state based on interactions.
 * - Transitioning to the appropriate next state depending on the outcome (e.g., PiratesState_2,
 * PiratesState_3, or PiratesState_4).
 * - Managing the activation and deactivation of shipboard components.
 *
 * This state ensures gameplay progression by coordinating player inputs, shipboard updates,
 * and handling victory, defeat, or ties between the player and the PiratesCard.
 */
public class PiratesState_1 extends PiratesState implements Serializable {
    public PiratesState_1(PiratesCard piratesCard) {
        super(piratesCard);
    }

    /**
     * Activates the effect of the PiratesCard, based on the input command and the current game state.
     * This method processes player decisions, updates the shipboard and game state, and transitions to the next game state accordingly.
     *
     * @param inputCommand the InputCommand object containing the player's choices and coordinates related to the activation effect.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        if(inputCommand.getChoice()) {// are you sure you want to use the battery?
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            AtomicInteger numOfBatteries = new AtomicInteger(0);
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            // if player clicks on empty batteryComponent -> systems asks to confirm but cannot activate effect
            // because the flag setBatteryUsed is still false
            ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
            if(numOfBatteries.get() > 0) {
                ctOptional.ifPresent(ComponentTile::removeBatteryToken);
                piratesCard.setBatteryUsed(true);
                piratesCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            }
            transition(new PiratesState_2(piratesCard));
        }
        else { // choose to stop using the batteries
            if(shipboard.getCannonStrength() > piratesCard.getCannonStrength()) { // win case
                transition(new PiratesState_3(piratesCard)); // decide to lose daysOnFlight and take credits or not
            }
            else if (shipboard.getCannonStrength() <= piratesCard.getCannonStrength()) { // lose or tie case
                if(shipboard.getCannonStrength() < piratesCard.getCannonStrength()) {
                    piratesCard.addDefeatedPlayer(currentPlayer);
                }
                // deactivates all components
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 7; j++){
                        game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                    }
                }
                piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    if(!piratesCard.getDefeatedPlayers().isEmpty()){
                        piratesCard.setCurrDefeatedPlayerToFirst();
                        transition(new PiratesState_4(piratesCard)); // all defeated players get shot
                    }
                    else {
                        game.manageInvalidPlayers();
                        game.setCurrPlayerToLeader();
                        game.setCurrCard(null);
                        piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                        piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                        piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                    }
                }
                else {
                    game.setCurrPlayerToNext();
                    piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    transition(new PiratesState_1(piratesCard));
                }
            }
        }
    }

    @Override
    public String getStateName() {
        return "PiratesState_1";
    }
}
