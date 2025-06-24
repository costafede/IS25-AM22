package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a general component tile in a game, which can have various sides with specific functionalities.
 * A tile has four sides (top, bottom, left, right), each represented by a {@link Side} and can be interacted with
 * through its various properties and behaviors. This class serves as a base for more specialized component tiles.
 *
 * A component tile includes methods to rotate the tile, retrieve and modify its properties, and determine
 * specific characteristics about its sides, such as whether they are smooth, shielded, or equipped with other
 * functionalities like cannons or engines.
 *
 * Subclasses extending this class can provide specific implementations for behaviors and properties, such as
 * holding good blocks, managing crew members, or enabling specific game mechanics.
 *
 * Key functionalities include:
 * - Retrieving or setting side properties.
 * - Rotating the tile clockwise or counter-clockwise.
 * - Identifying the presence of special features on the tile sides such as cannons, engines, or shields.
 * - Managing good blocks and astronauts if applicable in the subclass.
 *
 * A tile also holds information about its associated image name, which is helpful for visualization or rendering.
 */
public abstract class ComponentTile implements Serializable {
    protected Side topSide;
    protected Side bottomSide;
    protected Side leftSide;
    protected Side rightSide;
    private final String pngName;
    private int color; //needed for the algorithm of checkShipboard
    private int numOfRotations = 0;  //number of 90 degrees rotations, clockwise if positive, counterclockwise if negative

    /**
     * Constructs a ComponentTile with the specified parameters.
     *
     * @param pngName the name of the image file associated with the component tile
     * @param topSide the configuration of the top side of the component tile
     * @param bottomSide the configuration of the bottom side of the component tile
     * @param leftSide the configuration of the left side of the component tile
     * @param rightSide the configuration of the right side of the component tile
     */
    public ComponentTile(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        this.topSide = topSide;
        this.bottomSide = bottomSide;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.pngName = pngName;
        this.color = 0;
    }

    /**
     * Retrieves the color associated with this component tile.
     *
     * @return the color of the component tile as an integer
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color of the component tile.
     *
     * @param color the integer value representing the color to be set
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Determines if the current component tile has an alien addon.
     *
     * @return true if the component tile has an alien addon, false otherwise
     */
    public boolean isAlienAddon(){
        return false;
    }

    /**
     * Rotates the component tile 90 degrees clockwise.
     *
     * This method updates the orientation of the component tile by reassigning the
     * sides to their new positions in a clockwise direction:
     * - The top side becomes the left side.
     * - The left side becomes the bottom side.
     * - The bottom side becomes the right side.
     * - The right side becomes the top side.
     *
     * Additionally, the number of rotations (`numOfRotations`) is incremented to
     * keep track of how many times the tile has been rotated.
     */
    public void rotateClockwise() {
        Side tmp = topSide;
        topSide = leftSide;
        leftSide = bottomSide;
        bottomSide = rightSide;
        rightSide = tmp;
        numOfRotations++;
    }

    /**
     * Rotates the component tile's sides counter-clockwise.
     *
     * Updates the sides of the tile by shifting their positions counter-clockwise:
     * - The top side becomes the right side.
     * - The right side becomes the bottom side.
     * - The bottom side becomes the left side.
     * - The left side becomes the top side.
     *
     * Also decrements the counter tracking the number of rotations performed.
     */
    public void rotateCounterClockwise() {
        Side tmp = topSide;
        topSide = rightSide;
        rightSide = bottomSide;
        bottomSide = leftSide;
        leftSide = tmp;
        numOfRotations--;
    }

    /**
     * Retrieves the name of the PNG file associated with this component tile.
     *
     * @return the name of the PNG file as a String
     */
    public String getPngName() {
        return pngName;
    }

    /**
     * Returns the top side of the component tile.
     *
     * @return the top side of the component tile as a {@code Side} enum value
     */
    public Side getTopSide(){
        return topSide;
    }

    /**
     * Retrieves the bottom side of the component tile.
     *
     * @return the bottom side of the component tile as a {@link Side} enum value.
     */
    public Side getBottomSide(){
        return bottomSide;
    }

