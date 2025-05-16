package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the first state in the Meteor Swarm sequence. This state handles the interaction
 * where the current player decides how to respond to the meteor swarm event using battery
 * components or letting meteors impact their shipboard. This state transitions to the next
 * appropriate state based on the player's actions and game conditions.
 *
 * This class extends the abstract {@link MeteorSwarmState} to implement specific behaviors
 * for MeteorSwarmState_1. It manages the logic for meteor impact resolution and transitions
 * to subsequent states (e.g., MeteorSwarmState_2 or MeteorSwarmState_3) depending on game outcomes.
 *
 * Key responsibilities include:
 * - Processing player input and deciding whether to use battery components.
 * - Resolving meteor impact effects based on meteor orientation and shipboard state.
 * - Transitioning to the appropriate next state in the meteor swarm sequence.
 */
public class MeteorSwarmState_1 extends MeteorSwarmState implements Serializable {
    public MeteorSwarmState_1(MeteorSwarmCard meteorSwarmCard) { super(meteorSwarmCard); }

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
                meteorSwarmCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
                meteorSwarmCard.setBatteryUsed(true);
            }
            transition(new MeteorSwarmState_2(meteorSwarmCard));
            meteorSwarmCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
        }
        else {
            Meteor incomingMeteor = meteorSwarmCard.getNumberToMeteor().get(meteorSwarmCard.getIndexOfIncomingMeteor());
            int col = meteorSwarmCard.getDice1() + meteorSwarmCard.getDice2() - 4; // offset
            int row = meteorSwarmCard.getDice1() + meteorSwarmCard.getDice2() - 5; // offset
            // meteor hitting logic
            if(incomingMeteor.getOrientation() == Orientation.TOP) {
                if( !((incomingMeteor.isBig() && shipboard.isBottomSideCannon(col)) ||
                        (!incomingMeteor.isBig() && shipboard.isBottomSideShielded()) ||
                        (!incomingMeteor.isBig() && !shipboard.isExposedConnectorOnBottom(col)))) {
                    boolean destructionComplete = false;
                    for(int i = 4; i >= 0 && !destructionComplete; i--) {
                        if(shipboard.getComponentTileFromGrid(i, col).isPresent()) {
                            shipboard.destroyTile(i, col);
                            destructionComplete = true;
                        }
                    }
                }
            }
            else if(incomingMeteor.getOrientation() == Orientation.BOTTOM) {
                if( !((incomingMeteor.isBig() && shipboard.isTopSideCannon(col)) ||
                        (!incomingMeteor.isBig() && shipboard.isTopSideShielded()) ||
                        (!incomingMeteor.isBig() && !shipboard.isExposedConnectorOnTop(col)))) {
                    boolean destructionComplete = false;
                    for(int i = 0; i < 5 && !destructionComplete; i++) {
                        if(shipboard.getComponentTileFromGrid(i, col).isPresent()) {
                            shipboard.destroyTile(i, col);
                            destructionComplete = true;
                        }
                    }
                }
            }
            else if(incomingMeteor.getOrientation() == Orientation.LEFT) {
                if( !((incomingMeteor.isBig() && shipboard.isRightSideCannon(row)) ||
                        (!incomingMeteor.isBig() && shipboard.isRightSideShielded()) ||
                        (!incomingMeteor.isBig() && !shipboard.isExposedConnectorOnRight(row)))) {
                    boolean destructionComplete = false;
                    for(int j = 6; j >= 0 && !destructionComplete; j--) {
                        if(shipboard.getComponentTileFromGrid(row, j).isPresent()) {
                            shipboard.destroyTile(row, j);
                            destructionComplete = true;
                        }
                    }
                }
            }
            else if(incomingMeteor.getOrientation() == Orientation.RIGHT) {
                if( !((incomingMeteor.isBig() && shipboard.isLeftSideCannon(row)) ||
                        (!incomingMeteor.isBig() && shipboard.isLeftSideShielded()) ||
                        (!incomingMeteor.isBig() && !shipboard.isExposedConnectorOnLeft(row)))) {
                    boolean destructionComplete = false;
                    for(int j = 0; j < 7 && !destructionComplete; j++) {
                        if(shipboard.getComponentTileFromGrid(row, j).isPresent()) {
                            shipboard.destroyTile(row, j);
                            destructionComplete = true;
                        }
                    }
                }
            }

            // deactivates all components
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                }
            }
            meteorSwarmCard.getObservableModel().updateAllShipboardList(game.getShipboards());

            if(shipboard.highlightShipWrecks() > 1) {
                transition(new MeteorSwarmState_3(meteorSwarmCard));
                meteorSwarmCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
            }
            else {
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    setNewDices();
                    meteorSwarmCard.setNextIndexOfMeteor();
                    meteorSwarmCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
                    if(meteorSwarmCard.thereAreStillMeteors()) {
                        game.setCurrPlayerToLeader();
                        meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                        transition(new MeteorSwarmState_1(meteorSwarmCard));
                        meteorSwarmCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
                    }
                    else {
                        game.manageInvalidPlayers();
                        game.setCurrPlayerToLeader();
                        game.setCurrCard(null);
                        meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                        meteorSwarmCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
                        meteorSwarmCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                        meteorSwarmCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                    }
                }
                else {
                    game.setCurrPlayerToNext();
                    meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                    transition(new MeteorSwarmState_1(meteorSwarmCard));
                    meteorSwarmCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
                }
            }
        }
    }

    @Override
    public String getStateName() {
        return "MeteorSwarmState_1";
    }

    private void setNewDices() {
        game.getDices().rollDices();
        meteorSwarmCard.setDice1(game.getDices().getDice1());
        meteorSwarmCard.setDice2(game.getDices().getDice2());
    }
}
