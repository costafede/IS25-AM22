package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class CombatZoneState_1 extends CombatZoneState {
    public CombatZoneState_1(CombatZoneCard combatZoneCard) {
        super(combatZoneCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        // player with the fewest crew members loses daysOnFlight
        String playerFewestMembers = game.getCurrPlayer();
        int minCrewNumber = game.getShipboards().get(playerFewestMembers).getCrewNumber();
        for(String player : game.getPlayerList()){
            if(game.getShipboards().get(player).getCrewNumber() < minCrewNumber){
                playerFewestMembers = player;
            }
        }
        game.getFlightboard().shiftRocket(game.getShipboards(), playerFewestMembers, combatZoneCard.getFlightDaysLost());

        transition(new CombatZoneState_2(combatZoneCard));
    }
}
