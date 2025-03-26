package it.polimi.ingsw.is25am22new.Model.Flightboards;

import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Level2FlightBoard extends Flightboard {

    public Level2FlightBoard(Game game) {
        super(game, 24);
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
        setOrderedRocketsAndDaysOnFlight(game.getShipboards());
    }

    private void setOrderedRocketsAndDaysOnFlight(Map<String, Shipboard> shipboards) {
        // called after all rockets have been placed
        orderedRockets =
                positions.entrySet().stream()
                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        for(String nickname : positions.keySet()) {
            shipboards.get(nickname).setDaysOnFlight(positions.get(nickname));
        }
    }
}
