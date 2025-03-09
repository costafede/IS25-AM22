package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;

public class SpecialStorageCompartment extends StorageCompartment {

    public SpecialStorageCompartment() {
        super();
    }

    //Check if the block is placeable in the tile (Only red blocks can be placed)
    //So it does the same as the parent class, but it checks if the block is a red block
    public boolean isBlockPlaceableTile(GoodBlock gb) {
        return (super.isBlockPlaceableTile(gb) && gb.equals(GoodBlock.REDBLOCK));
    }

    //Add a block to the tile (Only red blocks can be placed)
    //So it does the same as the parent class, but it checks if the block is a red block
    public void addBlockTile(GoodBlock gb) {
        if(isBlockPlaceableTile(gb) && gb.equals(GoodBlock.REDBLOCK))
            goodBlocks.add(gb);
    }

    //Remove a block from the tile (Do we have to check if the block is red or not?)
    //Cause containing gb means it is a red block
    public void removeBlockTile(GoodBlock gb) {
        if(goodBlocks.contains(gb) && gb.equals(GoodBlock.REDBLOCK))
            goodBlocks.remove(gb);
    }
}
