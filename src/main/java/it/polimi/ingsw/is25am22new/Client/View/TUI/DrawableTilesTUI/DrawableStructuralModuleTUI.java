package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StructuralModule;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * The DrawableStructuralModuleTUI class provides a textual representation for a StructuralModule
 * component tile. It extends the DrawableComponentTileTUI abstract class and is responsible for rendering
 * the StructuralModule's state in a textual 2D format for use in a terminal or console-based user interface.
 *
 * This class generates a 5-line textual representation of the StructuralModule. Each side of the module
 * (top, bottom, left, right) is represented by specific symbols based on its type:
 * - "S" for a smooth side.
 * - "1" for a side with a single pipe.
 * - "2" for a side with two pipes.
 * - "3" for a universal pipe.
 *
 * The center of the StructuralModule is marked with "SM" to represent the module itself.
 *
 * The draw method evaluates the type of each side and constructs the textual output accordingly.
 */
public class DrawableStructuralModuleTUI extends DrawableComponentTileTUI {

    StructuralModule structuralModule;

    public DrawableStructuralModuleTUI(StructuralModule structuralModule) {
        this.structuralModule = structuralModule;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

        Side topSide = structuralModule.getTopSide();
        Side bottomSide = structuralModule.getBottomSide();
        Side leftSide = structuralModule.getLeftSide();
        Side rightSide = structuralModule.getRightSide();

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
                left + "  SM " + right,
                "       ",
                "   " + bottom + "   ",
        };
    }
}
