package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableDoubleEngine;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableDoubleEngineTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

/**
 * Represents a specialized type of engine that focuses specifically on the bottom side
 * for activation. This engine extends the base functionality of the Engine class by
 * offering a doubled strength when the bottom side is activated. It is designed to
 * handle additional functionality such as determining its double-engine capability
 * and providing unique rendering and string representation.
 */
public class DoubleEngine extends Engine {
    private boolean bottomSideActive;

    public DoubleEngine(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.bottomSideActive = false;
    }

    // Returns the strength of the engine based on being active or not
    @Override
    public int getEngineStrength() {
        if (bottomSideActive)
            return 2;
        return 0;
    }

    // Activates the engine component --> Only the bottom side can be activated
    @Override
    public void activateComponent() {
        bottomSideActive = true;
    }

    @Override
    public void deactivateComponent() {
        bottomSideActive = false;
    }

    @Override
    public boolean isDoubleEngine() {
        return true;
    }

    @Override
    public String toString() {
        if (topSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: Double Engine" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: Double Engine" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Double Engine" + " Rigth: " + rightSide;
        else if (rightSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Double Engine";
        else return "";
    }

    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableDoubleEngineTUI(this);
    }

    public DrawableComponentTile getDrawable() {
        return new DrawableDoubleEngine();
    }

    @Override
    public boolean isActivated() {
        return bottomSideActive;
    }
}
