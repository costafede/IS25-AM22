package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneState_9;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class CombatZoneState2_2 extends CombatZoneState2 implements Serializable {
    public CombatZoneState2_2(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();


        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(combatZoneCard2.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isDoubleCannon()) {
            // activates the component
            ctOptional.ifPresent(ComponentTile::activateComponent);
            combatZoneCard2.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        combatZoneCard2.setBatteryUsed(false);

        transition(new CombatZoneState2_1(combatZoneCard2));
    }

    @Override
    public String getStateName() {
        return "CombatZoneState2_2";
    }
}
