package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
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
        if(!piratesCard.getDefeatedPlayers().isEmpty()){
            String defeatedPlayer = piratesCard.getDefeatedPlayers().getFirst();
            Shipboard shipboard = game.getShipboards().get(defeatedPlayer);
            game.getDices().rollDices();

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
            }

            transition(new PiratesState_5(piratesCard));
        }
        else{
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }

    }
}
