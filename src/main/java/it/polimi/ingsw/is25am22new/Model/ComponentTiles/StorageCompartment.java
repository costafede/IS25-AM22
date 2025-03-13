package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import java.util.List;

public class StorageCompartment extends ComponentTile{
    protected int capacity;
    protected List<GoodBlock> goodBlocks;

    public StorageCompartment() {
        super();
        this.capacity = capacity;
        this.goodBlocks = goodBlocks;
    }

    // Check if the block can be placed in the tile (Every block except the red block can be placed)
    @Override
    public boolean isBlockPlaceable(GoodBlock gb) {
        if(goodBlocks.size() < capacity && !gb.equals(GoodBlock.REDBLOCK))
            return true;
        else
            return false;
    }

    // Add a block to the tile (Do we have to check if the block is not red again?)
    public void addBlockTile(GoodBlock gb) {
        if(isBlockPlaceable(gb) && !gb.equals(GoodBlock.REDBLOCK))
            goodBlocks.add(gb);
    }

    //Remove a block from the tile (What do we do if the block is not in the tile?)
    public void removeBlockTile(GoodBlock gb) {
        if(goodBlocks.contains(gb))
            goodBlocks.remove(gb);
    }

    // Returns the list of good blocks in the tile
    public List<GoodBlock> getGoodBlocks() {
        return goodBlocks;
    }
}
