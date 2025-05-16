package it.polimi.ingsw.is25am22new.Model.Flightboards;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The TutorialFlightBoard class represents a specific implementation of a flightboard
 * used in a tutorial game setting. It extends the base Flightboard class and provides
 * customized logic for placing rockets at predefined starting positions and determining
 * the order of rockets based on their positions.
 *
 * This class is designed to manage tutorial-specific rocket placement and ordering
 * logic while leveraging the core functionality provided by the Flightboard base class.
 */
public class TutorialFlightBoard extends Flightboard{

    public TutorialFlightBoard(Game game) {
        super(game, 18);
    }

    public TutorialFlightBoard(List<String> orderedRockets, Map<String, Integer> positions, int flightBoardLength) {
        super(orderedRockets, positions, flightBoardLength);
    }

    /**
     * Places a rocket on the flightboard at the specified position based on the
     * tutorial-specific starting position logic. This method maps the given position
     * to a predefined flightboard index and updates the positions map and ordered rockets list.
     *
     * @param nickname The unique nickname of the rocket being placed on the flightboard.
     * @param pos The position on the flightboard where the rocket is to be placed.
     *            Valid positions range from 0 to 3, corresponding to predefined starting positions.
     *            Throws an IllegalArgumentException if the position is invalid.
     */
    @Override
    public void placeRocket(String nickname, int pos) {
        // positions go from 0 to numberOfPlayers - 1
        if(pos < 0 || pos > 3)
            throw new IllegalArgumentException("Invalid position");
        if(pos == 0) {
            positions.put(nickname, 4);
        } else if(pos == 1) {
            positions.put(nickname, 2);
        } else if(pos == 2) {
            positions.put(nickname, 1);
        } else {
            positions.put(nickname, 0);
        }
        setOrderedRocketsAndDaysOnFlight(game.getShipboards());
    }

    /**
     * Updates the ordered list of rockets based on their positions and sets the number
     * of days in flight for each rocket on their respective shipboards. The rockets are
     * ordered in descending order of their positions on the flightboard.
     *
     * @param shipboards A map of rocket nicknames to their corresponding Shipboard objects,
     *                   used to update each rocket's days on flight based on its position.
     */
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

    @Override
    public List<Integer> getStartingPositions() {
        return List.of(4, 2, 1, 0);
    }
}
