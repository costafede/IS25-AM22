package it.polimi.ingsw.is25am22new.Model.Flightboards;

import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Flightboard {
    protected List<String> orderedRockets;
    protected Map<String, Integer> positions; // positions are relative to the flightboard
    protected int flightBoardLength;

    // integers in positions map are 0 <= x < 24
    public Flightboard(int flightBoardLength) {
        this.orderedRockets = new ArrayList<>();
        this.positions = new HashMap<>();
        this.flightBoardLength = flightBoardLength;
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

    public void shiftRocket(Map<String, Shipboard> shipboards, String nickname, int steps) {
        // steps are positive if backward, negative if forward
        int initialPosition = shipboards.get(nickname).getDaysOnFlight();
        int finalPosition = initialPosition - steps;
        int moreSteps = 0;
        for(Shipboard shipboard : shipboards.values()) {
            if(steps > 0) {
                if(shipboard.getDaysOnFlight() < initialPosition && shipboard.getDaysOnFlight() >= finalPosition) {
                    moreSteps--;
                }
            }
            else if(steps < 0) {
                if(shipboard.getDaysOnFlight() > initialPosition && shipboard.getDaysOnFlight() <= finalPosition) {
                    moreSteps++;
                }
            }
        }

        shipboards.get(nickname).setDaysOnFlight(finalPosition + moreSteps);
        if(finalPosition + moreSteps < 0) { // absolute position
            positions.put(nickname, flightBoardLength + ((finalPosition + moreSteps) % flightBoardLength));
            //alternatively ??
            //positions.put(nickname, (flightBoardLength + (finalPosition + moreSteps)) % flightBoardLength);
        }
        else {
            positions.put(nickname, (finalPosition + moreSteps) % flightBoardLength);
        }

        reoderRockets(shipboards.values().stream().toList());
    }

    private void reoderRockets(List<Shipboard> shipboards) {
        orderedRockets.sort((a, b) -> Integer.compare(
                shipboards.stream().filter(s -> s.getNickname().equals(b)).findFirst().map(Shipboard::getDaysOnFlight).orElse(0),
                shipboards.stream().filter(s -> s.getNickname().equals(a)).findFirst().map(Shipboard::getDaysOnFlight).orElse(0)
        ));
    }

    public abstract void placeRocket(String nickname, int pos);
}