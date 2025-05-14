package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
