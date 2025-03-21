package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class OpenSpaceState_2 extends OpenSpaceState{
    public OpenSpaceState_2(OpenSpaceCard openSpaceCard) {
        super(openSpaceCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get().activateComponent();
        transition(new OpenSpaceState_1(openSpaceCard));
    }
}
