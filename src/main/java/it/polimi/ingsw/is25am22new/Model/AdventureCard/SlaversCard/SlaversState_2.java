package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState_1;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class SlaversState_2 extends SlaversState implements Serializable {
    public SlaversState_2(SlaversCard slaversCard) {
        super(slaversCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(slaversCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }
        slaversCard.setBatteryUsed(false);
        transition(new SlaversState_1(slaversCard));
    }

    @Override
    public String getStateName() {
        return "SlaversState_2";
    }
}
