package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public class CorrectingShipPhase extends GamePhase {
    public CorrectingShipPhase(Game game) {
        super(game);
        phaseType = PhaseType.CORRECTINGSHIP;
    }

    public void trySwitchToNextPhase(){
        boolean flag = true;
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).checkShipboard())
                flag = false;
        }
        if(flag) {
            transition(new CardPhase(game));
        }
    }
}
