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

    //Constructor -> Need to understand what to do with the parameter
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the value from the JSON file
    public AlienAddon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
    }

    public String getAddonColor() {
        return color;
    }

    public boolean isAlienAddon() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableAlienAddon();
    }

    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableAlienAddonTUI(this);
    }
}
