package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents the third state in the Combat Zone card's state machine. In this state, specific
 * conditions are checked and effects are activated based on the game state and player actions.
 *
 * Responsibilities of this state include:
 * - Evaluating the player's input command to determine the target location (row and column) on the shipboard.
 * - Checking if the conditions to activate a double engine component tile are satisfied.
 * - Activating the component if all conditions are met.
 * - Updating the shipboard state for the current player using an observable model.
 * - Resetting the battery usage status after the effect is activated.
 * - Transitioning the Combat Zone card to the first state, {@code CombatZoneState_1}, after completing the operations.
 */
public class CombatZoneState_3 extends CombatZoneState implements Serializable {

    /**
     * Initializes the state with the provided adventure card.
     * @param combatZoneCard
     */
    public CombatZoneState_3(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    /**
     * Evaluates the player's input command to determine the target location (row and column) on the shipboard.
     * @param inputCommand
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();
        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(combatZoneCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleEngine()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
        }
        combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);

        combatZoneCard.setBatteryUsed(false);

        transition(new CombatZoneState_1(combatZoneCard));
    }

    /**
     * Returns the name of the state, used for debugging purposes.
     * @return
     */
    @Override
    public String getStateName() {
        return "CombatZoneState_3";
    }
}
