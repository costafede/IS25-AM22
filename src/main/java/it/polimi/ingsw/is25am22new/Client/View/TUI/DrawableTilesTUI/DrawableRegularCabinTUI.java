package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.RegularCabin;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

public class DrawableRegularCabinTUI extends DrawableComponentTileTUI {

    RegularCabin regularCabin;

    public DrawableRegularCabinTUI(RegularCabin regularCabin) {
        this.regularCabin = regularCabin;
    }

    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String alien;

        Side topSide = regularCabin.getTopSide();
        Side bottomSide = regularCabin.getBottomSide();
        Side leftSide = regularCabin.getLeftSide();
        Side rightSide = regularCabin.getRightSide();
        boolean purpleAlienPresent = regularCabin.isPurpleAlienPresent();
        boolean brownAlienPresent = regularCabin.isBrownAlienPresent();
        int numOfAstronauts = regularCabin.getNumOfAstronauts();

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

        if (purpleAlienPresent){alien = "P";}
        else if (brownAlienPresent){alien = "B";}
        else {alien = " ";}

        return new String[]{
                "   " + top + "   ",
                "   " + alien + "   ",
                left + "  RC " + right,
                "   " + numOfAstronauts + "   ",
                "   " + bottom + "   ",
        };
    }
}
