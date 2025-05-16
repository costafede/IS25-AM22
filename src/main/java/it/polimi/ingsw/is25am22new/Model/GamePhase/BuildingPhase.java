package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * Represents the building phase of the game. During this phase,
 * players construct their shipboards and prepare for subsequent game phases.
 * The phase transitions are determined based on the state of each player's shipboard.
 */
public class BuildingPhase extends GamePhase {
    public BuildingPhase(Game game) {
        super(game);
        phaseType = PhaseType.BUILDING;
    }

    public void trySwitchToNextPhase(){
        boolean flag_finished = true;
        boolean flag_valid = true;
        boolean flag_shipboards_populated = true;
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).isFinishedShipboard())
                flag_finished = false;
            if(!game.getShipboards().get(player).checkShipboard())
                flag_valid = false;
            if(!game.getShipboards().get(player).allCabinsArePopulated())
                flag_shipboards_populated = false;
        }
        if(flag_finished && !flag_valid)
            transition(new CorrectingShipPhase(game));
        if(flag_finished && flag_valid && !flag_shipboards_populated)
            transition(new PlaceCrewMembersPhase(game));
        if(flag_finished && flag_valid && flag_shipboards_populated)
            transition(new CardPhase(game));
    }
}
