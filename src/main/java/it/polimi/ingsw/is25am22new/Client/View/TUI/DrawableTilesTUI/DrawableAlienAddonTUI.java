package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;


import it.polimi.ingsw.is25am22new.Model.ComponentTiles.AlienAddon;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents the textual user interface (TUI) drawable for an {@code AlienAddon} component tile.
 * This class extends {@code DrawableComponentTileTUI} and provides a visual representation of an
 * alien addon component in a text-based user interface context.
 * The tile's appearance depends on the sides of the component and its color.
 */
public class DrawableAlienAddonTUI extends DrawableComponentTileTUI {

    AlienAddon alienAddon;
    
    public DrawableAlienAddonTUI(AlienAddon alienAddon) {
        this.alienAddon = alienAddon;
    }
    
    @Override
    public String[] draw() {
        String top;
        String bottom;
        String left;
        String right;
        String type = "";

        if (alienAddon.getTopSide().equals(SMOOTH)){top = "S";}
        else if (alienAddon.getTopSide().equals(ONEPIPE)){top = "1";}
        else if (alienAddon.getTopSide().equals(TWOPIPES)){top = "2";}
        else {top = "3";}

        if (alienAddon.getBottomSide().equals(SMOOTH)){bottom = "S";}
        else if (alienAddon.getBottomSide().equals(ONEPIPE)){bottom = "1";}
        else if (alienAddon.getBottomSide().equals(TWOPIPES)){bottom = "2";}
        else {bottom = "3";}

        if (alienAddon.getLeftSide().equals(SMOOTH)){left = "S";}
        else if (alienAddon.getLeftSide().equals(ONEPIPE)){left = "1";}
        else if (alienAddon.getLeftSide().equals(TWOPIPES)){left = "2";}
        else {left = "3";}

        if (alienAddon.getRightSide().equals(SMOOTH)){right = "S";}
        else if (alienAddon.getRightSide().equals(ONEPIPE)){right = "1";}
        else if (alienAddon.getRightSide().equals(TWOPIPES)){right = "2";}
        else {right = "3";}

        if (alienAddon.getAddonColor().equals("brown")){type = "BAA";}
        else if (alienAddon.getAddonColor().equals("purple")){type = "PAA";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + " " + type + " " + right,
                "       ",
                "   " + bottom + "   ",
        };
    }
}
