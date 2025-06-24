package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableAlienAddon;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableAlienAddonTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;

/**
 * AlienAddon represents a specific type of component tile that includes additional functionality
 * for alien presence and specific coloring. It extends the {@code ComponentTile} class.
 * This class visually represents alien add-ons with specific styles based on predefined sides and color attributes.
 */
public class AlienAddon extends ComponentTile {
    private final String color;

    /**
     * Constructs an AlienAddon object, which is a specific type of component tile
     * representing an alien-themed add-on with configurable sides and a color attribute.
     * This class extends {@code ComponentTile} and provides additional functionality related
     * to alien-specific properties.
     *
     * @param pngName the name of the PNG file representing the visual appearance of the tile.
     * @param topSide the configuration of the top side of the tile, represented as a {@code Side}.
     * @param bottomSide the configuration of the bottom side of the tile, represented as a {@code Side}.
     * @param leftSide the configuration of the left side of the tile, represented as a {@code Side}.
     * @param rightSide the configuration of the right side of the tile, represented as a {@code Side}.
     * @param color a {@code String} representing the color attribute of the alien add-on.
     */
    //Constructor -> Need to understand what to do with the parameter
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the value from the JSON file
    public AlienAddon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
    }

    /**
     * Retrieves the color associated with the alien add-on.
     *
     * @return a string representing the color of the alien add-on.
     */
    public String getAddonColor() {
        return color;
    }

    /**
     * Determines whether the current component tile is categorized as an alien addon.
     *
     * @return true if this component tile is an alien addon, false otherwise
     */
    public boolean isAlienAddon() {
        return true;
    }

    /**
     * Returns a string representation of the AlienAddon object.
     * The string includes the class name, the color of the addon,
     * and the descriptions of all four sides (top, bottom, left, right).
     *
     * @return a string representing the AlienAddon, including its color and side configurations.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Retrieves a drawable representation of the current component tile.
     *
     * @return An instance of DrawableAlienAddon, which is a specific drawable representation
     *         for the alien-related add-on component tile.
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableAlienAddon();
    }

    /**
     * Retrieves the textual user interface (TUI) drawable associated with the {@code AlienAddon} component tile.
     *
     * This method returns a new instance of a {@code DrawableAlienAddonTUI}, which provides a textual representation
     * of the current {@code AlienAddon} instance, including its sides and color properties.
     *
     * @return a {@code DrawableComponentTileTUI} object that visually represents the {@code AlienAddon} component in a text-based interface.
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableAlienAddonTUI(this);
    }
}
