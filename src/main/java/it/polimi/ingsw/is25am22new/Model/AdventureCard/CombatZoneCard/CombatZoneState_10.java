package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesState_4;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;

public class CombatZoneState_10 extends CombatZoneState{
    public CombatZoneState_10(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        Shot incomingShot = combatZoneCard.getNumberToShot().get(combatZoneCard.getIndexOfIncomingShot());
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int col = game.getDices().getDice1() + game.getDices().getDice2();
        int row = game.getDices().getDice1() + game.getDices().getDice2();
        int x = 0;
        int y = 0;

        if(inputCommand.getChoice()) { // are you sure to activate the component?
            x = inputCommand.getRow();
            y = inputCommand.getCol();
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            if(combatZoneCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
                // activates the component
                ctOptional.ifPresent(ComponentTile::activateComponent);
            }
        }

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

        combatZoneCard.setBatteryUsed(false);

        combatZoneCard.setNextIndexOfShot();
        if(combatZoneCard.thereAreStillShots()) {
            transition(new CombatZoneState_9(combatZoneCard));
        }
        else {
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }
    }
}
