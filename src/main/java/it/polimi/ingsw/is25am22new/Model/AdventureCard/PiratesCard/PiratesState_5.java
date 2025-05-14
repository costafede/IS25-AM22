package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState_1;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class PiratesState_5 extends PiratesState implements Serializable {
    public PiratesState_5(PiratesCard piratesCard) {
        super(piratesCard);
    }
    
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String defeatedPlayer = piratesCard.getCurrDefeatedPlayer();
        Shipboard shipboard = game.getShipboards().get(defeatedPlayer);
        int x = 0;
        int y = 0;

        x = inputCommand.getRow();
        y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(piratesCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isShieldGenerator()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            piratesCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
        }

        piratesCard.setBatteryUsed(false);

        transition(new PiratesState_4(piratesCard));
    }

    @Override
    public String getStateName() {
        return "PiratesState_5";
    }
}
