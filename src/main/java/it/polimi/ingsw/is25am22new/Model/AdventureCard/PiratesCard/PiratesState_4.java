package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the fourth state in the Pirates game state machine.
 * This state handles specific game logic such as processing incoming shots,
 * handling player decisions regarding battery usage, tile destruction,
 * and transitioning to subsequent game states.
 */
public class PiratesState_4 extends PiratesState implements Serializable {
    public PiratesState_4(PiratesCard piratesCard){
        super(piratesCard);
    }

    /**
     * Activates the effect of a specific game state during the "Pirates" phase in the game. The method
     * handles various game scenarios such as using batteries, processing an incoming shot, deactivating
     * components, and transitioning to the next game state.
     *
     * @param inputCommand an object containing user input data. It may include the player's choice
     *                      to continue using batteries, the coordinates of the selected grid position,
     *                      or any other input to control the effect's activation.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        Shot incomingShot = piratesCard.getNumberToShot().get(piratesCard.getIndexOfIncomingShot());
        Shipboard shipboard = game.getShipboards().get(piratesCard.getCurrDefeatedPlayer());
        int col = piratesCard.getDice1() + piratesCard.getDice2() - 4;
        int row = piratesCard.getDice1() + piratesCard.getDice2() - 5;

        if(inputCommand.getChoice()) { // continue using batteries?
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
                piratesCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
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
                    game.getShipboards().get(piratesCard.getCurrDefeatedPlayer()).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                }
            }
            piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());

            if(shipboard.highlightShipWrecks() > 1) {
                transition(new PiratesState_6(piratesCard));
            }
            else {
                if(piratesCard.getCurrDefeatedPlayer().equals(piratesCard.getLastDefeatedPlayer())) {
                    piratesCard.setNewDices();
                    piratesCard.setNextIndexOfShot();
                    piratesCard.setCurrDefeatedPlayerToFirst();
                    game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
                    piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    if(piratesCard.thereAreStillShots()) {
                        transition(new PiratesState_4(piratesCard));
                    }
                    else {
                        game.manageInvalidPlayers();
                        game.setCurrPlayerToLeader();
                        game.setCurrCard(null);
                        piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                        piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                        piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                    }
                }
                else {
                    piratesCard.setCurrDefeatedPlayerToNext();
                    game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
                    piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    transition(new PiratesState_4(piratesCard));
                }
            }
        }
    }

    @Override
    public String getStateName() {
        return "PiratesState_4";
    }
}
