package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableCannon;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableComponentTile;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * The Cannon class represents a specific type of ComponentTile that includes cannon functionality.
 * A Cannon can have active cannon barrels on any of its four sides (top, bottom, left, right).
 * Each side of the cannon can be individually toggled as a cannon barrel.
 * The class provides functionality for rotating the cannon and determining its strength.
 */
public class Cannon extends ComponentTile {
    protected boolean topSideCannon;
    protected boolean bottomSideCannon;
    protected boolean leftSideCannon;
    protected boolean rightSideCannon;

    public Cannon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideCannon = true;
        this.bottomSideCannon = false;
        this.leftSideCannon = false;
        this.rightSideCannon = false;
    }

    public double getCannonStrength() {
        if(topSideCannon) return 1;
        else return 0.5;
    }

    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideCannon;
        leftSideCannon = bottomSideCannon;
        bottomSideCannon = rightSideCannon;
        rightSideCannon = topSideCannon;
        topSideCannon = temp1;
    }

    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideCannon;
        leftSideCannon = topSideCannon;
        topSideCannon = rightSideCannon;
        rightSideCannon = bottomSideCannon;
        bottomSideCannon = temp1;
    }

    public boolean isTopSideCannon() {
        return topSideCannon;
    }

    public boolean isBottomSideCannon() {
        return bottomSideCannon;
    }

    public boolean isLeftSideCannon() {
        return leftSideCannon;
    }

    public boolean isRightSideCannon() {
        return rightSideCannon;
    }

    @Override
    public String toString() {
        if (topSideCannon) return getClass().getSimpleName() + " Top: Cannon Barrel" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideCannon) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: Cannon Barrel" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideCannon) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Cannon Barrel" + " Rigth: " + rightSide;
        else if (rightSideCannon) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Cannon Barrel";
        else return "";
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;

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

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableCannon();
    }
}