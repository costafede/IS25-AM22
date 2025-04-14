package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import java.io.Serializable;

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
        return (big ? "Big" : "Small") + " shot facing " + orientation;
    }
}
