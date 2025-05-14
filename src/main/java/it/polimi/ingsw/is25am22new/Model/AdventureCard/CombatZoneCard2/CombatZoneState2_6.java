package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

public class CombatZoneState2_6 extends CombatZoneState2 implements Serializable {
    public CombatZoneState2_6(CombatZoneCard2 combatZoneCard2) {
        super(combatZoneCard2);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        String playerFewestMembers = game.getCurrPlayer();
        int minCrewNumber = game.getShipboards().get(playerFewestMembers).getCrewNumber();
        for(String player : game.getPlayerList()){
            if(game.getShipboards().get(player).getCrewNumber() < minCrewNumber){
                playerFewestMembers = player;
            } else if (game.getShipboards().get(player).getCrewNumber() == minCrewNumber) {
                playerFewestMembers =
                        // who is ahead receives penalty
                        game.getShipboards().get(player).getDaysOnFlight() >
                                game.getShipboards().get(playerFewestMembers).getDaysOnFlight() ?
                                player : playerFewestMembers;
            }
        }
        game.setCurrPlayer(playerFewestMembers);
        combatZoneCard2.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
        transition(new CombatZoneState2_7(combatZoneCard2));
    }

    @Override
    public String getStateName() {
        return "CombatZoneState2_6";
    }
}
