package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.io.Serializable;

public class SmugglersState_2 extends SmugglersState implements Serializable {
    public SmugglersState_2(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        ComponentTile doubleCannon = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
        doubleCannon.activateComponent();
        smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
        if(doubleCannon.getCannonStrength() == 0)
            throw new IllegalArgumentException("You didn't select a cannon");
        transition(new SmugglersState_1(smugglersCard));
    }

    @Override
    public String getStateName() {
        return "SmugglersState_2";
    }
}
