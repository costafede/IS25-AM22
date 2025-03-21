package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class SmugglersState_2 extends SmugglersState{
    public SmugglersState_2(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get().activateComponent();
        transition(new SmugglersState_1(smugglersCard));
    }
}
