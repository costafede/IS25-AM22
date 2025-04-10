package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Orientation;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesState_4;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.Shot;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

public class CombatZoneState_10 extends CombatZoneState implements Serializable {
    public CombatZoneState_10(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = 0;
        int y = 0;

        if(inputCommand.getChoice()) { // are you sure to activate the component?
            x = inputCommand.getRow();
            y = inputCommand.getCol();
            Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
            if(combatZoneCard.isBatteryUsed() && ctOptional.isPresent() && ctOptional.get().isBattery()) {
                // activates the component
                ctOptional.ifPresent(ComponentTile::activateComponent);
            }
        }

        combatZoneCard.setBatteryUsed(false);

        transition(new CombatZoneState_9(combatZoneCard));
    }
}
