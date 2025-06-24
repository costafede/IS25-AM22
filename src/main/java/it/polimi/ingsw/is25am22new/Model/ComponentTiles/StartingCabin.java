package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableStartingCabin;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableStartingCabinTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a specialized type of cabin in the game that serves as the starting point.
 * The StartingCabin sets predefined characteristics, including a default number of astronauts
 * and a color attribute that differentiates it from other cabins.
 * It inherits the base functionalities of the Cabin class.
 */
public class StartingCabin extends Cabin {

    private String color;
    /**
     * Constructs a {@code StartingCabin} tile with specified sides and color.
     * A StartingCabin always begins with two astronauts by default.
     *
     * @param pngName     file name of the tile's image
     * @param topSide     the {@link Side} on the top edge of the tile
     * @param bottomSide  the {@link Side} on the bottom edge of the tile
     * @param leftSide    the {@link Side} on the left edge of the tile
     * @param rightSide   the {@link Side} on the right edge of the tile
     * @param color       a string representing the color (or owner) of the cabin
     */
    public StartingCabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
        this.numOfAstronauts = 2;
    }

    /**
     * Returns the color assigned to this StartingCabin.
     *
     * @return the tile's color as a {@code String}
     */
    public String getColorTile() {
        return color;
    }

    /**
     * Indicates whether this tile is a starting cabin.
     * <p>
     * Always returns {@code true} for instances of this class.
     *
     * @return {@code true}, since this is a StartingCabin
     */
    public boolean isStartingCabin(){
        return true;
    }

    /**
     * Returns a string summary of the StartingCabin including its color,
     * number of astronauts, and the four sides.
     *
     * @return a formatted string representation of this cabin
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Astronaut's number: " + numOfAstronauts
                + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Returns the text-based drawable (TUI) representation of this StartingCabin.
     *
     * @return a {@link DrawableStartingCabinTUI} used for TUI rendering
     */
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableStartingCabinTUI(this);
    }

    /**
     * Returns the graphical drawable (GUI) representation of this StartingCabin.
     *
     * @return a {@link DrawableStartingCabin} used for GUI rendering
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableStartingCabin(this);
    }

}
