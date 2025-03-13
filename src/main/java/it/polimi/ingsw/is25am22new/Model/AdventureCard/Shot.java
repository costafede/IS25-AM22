package it.polimi.ingsw.is25am22new.Model.AdventureCard;

public class Shot {
    private boolean big;
    private Orientation orientation;

    public Shot(boolean big, Orientation orientation) {
        this.big = big;
        this.orientation = orientation;
    }

    public boolean isBig() { return big; }

    public Orientation getOrientation() { return orientation; }
}
