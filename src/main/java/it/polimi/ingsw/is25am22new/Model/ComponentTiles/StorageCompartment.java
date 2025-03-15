package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Side;

import java.util.ArrayList;
import java.util.List;

public class StorageCompartment extends ComponentTile{
    protected int capacity;
    protected List<GoodBlock> goodBlocks;

    public StorageCompartment(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int capacity) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.capacity = capacity;
        this.goodBlocks = new ArrayList<GoodBlock>();
    }

    // Check if the block can be placed in the tile (Every block except the red block can be placed)
    @Override
    public boolean isBlockPlaceable(GoodBlock gb) {
        return goodBlocks.size() < capacity && !gb.equals(GoodBlock.REDBLOCK);
    }

    // Add a block to the tile (Do we have to check if the block is not red again?)
    public void addGoodBlock(GoodBlock gb) {
        if(isBlockPlaceable(gb))
            goodBlocks.add(gb);
    }

    public boolean hasGoodBlock(GoodBlock gb) {
        return goodBlocks.contains(gb);
    }

    //Remove a block from the tile (What do we do if the block is not in the tile?)
    public GoodBlock removeGoodBlock(GoodBlock gb) {
        GoodBlock tmp = gb;
        goodBlocks.remove(gb);
        return tmp;
    }

    // Returns the list of good blocks in the tile
    public List<GoodBlock> getGoodBlocks() {
        return goodBlocks;
    }

    public int getCapacity() {
        return capacity;
    }
}
