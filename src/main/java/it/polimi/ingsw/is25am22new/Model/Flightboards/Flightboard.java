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

    // integers in positions map are 0 <= x < 24
    public Flightboard(Game game, int flightBoardLength) {
        this.orderedRockets = new ArrayList<>();
        this.positions = new HashMap<>();
        this.flightBoardLength = flightBoardLength;
        this.game = game;
    }

    public Flightboard(List<String> orderedRockets, Map<String, Integer> positions, int flightBoardLength) {
        this.orderedRockets = orderedRockets;
        this.positions = positions;
        this.flightBoardLength = flightBoardLength;
    }

    public List<String> getOrderedRockets() {
        return orderedRockets;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    /**
     * Shifts the rocket identified by its nickname a specified number of steps
     * either backward or forward on the flightboard.
     *
     * The method adjusts the position of a rocket on the flightboard,
     * updating both the shipboard's "days on flight" and the positions map.
     * It ensures no overlap of positions, resolving conflicts based on the
     * provided number of steps. Positive steps move the rocket backward,
     * while negative steps move it forward.
     *
     * @param nickname The unique nickname of the rocket to be shifted.
     * @param stepsBackPositive The number of steps to move the rocket.
     *                          Positive values indicate movement backward,
     *                          and negative values indicate movement forward.
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

    private void reorderRockets(List<Shipboard> shipboards) {
        orderedRockets.sort((a, b) -> Integer.compare(
                shipboards.stream().filter(s -> s.getNickname().equals(b)).findFirst().map(Shipboard::getDaysOnFlight).orElse(0),
                shipboards.stream().filter(s -> s.getNickname().equals(a)).findFirst().map(Shipboard::getDaysOnFlight).orElse(0)
        ));
    }

    public abstract void placeRocket(String nickname, int pos);

    public int getFlightBoardLength(){
        return flightBoardLength;
    }

    private int mathematical_module(int n, int mod){    //I need the mathematical definition of the % operator, not the Java one
        return ((n % mod) + mod) % mod;
    }

    //returns a list of the indexes of the starting positions: List(0) is the first place, so List(0) = 6 for the level2 flightoboard
    public abstract List<Integer> getStartingPositions();
}