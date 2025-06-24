package it.polimi.ingsw.is25am22new.Model.Flightboards;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Flightboard class represents a base abstraction for the game flightboard that tracks the
 * positions of rockets and their ordered placement during the game.
 * It provides core functionality for managing the rockets' movement, placement, and ordering.
 * This class is intended to be extended by specific implementations of flightboards.
 */
public abstract class Flightboard implements Serializable {
    protected List<String> orderedRockets;
    protected Map<String, Integer> positions; // positions are relative to the flightboard
    protected int flightBoardLength;
    protected Game game;

    /**
     * Constructs a Flightboard with the given game and flight board length.
     * Initializes an empty list of ordered rockets and an empty positions map.
     *
     * @param game the game associated with the flight board
     * @param flightBoardLength the length of the flight board
     */
    // integers in positions map are 0 <= x < 24
    public Flightboard(Game game, int flightBoardLength) {
        this.orderedRockets = new ArrayList<>();
        this.positions = new HashMap<>();
        this.flightBoardLength = flightBoardLength;
        this.game = game;
    }

    /**
     * Constructs a Flightboard with the specified ordered list of rocket nicknames,
     * a map of rocket positions, and the length of the flightboard.
     *
     * @param orderedRockets The ordered list of rocket nicknames representing their sequence on the flightboard.
     * @param positions The map containing the positions of rockets, where the key is the rocket's nickname and the value is its position.
     * @param flightBoardLength The length of the flightboard, representing the total number of valid positions.
     */
    public Flightboard(List<String> orderedRockets, Map<String, Integer> positions, int flightBoardLength) {
        this.orderedRockets = orderedRockets;
        this.positions = positions;
        this.flightBoardLength = flightBoardLength;
    }

    /**
     * Retrieves the list of rockets ordered by their positions on the flightboard
     * in descending order. The ordering reflects the current state of the game,
     * where the rockets with the higher positions appear earlier in the list.
     *
     * @return A list of rocket nicknames ordered by their positions on the flightboard.
     */
    public List<String> getOrderedRockets() {
        return orderedRockets;
    }

    /**
     * Retrieves the current mapping of rocket nicknames to their respective positions
     * on the flightboard. The positions indicate the rockets' locations and are
     * represented as integer values.
     *
     * @return a map where the keys are rocket nicknames (String) and the values are
     * their corresponding positions (Integer) on the flightboard.
     */
    public Map<String, Integer> getPositions() {
        return positions;
    }

    /**
     * Shifts a rocket's position on the flightboard based on the specified number of steps.
     * The movement direction depends on the sign of the steps:
     * positive steps indicate a backward shift, and negative steps indicate a forward shift.
     *
     * @param nickname the unique identifier of the rocket to be shifted
     * @param stepsBackPositive the number of steps to shift the rocket; positive for a backward shift, negative for a forward shift
     */
    public void shiftRocket(String nickname, int stepsBackPositive) {
        // Does not manage who should shift first (usually the last one in a sequence)
        // Steps are positive if backward, negative if forward
        Map<String, Shipboard> shipboards = game.getShipboards();
        int finalPosition = shipboards.get(nickname).getDaysOnFlight(); //I initialize the final position with the beginning one

        for(int i = Math.abs(stepsBackPositive); i > 0; i--) {
            do finalPosition = stepsBackPositive > 0 ? finalPosition - 1 : finalPosition + 1;
            while (positions.containsValue(mathematical_module(finalPosition, flightBoardLength)) && (mathematical_module(finalPosition, flightBoardLength) != positions.get(nickname)));
        }
        shipboards.get(nickname).setDaysOnFlight(finalPosition);
        positions.put(nickname, mathematical_module(finalPosition, flightBoardLength));
        reorderRockets(shipboards.values().stream().toList());
    }

    /**
     * Reorders the rockets based on their days on flight in descending order.
     * The comparison is performed using the corresponding days on flight information
     * retrieved from the provided list of shipboards.
     *
     * @param shipboards the list of Shipboard objects containing the relevant data
     *                   for rockets, including their nickname and days on flight
     */
    private void reorderRockets(List<Shipboard> shipboards) {
        orderedRockets.sort((a, b) -> Integer.compare(
                shipboards.stream().filter(s -> s.getNickname().equals(b)).findFirst().map(Shipboard::getDaysOnFlight).orElse(0),
                shipboards.stream().filter(s -> s.getNickname().equals(a)).findFirst().map(Shipboard::getDaysOnFlight).orElse(0)
        ));
    }

    /**
     * Removes a rocket, identified by its unique nickname, from the flightboard.
     * The rocket is removed from both the ordered list of rockets and the
     * positions map that tracks their locations.
     *
     * @param nickname The unique identifier of the rocket to be removed.
     */
    public void removeRocket(String nickname) {
        orderedRockets.remove(nickname);
        positions.remove(nickname);
    }

    /**
     * Places a rocket identified by a unique nickname at a specific position
     * on the flightboard. The position is determined based on predefined rules
     * or game configuration logic. Updates the rocket's placement information
     * and adjusts the internal ordered list of rockets.
     *
     * @param nickname The unique identifier of the rocket to be placed.
     * @param pos      The designated position on the flightboard where the rocket
     *                 should be placed. The valid range for this parameter is
     *                 determined by the specific implementation of the flightboard.
     */
    public abstract void placeRocket(String nickname, int pos);

    /**
     * Retrieves the length of the flight board.
     *
     * @return the length of the flight board, which determines the range of positions
     *         available on the board for rocket placement.
     */
    public int getFlightBoardLength(){
        return flightBoardLength;
    }

    /**
     * Computes the mathematical module of two numbers. Unlike the default
     * `%` operator in programming languages, this method ensures that
     * the result is always non-negative, even if the dividend is negative.
     * Mathematically, this is represented as:
     *
     * result = ((n % mod) + mod) % mod
     *
     * Here, the returned result satisfies the condition:
     * 0 <= result < mod.
     *
     * @param n The dividend, which can be any integer.
     * @param mod The modulus, which must be a positive integer.
     * @return The non-negative remainder when n is divided by mod.
     *         The result will always be in the range [0, mod - 1].
     */
    private int mathematical_module(int n, int mod){    //I need the mathematical definition of the % operator, not the Java one
        return ((n % mod) + mod) % mod;
    }

    /**
     * Retrieves a list of starting positions represented as indexes on the flightboard.
     * The indexes are returned in descending order of priority, where the first element
     * corresponds to the highest priority position. For example, the first position in
     * the list is associated with the top placement on the flightboard.
     *
     * @return A list of integers representing the indexes of starting positions on
     *         the flightboard, ordered by their priority from highest to lowest.
     */
    //returns a list of the indexes of the starting positions: List(0) is the first place, so List(0) = 6 for the level2 flightoboard
    public abstract List<Integer> getStartingPositions();
}