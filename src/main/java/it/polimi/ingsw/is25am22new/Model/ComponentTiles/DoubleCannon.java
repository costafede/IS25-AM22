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

    /**
     * Constructs a DoubleCannon object, representing a specialized cannon with
     * double-barrel functionality on one or more sides. The DoubleCannon extends
     * the Cannon base class, enabling additional behavior and properties.
     *
     * @param pngName the name of the image file representing this component visually
     * @param topSide the type of the top side of the cannon
     * @param bottomSide the type of the bottom side of the cannon
     * @param leftSide the type of the left side of the cannon
     * @param rightSide the type of the right side of the cannon
     */
    public DoubleCannon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideActive = false;
        this.bottomSideActive = false;
        this.leftSideActive = false;
        this.rightSideActive = false;
    }

    /**
     * Calculates the strength of the cannon based on its active sides.
     *
     * If the top side is active, the strength of the cannon will be 2.
     * If any of the left, right, or bottom sides are active (but not the top),
     * the strength of the cannon will be 1.
     * If no sides are active, the strength of the cannon will be 0.
     *
     * @return the strength of the cannon as a double value determined by the active sides:
     *         2 if the top side is active, 1 if any of the other sides are active, and 0 if no side is active.
     */
    @Override
    public double getCannonStrength() {
        if(topSideActive)    return 2;
        else if (leftSideActive || bottomSideActive || rightSideActive) return 1;
        return 0;
    }

    /**
     * Activates the components for each side of the DoubleCannon.
     * This method sets the active state of the top, bottom, left, and right sides
     * based on their respective cannon configurations.
     *
     * If a side has a cannon assigned to it, the corresponding active flag
     * for that side will be set to true, enabling functionality for that side.
     * Otherwise, the active flag will remain false.
     */
    @Override
    public void activateComponent(){
        topSideActive = topSideCannon;
        bottomSideActive = bottomSideCannon;
        rightSideActive = rightSideCannon;
        leftSideActive = leftSideCannon;
    }

    /**
     * Deactivates all sides of the DoubleCannon component.
     *
     * This method sets all active status fields (top, bottom, left, and right sides)
     * of the DoubleCannon to false, effectively disabling any operational functionality
     * associated with the component.
     */
    @Override
    public void deactivateComponent(){
        topSideActive = false;
        bottomSideActive = false;
        rightSideActive = false;
        leftSideActive = false;
    }
    /**
     * Determines if the current component is a DoubleCannon.
     *
     * @return true, indicating that the component is a DoubleCannon.
     */
    @Override
    public boolean isDoubleCannon() {
        return true;
    }

    /**
     * Returns a string representation of the DoubleCannon object, including its active status
     * and configuration of its top, bottom, left, and right sides.
     * The representation specifies which side contains the double cannon barrel and its
     * current activation status, while displaying the configuration of other sides.
     *
     * @return a string describing the current state and configuration of the DoubleCannon object
     */
    @Override
    public String toString() {
        if (topSideCannon) return getClass().getSimpleName() + " Active: " + topSideActive + " Top: Double Cannon Barrel" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideCannon) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: Double Cannon Barrel" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideCannon) return getClass().getSimpleName() + " Active: " + leftSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Double Cannon Barrel" + " Rigth: " + rightSide;
        else if (rightSideCannon) return getClass().getSimpleName() + " Active: " + rightSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Double Cannon Barrel";
        else return "";
    }

    /**
     * Creates and returns a drawable text-based UI (TUI) component that visually represents
     * the current state and configuration of the `DoubleCannon` object.
     * The returned `DrawableComponentTileTUI` uses symbols to depict the four sides
     * and activation status of the `DoubleCannon` for textual visualization.
     *
     * @return a `DrawableDoubleCannonTUI` instance representing the visual representation
     *         of the `DoubleCannon` component in text-based UI format.
     */
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableDoubleCannonTUI(this);
    }

    /**
     * Retrieves a drawable representation of the double cannon.
     *
     * @return a {@code DrawableComponentTile} instance representing the drawable version
     *         of the double cannon, specifically a {@code DrawableDoubleCannon}.
     */
    public DrawableComponentTile getDrawable(){
        return new DrawableDoubleCannon();
    }

    /**
     * Determines if the double cannon is currently activated on any of its sides.
     *
     * @return true if any of the sides (top, bottom, left, or right) of the double cannon is active;
     *         false otherwise.
     */
    @Override
    public boolean isActivated() {
        return topSideActive || bottomSideActive || leftSideActive || rightSideActive;
    }
}
