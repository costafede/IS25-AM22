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

    /**
     * Constructs a ShieldGenerator with the specified parameters. The ShieldGenerator
     * component includes specific configurations for its sides and the capability to determine
     * which sides are shieldable or shielded by default.
     *
     * @param pngName the name of the image file representing the visual appearance of the ShieldGenerator
     * @param topSide the configuration of the top side of the ShieldGenerator
     * @param bottomSide the configuration of the bottom side of the ShieldGenerator
     * @param leftSide the configuration of the left side of the ShieldGenerator
     * @param rightSide the configuration of the right side of the ShieldGenerator
     */
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

    /**
     * Rotates the ShieldGenerator 90 degrees clockwise.
     *
     * This method performs a rotation operation on the ShieldGenerator,
     * updating the shieldable properties of its sides to reflect the new
     * orientation. Specifically:
     * - The top-side shieldable property becomes the left-side shieldable property.
     * - The left-side shieldable property becomes the bottom-side shieldable property.
     * - The bottom-side shieldable property becomes the right-side shieldable property.
     * - The right-side shieldable property becomes the top-side shieldable property.
     *
     * This ensures that the shieldable attributes of the sides are realigned
     * according to the new rotational state of the ShieldGenerator while
     * maintaining its functionality as defined in the base component.
     *
     * The method also explicitly calls the superclass's `rotateClockwise` method
     * to handle rotation logic for elements or attributes defined in the parent class.
     */
    public void rotateClockwise() {
        super.rotateClockwise();
        boolean tmp = topSideShieldable;
        topSideShieldable = leftSideShieldable;
        leftSideShieldable = bottomSideShieldable;
        bottomSideShieldable = rightSideShieldable;
        rightSideShieldable = tmp;
    }

    /**
     * Rotates the shield generator component's sides counter-clockwise.
     *
     * Updates the state of the shieldability of each side of the component
     * by shifting their states in a counter-clockwise direction:
     * - The shieldability of the top side becomes that of the right side.
     * - The shieldability of the right side becomes that of the bottom side.
     * - The shieldability of the bottom side becomes that of the left side.
     * - The shieldability of the left side becomes that of the top side.
     *
     * This method also invokes the corresponding behavior in the superclass
     * to ensure additional functionality is correctly executed during the rotation.
     */
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        boolean tmp = topSideShieldable;
        topSideShieldable = rightSideShieldable;
        rightSideShieldable = bottomSideShieldable;
        bottomSideShieldable = leftSideShieldable;
        leftSideShieldable = tmp;
    }

    /**
     * Activates the component by setting the shielded state for all sides
     * of the shield generator based on their corresponding shieldable properties.
     *
     * This method will update the following fields to reflect the current shieldable
     * configuration of the component:
     * - topSideShielded is set to the value of topSideShieldable
     * - bottomSideShielded is set to the value of bottomSideShieldable
     * - leftSideShielded is set to the value of leftSideShieldable
     * - rightSideShielded is set to the value of rightSideShieldable
     *
     * The method ensures that the shielding status for the component aligns with
     * its defined shieldable capabilities.
     */
    public void activateComponent(){
        topSideShielded = topSideShieldable;
        bottomSideShielded = bottomSideShieldable;
        leftSideShielded = leftSideShieldable;
        rightSideShielded = rightSideShieldable;
    }

    /**
     * Deactivates the component by removing shielding from all sides.
     *
     * This method sets the shielded state of the top, bottom, left,
     * and right sides of the component to false, effectively
     * deactivating the component's defensive properties.
     */
    public void deactivateComponent() {
        topSideShielded = false;
        bottomSideShielded = false;
        leftSideShielded = false;
        rightSideShielded = false;
    }

    /**
     * Checks if the top side of the shield generator is currently shielded.
     *
     * @return true if the top side is shielded, false otherwise
     */
    public boolean isTopSideShielded() {
        return topSideShielded;
    }

    /**
     * Determines if the bottom side of the component is currently shielded.
     * The shielding state indicates whether the bottom side has an active shield
     * to provide protection or functionality specific to game mechanics.
     *
     * @return true if the bottom side of the component is shielded, false otherwise
     */
    public boolean isBottomSideShielded() {
        return bottomSideShielded;
    }

    /**
     * Determines whether the left side of the shield generator is currently shielded.
     *
     * @return true if the left side is shielded, otherwise false
     */
    public boolean isLeftSideShielded() {
        return leftSideShielded;
    }

    /**
     * Determines if the right side of this component tile is currently shielded.
     *
     * @return true if the right side is shielded, false otherwise
     */
    public boolean isRightSideShielded() {
        return rightSideShielded;
    }

    /**
     * Determines if the current component tile is a shield generator.
     *
     * @return true, as this represents a shield generator component tile.
     */
    public boolean isShieldGenerator() {
        return true;
    }

    /**
     * Provides a string representation of the ShieldGenerator object.
     * The representation includes the status of sides (Top, Bottom, Left, Right)
     * and indicates if a specific side is shieldable and active.
     * If no shields are applicable, an empty string is returned.
     *
     * @return a string describing the ShieldGenerator state, or an empty string
     *         if no shieldable sides exist.
     */
    @Override
    public String toString() {
        if (topSideShieldable) return getClass().getSimpleName() + " Active: " + topSideShielded + " Top: Shield" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideShieldable) return getClass().getSimpleName() + " Active: " + bottomSideShielded + " Top: " + topSide + " Bottom: Shield" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideShieldable) return getClass().getSimpleName() + " Active: " + leftSideShielded + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Shield" + " Rigth: " + rightSide;
        else if (rightSideShieldable) return getClass().getSimpleName() + " Active: " + rightSideShielded + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Shield";
        else return "";
    }

    /**
     * Returns a DrawableComponentTileTUI instance specifically configured to visually represent
     * the current ShieldGenerator through a text-based UI component.
     *
     * The returned DrawableShieldGeneratorTUI reflects the ShieldGenerator's state and sides,
     * including their shieldable properties and active status, as a text-based representation.
     *
     * @return an instance of DrawableShieldGeneratorTUI representing the current ShieldGenerator's state.
     */
    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableShieldGeneratorTUI(this);
    }

    /**
     * Provides the drawable representation of the ShieldGenerator component tile.
     *
     * @return an instance of DrawableShieldGenerator, which represents the visual and functional
     *         aspects of the ShieldGenerator tile.
     */
    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableShieldGenerator();
    }

    /**
     * Determines if the shield generator is currently activated based on the shielded
     * state of its top, bottom, left, or right sides.
     *
     * @return true if any of the sides (top, bottom, left, or right) are shielded;
     *         false otherwise
     */
    public boolean isActivated(){
        return topSideShielded || bottomSideShielded || leftSideShielded || rightSideShielded;
    }

    /**
     * Determines if the top side of the shield generator component
     * is shieldable, meaning it is capable of being shielded.
     *
     * @return true if the top side of the shield generator is shieldable,
     *         false otherwise
     */
    public boolean isTopSideShieldable() {
        return topSideShieldable;
    }

    /**
     * Determines if the bottom side of the shield generator is shieldable.
     *
     * @return true if the bottom side can be shielded, false otherwise
     */
    public boolean isBottomSideShieldable() {
        return bottomSideShieldable;
    }

    /**
     * Determines whether the left side of the shield generator is capable of being shielded.
     *
     * @return true if the left side of the shield generator can be shielded, false otherwise
     */
    public boolean isLeftSideShieldable() {
        return leftSideShieldable;
    }

    /**
     * Determines whether the right side of the shield generator component
     * is capable of supporting a shield based on its configuration.
     *
     * @return true if the right side is shieldable, false otherwise
     */
    public boolean isRightSideShieldable() {
        return rightSideShieldable;
    }
}
