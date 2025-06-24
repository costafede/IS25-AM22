package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

/**
 * Represents the state "CombatZoneState_8" during a combat encounter in the game.
 * This state enables the current player to select a shipwreck on their shipboard
 * and updates the relevant game and combat zone card properties accordingly.
 * Upon completing the necessary action, the state transitions either to the next combat state
 * or concludes the combat, managing invalid players and updating game observers.
 *
 * Inherits from the abstract class {@link CombatZoneState}, which defines the common behavior of all combat zone states.
 */
public class CombatZoneState_8 extends CombatZoneState implements Serializable {

    /**
     * Initializes the state with the provided adventure card.
     * @param combatZoneCard
     */
    public CombatZoneState_8(CombatZoneCard combatZoneCard) {
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

    /**
     * Returns the name of the state, used for debugging purposes.
     * @return
     */
    @Override
    public String getStateName() {
        return "CombatZoneState_8";
    }
}
