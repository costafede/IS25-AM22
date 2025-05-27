package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableEngine;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableEngineTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

/**
 * Represents a specific type of component tile called Engine, which can rotate and have an
 * activated engine on one or more sides.
 * The Engine class extends the {@code ComponentTile} class and defines specialized behaviors
 * such as engine activation and rotation of the engine sides.
 */
public class Engine extends ComponentTile {
    protected boolean topSideEngine;
    protected boolean bottomSideEngine;
    protected boolean leftSideEngine;
    protected boolean rightSideEngine;

    //Constructor -> Need to understand what to do with the parameters
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the values from the JSON file
    public Engine(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide){
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideEngine = false;
        this.bottomSideEngine = true;
        this.leftSideEngine = false;
        this.rightSideEngine = false;
    }

    public int getEngineStrength() {
        return 1;
    }

    //It's implemented in the subclasses that need it
    public void activateComponent(){
        return;
    }

    //It's implemented in the subclasses that need it
    public void deactivateComponent(){
        return;
    }

    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideEngine;
        leftSideEngine = bottomSideEngine;
        bottomSideEngine = rightSideEngine;
        rightSideEngine = topSideEngine;
        topSideEngine = temp1;
    }

    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideEngine;
        leftSideEngine = topSideEngine;
        topSideEngine = rightSideEngine;
        rightSideEngine = bottomSideEngine;
        bottomSideEngine = temp1;
    }

    public boolean isTopSideEngine() {
        return topSideEngine;
    }

    public boolean isBottomSideEngine() {
        return bottomSideEngine;
    }

    public boolean isLeftSideEngine() {
        return leftSideEngine;
    }

    public boolean isRightSideEngine() {
        return rightSideEngine;
    }

    @Override
    public String toString() {
        if (topSideEngine) return getClass().getSimpleName() + " Top: Engine" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideEngine) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: Engine" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideEngine) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Engine" + " Rigth: " + rightSide;
        else if (rightSideEngine) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Engine";
        else return "";
    }

    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableEngineTUI(this);
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableEngine();
    }
}
