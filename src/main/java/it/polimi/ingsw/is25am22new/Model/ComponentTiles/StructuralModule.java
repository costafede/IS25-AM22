package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableStructuralModule;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableStructuralModuleTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * The StructuralModule class represents a specialized component tile in the game.
 * It extends the ComponentTile class and provides additional functionality specific
 * to structural modules in the game mechanics. StructuralModule does not implement
 * storage-related or active functionalities but focuses on structural connectivity.
 */
public class StructuralModule extends ComponentTile {
    /**
     * Constructs a {@code StructuralModule} tile with the specified sides.
     *
     * A StructuralModule is a neutral or passive tile that connects other modules
     * but typically doesn't store items or perform special behavior.
     *
     * @param pngName     the image filename representing this tile
     * @param topSide     the {@link Side} on the top edge of the tile
     * @param bottomSide  the {@link Side} on the bottom edge of the tile
     * @param leftSide    the {@link Side} on the left edge of the tile
     * @param rightSide   the {@link Side} on the right edge of the tile
     */
    public StructuralModule(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide){
        super(pngName, topSide, bottomSide, leftSide, rightSide);
    }

    /**
     * Returns a string representation of this StructuralModule,
     * including its class name and the configuration of its four sides.
     *
     * @return a formatted string describing the tile
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide
                + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Returns the text-based (TUI) drawable representation for this StructuralModule.
     *
     * @return a {@link DrawableStructuralModuleTUI} instance
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableStructuralModuleTUI(this);
    }

    /**
     * Returns the graphical (GUI) drawable representation for this StructuralModule.
     *
     * @return a {@link DrawableStructuralModule} instance
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableStructuralModule();
    }

}
