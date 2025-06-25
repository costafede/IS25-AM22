package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableRegularCabin;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableRegularCabinTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * The RegularCabin class is a specific type of cabin that extends the functionality
 * of the Cabin superclass to include support for alien presence management. It allows
 * tracking and handling of different types of aliens (purple and brown) alongside the
 * existing astronaut crew members. This class implements methods for adding, removing,
 * and querying the presence of aliens, as well as managing and rendering the cabin's
 * state.
 */
public class RegularCabin extends Cabin {

    private boolean purpleAlienPresent;
    private boolean brownAlienPresent;

    /**
     * Constructs a RegularCabin, initializing its graphical representation and structural sides,
     * and setting the purple and brown alien presence to false by default.
     *
     * @param pngName the name of the image file representing the visual appearance of the cabin
     * @param topSide the type of the top side of the cabin
     * @param bottomSide the type of the bottom side of the cabin
     * @param leftSide the type of the left side of the cabin
     * @param rightSide the type of the right side of the cabin
     */
    public RegularCabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        purpleAlienPresent = false;
        brownAlienPresent = false;
    }

    /**
     * Adds an alien of the specified type to the cabin.
     *
     * This method places an alien into the cabin if no other alien
     * is currently present. It supports the addition of aliens of type
     * "brown" or "purple". If an alien is already present in the cabin,
     * an exception is thrown.
     *
     * @param color the type of alien to be added; valid values are "brown" and "purple"
     * @throws IllegalArgumentException if an alien is already present in the cabin
     *                                  or if the input color is invalid
     */
    @Override
    public void putAlien(String color){
        if(brownAlienPresent || purpleAlienPresent)
            throw new IllegalArgumentException("Cannot place an alien where another one is already present");
        if(color.equals("brown")){
            brownAlienPresent = true;
        }
        else if(color.equals("purple")){
            purpleAlienPresent = true;
        }
    }

    /**
     * Checks if an alien of the specified color is present in the cabin.
     * This method supports querying for "brown" and "purple" aliens.
     *
     * @param color the color of the alien to check for; valid values are "brown" and "purple"
     * @return true if an alien of the specified color is present, false otherwise
     */
    @Override
    public boolean isAlienPresent(String color){
        if(color.equals("brown")){
            return brownAlienPresent;
        }
        if(color.equals("purple")){
            return purpleAlienPresent;
        }
        return false;
    }

    /**
     * Calculates the number of crew members currently present in the cabin.
     * If the cabin contains a purple or brown alien, the crew number is restricted
     * to 1. Otherwise, it returns the number of astronauts present in the cabin.
     *
     * @return the number of crew members in the cabin. If a purple or brown alien is
     *         present, the return value is 1. Otherwise, it returns the total number
     *         of astronauts.
     */
    public int getCrewNumber(){
        if(isAlienPresent("purple") || isAlienPresent("brown")){
            return 1;
        }
        else return numOfAstronauts;
    }


    /**
     * Removes a crew member from the current cabin.
     *
     * This method is responsible for managing the removal of either an alien
     * (if one is present) or an astronaut from the cabin:
     * - If a purple alien is present, it is removed.
     * - If no purple alien is present but a brown alien is present, the brown alien is removed.
     * - If neither aliens are present, the number of astronauts in the cabin is decremented.
     */
    public void removeCrewMember(){
        if(isAlienPresent("purple")){
            purpleAlienPresent = false;

        } else if(isAlienPresent("brown")){
            brownAlienPresent = false;
        }
        else
            numOfAstronauts--;
    }

    /**
     * Checks if a purple alien is present in the cabin.
     *
     * @return true if a purple alien is currently present in the cabin, false otherwise
     */
    public boolean isPurpleAlienPresent() {
        return purpleAlienPresent;
    }

    /**
     * Checks whether a brown alien is currently present in the cabin.
     *
     * @return true if a brown alien is present, false otherwise
     */
    public boolean isBrownAlienPresent() {
        return brownAlienPresent;
    }

    /**
     * Returns a string representation of the RegularCabin object.
     * The representation includes the class name, the number of astronauts,
     * and the presence of purple and brown aliens. Additionally, it includes
     * descriptive details about the component's sides (top, bottom, left, and right).
     *
     * @return a string that summarizes the state of the RegularCabin object.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " Astronaut's number: " + numOfAstronauts + " Purple alien: " +
                purpleAlienPresent + " Brown alien: " + brownAlienPresent + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Returns the drawable text-based UI (TUI) representation of the RegularCabin.
     * The returned object is an instance of DrawableRegularCabinTUI, which generates
     * a textual visualization of the cabin's state, including alien presence,
     * number of astronauts, and side configurations.
     *
     * @return an instance of DrawableRegularCabinTUI representing the RegularCabin's textual UI.
     */
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableRegularCabinTUI(this);
    }

    /**
     * Retrieves the drawable representation of the current cabin.
     *
     * @return a DrawableComponentTile object representing the visual state of this RegularCabin.
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableRegularCabin(this);
    }
}
