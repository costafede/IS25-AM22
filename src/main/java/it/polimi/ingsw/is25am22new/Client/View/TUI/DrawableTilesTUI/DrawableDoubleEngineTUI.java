package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.DoubleEngine;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

public class DrawableDoubleEngineTUI extends DrawableComponentTileTUI {

    DoubleEngine doubleEngine;

    public DrawableDoubleEngineTUI(DoubleEngine doubleEngine) {
        this.doubleEngine = doubleEngine;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String active = "";

        Side topSide = doubleEngine.getTopSide();
        Side bottomSide = doubleEngine.getBottomSide();
        Side leftSide = doubleEngine.getLeftSide();
        Side rightSide = doubleEngine.getRightSide();
        boolean topSideEngine = doubleEngine.isTopSideEngine();
        boolean bottomSideEngine = doubleEngine.isBottomSideEngine();
        boolean leftSideEngine = doubleEngine.isLeftSideEngine();
        boolean rightSideEngine = doubleEngine.isRightSideEngine();

        if (topSide.equals(SMOOTH) && !topSideEngine){top = "S";}
        else if (topSide.equals(ONEPIPE) && !topSideEngine){top = "1";}
        else if (topSide.equals(TWOPIPES) && !topSideEngine){top = "2";}
        else if (topSide.equals(UNIVERSALPIPE) && !topSideEngine){top = "3";}
        else {top = "#";}

        if (bottomSide.equals(SMOOTH) && !bottomSideEngine){bottom = "S";}
        else if (bottomSide.equals(ONEPIPE) && !bottomSideEngine){bottom = "1";}
        else if (bottomSide.equals(TWOPIPES) && !bottomSideEngine){bottom = "2";}
        else if (bottomSide.equals(UNIVERSALPIPE) && !bottomSideEngine){bottom = "3";}
        else {bottom = "#";}

        if (leftSide.equals(SMOOTH) && !leftSideEngine){left = "S";}
        else if (leftSide.equals(ONEPIPE) && !leftSideEngine){left = "1";}
        else if (leftSide.equals(TWOPIPES) && !leftSideEngine){left = "2";}
        else if (leftSide.equals(UNIVERSALPIPE) && !leftSideEngine){left = "3";}
        else {left = "#";}

        if (rightSide.equals(SMOOTH) && !rightSideEngine){right = "S";}
        else if (rightSide.equals(ONEPIPE) && !rightSideEngine){right = "1";}
        else if (rightSide.equals(TWOPIPES) && !rightSideEngine){right = "2";}
        else if (rightSide.equals(UNIVERSALPIPE) && !rightSideEngine){right = "3";}
        else {right = "#";}

        if (doubleEngine.isActivated()) {active = "A";}
        else {active = " ";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  DE " + right,
                "   " + active + "   ",
                "   " + bottom + "   ",
        };
    }
}
