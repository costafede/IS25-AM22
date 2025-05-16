package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.Optional;

/**
 * Represents the fifth state in a sequence of combat zone states during the game.
 * This state is responsible for handling specific mechanics related to crew member removal
 * from the shipboard and transitions to subsequent combat states based on game logic.
 *
 * The responsibilities of this state include:
 * - Removing crew members from cabin tiles on the shipboard, if applicable.
 * - Updating the observable model to reflect changes to the shipboard or current player.
 * - Transitioning to new combat zone states based on the number of removed crew members
 *   or the state of the crew on the shipboard.
 */
public class CombatZoneState_5 extends CombatZoneState implements Serializable {
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
                combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
                combatZoneCard.increaseRemovedMembers();
            }
        }

        if (combatZoneCard.getRemovedMembers() == combatZoneCard.getAstronautsToLose() ||
                !shipboard.thereIsStillCrew()) {
            game.setCurrPlayerToLeader();
            combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new CombatZoneState_6(combatZoneCard));
        }
        else if(combatZoneCard.getRemovedMembers() < combatZoneCard.getAstronautsToLose()) {
            transition(new CombatZoneState_5(combatZoneCard));
        }
    }

    @Override
    public String getStateName() {
        return "CombatZoneState_5";
    }

}
