package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatZoneState2_1 extends CombatZoneState2{
    public CombatZoneState2_1(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(game.getPlayerList().size() > 1) {
            String currentPlayer = game.getCurrPlayer();
            Shipboard shipboard = game.getShipboards().get(currentPlayer);

            if (inputCommand.getChoice()) {// are you sure you want to use the battery?
                int x = inputCommand.getRow();
                int y = inputCommand.getCol();
                AtomicInteger numOfBatteries = new AtomicInteger(0);
                Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
                // if player clicks on empty batteryComponent -> systems asks to confirm but cannot activate effect
                // because the flag setBatteryUsed is still false
                ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
                if (numOfBatteries.get() > 0) {
                    ctOptional.ifPresent(ComponentTile::removeBatteryToken);
                    combatZoneCard2.setBatteryUsed(true);
                }
                transition(new CombatZoneState2_2(combatZoneCard2));
            } else {
                if (game.getCurrPlayer().equals(game.getLastPlayer())) { // if last player
                    combatZoneCard2.getPlayerToStrength().put(currentPlayer, shipboard.getCannonStrength());
                    String playerLowestCannon = game.getCurrPlayer();
                    double lowestCannon = combatZoneCard2.getPlayerToStrength().get(playerLowestCannon);
                    for (String player : combatZoneCard2.getPlayerToStrength().keySet()) {
                        if (combatZoneCard2.getPlayerToStrength().get(player) < lowestCannon) {
                            playerLowestCannon = player;
                            lowestCannon = combatZoneCard2.getPlayerToStrength().get(playerLowestCannon);
                        } else if (combatZoneCard2.getPlayerToStrength().get(player) == lowestCannon) {
                            playerLowestCannon =
                                    // who is ahead receives penalty
                                    game.getShipboards().get(player).getDaysOnFlight() >
                                            game.getShipboards().get(playerLowestCannon).getDaysOnFlight() ?
                                            player : playerLowestCannon;
                        }
                    }

                    // deactivates all components for all players
                    for (String player : combatZoneCard2.getPlayerToStrength().keySet()) {
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 7; j++) {
                                game.getShipboards().get(player).getComponentTileFromGrid(i, j).ifPresent(ComponentTile::deactivateComponent);
                            }
                        }
                    }

                    game.getFlightboard().shiftRocket(playerLowestCannon, combatZoneCard2.getFlightDaysLost());
                    game.setCurrPlayerToLeader();

                    transition(new CombatZoneState2_3(combatZoneCard2));
                } else { // if not last player
                    combatZoneCard2.getPlayerToStrength().put(currentPlayer, shipboard.getCannonStrength());
                    game.setCurrPlayerToNext();
                    transition(new CombatZoneState2_1(combatZoneCard2));
                }
            }
        } else {
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }
    }

    @Override
    public String getStateName() {
        return "CombatZoneState2_1";
    }
}
