package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public class BuildingPhase extends GamePhase {
    public BuildingPhase(Game game) {
        super(game);
        phaseType = PhaseType.BUILDING;
    }

    public void trySwitchToNextPhase(){
        boolean flag = true;
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).isFinishedShipboard())
                flag = false;
        }
        if(flag)
            transition(new CorrectingShipPhase(game));
    }
}
