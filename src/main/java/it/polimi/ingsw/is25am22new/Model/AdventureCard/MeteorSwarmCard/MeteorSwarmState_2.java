package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Meteor;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class MeteorSwarmState_2 extends MeteorSwarmState implements Serializable {
    public MeteorSwarmState_2(MeteorSwarmCard meteorSwarmCard) { super(meteorSwarmCard); }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(meteorSwarmCard.isBatteryUsed() && ctOptional.isPresent() &&
                (ctOptional.get().isShieldGenerator() || ctOptional.get().isDoubleCannon())) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
        }

        meteorSwarmCard.setBatteryUsed(false);

        transition(new MeteorSwarmState_1(meteorSwarmCard));
    }

    @Override
    public String getStateName() {
        return "MeteorSwarmState_2";
    }
}
