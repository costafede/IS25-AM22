package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesState_4;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents the state of a Combat Zone card at a specific point, identified as "CombatZoneState_10".
 * This state involves interaction with the shipboard and managing the activation of shield generator components.
 * Upon processing the input command and the activation (if applicable), the state transitions to "CombatZoneState_9".
 *
 * This class extends the abstract CombatZoneState and implements behavior unique to this state.
 */
public class CombatZoneState_10 extends CombatZoneState implements Serializable {
    public CombatZoneState_10(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(combatZoneCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isShieldGenerator()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        combatZoneCard.setBatteryUsed(false);

        transition(new CombatZoneState_9(combatZoneCard));
    }

    @Override
    public String getStateName() {
        return "CombatZoneState_10";
    }
}
