package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

public class CombatZoneState_8 extends CombatZoneState implements Serializable {
    public CombatZoneState_8(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        shipboard.chooseShipWreck(x, y);
        combatZoneCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);

        combatZoneCard.setNextIndexOfShot();
        if(combatZoneCard.thereAreStillShots()) {
            transition(new CombatZoneState_9(combatZoneCard));
        }
        else {
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
            combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            combatZoneCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            combatZoneCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    @Override
    public String getStateName() {
        return "CombatZoneState_8";
    }
}
