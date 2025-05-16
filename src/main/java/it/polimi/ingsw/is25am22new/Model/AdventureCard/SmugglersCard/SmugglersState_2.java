package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.io.Serializable;

/**
 * Represents the second state of the Smugglers card within the game's state machine.
 * This state enables the activation of a specific component, updating the game state
 * accordingly and managing transitions to other states.
 *
 * This class extends the abstract SmugglersState class and implements Serializable
 * for object serialization.
 */
public class SmugglersState_2 extends SmugglersState implements Serializable {
    public SmugglersState_2(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    /**
     * Activates the effect of a component tile at the specified grid location defined by the
     * input command. Updates the current player's shipboard and transitions to a new state based
     * on the component activation. Throws an exception if the selected tile is not a cannon.
     *
     * @param inputCommand the input command containing the coordinates of the component tile
     *                     (row and column) on the shipboard to be activated.
     * @throws IllegalArgumentException if the selected component tile is not a cannon.
     */
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
