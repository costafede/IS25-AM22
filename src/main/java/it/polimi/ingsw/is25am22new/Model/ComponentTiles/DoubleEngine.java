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

    /**
     * Constructs a new DoubleEngine object, which extends the base Engine class.
     * The DoubleEngine provides specialized functionality, focusing on the activation
     * of the bottom side while maintaining inherent characteristics of the Engine class.
     */
    public DoubleEngine(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.bottomSideActive = false;
    }

    /**
     * Calculates the strength of the engine based on its activation state.
     * If the engine's bottom side is active, it returns a strength value of 2.
     * Otherwise, it returns 0 when the engine is inactive.
     *
     * @return the engine's strength value, where 2 represents an active state
     *         and 0 represents an inactive state.
     */
    // Returns the strength of the engine based on being active or not
    @Override
    public int getEngineStrength() {
        if (bottomSideActive)
            return 2;
        return 0;
    }

    /**
     * Activates the bottom side of the engine component.
     * This method sets the bottom side of the engine to an active state,
     * allowing it to contribute its functionality as defined in the
     * component's behavior.
     */
    // Activates the engine component --> Only the bottom side can be activated
    @Override
    public void activateComponent() {
        bottomSideActive = true;
    }

    /**
     * Deactivates the bottom side of the engine component.
     * This method sets the `bottomSideActive` flag to false, indicating that
     * the bottom side of this engine is no longer active.
     *
     * The deactivation affects the functionality of methods such as
     * {@code getEngineStrength()}, which will now return zero.
     *
     * Overrides the {@code deactivateComponent} method from the parent class.
     */
    @Override
    public void deactivateComponent() {
        bottomSideActive = false;
    }

    /**
     * Determines whether the engine is a double engine.
     *
     * @return true if the engine is a double engine; otherwise, false.
     */
    @Override
    public boolean isDoubleEngine() {
        return true;
    }

    /**
     * Returns a string representation of the current state of the DoubleEngine.
     * The string includes information about the active state of the engine,
     * the side that is marked as "Double Engine," and the states of the other sides.
     *
     * @return a string describing the state of the DoubleEngine
     */
    @Override
    public String toString() {
        if (topSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: Double Engine" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: Double Engine" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Double Engine" + " Rigth: " + rightSide;
        else if (rightSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Double Engine";
        else return "";
    }

    /**
     * Returns the drawable text-based UI (TUI) component representation for the DoubleEngine.
     * This implementation provides a visual rendering of the DoubleEngine's current state,
     * including its configuration, activation status, and associated sides.
     *
     * @return a DrawableDoubleEngineTUI instance representing the current state of this DoubleEngine.
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableDoubleEngineTUI(this);
    }

    /**
     * Returns the drawable representation of the double engine component tile.
     *
     * @return a {@link DrawableComponentTile} instance that provides the visual representation
     *         of the double engine component.
     */
    public DrawableComponentTile getDrawable() {
        return new DrawableDoubleEngine();
    }

    /**
     * Determines whether the bottom side of the double engine is currently active.
     *
     * @return true if the bottom side is active; false otherwise
     */
    @Override
    public boolean isActivated() {
        return bottomSideActive;
    }
}
