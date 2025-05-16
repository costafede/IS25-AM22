package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the state CombatZoneState_6 in the CombatZoneState context.
 * This state is responsible for handling specific actions associated with the Combat Zone
 * during this phase of the game, including activating battery components and transitioning
 * to other states based on game conditions.
 *
 * Key responsibilities of this state:
 * - Handles the activation of battery effects for components on the shipboard.
 * - Manages player decisions related to battery usage.
 * - Updates the strength of players based on their cannon strength.
 * - Determines and transitions to the next game states based on game rules and conditions:
 *   - If the player's turn ends, and it is not the last player, transitions back to CombatZoneState_6 for the next player.
 *   - If the current player is the last in turn order, calculates the player with the lowest cannon strength and transitions to CombatZoneState_9.
 *
 * Deactivates all components for all players upon completion of the phase if applicable.
 */
public class CombatZoneState_6 extends CombatZoneState implements Serializable {
    public CombatZoneState_6(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

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
                combatZoneCard.setBatteryUsed(true);
                combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            }
            transition(new CombatZoneState_7(combatZoneCard));
        }
        else {
            if(game.getCurrPlayer().equals(game.getLastPlayer())) { // if last player
                combatZoneCard.getPlayerToStrength().put(currentPlayer, shipboard.getCannonStrength());
                String playerLowestCannon = game.getCurrPlayer();
                double lowestCannon = combatZoneCard.getPlayerToStrength().get(playerLowestCannon);
                for(String player : combatZoneCard.getPlayerToStrength().keySet()) {
                    if(combatZoneCard.getPlayerToStrength().get(player) < lowestCannon) {
                        playerLowestCannon = player;
                        lowestCannon = combatZoneCard.getPlayerToStrength().get(playerLowestCannon);
                    } else if (combatZoneCard.getPlayerToStrength().get(player) == lowestCannon) {
                        playerLowestCannon =
                                // who is ahead receives penalty
                                game.getShipboards().get(player).getDaysOnFlight() >
                                        game.getShipboards().get(playerLowestCannon).getDaysOnFlight() ?
                                        player : playerLowestCannon;
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

                game.setCurrPlayer(playerLowestCannon);
                combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new CombatZoneState_9(combatZoneCard));
            }
            else { // if not last player
                combatZoneCard.getPlayerToStrength().put(currentPlayer, shipboard.getCannonStrength());
                game.setCurrPlayerToNext();
                combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new CombatZoneState_6(combatZoneCard));
            }
        }
    }

    @Override
    public String getStateName() {
        return "CombatZoneState_6";
    }
}
