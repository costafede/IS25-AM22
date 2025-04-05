package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.HashMap;
import java.util.Map;

public class StorageCompartment extends ComponentTile{
    protected int capacity;
    protected Map<GoodBlock, Integer> goodBlocks;

    public StorageCompartment(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int capacity) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.capacity = capacity;
        this.goodBlocks = new HashMap<>();

        this.goodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        this.goodBlocks.put(GoodBlock.GREENBLOCK, 0);
        this.goodBlocks.put(GoodBlock.REDBLOCK, 0);
        this.goodBlocks.put(GoodBlock.BLUEBLOCK, 0);
    }

    // Check if the block can be placed in the tile (Every block except the red block can be placed)
    @Override
    public boolean isBlockPlaceable(GoodBlock gb) {
        int totalBlocks = 0;
        for (GoodBlock gb2 : goodBlocks.keySet()) {
            totalBlocks += goodBlocks.get(gb2);
        }
        return totalBlocks < capacity && !gb.equals(GoodBlock.REDBLOCK);
    }

    public void addGoodBlock(GoodBlock gb) {
        if(!isBlockPlaceable(gb))
            throw new IllegalArgumentException("Block not placeable");
        goodBlocks.put(gb, goodBlocks.get(gb) + 1);
    }

    public boolean hasGoodBlock(GoodBlock gb) {
        return goodBlocks.get(gb) > 0;
    }

    public GoodBlock removeGoodBlock(GoodBlock gb) {
        if(goodBlocks.get(gb) > 0) {
            goodBlocks.put(gb, goodBlocks.get(gb) - 1);
        }
        else {
            throw new IllegalStateException("No blocks");
        }
        return gb;
    }

    public boolean isStorageCompartment() {
        return true;
    }

    // Returns the list of good blocks in the tile
    public Map<GoodBlock, Integer> getGoodBlocks() {
        return goodBlocks;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumGoodBlocks(GoodBlock gb) {
        return goodBlocks.get(gb);
    }
}
