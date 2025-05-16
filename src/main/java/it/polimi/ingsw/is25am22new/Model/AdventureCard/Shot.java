package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import java.io.Serializable;

/**
 * Represents a shot fired in the game.
 * A shot can vary in size ("BIG" or "SMALL") and originates from one of
 * the four possible orientations, as defined in the Orientation enum.
 * This class models the behavior of a shot and provides methods to retrieve
 * its properties such as size and orientation. It also includes a textual
 * representation of the shot's details using its orientation and size.
 */
public class Shot implements Serializable {
    private final boolean big;
    private final Orientation orientation;

    public Shot(boolean big, Orientation orientation) {
        this.big = big;
        this.orientation = orientation;
    }

    public boolean isBig() { return big; }

    public Orientation getOrientation() { return orientation; }

    @Override
    public String toString() {
        switch (orientation){
            case Orientation.TOP -> {
                return (big ? "BIG" : "SMALL") + " shot coming from BOTTOM";
            }
            case Orientation.BOTTOM -> {
                return (big ? "BIG" : "SMALL") + " shot coming from TOP";
            }
            case Orientation.LEFT -> {
                return (big ? "BIG" : "SMALL") + " shot coming from RIGHT";
            }
            case Orientation.RIGHT -> {
                return (big ? "BIG" : "SMALL") + " shot coming from LEFT";
            }
            default -> {
                return (big ? "BIG" : "SMALL") + " shot default ERROR";
            }
        }
    }
}
