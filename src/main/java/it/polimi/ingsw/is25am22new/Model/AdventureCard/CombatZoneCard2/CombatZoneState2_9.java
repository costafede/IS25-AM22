package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

public class CombatZoneState2_9 extends CombatZoneState2 implements Serializable {
    public CombatZoneState2_9(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        shipboard.chooseShipWreck(x, y);

        combatZoneCard2.setNextIndexOfShot();
        if(combatZoneCard2.thereAreStillShots()) {
            transition(new CombatZoneState2_7(combatZoneCard2));
        }
        else {
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
        }
    }

    @Override
    public String getStateName() {
        return "CombatZoneState2_9";
    }
}
