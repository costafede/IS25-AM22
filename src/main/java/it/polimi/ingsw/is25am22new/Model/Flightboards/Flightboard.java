package it.polimi.ingsw.is25am22new.Model.Flightboards;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Flightboard {
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
}