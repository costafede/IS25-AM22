package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableBatteryComponent;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableBatteryComponentTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a specialized component tile in the form of a battery compartment.
 * A BatteryComponent can hold battery tokens, which are used to power certain actions or mechanisms.
 * This class extends the base functionality provided by ComponentTile, adding battery-specific behavior.
 */
public class BatteryComponent extends ComponentTile {
    private int numOfBatteries;

    /**
     * Constructs a BatteryComponent with the provided parameters.
     * A BatteryComponent represents a specialized type of component tile
     * that holds battery tokens used to power various game mechanisms.
     *
     * @param pngName      the filename of the image representing this component
     * @param topSide      the type of the top side of the component
     * @param bottomSide   the type of the bottom side of the component
     * @param leftSide     the type of the left side of the component
     * @param rightSide    the type of the right side of the component
     * @param numOfBatteries the initial number of batteries in this component
     */
    public BatteryComponent(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int numOfBatteries) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.numOfBatteries = numOfBatteries;
    }

    /**
     * Retrieves the number of battery tokens currently stored in the battery component.
     *
     * @return the number of batteries in this component.
     */
    public int getNumOfBatteries() {
        return numOfBatteries;
    }

    /**
     * Decreases the number of battery tokens in this component by one.
     * If there are no battery tokens remaining, an IllegalArgumentException is thrown.
     *
     * Throws:
     * - IllegalArgumentException: If an attempt is made to remove a battery token when none are available.
     */
    public void removeBatteryToken() {
        if(numOfBatteries <= 0)
            throw new IllegalArgumentException("Cannot remove batteries if there are none");
        numOfBatteries--;
    }

    /**
     * Determines whether this component is classified as a battery.
     *
     * @return true if the component is identified as a battery, false otherwise.
     */
    public boolean isBattery() {
        return true;
    }

    /**
     * Returns a string representation of the BatteryComponent instance.
     * The string includes the class name and details about the number
     * of batteries and the properties of the top, bottom, left, and right sides.
     *
     * @return a string providing a representation of the BatteryComponent,
     *         including the number of batteries and the side configurations.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " Batteries number: " + numOfBatteries + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    /**
     * Creates and returns a drawable text-based UI (TUI) representation of the battery component.
     * This method instantiates a new DrawableBatteryComponentTUI object associated
     * with the current battery component, enabling its visual rendering in a text-based interface.
     *
     * @return an instance of DrawableBatteryComponentTUI corresponding to this battery component
     */
    @Override
    public DrawableBatteryComponentTUI getDrawableTUI() {
        return new DrawableBatteryComponentTUI(this);
    }

    /**
     * Returns a DrawableComponentTile representation of the current BatteryComponent.
     * This representation is used to visually or textually describe the battery component
     * in the context of the application's UI or rendering logic.
     *
     * @return a DrawableBatteryComponent instance representing the battery component.
     */
    public DrawableComponentTile getDrawable() {
        return new DrawableBatteryComponent(this);
    }
}