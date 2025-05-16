package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a specialized type of cabin in the game that serves as the starting point.
 * The StartingCabin sets predefined characteristics, including a default number of astronauts
 * and a color attribute that differentiates it from other cabins.
 * It inherits the base functionalities of the Cabin class.
 */
public class StartingCabin extends Cabin {

    private String color;

    public StartingCabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
        this.numOfAstronauts = 2;
    }

    public String getColorTile() {
        return color;
    }

    public boolean isStartingCabin(){
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Astronaut's number: " + numOfAstronauts + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

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
