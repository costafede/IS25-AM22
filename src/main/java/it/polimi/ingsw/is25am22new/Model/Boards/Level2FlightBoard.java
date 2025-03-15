package it.polimi.ingsw.is25am22new.Model.Boards;

import java.util.List;
import java.util.Map;

public class Level2FlightBoard extends Flightboard {

    public Level2FlightBoard() {
        super(24);
    }

    public Level2FlightBoard(List<String> orderedRockets, Map<String, Integer> positions, int flightBoardLength) {
        super(orderedRockets, positions, flightBoardLength);
    }

    @Override
    public void placeRocket(String nickname, int pos) {
        // positions go from 0 to numberOfPlayers - 1
        if(pos == 0) {
            positions.put(nickname, 6);
        } else if(pos == 1) {
            positions.put(nickname, 3);
        } else if(pos == 2) {
            positions.put(nickname, 1);
        } else if(pos == 3) {
            positions.put(nickname, 0);
        }
    }

}
