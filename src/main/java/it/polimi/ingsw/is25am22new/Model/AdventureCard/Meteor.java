package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import java.io.Serializable;

/**
 * Represents a meteor object in the game.
 * The Meteor class models a meteor's size and the direction it is coming from,
 * encapsulated in the Orientation enum. A meteor can either be "BIG" or "SMALL"
 * and can originate from one of the four orientations: TOP, BOTTOM, LEFT, or RIGHT.
 * The class provides functionality for retrieving its size and orientation.
 */
public class Meteor implements Serializable {
    private final boolean big;
    private final Orientation orientation;

    public Meteor(boolean big, Orientation orientation) {
        this.big = big;
        this.orientation = orientation;
    }

    public boolean isBig() {
        return big;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        switch (orientation){
            case Orientation.TOP -> {
                return (big ? "BIG" : "SMALL") + " meteor coming from BOTTOM";
            }
            case Orientation.BOTTOM -> {
                return (big ? "BIG" : "SMALL") + " meteor coming from TOP";
            }
            case Orientation.LEFT -> {
                return (big ? "BIG" : "SMALL") + " meteor coming from RIGHT";
            }
            case Orientation.RIGHT -> {
                return (big ? "BIG" : "SMALL") + " meteor coming from LEFT";
            }
            default -> {
                return (big ? "BIG" : "SMALL") + " meteor default ERROR";
            }
        }
    }
}
