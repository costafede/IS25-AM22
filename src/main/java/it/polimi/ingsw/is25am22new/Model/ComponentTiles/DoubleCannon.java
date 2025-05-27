package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableDoubleCannon;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableDoubleCannonTUI;

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

    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableDoubleCannonTUI(this);
    }

    public DrawableComponentTile getDrawable(){
        return new DrawableDoubleCannon();
    }

    @Override
    public boolean isActivated() {
        return topSideActive || bottomSideActive || leftSideActive || rightSideActive;
    }
}
