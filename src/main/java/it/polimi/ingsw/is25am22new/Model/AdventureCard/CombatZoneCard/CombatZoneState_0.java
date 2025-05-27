package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * Represents the initial state of a combat zone in the game, responsible for managing the
 * specific mechanics and transitions when the combat zone is first activated.
 * This state extends the abstract CombatZoneState class and implements Serializable.
 *
 * In CombatZoneState_0, the effect tied to the card is executed, which includes:
 * - Determining the player with the fewest crew members to apply a penalty if certain conditions are met.
 * - Adjusting the game state by shifting the rocket of the affected player backward based on flight days lost.
 * - Setting the current player to the leader.
 * - Updating the observable model for relevant game boards and players.
 * - Managing invalid players if conditions are not satisfied.
 * - Transitioning to the next state if necessary.
 *
 * Key behaviors include:
 * - Resolving the penalty for players with fewer crew members or tied conditions.
 * - Coordinating updates to the game components according to the state.
 * - Transitioning to a new state for subsequent combat zone mechanics.
 */
public class CombatZoneState_0 extends CombatZoneState implements Serializable {
    public CombatZoneState_0(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(game.getFlightboard().getOrderedRockets().size() > 1) {
            // player with the fewest crew members loses daysOnFlight
            String playerFewestMembers = game.getCurrPlayer();
            int minCrewNumber = game.getShipboards().get(playerFewestMembers).getCrewNumber();
            for (String player : game.getPlayerList()) {
                if (game.getShipboards().get(player).getCrewNumber() < minCrewNumber) {
                    playerFewestMembers = player;
                    minCrewNumber = game.getShipboards().get(player).getCrewNumber();
                } else if (game.getShipboards().get(player).getCrewNumber() == minCrewNumber) {
                    playerFewestMembers =
                            // who is ahead receives penalty
                            game.getShipboards().get(player).getDaysOnFlight() >
                                    game.getShipboards().get(playerFewestMembers).getDaysOnFlight() ?
                                    player : playerFewestMembers;
                }
            }
            game.getFlightboard().shiftRocket(playerFewestMembers, combatZoneCard.getFlightDaysLost());
            game.setCurrPlayerToLeader();
            combatZoneCard.getObservableModel().updateAllShipboard(playerFewestMembers, game.getShipboards().get(playerFewestMembers));
            combatZoneCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            combatZoneCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new CombatZoneState_1(combatZoneCard));
        } else {
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
        return "CombatZoneState_0";
    }
}
