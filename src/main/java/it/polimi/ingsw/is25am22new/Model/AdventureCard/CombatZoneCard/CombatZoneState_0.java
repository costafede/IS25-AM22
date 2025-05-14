package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

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
