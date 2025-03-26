package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatZoneState_9 extends CombatZoneState {
    public CombatZoneState_9(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        game.getDices().rollDices();

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
            transition(new CombatZoneState_10(combatZoneCard));
        }
        else {
            Shot incomingShot = combatZoneCard.getNumberToShot().get(combatZoneCard.getIndexOfIncomingShot());
            int col = combatZoneCard.getDice1() + combatZoneCard.getDice2();
            int row = combatZoneCard.getDice1() + combatZoneCard.getDice2();
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

            combatZoneCard.setNewDices();

            combatZoneCard.setNextIndexOfShot();
            if(combatZoneCard.thereAreStillShots()) {
                transition(new CombatZoneState_9(combatZoneCard));
            }
            else {
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
            }
        }

    }
}
