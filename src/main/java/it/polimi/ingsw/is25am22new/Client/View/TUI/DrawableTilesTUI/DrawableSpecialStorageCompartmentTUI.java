package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.SpecialStorageCompartment;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.util.Map;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

public class DrawableSpecialStorageCompartmentTUI extends DrawableComponentTileTUI {

    SpecialStorageCompartment specialStorageCompartment;

    public DrawableSpecialStorageCompartmentTUI(SpecialStorageCompartment specialStorageCompartment) {
        this.specialStorageCompartment = specialStorageCompartment;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

        Side topSide = specialStorageCompartment.getTopSide();
        Side bottomSide = specialStorageCompartment.getBottomSide();
        Side leftSide = specialStorageCompartment.getLeftSide();
        Side rightSide = specialStorageCompartment.getRightSide();
        int capacity = specialStorageCompartment.getCapacity();

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

        Map<GoodBlock, Integer> goodBlocks = specialStorageCompartment.getGoodBlocks();

        int redBlock = goodBlocks.getOrDefault(GoodBlock.REDBLOCK, 0);
        int yellowBlock = goodBlocks.getOrDefault(GoodBlock.YELLOWBLOCK, 0);
        int blueBlock = goodBlocks.getOrDefault(GoodBlock.BLUEBLOCK, 0);
        int greenBlock = goodBlocks.getOrDefault(GoodBlock.GREENBLOCK, 0);

        return new String[]{
                "   " + top + "   ",
                " R" + redBlock + " Y" + yellowBlock + " ",
                left + " SS" + capacity + " " + right,
                " G" + greenBlock + " B" + blueBlock + " ",
                "   " + bottom + "   ",
        };
    }
}
