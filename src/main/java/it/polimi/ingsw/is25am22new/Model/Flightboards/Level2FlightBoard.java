package it.polimi.ingsw.is25am22new.Model.Flightboards;

import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a Level 2 specific implementation of the Flightboard,
 * designed with predefined starting positions and unique rocket placement
 * logic for a more complex game configuration.
 */
public class Level2FlightBoard extends Flightboard {

    /**
     * Constructs a Level 2 Flightboard with a specific game configuration
     * and predefined settings suitable for Level 2 gameplay.
     *
     * @param game The game instance to which this flightboard belongs,
     *             providing context and data for the flightboard's behavior.
     */
    public Level2FlightBoard(Game game) {
        super(game, 24);
    }

    /**
     * Constructs a Level2FlightBoard with the specified ordered list of rocket nicknames,
     * a map of rocket positions, and the length of the flightboard. This constructor is used
     * to initialize a Level 2 Flightboard with predefined settings for more complex configurations.
     *
     * @param orderedRockets The ordered list of rocket nicknames representing their sequence on the flightboard.
     * @param positions A map containing the positions of rockets, where the key is the rocket's nickname and the value is its position.
     * @param flightBoardLength The length of the flightboard, representing the total number of valid positions.
     */
    public Level2FlightBoard(List<String> orderedRockets, Map<String, Integer> positions, int flightBoardLength) {
        super(orderedRockets, positions, flightBoardLength);
    }

    /**
     * Places a rocket with the specified nickname at the designated position
     * on the flightboard, based on predefined starting positions for rockets.
     * Updates the internal positions map and recalculates the ordered list
     * of rockets and their days on flight.
     *
     * @param nickname The unique identifier of the rocket to be placed.
     * @param pos The placement index, representing the rocket's starting
     *            position. Valid values are in the range 0 to 3
     *            (inclusive), corresponding to predefined game positions.
     * @throws IllegalArgumentException If the provided position is outside
     *                                  the valid range (0 to 3).
     */
    @Override
    public void placeRocket(String nickname, int pos) {
        // positions go from 0 to numberOfPlayers - 1
        if(pos < 0 || pos > 3)
            throw new IllegalArgumentException("Invalid position");
        if(pos == 0) {
            positions.put(nickname, 6);
        } else if(pos == 1) {
            positions.put(nickname, 3);
        } else if(pos == 2) {
            positions.put(nickname, 1);
        } else {
            positions.put(nickname, 0);
        }
        setOrderedRocketsAndDaysOnFlight(game.getShipboards());
    }

    /**
     * Updates the current ordered list of rockets based on their positions in descending order
     * and sets the number of days on flight for each rocket using the provided shipboards data.
     *
     * @param shipboards A map of rocket nicknames to their corresponding Shipboard objects,
     *                   used to update the days on flight for each rocket.
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

    /**
     * Retrieves the predefined starting positions for rockets on the flightboard.
     * These positions are determined based on the specific configurations
     * of a Level 2 flightboard.
     *
     * @return A list of integers representing the starting positions,
     *         ordered as per the flightboard's setup.
     */
    @Override
    public List<Integer> getStartingPositions() {
        return List.of(6, 3, 1, 0);
    }
}
