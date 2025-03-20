package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Objects;
import java.util.Optional;

public class MeteorSwarmState_2 extends MeteorSwarmState {
    public MeteorSwarmState_2(MeteorSwarmCard meteorSwarmCard) { super(meteorSwarmCard); }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        Meteor incomingMeteor = meteorSwarmCard.getNumberToMeteor().get(meteorSwarmCard.getIndexOfIncomingMeteor());
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int col = game.getDices().getDice1() + game.getDices().getDice2();
        int row = game.getDices().getDice1() + game.getDices().getDice2();

        if(inputCommand.getChoice()) {
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            if(meteorSwarmCard.isBatteryUsed()) {
                // activates the component
                shipboard.getComponentTileFromGrid(x, y).ifPresent(ComponentTile::activateComponent);
            }
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

            // reset batteryUsed, deactivate components, next player
            meteorSwarmCard.setBatteryUsed(false);
            shipboard.getComponentTileFromGrid(x, y).ifPresent(ComponentTile::deactivateComponent);

            if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                meteorSwarmCard.setNextIndexOfMeteor();
                game.setCurrPlayerToLeader();
                if(meteorSwarmCard.thereAreStillMeteors()) {
                    transition(new MeteorSwarmState_1(meteorSwarmCard));
                }
                else {
                    game.setCurrCard(null);
                }
            }
            else {
                game.setCurrPlayerToNext();
                transition(new MeteorSwarmState_1(meteorSwarmCard));
            }
        }
    }
}
