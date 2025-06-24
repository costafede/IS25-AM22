package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObservableModel;

import java.util.ArrayList;
import java.util.List;


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


    /**
     * Constructs a CorrectingShipPhase object, representing the phase in the game
     * where players validate and potentially correct their ship configurations.
     * This phase ensures all game rules are adhered to before transitioning to the
     * next phase of the game.
     *
     * @param game the current game instance, containing the state and data necessary
     *             for the CorrectingShipPhase to perform its operations.
     */
    public CorrectingShipPhase(Game game) {
        super(game);
        phaseType = PhaseType.CORRECTINGSHIP;
    }

    /**
     * Attempts to transition from the current "Correcting Ship Phase" to the next game phase
     * based on the state of the shipboards for all players.
     *
     * The method evaluates two key conditions for each player's shipboard:
     * 1. Validity: Ensures the shipboard's configuration complies with the game rules by invoking
     *    {@link Shipboard#checkShipboard()}.
     * 2. Population: Checks if all cabins on the shipboard are populated with crew members by
     *    invoking {@link Shipboard#allCabinsArePopulated()}.
     *
     * Transition Rules:
     * - If all shipboards are valid (`checkShipboard()` returns true for all players) and at least
     *   one shipboard has unpopulated cabins (`allCabinsArePopulated()` returns false for any player),
     *   the phase transitions to the {@link PlaceCrewMembersPhase}.
     * - If all shipboards are valid and all cabins are populated, the phase transitions to the
     *   {@link CardPhase}.
     * - If any shipboard is invalid, the phase does not transition to the next phase and remains
     *   in the "Correcting Ship Phase."
     */
    public void trySwitchToNextPhase(){
        boolean flag_valid = true;
        boolean flag_shipboards_populated = true;
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).checkShipboard()) {
                flag_valid = false;
            }
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
