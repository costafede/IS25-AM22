package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the first state in the combat zone sequence of the game.
 * This class is responsible for managing the logic and transitions associated with the first phase of combat zone mechanics.
 *
 * The state involves the following behaviors:
 * - Checks if the player wants to activate a battery component and deducts it if chosen and available.
 * - Updates the shipboard state for the current player and transitions to subsequent states based on game logic.
 * - Handles the logic for determining the player with the weakest engine strength when all players have taken their turn.
 * - Deactivates all components for all players once the weakest engine player is identified.
 *
 * Transitions to:
 * - `CombatZoneState_3` if the battery is used by the player.
 * - `CombatZoneState_5` if all players have finished their turns and the weakest engine player is identified.
 * - `CombatZoneState_1` for the next player's turn in the sequence.
 */
public class CombatZoneState_1 extends CombatZoneState implements Serializable {
    public CombatZoneState_1(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        if(inputCommand.getChoice()) {// do you want to use the battery?
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            AtomicInteger numOfBatteries = new AtomicInteger(0);
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            // if player clicks on empty batteryComponent -> systems asks to confirm but cannot activate effect
            // because the flag setBatteryUsed is still false
            ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
            if(numOfBatteries.get() > 0) {
                ctOptional.ifPresent(ComponentTile::removeBatteryToken);
                combatZoneCard.setBatteryUsed(true);
            }
            combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            transition(new CombatZoneState_3(combatZoneCard));
        }
        else{
            if(game.getCurrPlayer().equals(game.getLastPlayer())) { // if last player
                combatZoneCard.getPlayerToStrength().put(currentPlayer, (double)shipboard.getEngineStrength());
                String playerLowestEngine = game.getCurrPlayer();
                double lowestEngine = combatZoneCard.getPlayerToStrength().get(playerLowestEngine);
                for(String player : combatZoneCard.getPlayerToStrength().keySet()) {
                    if(combatZoneCard.getPlayerToStrength().get(player) < lowestEngine) {
                        playerLowestEngine = player;
                        lowestEngine = combatZoneCard.getPlayerToStrength().get(playerLowestEngine);
                    } else if (combatZoneCard.getPlayerToStrength().get(player) == lowestEngine) {
                        playerLowestEngine =
                                // who is ahead receives penalty
                                game.getShipboards().get(player).getDaysOnFlight() >
                                        game.getShipboards().get(playerLowestEngine).getDaysOnFlight() ?
                                        player : playerLowestEngine;
                    }
                }

                // deactivates all components for all players
                for(String player : combatZoneCard.getPlayerToStrength().keySet()) {
                    for(int i = 0; i < 5; i++){
                        for(int j = 0; j < 7; j++){
                            game.getShipboards().get(player).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                        }
                    }
                }
                combatZoneCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                game.setCurrPlayer(playerLowestEngine);
                combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new CombatZoneState_5(combatZoneCard));
            }
            else { // if not last player
                combatZoneCard.getPlayerToStrength().put(currentPlayer, (double)shipboard.getEngineStrength());
                game.setCurrPlayerToNext();
                combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new CombatZoneState_1(combatZoneCard));
            }
        }

    }

    @Override
    public String getStateName() {
        return "CombatZoneState_1";
    }
}
