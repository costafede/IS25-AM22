package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

/**
 * Represents an abstract cabin component tile used in the game.
 * A cabin can hold astronauts, and its primary purpose is to manage
 * the number of astronauts available on it. This class is a building block
 * for implementing specific types of cabins in the game such as starting cabins
 * or regular cabins.
 *
 * Inherits properties and methods common to all component tiles from the
 * {@link ComponentTile} superclass, with added focus on functionalities
 * specific to cabins.
 *
 * The cabin features methods to add or remove crew members, set the number
 * of astronauts, and determine whether a tile is a cabin. It also provides
 * a functionality to render the cabin in string format for representation.
 * Additional features may be implemented in subclasses to enhance cabin behavior.
 */
public abstract class Cabin extends ComponentTile {

    public int getNumOfAstronauts() {
        return numOfAstronauts;
    }

    protected int numOfAstronauts;

    public Cabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        numOfAstronauts = 0;
    }

    public void removeCrewMember() {
        if(numOfAstronauts <= 0)
            throw new IllegalArgumentException("Cannot remove Crew member when there are none");
        numOfAstronauts--;
    }

    public void putAstronauts(){
        numOfAstronauts = 2;
    }

    public int getCrewNumber(){
        return numOfAstronauts;
    }

    public boolean isCabin() { return true;}

    public String[] draw(){
        return new String[]{
                "       ",
                "       ",
                "       ",
                "       ",
                "       "
        };
    }
}
