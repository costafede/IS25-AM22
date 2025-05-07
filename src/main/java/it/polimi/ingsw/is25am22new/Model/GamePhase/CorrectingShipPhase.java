package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public class CorrectingShipPhase extends GamePhase {
    public CorrectingShipPhase(Game game) {
        super(game);
        phaseType = PhaseType.CORRECTINGSHIP;
    }

    public void trySwitchToNextPhase(){
        boolean flag_valid = true;
        boolean flag_shipboards_populated = true;
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).checkShipboard())
                flag_valid = false;
            if(!game.getShipboards().get(player).allCabinsArePopulated())
                flag_shipboards_populated = false;
        }
        if(flag_valid && !flag_shipboards_populated) {
            transition(new PlaceCrewMembersPhase(game));
        }
        if(flag_valid && flag_shipboards_populated) {
            transition(new CardPhase(game));
        }
    }
}
