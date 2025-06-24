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

    /**
     * Constructs a Cannon object, which represents a component tile with cannon functionality
     * configured on specific sides. This class extends the functionality of ComponentTile,
     * allowing the Cannon to manage its side-specific behavior and appearance.
     *
     * @param pngName the name of the image file representing this component visually
     * @param topSide the type of the top side of the cannon
     * @param bottomSide the type of the bottom side of the cannon
     * @param leftSide the type of the left side of the cannon
     * @param rightSide the type of the right side of the cannon
     */
    public Cannon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideCannon = true;
        this.bottomSideCannon = false;
        this.leftSideCannon = false;
        this.rightSideCannon = false;
    }

    /**
     * Returns the cannon's strength based on its position.
     * If the cannon is located on the top side, the returned strength is 1.
     * Otherwise, the strength is 0.5.
     *
     * @return the strength of the cannon, which is 1 if the cannon is on the top side,
     *         or 0.5 otherwise
     */
    public double getCannonStrength() {
        if(topSideCannon) return 1;
        else return 0.5;
    }

    /**
     * Rotates the current component clockwise by 90 degrees.
     *
     * This method updates the orientation of the component by shifting its side-specific
     * properties (`topSideCannon`, `rightSideCannon`, `bottomSideCannon`, `leftSideCannon`)
     * in a clockwise manner. The specific configuration of the sides is updated as follows:
     * - The left side cannon becomes the bottom side cannon.
     * - The bottom side cannon becomes the right side cannon.
     * - The right side cannon becomes the top side cannon.
     * - The top side cannon becomes the left side cannon.
     *
     * Additionally, it invokes the superclass's `rotateClockwise` method to ensure any
     * inherited behavior related to rotation is executed.
     */
    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideCannon;
        leftSideCannon = bottomSideCannon;
        bottomSideCannon = rightSideCannon;
        rightSideCannon = topSideCannon;
        topSideCannon = temp1;
    }

    /**
     * Rotates the Cannon object counterclockwise by 90 degrees.
     *
     * This method updates the state of the cannon's sides, reassigning
     * the values of `leftSideCannon`, `topSideCannon`, `rightSideCannon`,
     * and `bottomSideCannon` to reflect their positions after a counterclockwise rotation.
     * Specifically:
     * - The left side becomes the top side.
     * - The top side becomes the right side.
     * - The right side becomes the bottom side.
     * - The bottom side becomes the left side.
     *
     * Additionally, this method invokes the superclass implementation of
     * `rotateCounterClockwise`, ensuring any superclass-specific rotation
     * logic is executed.
     */
    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideCannon;
        leftSideCannon = topSideCannon;
        topSideCannon = rightSideCannon;
        rightSideCannon = bottomSideCannon;
        bottomSideCannon = temp1;
    }

    /**
     * Checks if the current cannon is located on the top side.
     *
     * @return true if the cannon is located on the top side, false otherwise
     */
    public boolean isTopSideCannon() {
        return topSideCannon;
    }

    /**
     * Determines whether the bottom side of the cannon has an active cannon.
     *
     * @return true if the bottom side of the cannon is active, false otherwise.
     */
    public boolean isBottomSideCannon() {
        return bottomSideCannon;
    }

    /**
     * Checks if the cannon is present on the left side of the component tile.
     *
     * @return true if the left side contains a cannon, false otherwise
     */
    public boolean isLeftSideCannon() {
        return leftSideCannon;
    }

    /**
     * Determines whether the right side of the current cannon is configured as a cannon.
     *
     * @return true if the right side is a cannon, false otherwise.
     */
    public boolean isRightSideCannon() {
        return rightSideCannon;
    }

    /**
     * Returns a string representation of the Cannon object.
     * The representation includes the class name and the configuration
     * of its sides, specifically identifying the side containing the cannon barrel.
     *
     * @return a string describing the Cannon object and its side configuration,
     *         or an empty string if no cannon barrel is present.
     */
    @Override
    public String toString() {
        if (topSideCannon) return getClass().getSimpleName() + " Top: Cannon Barrel" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideCannon) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: Cannon Barrel" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideCannon) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Cannon Barrel" + " Rigth: " + rightSide;
        else if (rightSideCannon) return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Cannon Barrel";
        else return "";
    }

    /**
     * Provides a drawable textual representation for the Cannon component tile.
     * This method creates and returns a new instance of DrawableCannonTUI, which is
     * responsible for rendering the visual representation of the Cannon in a terminal-based
     * text user interface (TUI).
     *
     * @return a DrawableCannonTUI instance that renders the Cannon's state in a textual format.
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableCannonTUI(this);
    }

    /**
     * Retrieves a drawable representation of the cannon component tile.
     *
     * @return an instance of {@link DrawableCannon}, representing the drawable version
     *         of the cannon component tile.
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableCannon();
    }
}