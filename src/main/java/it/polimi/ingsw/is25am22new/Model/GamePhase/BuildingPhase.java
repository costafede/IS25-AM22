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
    public BuildingPhase(Game game) {
        super(game);
        phaseType = PhaseType.BUILDING;
    }

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
     * removes the rockets of those players whose shipboard is invalid from the flight board and rearranges
     * the positions of the other rockets according to the game rules
     * @param incorrectShipboards list of the incorrect shipboards
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
