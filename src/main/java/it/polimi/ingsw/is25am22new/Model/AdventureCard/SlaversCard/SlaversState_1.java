package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SlaversState_1 extends SlaversState implements Serializable {
    public SlaversState_1(SlaversCard slaversCard) {
        super(slaversCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        if(inputCommand.getChoice()) { // want to use battery?
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            AtomicInteger numOfBatteries = new AtomicInteger(0);
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            // if player clicks on empty batteryComponent -> systems asks to confirm but cannot activate effect
            // because the flag setBatteryUsed is still false
            ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
            if(numOfBatteries.get() > 0) {
                ctOptional.ifPresent(ComponentTile::removeBatteryToken);
                slaversCard.setBatteryUsed(true);
            }
            transition(new SlaversState_2(slaversCard));
        }
        else { // choose to stop using batteries
            if(shipboard.getCannonStrength() > slaversCard.getCannonStrength()) { // win case
                transition(new SlaversState_3(slaversCard)); // decide to lose daysOnFlight and take credits or not
            }
            else if (shipboard.getCannonStrength() < slaversCard.getCannonStrength()) { // lose case
                slaversCard.resetSelectedMembers();
                transition(new SlaversState_4(slaversCard));
            }
            else {// in case of tie nothing happens and the slavers attack next player
                // deactivates all components
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 7; j++){
                        game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                    }
                }
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    game.manageInvalidPlayers();
                    game.setCurrPlayerToLeader();
                    game.setCurrCard(null);
                }
                else {
                    game.setCurrPlayerToNext();
                    transition(new SlaversState_1(slaversCard));
                }
            }
        }
    }

    @Override
    public String getStateName() {
        return "SlaversState_1";
    }
}
