package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Capacity: " + capacity + getGoodBlocks()
                + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

        if (topSide.equals(SMOOTH)){top = "S";}
        else if (topSide.equals(ONEPIPE)){top = "1";}
        else if (topSide.equals(TWOPIPES)){top = "2";}
        else {top = "3";}

        if (bottomSide.equals(SMOOTH)){bottom = "S";}
        else if (bottomSide.equals(ONEPIPE)){bottom = "1";}
        else if (bottomSide.equals(TWOPIPES)){bottom = "2";}
        else {bottom = "3";}

        if (leftSide.equals(SMOOTH)){left = "S";}
        else if (leftSide.equals(ONEPIPE)){left = "1";}
        else if (leftSide.equals(TWOPIPES)){left = "2";}
        else {left = "3";}

        if (rightSide.equals(SMOOTH)){right = "S";}
        else if (rightSide.equals(ONEPIPE)){right = "1";}
        else if (rightSide.equals(TWOPIPES)){right = "2";}
        else {right = "3";}

        Map<GoodBlock, Integer> goodBlocks = getGoodBlocks();

        int redBlock = goodBlocks.getOrDefault(GoodBlock.REDBLOCK, 0);
        int yellowBlock = goodBlocks.getOrDefault(GoodBlock.YELLOWBLOCK, 0);
        int blueBlock = goodBlocks.getOrDefault(GoodBlock.BLUEBLOCK, 0);
        int greenBlock = goodBlocks.getOrDefault(GoodBlock.GREENBLOCK, 0);

        return new String[]{
                "   " + top + "   ",
                " R" + redBlock + " Y" + yellowBlock + " ",
                left + "  S" + capacity + " " + right,
                " G" + greenBlock + " B" + blueBlock + " ",
                "   " + bottom + "   ",
        };
    }
}
