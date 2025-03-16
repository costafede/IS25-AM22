package it.polimi.ingsw.is25am22new.Model.Flightboards;

import java.util.List;
import java.util.Map;

public class TutorialFlightBoard extends Flightboard{

    public TutorialFlightBoard() {
        super(18);
    }

    public TutorialFlightBoard(List<String> orderedRockets, Map<String, Integer> positions, int flightBoardLength) {
        super(orderedRockets, positions, flightBoardLength);
    }

    @Override
    public void placeRocket(String nickname, int pos) {
        // positions go from 0 to numberOfPlayers - 1
        if(pos == 0) {
            positions.put(nickname, 4);
        } else if(pos == 1) {
            positions.put(nickname, 2);
        } else if(pos == 2) {
            positions.put(nickname, 1);
        } else if(pos == 3) {
            positions.put(nickname, 0);
        }
    }
}
