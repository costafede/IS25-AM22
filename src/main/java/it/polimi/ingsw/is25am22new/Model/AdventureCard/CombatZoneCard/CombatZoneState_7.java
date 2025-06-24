package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents the seventh state in the Combat Zone state machine. This state is part of the overall
 * combat zone card mechanism in the game for managing specific interactions and effects.
 *
 * In the context of this state, the activation of certain shipboard components (particularly
 * double cannon components) is managed, contingent upon specific conditions. Once the effect
 * is activated, the process transitions to the next state in the sequence.
 *
 * Responsibilities of this class include:
 * - Activating effects of specified shipboard components based on the provided input.
 * - Interfacing with game entities to update the observable model after activating components.
 * - Managing the transition from this state to the subsequent state.
 */
public class CombatZoneState_7 extends CombatZoneState implements Serializable {

    /**
     * Initializes the state with the provided adventure card.
     * @param combatZoneCard
     */
    public CombatZoneState_7(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    /**
     * Activates the effect of the component tile at the specified location on the shipboard.
     * @param inputCommand
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(combatZoneCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        combatZoneCard.setBatteryUsed(false);

        transition(new CombatZoneState_6(combatZoneCard));
    }

    /**
     * Returns the name of the state, used for debugging purposes.
     * @return
     */
    @Override
    public String getStateName() {
        return "CombatZoneState_7";
    }
}
