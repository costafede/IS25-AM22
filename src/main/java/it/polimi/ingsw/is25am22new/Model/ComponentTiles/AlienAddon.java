package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableAlienAddon;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableComponentTile;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;

/**
 * AlienAddon represents a specific type of component tile that includes additional functionality
 * for alien presence and specific coloring. It extends the {@code ComponentTile} class.
 * This class visually represents alien add-ons with specific styles based on predefined sides and color attributes.
 */
public class AlienAddon extends ComponentTile {
    private final String color;

    //Constructor -> Need to understand what to do with the parameter
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the value from the JSON file
    public AlienAddon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
    }

    public String getAddonColor() {
        return color;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String type = "";

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

        if (color.equals("brown")){type = "BAA";}
        else if (color.equals("purple")){type = "PAA";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + " " + type + " " + right,
                "       ",
                "   " + bottom + "   ",
        };
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableAlienAddon();
    }

}
