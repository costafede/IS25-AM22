package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmState_1;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * The SlaversState_2 class represents a specific state of a SlaversCard in the game.
 * This state manages the card's effect execution and its transition to a subsequent state.
 * It extends the SlaversState class and implements Serializable for persistence.
 *
 * Responsibilities of this class include:
 * 1. Activating the effect of the associated SlaversCard when certain conditions are met.
 * 2. Transitioning the card to the next state upon completion of its functionality.
 * 3. Returning the identifiable name of this specific state.
 *
 * The main effect in this state involves activating a double cannon component
 * on the shipboard grid if battery usage is flagged and conditions are satisfied.
 */
public class SlaversState_2 extends SlaversState implements Serializable {

    /**
     * Initializes the SlaversState_2 object with the provided SlaversCard.
     * @param slaversCard
     */
    public SlaversState_2(SlaversCard slaversCard) {
        super(slaversCard);
    }

    /**
     * Activates the effect associated with the current state of a SlaversCard.
     * This method checks if specific conditions are met on the game board
     * and applies the effect to the relevant components. Upon execution,
     * it transitions the SlaversCard to the next state.
     *
     * @param inputCommand An object containing the row and column indexes
     *                     for targeting a specific component on the shipboard
     *                     and additional instructions for processing.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(slaversCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }
        slaversCard.setBatteryUsed(false);
        transition(new SlaversState_1(slaversCard));
    }

    /**
     * Returns the identifiable name of this state.
     * @return
     */
    @Override
    public String getStateName() {
        return "SlaversState_2";
    }
}