    /**
     * Retrieves the side of the component tile located on the left.
     *
     * @return the left side of the component tile as a {@code Side} enum.
     */
    public Side getLeftSide(){
        return leftSide;
    }

    /**
     * Retrieves the right side of the component tile.
     *
     * @return the {@link Side} representing the right side of this component tile.
     */
    public Side getRightSide(){
        return rightSide;
    }

    /**
     * Adds the specified GoodBlock to the component tile.
     *
     * @param gb the GoodBlock to be added to this component tile
     */
    public void addGoodBlock(GoodBlock gb){
        return;
    }

    /**
     * Removes the specified GoodBlock from the current component tile.
     *
     * @param goodBlock the GoodBlock to be removed
     * @return the removed GoodBlock if it was successfully removed, or null if the block was not present
     */
    public GoodBlock removeGoodBlock(GoodBlock goodBlock){
        return null;
    }

    /**
     * Determines if the current component tile functions as a storage compartment.
     *
     * @return true if the component tile is a storage compartment, false otherwise
     */
    public boolean isStorageCompartment(){ return false; }

    /**
     * Checks if the left side of the component tile is smooth.
     *
     * The left side is considered smooth if its type is {@code Side.SMOOTH}.
     *
     * @return {@code true} if the left side is smooth, {@code false} otherwise.
     */
    public boolean isLeftSideSmooth(){
        return leftSide == Side.SMOOTH;
    }

    /**
     * Checks if the right side of the component tile is smooth.
     *
     * @return true if the right side is smooth, false otherwise
     */
    public boolean isRightSideSmooth(){
        return rightSide == Side.SMOOTH;
    }

    /**
     * Checks if the top side of the component tile is smooth.
     *
     * @return true if the top side of the component is smooth, false otherwise
     */
    public boolean isTopSideSmooth(){
        return topSide == Side.SMOOTH;
    }

    /**
     * Checks if the bottom side of the component tile is smooth.
     *
     * @return true if the bottom side is of type SMOOTH, false otherwise.
     */
    public boolean isBottomSideSmooth(){
        return bottomSide == Side.SMOOTH;
    }

    /**
     * Activates the component tile and sets its state to active.
     * The specific behavior of activation depends on the type of the component tile
     * and its role within the application or game.
     *
     * This method may be used to enable the functionality of the component, switch its
     * state, prepare it for interaction, or trigger associated effects based on its type
     * and configuration.
     */
    public void activateComponent(){
        return;
    }

    /**
     * Deactivates the component associated with the current instance.
     *
     * This method is used to modify the state of the component, rendering it
     * inactive or unfunctional depending on the game mechanics. Once deactivated,
     * the component should no longer behave as if it is active or enabled.
     *
     * The specifics of what deactivation entails are determined by the
     * implementation context of the component, such as whether it affects
     * connectivity, availability of resources, engines, or shields.
     */
    public void deactivateComponent(){
        return;
    }

    /**
     * Checks if the bottom side of the component tile is shielded.
     * A shielded side typically represents a side that has additional
     * protection or special properties to block specific interactions.
     *
     * @return true if the bottom side of the component tile is shielded, false otherwise
     */
    public boolean isBottomSideShielded(){
        return false;
    }

    /**
     * Checks whether the top side of this component tile is shielded.
     *
     * @return true if the top side is shielded, false otherwise
     */
    public boolean isTopSideShielded(){
        return false;
    }

    /**
     * Determines whether the left side of the component tile is shielded.
     *
     * @return true if the left side of the component tile is shielded; false otherwise
     */
    public boolean isLeftSideShielded(){
        return false;
    }

    /**
     * Determines whether the right side of the component tile is shielded.
     *
     * @return true if the right side of the component tile is shielded, false otherwise
     */
    public boolean isRightSideShielded(){
        return false;
    }

    /**
     * Determines if the left side of the component tile is configured as a cannon.
     *
     * @return true if the left side is a cannon, false otherwise
     */
    public boolean isLeftSideCannon(){
        return false;
    }

