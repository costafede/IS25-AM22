package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable.DrawableShieldGenerator;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

/**
 * The ShieldGenerator class represents a shielding component in a system.
 * It allows the activation and deactivation of shields on specific sides,
 * enabling modular control of protection over the component's boundaries.
 * The class inherits from ComponentTile and introduces additional functionality
 * specific to shielding capabilities.
 */
public class ShieldGenerator extends ComponentTile {
    private boolean topSideShieldable, bottomSideShieldable, leftSideShieldable, rightSideShieldable;   //sono i lati su cui si può attivare lo scudo
    private boolean topSideShielded, bottomSideShielded, leftSideShielded, rightSideShielded; //booleani che servono a capire che lati sono effettivamente attivi

    public ShieldGenerator(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideShieldable = true;
        this.bottomSideShieldable = false;
        this.leftSideShieldable = false;
        this.rightSideShieldable = true;
        this.topSideShielded = false;
        this.bottomSideShielded = false;
        this.leftSideShielded = false;
        this.rightSideShielded = false;
    }

    public void rotateClockwise() {
        super.rotateClockwise();
        boolean tmp = topSideShieldable;
        topSideShieldable = leftSideShieldable;
        leftSideShieldable = bottomSideShieldable;
        bottomSideShieldable = rightSideShieldable;
        rightSideShieldable = tmp;
    }

    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        boolean tmp = topSideShieldable;
        topSideShieldable = rightSideShieldable;
        rightSideShieldable = bottomSideShieldable;
        bottomSideShieldable = leftSideShieldable;
        leftSideShieldable = tmp;
    }

    public void activateComponent(){
        topSideShielded = topSideShieldable;
        bottomSideShielded = bottomSideShieldable;
        leftSideShielded = leftSideShieldable;
        rightSideShielded = rightSideShieldable;
    }

    public void deactivateComponent() {
        topSideShielded = false;
        bottomSideShielded = false;
        leftSideShielded = false;
        rightSideShielded = false;
    }

    public boolean isTopSideShielded() {
        return topSideShielded;
    }

    public boolean isBottomSideShielded() {
        return bottomSideShielded;
    }

    public boolean isLeftSideShielded() {
        return leftSideShielded;
    }

    public boolean isRightSideShielded() {
        return rightSideShielded;
    }

    public boolean isShieldGenerator() {
        return true;
    }

    @Override
    public String toString() {
        if (topSideShieldable) return getClass().getSimpleName() + " Active: " + topSideShielded + " Top: Shield" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideShieldable) return getClass().getSimpleName() + " Active: " + bottomSideShielded + " Top: " + topSide + " Bottom: Shield" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideShieldable) return getClass().getSimpleName() + " Active: " + leftSideShielded + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Shield" + " Rigth: " + rightSide;
        else if (rightSideShieldable) return getClass().getSimpleName() + " Active: " + rightSideShielded + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Shield";
        else return "";
    }

    @Override
    public String[] draw(){
        String top = "";
        String bottom = "";
        String left = "";
        String right = "";
        String active = "";

        if (topSide.equals(SMOOTH) && !topSideShieldable){top = "   S   ";}
        else if (topSide.equals(ONEPIPE) && !topSideShieldable){top = "   1   ";}
        else if (topSide.equals(TWOPIPES) && !topSideShieldable){top = "   2   ";}
        else if (topSide.equals(UNIVERSALPIPE) && !topSideShieldable){top = "   3   ";}
        else if (topSide.equals(SMOOTH) && topSideShieldable){top = "   S%  ";}
        else if (topSide.equals(ONEPIPE) && topSideShieldable){top = "   1%  ";}
        else if (topSide.equals(TWOPIPES) && topSideShieldable){top = "   2%  ";}
        else if (topSide.equals(UNIVERSALPIPE) && topSideShieldable){top = "   3%  ";}

        if (bottomSide.equals(SMOOTH) && !bottomSideShieldable){bottom = "   S   ";}
        else if (bottomSide.equals(ONEPIPE) && !bottomSideShieldable){bottom = "   1   ";}
        else if (bottomSide.equals(TWOPIPES) && !bottomSideShieldable){bottom = "   2   ";}
        else if (bottomSide.equals(UNIVERSALPIPE) && !bottomSideShieldable){bottom = "   3   ";}
        else if (bottomSide.equals(SMOOTH) && bottomSideShieldable){bottom = "   S%  ";}
        else if (bottomSide.equals(ONEPIPE) && bottomSideShieldable){bottom = "   1%  ";}
        else if (bottomSide.equals(TWOPIPES) && bottomSideShieldable){bottom = "   2%  ";}
        else if (bottomSide.equals(UNIVERSALPIPE) && bottomSideShieldable){bottom = "   3%  ";}

        if (leftSide.equals(SMOOTH) && !leftSideShieldable){left = "S ";}
        else if (leftSide.equals(ONEPIPE) && !leftSideShieldable){left = "1 ";}
        else if (leftSide.equals(TWOPIPES) && !leftSideShieldable){left = "2 ";}
        else if (leftSide.equals(UNIVERSALPIPE) && !leftSideShieldable){left = "3 ";}
        else if (leftSide.equals(SMOOTH) && leftSideShieldable){left = "S%";}
        else if (leftSide.equals(ONEPIPE) && leftSideShieldable){left = "1%";}
        else if (leftSide.equals(TWOPIPES) && leftSideShieldable){left = "2%";}
        else if (leftSide.equals(UNIVERSALPIPE) && leftSideShieldable){left = "3%";}

        if (rightSide.equals(SMOOTH) && !rightSideShieldable){right = " S";}
        else if (rightSide.equals(ONEPIPE) && !rightSideShieldable){right = " 1";}
        else if (rightSide.equals(TWOPIPES) && !rightSideShieldable){right = " 2";}
        else if (rightSide.equals(UNIVERSALPIPE) && !rightSideShieldable){right = " 3";}
        else if (rightSide.equals(SMOOTH) && rightSideShieldable){right = "%S";}
        else if (rightSide.equals(ONEPIPE) && rightSideShieldable){right = "%1";}
        else if (rightSide.equals(TWOPIPES) && rightSideShieldable){right = "%2";}
        else if (rightSide.equals(UNIVERSALPIPE) && rightSideShieldable){right = "%3";}

        if(isActive())
            active = "A";
        else
            active = " ";

        return new String[]{
                top,
                "       ",
                left + " SG" + right,
                "   " + active + "   ",
                bottom,
        };
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableShieldGenerator();
    }

    public boolean isActive(){
        return topSideShielded || bottomSideShielded || leftSideShielded || rightSideShielded;
    }


}
