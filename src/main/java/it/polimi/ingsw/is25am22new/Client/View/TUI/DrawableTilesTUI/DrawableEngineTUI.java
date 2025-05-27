package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Engine;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

public class DrawableEngineTUI extends DrawableComponentTileTUI {

    Engine engine;

    public DrawableEngineTUI(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

        Side topSide = engine.getTopSide();
        Side bottomSide = engine.getBottomSide();
        Side leftSide = engine.getLeftSide();
        Side rightSide = engine.getRightSide();
        boolean topSideEngine = engine.isTopSideEngine();
        boolean bottomSideEngine = engine.isBottomSideEngine();
        boolean leftSideEngine = engine.isLeftSideEngine();
        boolean rightSideEngine = engine.isRightSideEngine();

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

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  E  " + right,
                "       ",
                "   " + bottom + "   ",
        };
    }
}
