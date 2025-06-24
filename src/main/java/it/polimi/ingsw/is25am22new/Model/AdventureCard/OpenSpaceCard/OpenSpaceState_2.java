package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.io.Serializable;

/**
 * Represents the second state of an OpenSpaceCard in the game flow.
 * This state extends the functionality of the OpenSpaceState base class
 * and adds specific behaviors for processing the OpenSpaceCard when in this state.
 */
public class OpenSpaceState_2 extends OpenSpaceState implements Serializable {

    /**
     * Initializes the OpenSpaceState_2 object with the given OpenSpaceCard.
     * @param openSpaceCard
     */
    public OpenSpaceState_2(OpenSpaceCard openSpaceCard) {
        super(openSpaceCard);
    }

    /**
     * Returns the name of the current state, which is "OpenSpaceState_2".
     * @return
     */
    @Override
    public String getStateName() {
        return "OpenSpaceState_1";
    }

    /**
     * Activates the effect of a specific component tile on the current player's shipboard,
     * transitioning the game to the next state if the component is valid for activation.
     *
     * @param inputCommand An InputCommand object containing the row and column coordinates of the component tile
     *                     to be activated on the current player's shipboard.
     *                     The row and column values identify the position of the tile in the grid.
     *                     The specified tile must be an engine component; otherwise, an IllegalArgumentException is thrown.
     */
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