    /**
     * Determines if the bottom side of the component tile has a cannon.
     *
     * @return true if the bottom side of the component tile has a cannon, false otherwise
     */
    public boolean isBottomSideCannon(){
        return false;
    }

    /**
     * Determines whether the top side of the component tile is equipped with a cannon.
     *
     * @return true if the top side of the tile is a cannon, false otherwise
     */
    public boolean isTopSideCannon(){
        return false;
    }

    /**
     * Checks if the right side of the component tile is equipped with a cannon.
     *
     * @return true if the right side of the component tile has a cannon, false otherwise
     */
    public boolean isRightSideCannon(){
        return false;
    }

    /**
     * Determines if the top side of the component tile represents an engine.
     *
     * @return true if the top side is an engine, false otherwise.
     */
    public boolean isTopSideEngine(){
        return false;
    }

    /**
     * Determines if the left side of the component tile has an engine.
     *
     * @return true if the left side of the component tile is an engine, false otherwise
     */
    public boolean isLeftSideEngine(){
        return false;
    }

    /**
     * Checks if the right side of the component tile is an engine.
     *
     * @return true if the right side is an engine, false otherwise
     */
    public boolean isRightSideEngine(){
        return false;
    }

    /**
     * Determines whether the bottom side of the component tile has an engine.
     *
     * @return true if the bottom side of the component tile is an engine; false otherwise.
     */
    public boolean isBottomSideEngine(){
        return false;
    }

    /**
     * Retrieves the engine strength of the component tile.
     *
     * @return the strength of the engine, represented as an integer value
     */
    public int getEngineStrength(){
        return 0;
    }

    /**
     * Retrieves the number of batteries associated with this component tile.
     *
     * @return the number of batteries available in this component tile
     */
    public int getNumOfBatteries() {
        return 0;
    }

    /**
     * Removes one battery token from this component.
     *
     * The method is used to decrease the number of battery tokens
     * associated with the component tile. It may be overridden or
     * implemented in subclasses to provide specific behavior based
     * on the type of component.
     */
    public void removeBatteryToken(){
        return;
    }

    /**
     * Calculates and retrieves the strength of the cannon for this component tile.
     * The cannon strength represents the offensive power held by the component
     * and is determined based on its internal configurations and properties.
     *
     * @return the strength of the cannon as a double value
     */
    public double getCannonStrength(){
        return 0;
    }

    /**
     * Removes a crew member from the component tile.
     *
     * This method is expected to be used in component tiles that can hold crew members, such as cabins.
     * It decreases the count of crew members associated with the component if applicable.
     *
     * Note that the implementation of this method may vary depending on the specific
     * subclass or context where the method is overridden.
     */
    public void removeCrewMember(){
        return;
    }

    /**
     * Sets the number of astronauts in the component tile.
     *
     * This method is used to initialize or reset the number of astronauts
     * in the context of a component tile. It ensures that the component
     * tile can function with the appropriate number of astronauts.
     *
     * For implementation in subclasses such as Cabin, this method
     * may modify the number of astronauts to a specific predefined value
     * based on game mechanics.
     */
    public void putAstronauts(){
        return;
    }

    /**
     * Determines if a given GoodBlock can be placed on the component tile.
     *
     * @param gb the GoodBlock to check for placement
     * @return true if the GoodBlock can be placed, false otherwise
     */
    public boolean isBlockPlaceable(GoodBlock gb){
        return false;
    }

    /**
     * Retrieves the color associated with the add-on of a component tile.
     *
     * @return a String representing the add-on color, or null if no color is set.
     */
    public String getAddonColor(){
        return null;
    }

    /**
     * Adds an alien of the specified color to the component tile.
     * The color indicates the type or category of the alien to be added.
     *
     * @param color the color of the alien to add. Must be a non-null, non-empty string
     *              representing a valid alien type.
     */
    public void putAlien(String color){
        return;
    }

