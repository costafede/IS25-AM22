package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableBatteryComponent;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableComponentTile;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a specialized component tile in the form of a battery compartment.
 * A BatteryComponent can hold battery tokens, which are used to power certain actions or mechanisms.
 * This class extends the base functionality provided by ComponentTile, adding battery-specific behavior.
 */
public class BatteryComponent extends ComponentTile {
    private int numOfBatteries;

    public BatteryComponent(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int numOfBatteries) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.numOfBatteries = numOfBatteries;
    }

    public int getNumOfBatteries() {
        return numOfBatteries;
    }

    public void removeBatteryToken() {
        if(numOfBatteries <= 0)
            throw new IllegalArgumentException("Cannot remove batteries if there are none");
        numOfBatteries--;
    }

    public boolean isBattery() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Batteries number: " + numOfBatteries + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
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
                left + "  BC " + right,
                "   " + numOfBatteries + "   ",
                "   " + bottom + "   ",
        };
    }

    public DrawableComponentTile getDrawable() {
        return new DrawableBatteryComponent(this);
    }
}