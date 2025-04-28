package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

public class SpecialStorageCompartment extends StorageCompartment {

    public SpecialStorageCompartment(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int capacity) {
        super(pngName, topSide, bottomSide, leftSide, rightSide, capacity);
    }

    //Check if the block is placeable in the tile
    public boolean isBlockPlaceable(GoodBlock gb) {
        int totalBlocks = 0;
        for (GoodBlock gb2 : goodBlocks.keySet()) {
            totalBlocks += goodBlocks.get(gb2);
        }
        return totalBlocks < capacity;
    }

    //Add a block to the tile
    public void addGoodBlock(GoodBlock gb) {
        if(!isBlockPlaceable(gb))
            throw new IllegalArgumentException("Block not placeable");
        goodBlocks.put(gb, goodBlocks.get(gb) + 1);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Capacity: " + capacity + getGoodBlocks()
                + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    @Override
    public String[] draw(){
        return new String[]{
                " ",
                " "
        };
    }
}
