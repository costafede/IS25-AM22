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

}
