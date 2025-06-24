package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Cannon;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

/**
 * The DrawableCannonTUI class provides a textual representation for a Cannon component tile.
 * It extends the DrawableComponentTileTUI abstract class and is responsible for rendering the
 * visual layout of the Cannon's state in a textual 2D format for use in a terminal or console-based
 * user interface.
 *
 * Each of the four sides of the Cannon (top, bottom, left, right) is represented with
 * specific symbols based on their type and whether they are configured as active cannon barrels:
 * - "S" for a smooth side.
 * - "1" for a side with one pipe.
 * - "2" for a side with two pipes.
 * - "3" for a universal pipe.
 * - "$" for an active cannon barrel on that side.
 *
 * The draw method outputs the textual representation with the center of the Cannon marked as "C".
 */
public class DrawableCannonTUI extends DrawableComponentTileTUI {

    Cannon cannon;

    public DrawableCannonTUI(Cannon cannon) {
        this.cannon = cannon;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

        Side topSide = cannon.getTopSide();
        Side bottomSide = cannon.getBottomSide();
        Side leftSide = cannon.getLeftSide();
        Side rightSide = cannon.getRightSide();
        boolean topSideCannon = cannon.isTopSideCannon();
        boolean bottomSideCannon = cannon.isBottomSideCannon();
        boolean leftSideCannon = cannon.isLeftSideCannon();
        boolean rightSideCannon = cannon.isRightSideCannon();


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

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  C  " + right,
                "       ",
                "   " + bottom + "   ",
        };
    }
}
