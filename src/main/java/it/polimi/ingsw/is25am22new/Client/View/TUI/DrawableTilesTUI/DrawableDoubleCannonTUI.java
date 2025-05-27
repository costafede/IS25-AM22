package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.DoubleCannon;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

public class DrawableDoubleCannonTUI extends DrawableComponentTileTUI {

    DoubleCannon doubleCannon;

    public DrawableDoubleCannonTUI(DoubleCannon doubleCannon) {
        this.doubleCannon = doubleCannon;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String active = "";

        Side topSide = doubleCannon.getTopSide();
        Side bottomSide = doubleCannon.getBottomSide();
        Side leftSide = doubleCannon.getLeftSide();
        Side rightSide = doubleCannon.getRightSide();
        boolean topSideCannon = doubleCannon.isTopSideCannon();
        boolean bottomSideCannon = doubleCannon.isBottomSideCannon();
        boolean rightSideCannon = doubleCannon.isRightSideCannon();
        boolean leftSideCannon = doubleCannon.isLeftSideCannon();

        if (topSide.equals(SMOOTH) && !topSideCannon){top = "S";}
        else if (topSide.equals(ONEPIPE) && !topSideCannon){top = "1";}
        else if (topSide.equals(TWOPIPES) && !topSideCannon){top = "2";}
        else if (topSide.equals(UNIVERSALPIPE) && !topSideCannon){top = "3";}
        else {top = "$";}

        if (bottomSide.equals(SMOOTH) && !bottomSideCannon){bottom = "S";}
        else if (bottomSide.equals(ONEPIPE) && !bottomSideCannon){bottom = "1";}
        else if (bottomSide.equals(TWOPIPES) && !bottomSideCannon){bottom = "2";}
        else if (bottomSide.equals(UNIVERSALPIPE) && !bottomSideCannon){bottom = "3";}
        else {bottom = "$";}

        if (leftSide.equals(SMOOTH) && !leftSideCannon){left = "S";}
        else if (leftSide.equals(ONEPIPE) && !leftSideCannon){left = "1";}
        else if (leftSide.equals(TWOPIPES) && !leftSideCannon){left = "2";}
        else if (leftSide.equals(UNIVERSALPIPE) && !leftSideCannon){left = "3";}
        else {left = "$";}

        if (rightSide.equals(SMOOTH) && !rightSideCannon){right = "S";}
        else if (rightSide.equals(ONEPIPE) && !rightSideCannon){right = "1";}
        else if (rightSide.equals(TWOPIPES) && !rightSideCannon){right = "2";}
        else if (rightSide.equals(UNIVERSALPIPE) && !rightSideCannon){right = "3";}
        else {right = "$";}

        if (doubleCannon.isActivated()) {active = "A";}
        else {active = " ";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  DC " + right,
                "   " + active + "   ",
                "   " + bottom + "   ",
        };
    }
}
