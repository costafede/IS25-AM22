package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class CombatZoneState2_8 extends CombatZoneState2 implements Serializable {

    /**
     * Initializes the state with the provided adventure card.
     * @param combatZoneCard2
     */
    public CombatZoneState2_8(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
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
        if(combatZoneCard2.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isShieldGenerator()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            combatZoneCard2.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        combatZoneCard2.setBatteryUsed(false);

        transition(new CombatZoneState2_7(combatZoneCard2));
    }

    /**
     * Returns the name of the state, used for debugging purposes.
     * @return
     */
    @Override
    public String getStateName() {
        return "CombatZoneState2_8";
    }
}
