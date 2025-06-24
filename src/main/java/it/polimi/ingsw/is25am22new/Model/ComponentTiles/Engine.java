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

    /**
     * Constructs an Engine component tile with specified visual representation
     * and side configurations. Initializes specific engine sides as active or inactive.
     *
     * @param pngName the name of the image file associated with the engine tile
     * @param topSide the configuration of the top side of the engine tile
     * @param bottomSide the configuration of the bottom side of the engine tile
     * @param leftSide the configuration of the left side of the engine tile
     * @param rightSide the configuration of the right side of the engine tile
     */
    //Constructor -> Need to understand what to do with the parameters
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the values from the JSON file
    public Engine(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide){
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideEngine = false;
        this.bottomSideEngine = true;
        this.leftSideEngine = false;
        this.rightSideEngine = false;
    }

    /**
     * Retrieves the numerical strength of the engine.
     *
     * @return an integer representing the strength of the engine
     */
    public int getEngineStrength() {
        return 1;
    }

    /**
     * Activates the component associated with the specific subclass that implements this method.
     * This method serves as a placeholder in the parent class and is intended to be overridden
     * by subclasses to provide specific activation logic pertinent to the component.
     *
     * Subclasses should implement this method to define how their respective component
     * functionality is initialized or triggered.
     */
    //It's implemented in the subclasses that need it
    public void activateComponent(){
        return;
    }

    /**
     * Deactivates the functionality of a specific component within the game.
     * This method is abstract and is intended to be implemented in subclasses,
     * where it defines the specific logic for deactivating the component based
     * on the component's nature and operational behavior.
     *
     * Subclasses should override this method to provide a concrete implementation
     * tailored to the requirements of the respective component.
     */
    //It's implemented in the subclasses that need it
    public void deactivateComponent(){
        return;
    }

    /**
     * Rotates the engine component 90 degrees clockwise and updates the state of
     * the engine on each side of the component.
     *
     * This method rearranges the engine states by shifting them in a clockwise
     * direction:
     * - The state of the left side engine is assigned to the bottom side.
     * - The state of the bottom side engine is assigned to the right side.
     * - The state of the right side engine is assigned to the top side.
     * - The state of the top side engine is assigned to the left side.
     *
     * The method also invokes the superclass implementation of the rotation
     * functionality to ensure consistency with the base component tile logic.
     *
     * This reorientation is useful to update the directional configuration of the
     * engine component.
     */
    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideEngine;
        leftSideEngine = bottomSideEngine;
        bottomSideEngine = rightSideEngine;
        rightSideEngine = topSideEngine;
        topSideEngine = temp1;
    }

    /**
     * Rotates the engine component's side configurations counter-clockwise.
     *
     * This method updates the state of the engine's side-specific boolean flags by
     * shifting their values counter-clockwise:
     * - The left side's state becomes the top side's state.
     * - The top side's state becomes the right side's state.
     * - The right side's state becomes the bottom side's state.
     * - The bottom side's state becomes the left side's state.
     *
     * Invokes the superclass's {@code rotateCounterClockwise} method to ensure the
     * rotation logic is consistent with the parent class's implementation for other
     * component tiles in the game environment.
     */
    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideEngine;
        leftSideEngine = topSideEngine;
        topSideEngine = rightSideEngine;
        rightSideEngine = bottomSideEngine;
        bottomSideEngine = temp1;
    }

    /**
     * Determines if the engine is configured as a top-side engine.
     *
     * @return true if the engine is a top-side engine, false otherwise.
     */
    public boolean isTopSideEngine() {
        return topSideEngine;
    }

    /**
     * Checks if the engine is located on the bottom side of the component tile.
     *
     * @return true if the engine is on the bottom side, false otherwise
     */
    public boolean isBottomSideEngine() {
        return bottomSideEngine;
    }

    /**
     * Determines if the engine is located on the left side.
     *
     * @return true if the engine is positioned on the left side, false otherwise
     */
    public boolean isLeftSideEngine() {
        return leftSideEngine;
    }

    /**
     * Checks if the engine is located on the right side.
     *
     * @return true if the engine is on the right side, false otherwise
     */
    public boolean isRightSideEngine() {
        return rightSideEngine;
    }

    /**
     * Returns a string representation of the Engine object, indicating the type of engine
     * (if present) and the state of its sides.
     *
     * The output format includes the class name and specifies which side, if any, has an engine:
     * - "Top", "Bottom", "Left", or "Right" are used to represent the sides.
     * - The side with an engine is labeled as "Engine".
     * - If no engine is present, an empty string is returned.
     *
     * @return a string describing the engine's configuration or an empty string if no side has an engine
     */
    @Override
    public String toString() {
        if (topSideEngine) return getClass().getSimpleName() + " Top: Engine" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideEngine) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: Engine" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideEngine) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Engine" + " Rigth: " + rightSide;
        else if (rightSideEngine) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Engine";
        else return "";
    }

    /**
     * Returns a drawable representation of the Engine object for a text-based
     * user interface (TUI). The returned object is responsible for generating
     * a visual structure of the Engine's state and its side configurations in a textual format.
     *
     * @return an instance of DrawableEngineTUI, which provides the textual
     * representation of the Engine object for the TUI.
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableEngineTUI(this);
    }

    /**
     * Retrieves the drawable representation of the engine component.
     *
     * @return a {@code DrawableComponentTile} instance representing the drawable
     *         visualization of an engine, specifically a {@code DrawableEngine} instance.
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableEngine();
    }
}
