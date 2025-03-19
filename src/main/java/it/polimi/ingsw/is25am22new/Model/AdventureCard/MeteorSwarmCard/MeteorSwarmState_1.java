package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MeteorSwarmState_1 extends MeteorSwarmState{
    public MeteorSwarmState_1(MeteorSwarmCard meteorSwarmCard) { super(meteorSwarmCard); }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        if(meteorSwarmCard.thereAreStillMeteors()) {
            game.getDices().rollDices();
            int x = inputCommand.getRow();
            int y = inputCommand.getCol();
            AtomicInteger numOfBatteries = new AtomicInteger(0);

            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            ctOptional.ifPresent(ct -> numOfBatteries.set(ct.getNumOfBatteries()));
            if(numOfBatteries.get() > 0) {
                ctOptional.get().removeBatteryToken();
                meteorSwarmCard.setBatteryUsed(true);
            }
        }
        else { // resolves the card
            game.setCurrCard(null);
        }
    }

    @Override
    public void transition(MeteorSwarmState meteorSwarmState) {
        // transition made after battery token used
        meteorSwarmCard.setMeteorSwarmState(meteorSwarmState);
    }
}
