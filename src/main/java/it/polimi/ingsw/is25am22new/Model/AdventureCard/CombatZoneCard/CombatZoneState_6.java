package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatZoneState_6 extends CombatZoneState {
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

                game.setCurrPlayer(playerLowestCannon);
                transition(new CombatZoneState_9(combatZoneCard));
            }
            else { // if not last player
                combatZoneCard.getPlayerToStrength().put(currentPlayer, shipboard.getCannonStrength());
                game.setCurrPlayerToNext();
                transition(new CombatZoneState_6(combatZoneCard));
            }
        }
    }
}
