package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableCannon;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableCannonTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;

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
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableCannonTUI(this);
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableCannon();
    }
}