package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StartingCabin;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

public class DrawableStartingCabinTUI extends DrawableComponentTileTUI {

    StartingCabin startingCabin;

    public DrawableStartingCabinTUI(StartingCabin startingCabin) {
        this.startingCabin = startingCabin;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

        Side topSide = startingCabin.getTopSide();
        Side bottomSide = startingCabin.getBottomSide();
        Side leftSide = startingCabin.getLeftSide();
        Side rightSide = startingCabin.getRightSide();
        int numOfAstronauts = startingCabin.getNumOfAstronauts();

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

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  SC " + right,
                "   " + numOfAstronauts + "   ",
                "   " + bottom + "   ",
        };
    }
}
