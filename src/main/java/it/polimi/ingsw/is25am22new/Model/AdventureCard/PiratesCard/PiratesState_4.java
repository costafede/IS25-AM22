package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PiratesState_4 extends PiratesState{
    public PiratesState_4(PiratesCard piratesCard){
        super(piratesCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        Shot incomingShot = piratesCard.getNumberToShot().get(piratesCard.getIndexOfIncomingShot());
        String defeatedPlayer = piratesCard.getDefeatedPlayers().getFirst();
        Shipboard shipboard = game.getShipboards().get(defeatedPlayer);
        int col = piratesCard.getDice1() + piratesCard.getDice2();
        int row = piratesCard.getDice1() + piratesCard.getDice2();

        if(inputCommand.getChoice()) { // are you sure you want to use the battery?
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            AtomicInteger numOfBatteries = new AtomicInteger(0);
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            // if player clicks on empty batteryComponent -> systems asks to confirm but cannot activate effect
            // because the flag setBatteryUsed is still false
            ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
            if (numOfBatteries.get() > 0) {
                ctOptional.ifPresent(ComponentTile::removeBatteryToken);
                piratesCard.setBatteryUsed(true);
            }
            transition(new PiratesState_5(piratesCard));
        }
        else {
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
                    game.getShipboards().get(defeatedPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                }
            }

            piratesCard.setNewDices();

            if(piratesCard.getCurrDefeatedPlayer().equals(piratesCard.getLastDefeatedPlayer())) {
                piratesCard.setNextIndexOfShot();
                piratesCard.setCurrDefeatedPlayerToFirst();
                if(piratesCard.thereAreStillShots()) {
                    transition(new PiratesState_4(piratesCard));
                }
                else {
                    game.manageInvalidPlayers();
                    game.setCurrPlayerToLeader();
                    game.setCurrCard(null);
                }
            }
            else {
                piratesCard.setCurrDefeatedPlayerToNext();
                transition(new PiratesState_4(piratesCard));
            }
        }
    }
}
