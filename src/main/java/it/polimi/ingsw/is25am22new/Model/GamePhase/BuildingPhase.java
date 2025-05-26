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
        List<Shipboard> incorrectShipboards = new ArrayList<Shipboard>();
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
        if(flag_rocket_placed && flag_valid && !flag_shipboards_populated)
            transition(new PlaceCrewMembersPhase(game));
        if(flag_rocket_placed && flag_valid && flag_shipboards_populated)
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
        for(String player : flightboard.getOrderedRockets())
            stepForwardInStartingPosition(flightboard, player);
        game.updateAllFlightboard(flightboard);
        game.updateAllShipboardList(game.getShipboards());
    }

    private void stepForwardInStartingPosition(Flightboard flightboard, String player) {
        int pos = flightboard.getPositions().get(player);
        flightboard.removeRocket(player);
        switch(pos){
            case 4: break;
            case 2:
                if(!flightboard.getPositions().containsValue(4))
                    flightboard.placeRocket(player, 4);
                break;
            case 1:
                if(!flightboard.getPositions().containsValue(2))
                    flightboard.placeRocket(player, 2);
                break;
            case 0:
                if(!flightboard.getPositions().containsValue(1))
                    flightboard.placeRocket(player, 1);
                break;
        }
    }
}
