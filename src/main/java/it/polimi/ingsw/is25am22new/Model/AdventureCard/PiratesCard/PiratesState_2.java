package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents the PiratesState_2 state in the pirate's card state machine.
 * Inherits behavior from the PiratesState class, implementing specific effects and transitions
 * associated with this state.
 *
 * This state allows the activation of double cannon component tiles on the player's shipboard,
 * given certain conditions, utilizing the associated PiratesCard instance and game context.
 */
public class PiratesState_2 extends PiratesState implements Serializable {

    /**
     * Initializes the PiratesState_2 state with the provided PiratesCard instance.
     * @param piratesCard
     */
    public PiratesState_2(PiratesCard piratesCard) {
        super(piratesCard);
    }

    /**
     * Activates an effect based on the state of the game and the input command provided.
     * Specifically checks conditions on the player's shipboard and transitions to a new state
     * after applying the effect.
     *
     * @param inputCommand the command containing the input parameters such as row and column,
     *                     which determine the target tile to activate.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = 0;
        int y = 0;

        x = inputCommand.getRow();
        y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(piratesCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            piratesCard.getObservableModel().updateAllShipboard(currentPlayer,shipboard);
        }
        piratesCard.setBatteryUsed(false);

        transition(new PiratesState_1(piratesCard));
    }

    /**
     * Returns the name of the state, which is "PiratesState_2".
     * @return
     */
    @Override
    public String getStateName() {
        return "PiratesState_2";
    }
}
