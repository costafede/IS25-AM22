package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Side;

import java.util.ArrayList;

public class SpecialStorageCompartment extends StorageCompartment {

    public SpecialStorageCompartment(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int capacity) {
        super(pngName, topSide, bottomSide, leftSide, rightSide, capacity);
    }

    //Check if the block is placeable in the tile
    public boolean isBlockPlaceable(GoodBlock gb) {
        return goodBlocks.size() < capacity;
    }

    //Add a block to the tile
    public void addBlockTile(GoodBlock gb) {
        if(goodBlocks.size() < capacity)
            goodBlocks.add(gb);
    }

    //Remove a block from the tile (Do we have to check if the block is red or not?)
    public void removeBlockTile(GoodBlock gb) {
        goodBlocks.remove(gb);
    }
}
