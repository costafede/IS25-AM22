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

    /**
     * Retrieves the current number of astronauts in the cabin.
     *
     * @return the number of astronauts currently present in the cabin
     */
    public int getNumOfAstronauts() {
        return numOfAstronauts;
    }

    /**
     * Represents the number of astronauts currently present in the cabin.
     * This variable is used to track and manage the cabin's crew capacity.
     * It can be modified through various operations such as adding or removing crew members.
     */
    protected int numOfAstronauts;

    /**
     * Constructor for the Cabin class, which extends ComponentTile. Initializes a cabin
     * with the given parameters. The cabin manages its appearance and structural sides,
     * and it specifically initializes the number of astronauts to zero.
     *
     * @param pngName the name of the image file that visually represents the cabin.
     * @param topSide the type of the top side of the cabin.
     * @param bottomSide the type of the bottom side of the cabin.
     * @param leftSide the type of the left side of the cabin.
     * @param rightSide the type of the right side of the cabin.
     */
    public Cabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        numOfAstronauts = 0;
    }

    /**
     * Removes one crew member from the cabin.
     * <p>
     * Decreases the number of astronauts present in the cabin by 1.
     * If there are no crew members to remove (i.e., the number of astronauts is 0 or less),
     * an IllegalArgumentException is thrown.
     *
     * @throws IllegalArgumentException if there are no crew members in the cabin to remove
     */
    public void removeCrewMember() {
        if(numOfAstronauts <= 0)
            throw new IllegalArgumentException("Cannot remove Crew member when there are none");
        numOfAstronauts--;
    }

    /**
     * Sets the number of astronauts in the cabin to two.
     * This method is used to initialize or reset the number of astronauts
     * the cabin can hold to a predefined value of 2.
     */
    public void putAstronauts(){
        numOfAstronauts = 2;
    }

    /**
     * Retrieves the current number of astronauts in the cabin.
     *
     * @return the number of astronauts currently present in the cabin
     */
    public int getCrewNumber(){
        return numOfAstronauts;
    }

    /**
     * Determines if the current component tile is a cabin.
     *
     * @return true, as this represents a cabin component tile.
     */
    public boolean isCabin() { return true;}

    /**
     * Provides a visual representation of the cabin component as an array of strings.
     * Each string in the array represents a line of the cabin's visual structure.
     *
     * @return an array of strings representing the visual appearance of the cabin.
     */
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
