package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;

public class CombatZoneState_5 extends CombatZoneState{
    public CombatZoneState_5(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);
        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        Optional<ComponentTile> ctOptional = shipboard.getComponentTileFromGrid(x, y);
        if(ctOptional.isPresent() && ctOptional.get().isCabin()) {
            if(ctOptional.get().getCrewNumber() > 0) {
                ctOptional.get().removeCrewMember();
                combatZoneCard.increaseRemovedMembers();
            }
        }

        if (combatZoneCard.getRemovedMembers() == combatZoneCard.getAstronautsToLose() ||
                !shipboard.thereIsStillCrew()) {
            game.setCurrPlayerToLeader();
            transition(new CombatZoneState_6(combatZoneCard));
        }
        else if(combatZoneCard.getRemovedMembers() < combatZoneCard.getAstronautsToLose()) {
            transition(new CombatZoneState_5(combatZoneCard));
        }
    }

}
