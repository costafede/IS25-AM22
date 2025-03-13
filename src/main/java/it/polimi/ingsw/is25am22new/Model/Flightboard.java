package it.polimi.ingsw.is25am22new.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Flightboard {
    private List<String> orderedRockets;
    private Map<String, Integer> positions;

    public Flightboard() {
        this.orderedRockets = new ArrayList<>();
        this.positions = new HashMap<>();
    }

    public List<String> getOrderedRockets() {
        return orderedRockets;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public void setOrderedRockets(String nickname) {
        this.orderedRockets.add(nickname);
        this.setPositions(nickname);
    }

    private void setPositions(String nickname) {
        if (this.orderedRockets.isEmpty()) {

        }
    }

    public abstract int shiftRocket(String nickname, int steps); //return the new position (daysOnFlight in ShipBoard
    public abstract void placeRocket(String nickname, int position);
    public abstract boolean reoderRockets();
}