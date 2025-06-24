package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneState_8;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneState_9;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatZoneState2_7 extends CombatZoneState2 implements Serializable {

    /**
     * Constructor for the CombatZoneState2_7 class.
     * @param combatZoneCard2
     */
    public CombatZoneState2_7(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
    }

    /**
     * Activates the effect of a specific game state during the "CombatZone2" phase in the game. The method
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
            transition(new CombatZoneState2_8(combatZoneCard2));

        } else {
            Shot incomingShot = combatZoneCard2.getNumberToShot().get(combatZoneCard2.getIndexOfIncomingShot());
            int col = combatZoneCard2.getDice1() + combatZoneCard2.getDice2() - 4;
            int row = combatZoneCard2.getDice1() + combatZoneCard2.getDice2() - 5;
            // shot hitting logic
            if(incomingShot.getOrientation() == Orientation.TOP) {
                if(incomingShot.isBig() || !shipboard.isBottomSideShielded()) {
                    boolean destructionComplete = false;
                    for(int i = 4; i >= 0 && !destructionComplete; i--) {
                        if(shipboard.getComponentTileFromGrid(i, col).isPresent()) {
                            shipboard.destroyTile(i, col);
                            destructionComplete = true;
                        }
                    }
                }
            }
            else if(incomingShot.getOrientation() == Orientation.BOTTOM) {
                if(incomingShot.isBig() || !shipboard.isTopSideShielded()) {
                    boolean destructionComplete = false;
                    for(int i = 0; i < 5 && !destructionComplete; i++) {
                        if(shipboard.getComponentTileFromGrid(i, col).isPresent()) {
                            shipboard.destroyTile(i, col);
                            destructionComplete = true;
                        }
                    }
                }
            }
            else if(incomingShot.getOrientation() == Orientation.LEFT) {
                if(incomingShot.isBig() || !shipboard.isRightSideShielded()) {
                    boolean destructionComplete = false;
                    for(int j = 6; j >= 0 && !destructionComplete; j--) {
                        if(shipboard.getComponentTileFromGrid(row, j).isPresent()) {
                            shipboard.destroyTile(row, j);
                            destructionComplete = true;
                        }
                    }
                }
            }
            else if(incomingShot.getOrientation() == Orientation.RIGHT) {
                if(incomingShot.isBig() || !shipboard.isLeftSideShielded()) {
                    boolean destructionComplete = false;
                    for(int j = 0; j < 7 && !destructionComplete; j++) {
                        if(shipboard.getComponentTileFromGrid(row, j).isPresent()) {
                            shipboard.destroyTile(row, j);
                            destructionComplete = true;
                        }
                    }
                }
            }

            // reset batteryUsed, deactivate components, next player

            // deactivates all components
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                }
            }
            combatZoneCard2.getObservableModel().updateAllShipboardList(game.getShipboards());

            combatZoneCard2.setNewDices();

            if(shipboard.highlightShipWrecks() > 1){
                transition(new CombatZoneState2_9(combatZoneCard2));
            }
            else {
                combatZoneCard2.setNextIndexOfShot();
                if(combatZoneCard2.thereAreStillShots()) {
                    transition(new CombatZoneState2_7(combatZoneCard2));

                }
                else {
                    game.manageInvalidPlayers();
                    game.setCurrPlayerToLeader();
                    game.setCurrCard(null);
                    combatZoneCard2.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    combatZoneCard2.getObservableModel().updateAllFlightboard(game.getFlightboard());
                    combatZoneCard2.getObservableModel().updateAllShipboardList(game.getShipboards());
                }
            }
        }
    }

    /**
     * Returns the name of the state.
     * @return
     */
    @Override
    public String getStateName() {
        return "CombatZoneState2_7";
    }
}