    /**
     * Retrieves the number of crew members currently associated with this component tile.
     *
     * @return the current number of crew members
     */
    public int getCrewNumber(){
        return 0;
    }

    /**
     * Checks if an alien of the specified color is present on the component tile.
     *
     * @param color the color of the alien to check for
     * @return true if an alien of the specified color is present, false otherwise
     */
    public boolean isAlienPresent(String color){
        return false;
    }

    /**
     * Retrieves a mapping of the available GoodBlocks along with their respective quantities.
     * This method provides an overview of all the GoodBlocks currently associated with
     * this component and their count.
     *
     * @return a map where the keys are the GoodBlock instances and the values are
     *         the corresponding quantities of each GoodBlock.
     */
    public Map<GoodBlock, Integer> getGoodBlocks(){
        return null;
    }

    /**
     * Determines whether the component tile is a starting cabin.
     *
     * @return true if the component tile is designated as a starting cabin, false otherwise
     */
    public boolean isStartingCabin(){
        return false;
    }

    /**
     * Checks whether the specified GoodBlock is present in the component.
     *
     * @param gb the GoodBlock to check within this component
     * @return true if the specified GoodBlock is present, false otherwise
     */
    public boolean hasGoodBlock(GoodBlock gb) {
        return false;
    }

    /**
     * Determines whether this component tile is a cabin.
     *
     * @return true if this component tile represents a cabin, false otherwise
     */
    public boolean isCabin() {
        return false;
    }

    /**
     * Determines if the current component tile functions as a shield generator.
     *
     * @return true if the component tile is a shield generator, false otherwise
     */
    public boolean isShieldGenerator() {
        return false;
    }

    /**
     * Determines if the component tile has a double cannon feature.
     * A double cannon tile includes dual cannon functionality for certain
     * gameplay mechanics or visual representation.
     *
     * @return true if the component tile is equipped with a double cannon,
     *         false otherwise.
     */
    public boolean isDoubleCannon() {
        return false;
    }

    /**
     * Determines whether the component tile is a double engine.
     *
     * @return true if the component tile has a double engine, false otherwise.
     */
    public boolean isDoubleEngine() {
        return false;
    }

    /**
     * Determines if the current component tile is a battery.
     *
     * @return true if the component tile is a battery, false otherwise.
     */
    public boolean isBattery() {
        return false;
    }

    /**
     * Checks whether a purple alien is present in the component tile.
     *
     * @return true if a purple alien is present in the component; false otherwise
     */
    public boolean isPurpleAlienPresent(){ return false; }

    /**
     * Checks whether a brown alien is present on the component tile.
     *
     * @return true if a brown alien is present, false otherwise.
     */
    public boolean isBrownAlienPresent(){ return false; }

    /**
     * Returns the number of blocks of the specified type currently associated with this component.
     *
     * @param gb the type of GoodBlock to check for (e.g., REDBLOCK, YELLOWBLOCK, GREENBLOCK, or BLUEBLOCK)
     * @return the number of blocks of the specified type
     */
    public int getNumGoodBlocks(GoodBlock gb) {
        return 0;
    }

    /**
     * Retrieves the drawable text-based UI (TUI) representation of the component tile.
     * The returned object provides methods to visually represent the component tile
     * in a text-based format.
     *
     * @return an instance of DrawableComponentTileTUI representing the text-based UI of the component tile
     */
    public DrawableComponentTileTUI getDrawableTUI() {return null;}

    /**
     * Retrieves the number of rotations that the component tile has undergone.
     * The value represents how many times the tile has been rotated clockwise
     * from its original position.
     *
     * @return the number of rotations of the component tile
     */
    public int getNumOfRotations() {
        return numOfRotations;
    }

    /**
     * Determines whether the component is currently activated.
     *
     * @return true if the component is activated; false otherwise
     */
    public boolean isActivated() {
        return false;
    }

    /**
     * Retrieves the drawable representation of the component tile.
     *
     * @return an instance of {@code DrawableComponentTile} representing a visual or graphical
     *         depiction of the component tile.
     */
    public abstract DrawableComponentTile getDrawable();
}
