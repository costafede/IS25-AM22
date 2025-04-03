package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversState_2;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PiratesState_1 extends PiratesState {
    public PiratesState_1(PiratesCard piratesCard) {
        super(piratesCard);
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
                piratesCard.setBatteryUsed(true);
            }
            transition(new PiratesState_2(piratesCard));
        }
        else { // choose to stop using the batteries
            if(shipboard.getCannonStrength() > piratesCard.getCannonStrength()) { // win case
                transition(new PiratesState_3(piratesCard)); // decide to lose daysOnFlight and take credits or not
            }
            else if (shipboard.getCannonStrength() <= piratesCard.getCannonStrength()) { // lose or tie case
                if(shipboard.getCannonStrength() < piratesCard.getCannonStrength()) {
                    piratesCard.addDefeatedPlayer(currentPlayer);
                }
                // deactivates all components
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 7; j++){
                        game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
                    }
                }
                if(game.getCurrPlayer().equals(game.getLastPlayer())) {
                    if(!piratesCard.getDefeatedPlayers().isEmpty()){
                        piratesCard.setCurrDefeatedPlayerToFirst();
                        transition(new PiratesState_4(piratesCard)); // all defeated players get shot
                    }
                    else {
                        game.manageInvalidPlayers();
                        game.setCurrPlayerToLeader();
                        game.setCurrCard(null);
                    }
                }
                else {
                    game.setCurrPlayerToNext();
                    transition(new PiratesState_1(piratesCard));
                }
            }
        }
    }
}
