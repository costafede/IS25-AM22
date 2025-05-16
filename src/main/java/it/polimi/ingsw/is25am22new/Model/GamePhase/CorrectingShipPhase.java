package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

/**
 * The CorrectingShipPhase class represents a specific phase in the game where players
 * validate and correct their ship configurations. It extends the GamePhase abstract
 * class and provides the functionality to transition to the next game phase based
 * on the state of the shipboards for all players.
 *
 * This phase ensures that each player's shipboard is valid according to game rules
 * and checks whether all cabins on the shipboards are properly populated.
 * Based on these conditions, it transitions to the appropriate next phase.
 */
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
