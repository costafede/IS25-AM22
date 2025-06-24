package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatZoneState2_3 extends CombatZoneState2 implements Serializable {

    /**
     * Represents the state CombatZoneState2_3 in the CombatZoneState context.
     * @param combatZoneCard2
     */
    public CombatZoneState2_3(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
    }

    /**
     * Handles the activation of battery effects for components on the shipboard.
     * @param inputCommand
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
                combatZoneCard2.setBatteryUsed(true);
                combatZoneCard2.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
            }
            transition(new CombatZoneState2_4(combatZoneCard2));
        }
        else {
            if(game.getCurrPlayer().equals(game.getLastPlayer())) { // if last player
                combatZoneCard2.getPlayerToStrength().put(currentPlayer, (double)shipboard.getEngineStrength());
                String playerLowestEngine = game.getCurrPlayer();
                double lowestEngine = combatZoneCard2.getPlayerToStrength().get(playerLowestEngine);
                for(String player : combatZoneCard2.getPlayerToStrength().keySet()) {
                    if(combatZoneCard2.getPlayerToStrength().get(player) < lowestEngine) {
                        playerLowestEngine = player;
                        lowestEngine = combatZoneCard2.getPlayerToStrength().get(playerLowestEngine);
                    } else if (combatZoneCard2.getPlayerToStrength().get(player) == lowestEngine) {
                        playerLowestEngine =
                                // who is ahead receives penalty
                                game.getShipboards().get(player).getDaysOnFlight() >
                                        game.getShipboards().get(playerLowestEngine).getDaysOnFlight() ?
                                        player : playerLowestEngine;
                    }
                }

                // deactivates all components for all players
                for(String player : combatZoneCard2.getPlayerToStrength().keySet()) {
                    for(int i = 0; i < 5; i++){
                        for(int j = 0; j < 7; j++){
                            game.getShipboards().get(player).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                        }
                    }
                }
                combatZoneCard2.getObservableModel().updateAllShipboardList(game.getShipboards());

                game.getShipboards().get(playerLowestEngine).removeMostValuableGoodBlocks(2);
                combatZoneCard2.getObservableModel().updateAllBanks(game.getBank());
                combatZoneCard2.getObservableModel().updateAllShipboard(playerLowestEngine, game.getShipboards().get(playerLowestEngine));
                game.setCurrPlayerToLeader();
                combatZoneCard2.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new CombatZoneState2_6(combatZoneCard2));
            }
            else { // if not last player
                combatZoneCard2.getPlayerToStrength().put(currentPlayer, (double)shipboard.getEngineStrength());
                game.setCurrPlayerToNext();
                combatZoneCard2.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new CombatZoneState2_3(combatZoneCard2));
            }
        }
    }

    /**
     * Determines and transitions to the next game states based on game rules and conditions:
     * @return
     */
    @Override
    public String getStateName() {
        return "CombatZoneState2_3";
    }
}
