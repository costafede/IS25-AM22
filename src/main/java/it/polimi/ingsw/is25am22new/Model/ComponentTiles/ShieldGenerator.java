package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableShieldGenerator;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableShieldGeneratorTUI;

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
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableShieldGeneratorTUI(this);
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableShieldGenerator();
    }

    public boolean isActivated(){
        return topSideShielded || bottomSideShielded || leftSideShielded || rightSideShielded;
    }

    public boolean isTopSideShieldable() {
        return topSideShieldable;
    }

    public boolean isBottomSideShieldable() {
        return bottomSideShieldable;
    }

    public boolean isLeftSideShieldable() {
        return leftSideShieldable;
    }

    public boolean isRightSideShieldable() {
        return rightSideShieldable;
    }
}
