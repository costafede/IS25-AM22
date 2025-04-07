package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

public class BuildingPhase extends GamePhase {
    public BuildingPhase(Game game) {
        super(game);
        phaseType = PhaseType.BUILDING;
    }

    public void trySwitchToNextPhase(){
        boolean flag_finished = true;
        boolean flag_valid = true;
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).isFinishedShipboard())
                flag_finished = false;
            if(!game.getShipboards().get(player).checkShipboard())
                flag_valid = false;
        }
        if(flag_finished && !flag_valid)
            transition(new CorrectingShipPhase(game));
        if(flag_finished && flag_valid)
            transition(new CardPhase(game));
    }
}
