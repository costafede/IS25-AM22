package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.io.Serializable;

public class OpenSpaceState_2 extends OpenSpaceState implements Serializable {
    public OpenSpaceState_2(OpenSpaceCard openSpaceCard) {
        super(openSpaceCard);
    }

    @Override
    public String getStateName() {
        return "OpenSpaceState_1";
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        ComponentTile doubleEngine = game.getShipboards().get(game.getCurrPlayer()).getComponentTileFromGrid(inputCommand.getRow(), inputCommand.getCol()).get();
        doubleEngine.activateComponent();
        if(doubleEngine.getEngineStrength() == 0){
            throw new IllegalArgumentException("The tile you chose was not an engine");
        }
        transition(new OpenSpaceState_1(openSpaceCard));
        openSpaceCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
    }
}
