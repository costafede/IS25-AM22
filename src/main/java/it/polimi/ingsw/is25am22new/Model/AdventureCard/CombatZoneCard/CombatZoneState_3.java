package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;

public class CombatZoneState_3 extends CombatZoneState {
    public CombatZoneState_3(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

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

        if(!inputCommand.getChoice()) { // do you want to continue using batteries?
            if(game.getCurrPlayer().equals(game.getLastPlayer())) { // if last player
                transition(new CombatZoneState_4(combatZoneCard));
            }
            else { // if not last player
                combatZoneCard.getPlayerToStrength().put(currentPlayer, (double)shipboard.getEngineStrength());
                game.setCurrPlayerToNext();
                combatZoneCard.setBatteryUsed(false);
                transition(new CombatZoneState_2(combatZoneCard));
            }
        }
        else {
            transition(new CombatZoneState_2(combatZoneCard));
        }
    }
}
