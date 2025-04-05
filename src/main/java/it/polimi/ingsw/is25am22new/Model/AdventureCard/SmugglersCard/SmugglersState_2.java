package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

public class SmugglersState_2 extends SmugglersState{
    public SmugglersState_2(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        ComponentTile doubleCannon = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
        doubleCannon.activateComponent();
        if(doubleCannon.getCannonStrength() == 0)
            throw new IllegalArgumentException("You didn't select a cannon");
        transition(new SmugglersState_1(smugglersCard));
    }
}
