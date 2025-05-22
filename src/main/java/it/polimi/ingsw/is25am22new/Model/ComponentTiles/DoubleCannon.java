package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableDoubleCannon;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

/**
 * The DoubleCannon class represents a specific type of cannon that extends the functionality
 * of the Cannon base class. The DoubleCannon can have an active double-barrel cannon on one of
 * its sides, while the other parts of the cannon may vary in configuration.
 */
public class DoubleCannon extends Cannon {
    private boolean topSideActive;
    private boolean bottomSideActive;
    private boolean leftSideActive;
    private boolean rightSideActive;

    public DoubleCannon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideActive = false;
        this.bottomSideActive = false;
        this.leftSideActive = false;
        this.rightSideActive = false;
    }

    @Override
    public double getCannonStrength() {
        if(topSideActive)    return 2;
        else if (leftSideActive || bottomSideActive || rightSideActive) return 1;
        return 0;
    }

    @Override
    public void activateComponent(){
        topSideActive = topSideCannon;
        bottomSideActive = bottomSideCannon;
        rightSideActive = rightSideCannon;
        leftSideActive = leftSideCannon;
    }

    @Override
    public void deactivateComponent(){
        topSideActive = false;
        bottomSideActive = false;
        rightSideActive = false;
        leftSideActive = false;
    }
    @Override
    public boolean isDoubleCannon() {
        return true;
    }

    @Override
    public String toString() {
        if (topSideCannon) return getClass().getSimpleName() + " Active: " + topSideActive + " Top: Double Cannon Barrel" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideCannon) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: Double Cannon Barrel" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideCannon) return getClass().getSimpleName() + " Active: " + leftSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Double Cannon Barrel" + " Rigth: " + rightSide;
        else if (rightSideCannon) return getClass().getSimpleName() + " Active: " + rightSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Double Cannon Barrel";
        else return "";
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String active = "";

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

        if (topSideActive || bottomSideActive || leftSideActive || rightSideActive) {active = "A";}
        else {active = " ";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  DC " + right,
                "   " + active + "   ",
                "   " + bottom + "   ",
        };
    }

    public DrawableComponentTile getDrawable(){
        return new DrawableDoubleCannon();
    }

    @Override
    public boolean isActivated() {
        return topSideActive || bottomSideActive || leftSideActive || rightSideActive;
    }
}
