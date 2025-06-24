package it.polimi.ingsw.is25am22new.Model.GamePhase;

import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the building phase of the game. During this phase,
 * players construct their shipboards and prepare for subsequent game phases.
 * The phase transitions are determined based on the state of each player's shipboard.
 */
public class BuildingPhase extends GamePhase {


    /**
     * Constructs a BuildingPhase object which represents the phase in the game where building actions are taken.
     *
     * @param game the current game instance associated with this phase
     */
    public BuildingPhase(Game game) {
        super(game);
        phaseType = PhaseType.BUILDING;
    }

    /**
     * Attempts to transition the game to the next phase based on the current state of the shipboards and game rules.
     * <br>
     * This method evaluates all shipboards of the players to ensure certain conditions are met for progression.
     * If all players have placed their rockets but errors are detected in shipboards, corrective actions or transitions may occur.
     * Additional transitions may take place based on whether all cabins are populated and rockets are placed correctly.
     * <br>
     * The following conditions are checked:
     * - Whether rockets have been placed on all shipboards.
     * - Whether shipboards are valid (no configuration errors).
     * - Whether all cabins on the shipboards are populated.
     *
     * Transition logic:
     * - If all rockets are placed and errors exist in shipboards:
     *   - In a tutorial game, incorrect shipboards' rockets are returned for correction.
     *   - In other cases, transitions to the "CorrectingShipPhase" phase.
     * - If rockets are placed but not all cabins are populated, transitions to the "PlaceCrewMembersPhase".
     * - If all rockets are placed and the shipboards are valid, transitions to the "CardPhase".
     */
    public void trySwitchToNextPhase(){
        boolean flag_rocket_placed = true;
        boolean flag_valid = true;
        boolean flag_shipboards_populated = true;
        List<Shipboard> incorrectShipboards = new ArrayList<>();
        for(String player : game.getPlayerList()){
            if(!game.getShipboards().get(player).isRocketPlaced())
                flag_rocket_placed = false;
            if(!game.getShipboards().get(player).checkShipboard()) {
                flag_valid = false;
                incorrectShipboards.add(game.getShipboards().get(player));
            }
            if(!game.getShipboards().get(player).allCabinsArePopulated())
                flag_shipboards_populated = false;
        }
        if(flag_rocket_placed && !flag_valid) {
            if(game instanceof TutorialGame)
                returnRockets(incorrectShipboards);
            else
                transition(new CorrectingShipPhase(game));
        }
        else if(flag_rocket_placed && !flag_shipboards_populated)
            transition(new PlaceCrewMembersPhase(game));
        else if(flag_rocket_placed)
            transition(new CardPhase(game));
    }

    /**
     * Reverts the placement of rockets for the specified list of shipboards and updates the game state accordingly.
     *
     * @param incorrectShipboards the list of shipboards with incorrect rocket placements to be reverted
     */
    private void returnRockets(List<Shipboard> incorrectShipboards) {
        Flightboard flightboard = game.getFlightboard();
        for(Shipboard shipboard : incorrectShipboards){
            flightboard.removeRocket(shipboard.getNickname());
            shipboard.setRocketPlaced(false);
            shipboard.setFinishedShipboard(false);
            shipboard.setCorrectingShip(true);
        }
        for(String player : new ArrayList<>(flightboard.getOrderedRockets()))
            for(int i = 0; i < incorrectShipboards.size(); i++)
                stepForwardInStartingPositions(flightboard, player);
        game.updateAllFlightboard(flightboard);
        game.updateAllShipboardList(game.getShipboards());
    }

    /**
     * Moves the player's rocket forward in the sequence of starting positions on the flightboard.
     * The new starting position is determined based on the player's current position and the
     * availability of other positions in the sequence. After determining the new position, the
     * player's rocket is removed and re-placed at the updated position.
     *
     * @param flightboard The flightboard object representing the current state of the game,
     *                    including the positions of all rockets and their ordering.
     * @param player The unique identifier of the player whose rocket is to be moved.
     */
    private void stepForwardInStartingPositions(Flightboard flightboard, String player) {
        int pos = flightboard.getPositions().get(player);
        int new_starting_pos;
        switch(pos){
            case 4:
                new_starting_pos = 0;
                break;
            case 2:
                if(!flightboard.getPositions().containsValue(4))
                    new_starting_pos = 0;
                else
                    new_starting_pos = 1;
                break;
            case 1:
                if(!flightboard.getPositions().containsValue(2))
                    new_starting_pos = 1;
                else
                    new_starting_pos = 2;
                break;
            case 0:
                if(!flightboard.getPositions().containsValue(1))
                    new_starting_pos = 2;
                else
                    new_starting_pos = 3;
                break;
            default:
                new_starting_pos = -1;
                break;
        }
        flightboard.removeRocket(player);
        flightboard.placeRocket(player, new_starting_pos);
    }
}
