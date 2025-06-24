package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableStorageCompartment;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableStartingCabinTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableStorageCompartmentTUI;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a storage compartment component within a tile that can hold different types
 * of good blocks up to a specified capacity. This class extends the ComponentTile class
 * and includes functionality for managing the storage of good blocks.
 */
public class StorageCompartment extends ComponentTile{
    protected int capacity;
    protected Map<GoodBlock, Integer> goodBlocks;
    /**
     * Constructs a {@code StorageCompartment} tile with the given sides and capacity.
     *
     * Initializes the internal block storage with all block types (YELLOW, GREEN, RED, BLUE) set to 0.
     *
     * @param pngName     the name of the image file representing the tile
     * @param topSide     the {@link Side} on the top edge
     * @param bottomSide  the {@link Side} on the bottom edge
     * @param leftSide    the {@link Side} on the left edge
     * @param rightSide   the {@link Side} on the right edge
     * @param capacity    the maximum number of {@link GoodBlock}s that can be stored
     */
    public StorageCompartment(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int capacity) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.capacity = capacity;
        this.goodBlocks = new HashMap<>();

        this.goodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        this.goodBlocks.put(GoodBlock.GREENBLOCK, 0);
        this.goodBlocks.put(GoodBlock.REDBLOCK, 0);
        this.goodBlocks.put(GoodBlock.BLUEBLOCK, 0);
    }

    /**
     * Determines if a block can be placed in the compartment.
     *
     * A block can be placed if:
     *   The total number of blocks is less than the compartment's {@code capacity}
     *   The block is not a {@link GoodBlock#REDBLOCK}
     *
     * @param gb the block to check
     * @return {@code true} if the block can be placed; {@code false} otherwise
     */
    @Override
    public boolean isBlockPlaceable(GoodBlock gb) {
        int totalBlocks = 0;
        for (GoodBlock gb2 : goodBlocks.keySet()) {
            totalBlocks += goodBlocks.get(gb2);
        }
        return totalBlocks < capacity && !gb.equals(GoodBlock.REDBLOCK);
    }

    /**
     * Adds a {@link GoodBlock} to the storage compartment.
     *
     * @param gb the block to add
     * @throws IllegalArgumentException if the block is not placeable
     */
    public void addGoodBlock(GoodBlock gb) {
        if (!isBlockPlaceable(gb))
            throw new IllegalArgumentException("Block not placeable");
        goodBlocks.put(gb, goodBlocks.get(gb) + 1);
    }

    /**
     * Checks if the specified {@link GoodBlock} is currently stored in the compartment.
     *
     * @param gb the block to check
     * @return {@code true} if at least one block of that type is present; {@code false} otherwise
     */
    public boolean hasGoodBlock(GoodBlock gb) {
        return goodBlocks.get(gb) > 0;
    }

    /**
     * Removes one instance of the specified {@link GoodBlock} from the compartment.
     *
     * @param gb the block to remove
     * @return the removed {@link GoodBlock}
     * @throws IllegalStateException if no such block is present
     */
    public GoodBlock removeGoodBlock(GoodBlock gb) {
        if (goodBlocks.get(gb) > 0) {
            goodBlocks.put(gb, goodBlocks.get(gb) - 1);
        } else {
            throw new IllegalStateException("No blocks");
        }
        return gb;
    }

    /**
     * Indicates whether this tile is a storage compartment.
     * <p>
     * Always returns {@code true} for instances of this class.
     *
     * @return {@code true}
     */
    public boolean isStorageCompartment() {
        return true;
    }

    /**
     * Returns the map of stored blocks and their respective counts.
     *
     * @return a {@code Map} of {@link GoodBlock} to integer quantity
     */
    public Map<GoodBlock, Integer> getGoodBlocks() {
        return goodBlocks;
    }

    /**
     * Returns the maximum capacity of this storage compartment.
     *
     * @return the block capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of stored blocks of a specific type.
     *
     * @param gb the block type
     * @return the number of blocks of that type
     */
    public int getNumGoodBlocks(GoodBlock gb) {
        return goodBlocks.get(gb);
    }

    /**
     * Returns a string describing the storage compartment, including its capacity,
     * contents, and sides.
     *
     * @return a formatted string representation
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " Capacity: " + capacity + getGoodBlocks()
                + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Returns the text-based drawable representation for the tile.
     *
     * @return the TUI drawable for this storage compartment
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableStorageCompartmentTUI(this);
    }

    /**
     * Returns the graphical drawable representation for the tile.
     *
     * @return the GUI drawable for this storage compartment
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableStorageCompartment(this);
    }

}
