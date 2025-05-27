package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the ninth state in the combat zone sequence during gameplay.
 * This state handles the activation of battery usage and incoming shot logic,
 * updating the state of the game based on the player's choices and the results of the incoming attack.
 */
public class CombatZoneState_9 extends CombatZoneState implements Serializable {
    public CombatZoneState_9(CombatZoneCard combatZoneCard) {
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
            transition(new CombatZoneState_10(combatZoneCard));
        }
        else {
            Shot incomingShot = combatZoneCard.getNumberToShot().get(combatZoneCard.getIndexOfIncomingShot());
            int col = combatZoneCard.getDice1() + combatZoneCard.getDice2() - 4;
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

            // reset batteryUsed, deactivate components, next player

            // deactivates all components
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                }
            }
            combatZoneCard.getObservableModel().updateAllShipboardList(game.getShipboards());

            combatZoneCard.setNewDices();

            if(shipboard.highlightShipWrecks() > 1){
                transition(new CombatZoneState_8(combatZoneCard));
            }
            else {
                combatZoneCard.setNextIndexOfShot();
                if(combatZoneCard.thereAreStillShots()) {
                    transition(new CombatZoneState_9(combatZoneCard));
                }
                else {
                    game.manageInvalidPlayers();
                    game.setCurrPlayerToLeader();
                    game.setCurrCard(null);
                    combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    combatZoneCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                    combatZoneCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                }
            }
        }

    }

    @Override
    public String getStateName() {
        return "CombatZoneState_9";
    }
}
