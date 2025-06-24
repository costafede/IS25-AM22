package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableSpecialStorageCompartment;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableShieldGeneratorTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableSpecialStorageCompartmentTUI;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.Map;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a special type of storage compartment within a component tile.
 * This class extends the functionality of the StorageCompartment class and provides
 * additional implementations for handling the storage and representation of good blocks.
 */
public class SpecialStorageCompartment extends StorageCompartment {
    /**
     * Creates a special storage compartment tile with the given sprite and sides.
     *
     * @param pngName     file name (without path) of the PNG sprite representing this tile
     * @param topSide     the {@link Side} on the top edge of the tile
     * @param bottomSide  the {@link Side} on the bottom edge of the tile
     * @param leftSide    the {@link Side} on the left edge of the tile
     * @param rightSide   the {@link Side} on the right edge of the tile
     * @param capacity    maximum number of {@link GoodBlock}s the compartment can hold
     */
    public SpecialStorageCompartment(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int capacity) {
        super(pngName, topSide, bottomSide, leftSide, rightSide, capacity);
    }

    /**
     * Determines whether a new {@link GoodBlock} can be placed in this compartment.
     * <p>
     * The compartment is considered <em>placeable</em> when the current number of blocks
     * stored is strictly less than its declared {@code capacity}.
     *
     * @param gb the block the caller wants to place (ignored for the test itself,
     *           but retained for future extensibility such as filtering per type)
     *
     * @return {@code true} if there is at least one free slot available;
     *         {@code false} when the compartment is already full
     */
    public boolean isBlockPlaceable(GoodBlock gb) {
        int totalBlocks = 0;
        for (GoodBlock gb2 : goodBlocks.keySet()) {
            totalBlocks += goodBlocks.get(gb2);
        }
        return totalBlocks < capacity;
    }

    /**
     * Adds the given {@link GoodBlock} to the compartment.
     *
     * @param gb the block to add
     *
     * @throws IllegalArgumentException if the compartment has reached
     *                                  its {@code capacity} and the block
     *                                  therefore cannot be placed
     */
    public void addGoodBlock(GoodBlock gb) {
        if(!isBlockPlaceable(gb))
            throw new IllegalArgumentException("Block not placeable");
        goodBlocks.put(gb, goodBlocks.get(gb) + 1);
    }

    /**
     * Returns a human-readable summary of this compartment, including its capacity,
     * the contents map, and the four sides.
     *
     * @return a concise string describing this instance
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " Capacity: " + capacity + getGoodBlocks()
                + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Builds the text-user-interface (TUI) drawable representation for this tile.
     *
     * @return a {@link DrawableComponentTileTUI} that knows how to paint the TUI view
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableSpecialStorageCompartmentTUI(this);
    }

    /**
     * Builds the graphical drawable representation for this tile (e.g., JavaFX, Swing, etc.).
     *
     * @return a {@link DrawableComponentTile} for rendering in the GUI
     */
    public DrawableComponentTile getDrawable(){
        return new DrawableSpecialStorageCompartment(this);
    }

}
