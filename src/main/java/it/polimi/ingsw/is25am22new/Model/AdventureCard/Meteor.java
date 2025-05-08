package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import java.io.Serializable;

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
                return (big ? "Big" : "Small") + " meteor coming from BOTTOM";
            }
            case Orientation.BOTTOM -> {
                return (big ? "Big" : "Small") + " meteor coming from TOP";
            }
            case Orientation.LEFT -> {
                return (big ? "Big" : "Small") + " meteor coming from RIGHT";
            }
            case Orientation.RIGHT -> {
                return (big ? "Big" : "Small") + " meteor coming from LEFT";
            }
            default -> {
                return (big ? "Big" : "Small") + " meteor default ERROR";
            }
        }
    }
}
