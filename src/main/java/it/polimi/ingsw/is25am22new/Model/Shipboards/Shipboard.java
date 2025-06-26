package it.polimi.ingsw.is25am22new.Model.Shipboards;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StartingCabin;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Represents a Shipboard used in a game to manage components, resources, and
 * their configurations while adhering to specific game rules and constraints.
 *
 * The Shipboard is characterized by a grid of component tiles, a standby area
 * for temporary tile storage, rules for validating its configuration, and
 * methods for managing game mechanics such as scoring, resource management,
 * and tile placement.
 *
 * Fields:
 * - abandoned: Whether the shipboard is in an abandoned state.
 * - daysOnFlight: Number of days the shipboard has been in flight.
 * - color: The color of the shipboard.
 * - nickname: Nickname associated with the shipboard.
 * - componentTilesGrid: The main grid representing the component tiles on the shipboard.
 * - standbyComponent: Temporary storage area for component tiles.
 * - discardedTiles: Count of tiles that have been discarded.
 * - finishedShipboard: Indicates if the shipboard has completed its setup.
 * - rocketPlaced: Whether a rocket is placed on the shipboard.
 * - correctingShip: Tracks if the ship is being corrected.
 * - CosmicCredits: Total credits representing the ship's resources.
 * - bank: The bank with which the shipboard interacts for credit-related operations.
 * - tileInHand: Represents the current tile being handled.
 * - componentTilesGridCopy: A copy of the main component grid for reference or backup.
 * - standbyComponentCopy: A copy of the standby component tiles for reference or backup.
 */
public class Shipboard implements Serializable {

    //all the info related to the shipboard and the owner of the ship
    private boolean abandoned;
    private int daysOnFlight;
    private String color;
    private String nickname;
    private transient ComponentTilesGrid componentTilesGrid;
    private transient Optional<ComponentTile>[] standbyComponent;
    private int discardedTiles;
    private boolean finishedShipboard;
    private boolean rocketPlaced;
    private boolean correctingShip;
    private int CosmicCredits;
    private Bank bank;
    private ComponentTile tileInHand;
    private ComponentTile[][] componentTilesGridCopy;
    private ComponentTile[] standbyComponentCopy;

    /**
     * Constructs a new Shipboard instance with the specified parameters.
     *
     * @param color the color of the Shipboard
     * @param nickname the nickname of the Shipboard
     * @param bank the Bank instance associated with the Shipboard
     */
    public Shipboard(String color, String nickname, Bank bank) {
        this.abandoned = false;
        this.tileInHand = null;
        this.daysOnFlight = 0;
        this.color = color;
        this.nickname = nickname;
        this.componentTilesGrid = new ComponentTilesGrid();
        this.standbyComponent = (Optional<ComponentTile>[]) new Optional[2];
        this.standbyComponent[0] = Optional.empty();
        this.standbyComponent[1] = Optional.empty();
        this.discardedTiles = 0;
        this.finishedShipboard = false;
        this.rocketPlaced = false;
        this.correctingShip = false;
        this.CosmicCredits = 0;
        this.bank = bank;
        this.componentTilesGridCopy = new ComponentTile[5][7];
        this.standbyComponentCopy = new ComponentTile[2];
        weldComponentTile(new StartingCabin(colorToPngName(color), Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, color), 2, 3);
        componentTilesGridCopy[2][3] = new StartingCabin(colorToPngName(color), Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, color);
    }

    /**
     * Retrieves the color value.
     *
     * @return the color as a String
     */
    public String getColor() {
        return color;
    }

    /**
     * Calculates the score for the shipboard based on specific game rules.
     * The score is determined by evaluating the storage compartments of the component tiles
     * and the type and quantity of good blocks contained within them. Additional adjustments
     * are made based on the abandoned state of the ship and the number of discarded tiles.
     *
     * @return the calculated score for the shipboard as an integer
     */
    public int getScore(){
        int score = 0;
        //"selling the goods" inside the component tiles
        for(Optional<ComponentTile> ct : componentTilesGrid){
            //it has to be present and it has to be a storageComponentTile
            if(ct.isPresent() && ct.get().isStorageCompartment()){
                for(GoodBlock gb : ct.get().getGoodBlocks().keySet()){
                    if(gb.equals(GoodBlock.REDBLOCK)){
                        score += 4 * ct.get().getGoodBlocks().get(gb);
                    }
                    else if(gb.equals(GoodBlock.YELLOWBLOCK)){
                        score += 3 * ct.get().getGoodBlocks().get(gb);
                    }
                    else if(gb.equals(GoodBlock.GREENBLOCK)){
                        score += 2 * ct.get().getGoodBlocks().get(gb);
                    }
                    else if(gb.equals(GoodBlock.BLUEBLOCK)){
                        score += ct.get().getGoodBlocks().get(gb);
                    }
                }
            }
        }
        if(abandoned)
            score /= 2;

        //Lose 1 point for each component in discardPile
        score -= discardedTiles;

        return score;
    }

    /**
     * Converts a given color name to its corresponding PNG image file name.
     *
     * @param color the name of the color (e.g., "red", "blue", "green", "yellow")
     * @return the file name of the PNG image corresponding to the specified color,
     *         or null if the color does not match any predefined options
     */
    private String colorToPngName(String color){
        return switch (color) {
            case "red" -> "GT-new_tiles_16_for web52.jpg";
            case "blue" -> "GT-new_tiles_16_for web33.jpg";
            case "green" -> "GT-new_tiles_16_for web34.jpg";
            case "yellow" -> "GT-new_tiles_16_for web61.jpg";
            default -> null;
        };
    }

    /**
     * Retrieves the nickname of the entity.
     *
     * @return the nickname as a String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Retrieves the number of days the flight has been active.
     *
     * @return the number of days the flight has been active as an integer.
     */
    public int getDaysOnFlight(){
        return daysOnFlight;
    }

    /**
     * Retrieves the current ComponentTilesGrid object associated with this instance.
     *
     * @return the ComponentTilesGrid object.
     */
    public ComponentTilesGrid getComponentTilesGrid() {
        return this.componentTilesGrid;
    }

    /**
     * Replaces the current grid of component tiles with a new instance of
     * {@code ComponentTilesGrid}.
     *
     * This method initializes {@code componentTilesGrid} by assigning it a new
     * instance of the {@code ComponentTilesGrid} class, effectively clearing or
     * resetting any previously set grid contents.
     */
    public void setNewComponentTilesGrid() {
        this.componentTilesGrid = new ComponentTilesGrid();
    }

    /**
     * Sets the number of days on flight.
     *
     * @param daysOnFlight the number of days the flight is planned to last
     */
    public void setDaysOnFlight(int daysOnFlight){
        this.daysOnFlight = daysOnFlight;
    }

    /**
     * Sets the specified ComponentTile at the given position in the component tiles grid copy.
     *
     * @param i the row index in the grid
     * @param j the column index in the grid
     * @param componentTile the ComponentTile to set at the specified position
     */
    public void setComponentTilesGridCopy(int i, int j, ComponentTile componentTile) {
        this.componentTilesGridCopy[i][j] = componentTile;
    }

    /**
     * Sets the standby component copy at the specified index.
     *
     * @param i the index at which the component should be set
     * @param componentTile the component to set at the specified index
     */
    public void setStandbyComponentCopy(int i, ComponentTile componentTile) {
        this.standbyComponentCopy[i] = componentTile;
    }

    /**
     * Sets a ComponentTile object at the specified position on the grid.
     *
     * @param i The row index where the ComponentTile should be placed.
     * @param j The column index where the ComponentTile should be placed.
     * @param componentTile The ComponentTile object to be placed at the specified grid position.
     */
    public void setComponentTileOnGrid(int i, int j, ComponentTile componentTile){
        componentTilesGrid.set(i, j, componentTile);
    }

    /**
     * Retrieves a copy of the component tile at the specified grid coordinates.
     *
     * @param i the row index of the component tile in the grid
     * @param j the column index of the component tile in the grid
     * @return a copy of the ComponentTile located at the specified grid coordinates
     */
    public ComponentTile getComponentTilesGridCopy(int i, int j) {
        return componentTilesGridCopy[i][j];
    }

    /**
     * Retrieves a copy of the standby component at the specified index.
     *
     * @param i the index of the standby component copy to retrieve
     * @return the standby component copy at the specified index
     */
    public ComponentTile getStandbyComponentCopy(int i) {
        return standbyComponentCopy[i];
    }

    /**
     * Retrieves the array of standby components, represented as Optional elements.
     * Each element in the array may or may not contain a ComponentTile instance.
     *
     */
    public Optional<ComponentTile>[] getStandbyComponent() {
        return standbyComponent;
    }

    /**
     * Sets the standby component at the specified index.
     *
     * @param i the index at which the standby component is to be set
     * @param standbyComponent the component to set as the standby, wrapped in an Optional
     */
    public void setStandbyComponent(int i, Optional<ComponentTile> standbyComponent) {
        this.standbyComponent[i] = standbyComponent;
    }

    /**
     * Initializes the standbyComponent array with a size of 2, where both elements are set to Optional.empty().
     * This method is used to reset or prepare the standbyComponent to a default state with no assigned values.
     */
    public void setNewStandbyComponent() {
        this.standbyComponent = (Optional<ComponentTile>[]) new Optional[2];
        this.standbyComponent[0] = Optional.empty();
        this.standbyComponent[1] = Optional.empty();
    }

    /**
     * Welds a specified component tile to the shipboard at the given grid coordinates.
     * If a component tile is already present at the specified coordinates, an exception is thrown.
     *
     * @param ct the component tile to be placed on the grid
     * @param i the row index of the grid where the component tile will be placed
     * @param j the column index of the grid where the component tile will be placed
     * @throws IllegalStateException if there is already a component tile present at the specified grid position
     */
    public void weldComponentTile (ComponentTile ct, int i, int j){
        if (componentTilesGrid.get(i, j).isPresent())
            throw new IllegalStateException("Cannot weld tile on an already existing one");
        componentTilesGrid.set(i, j, ct);
    }

    /**
     * Places a component tile in one of the standby positions, which allows storing up to two component tiles temporarily.
     * If both standby positions are already occupied, an exception is thrown.
     *
     * @param ct the component tile to be placed in the standby position
     * @throws IllegalStateException if both standby positions are already occupied
     */
    public void standbyComponentTile (ComponentTile ct){
        if(standbyComponent[0].isEmpty())
            standbyComponent[0] = Optional.of(ct);
        else if (standbyComponent[1].isEmpty())
            standbyComponent[1] = Optional.of(ct);
        else
            throw new IllegalStateException("Cannot standby other tiles");
    }

    /**
     * Picks a component tile from the standby position at the specified index.
     * If the standby position at the given index is empty, an exception will be thrown.
     * Once the tile is picked, the standby position is set to empty.
     *
     * @param index the index of the standby position to pick the component tile from
     * @return the component tile picked from the standby position
     * @throws IllegalStateException if the standby position at the specified index is empty
     */
    public ComponentTile pickStandByComponentTile (int index) {
        if(standbyComponent[index].isEmpty())
            throw new IllegalStateException("Cannot pick standby component tile");
        ComponentTile ct = standbyComponent[index].get();
        standbyComponent[index] = Optional.empty();
        return ct;
    }

    /**
     * Destroys a tile located at the specified grid coordinates.
     * If the tile does not exist at the given position, an exception is thrown.
     * In addition to removing the tile, updates adjacent tiles to account for
     * potential changes caused by the removal (e.g., alien addon management).
     *
     * @param i the row index of the tile to be destroyed
     * @param j the column index of the tile to be destroyed
     * @throws IllegalStateException if the specified grid position is empty and there is no tile to destroy
     */
    public void destroyTile (int i, int j){
        if(componentTilesGrid.get(i, j).isEmpty())
            throw new IllegalStateException("Cannot destroy a non existing tile");
        componentTilesGrid.set(i, j, null);
        discardedTiles++;
        manageAlienAddonRemoval(i-1, j);
        manageAlienAddonRemoval(i+1, j);
        manageAlienAddonRemoval(i, j-1);
        manageAlienAddonRemoval(i, j+1);
    }

    /**
     * Manages the removal and potential re-placement of alien addons on a component tile at the specified grid coordinates.
     * If an alien of a specific color ("purple" or "brown") is present on the component tile, it removes the alien crew member.
     * If the position is valid for placing an alien of that color, the alien is re-added to the tile.
     *
     * @param i the row index in the component grid to manage the alien addon
     * @param j the column index in the component grid to manage the alien addon
     */
    private void manageAlienAddonRemoval(int i, int j) {
        String color = null;
        if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isAlienPresent("purple")){
            color = "purple";
        }
        else if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isAlienPresent("brown")){
            color = "brown";
        }
        if(color != null){
            componentTilesGrid.get(i, j).get().removeCrewMember();
            if(isAlienPlaceable(i, j, color))
                componentTilesGrid.get(i, j).get().putAlien(color);
        }
    }

    /**
     * Checks if the shipboard is empty by verifying if all Optional tiles in the component grid are empty.
     *
     * @return true if all tiles in the component grid are empty, false if at least one tile is present.
     */
    public boolean isShipboardEmpty(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent()){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the validity of the shipboard configuration based on the placement of components.
     * This method ensures that the components such as engines and cannons meet placement rules,
     * verifies connections between tiles, and ensures all tiles are validated.
     *
     * The validation process includes:
     * - Ensuring engines and cannons are correctly placed without conflicts.
     * - Checking the connectivity of tiles using a recursive validation method.
     * - Ensuring all tiles are assigned a valid color during the validation process.
     *
     * @return true if the shipboard is valid according to the placement and connection rules;
     *         false if any validation rule is violated.
     */
    public boolean checkShipboard (){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++) {
                if(componentTilesGrid.get(i, j).isPresent()){
                    if(componentTilesGrid.get(i, j).get().isLeftSideEngine() || componentTilesGrid.get(i, j).get().isRightSideEngine()
                            || componentTilesGrid.get(i, j).get().isTopSideEngine())
                        return false;
                    if(componentTilesGrid.get(i, j).get().isBottomSideEngine() && componentTilesGrid.get(i+1, j).isPresent())
                        return false;
                    if(componentTilesGrid.get(i, j).get().isTopSideCannon() && componentTilesGrid.get(i-1, j).isPresent() ||
                       componentTilesGrid.get(i, j).get().isBottomSideCannon() && componentTilesGrid.get(i+1, j).isPresent() ||
                       componentTilesGrid.get(i, j).get().isRightSideCannon() && componentTilesGrid.get(i, j+1).isPresent() ||
                       componentTilesGrid.get(i, j).get().isLeftSideEngine() && componentTilesGrid.get(i, j-1).isPresent())
                        return false;
                }
            }
        }// verifies if cannons and engines are valid

        for(Optional<ComponentTile> ct : componentTilesGrid){
            ct.ifPresent(c -> c.setColor(-1));
        }

        boolean found = false;
        int firstTileX = -1, firstTileY = -1;

        for (int i = 0; i < 5 && !found; i++) {
            for (int j = 0; j < 7 && !found; j++) {
                if (componentTilesGrid.get(i, j).isPresent()) {
                    found = true; // Tile found, exit loops
                    firstTileX = i;
                    firstTileY = j;
                } else if (i == 4 && j == 6) {
                    return true; // Entire ship is empty, return valid state
                }
            }
        }

        if(!tileConnectedProperly(firstTileX, firstTileY)) //check if the connectors are properly connected with a recursive method
            return false;

        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().getColor() == -1)
                return false;
        }// verifies all Tiles have been colored

        return true;
    }

    /**
     * Checks if the tile at the specified position in the grid is properly connected
     * to its adjacent tiles based on predefined conditions and matching sides.
     *
     * @param i the row index of the tile in the grid
     * @param j the column index of the tile in the grid
     * @return true if the tile at position (i, j) is properly connected to its adjacent tiles, false otherwise
     */
    private boolean tileConnectedProperly(int i, int j){
        if(componentTilesGrid.get(i, j).isEmpty() || componentTilesGrid.get(i, j).get().getColor() == 1)
            return true;
        ComponentTile ct = componentTilesGrid.get(i, j).get();
        ct.setColor(1);
        Optional<ComponentTile> upperTile = componentTilesGrid.get(i-1, j);
        Optional<ComponentTile> lowerTile = componentTilesGrid.get(i+1, j);
        Optional<ComponentTile> leftTile = componentTilesGrid.get(i, j-1);
        Optional<ComponentTile> rightTile = componentTilesGrid.get(i, j+1);
        if (upperTile.isPresent() && upperTile.get().getColor() == -1 && !sidesMatch(ct.getTopSide(), upperTile.get().getBottomSide()) ||
            lowerTile.isPresent() && lowerTile.get().getColor() == -1 && !sidesMatch(ct.getBottomSide(), lowerTile.get().getTopSide()) ||
            leftTile.isPresent() && leftTile.get().getColor() == -1 && !sidesMatch(ct.getLeftSide(), leftTile.get().getRightSide()) ||
                rightTile.isPresent() && rightTile.get().getColor() == -1 && !sidesMatch(ct.getRightSide(), rightTile.get().getLeftSide()))
            return false;
        return (upperTile.isPresent() && ct.getTopSide().equals(Side.SMOOTH) && upperTile.get().getBottomSide().equals(Side.SMOOTH) || tileConnectedProperly(i-1, j)) && //if the adjacent sides are smooth I must not visit it with the recursive algorithm
                (lowerTile.isPresent() && ct.getBottomSide().equals(Side.SMOOTH) && lowerTile.get().getTopSide().equals(Side.SMOOTH) || tileConnectedProperly(i+1, j)) &&
                (leftTile.isPresent() && ct.getLeftSide().equals(Side.SMOOTH) && leftTile.get().getRightSide().equals(Side.SMOOTH) || tileConnectedProperly(i, j-1)) &&
                (rightTile.isPresent() && ct.getRightSide().equals(Side.SMOOTH) && rightTile.get().getLeftSide().equals(Side.SMOOTH) ||tileConnectedProperly(i, j+1));
    }

    /**
     * Determines if two sides of a component tile match based on the given rules.
     * This method evaluates compatibility between two sides to ensure a valid connection,
     * taking into account specific characteristics of each side type.
     *
     * @param s1 the first side of the component tile to be compared
     * @param s2 the second side of the component tile to be compared
     * @return true if the sides are compatible and match; false otherwise
     */
    private boolean sidesMatch(Side s1, Side s2){
        if(s1.equals(Side.SMOOTH) && !s2.equals(Side.SMOOTH) || !s1.equals(Side.SMOOTH) && s2.equals(Side.SMOOTH)) //connector adjacent to smooth side
            return false;
        if(s1.equals(Side.ONEPIPE) && s2.equals(Side.TWOPIPES) || s1.equals(Side.TWOPIPES) && s2.equals(Side.ONEPIPE)) //connectors incompatible
            return false;
        return true;
    }

    /**
     * Marks an entity or resource as abandoned.
     * This method updates the state of the object by setting the
     * 'abandoned' flag to true, indicating it is no longer in use
     * or has been deliberately relinquished.
     */
    public void abandons (){
        abandoned = true;
    }

    /**
     * Checks if the entity has been removed or ejected from a group or context.
     *
     * @return true if the entity has been kicked out, false otherwise
     */
    public boolean hasBeenKickedOut(){
        return abandoned;
    }

    /**
     * Counts the number of exposed connectors in a grid of component tiles.
     * Connectors are considered exposed if they are not adjacent to another component tile
     * and are not smooth on the respective side.
     *
     * @return the total number of exposed connectors in the grid
     */
    public int countExposedConnectors (){
        int exposedConnectors = 0;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++) {
                if(componentTilesGrid.get(i, j).isPresent()){
                    if(componentTilesGrid.get(i+1, j).isEmpty() && !componentTilesGrid.get(i, j).get().getBottomSide().equals(Side.SMOOTH))
                        exposedConnectors++;
                    if(componentTilesGrid.get(i-1, j).isEmpty() && !componentTilesGrid.get(i, j).get().getTopSide().equals(Side.SMOOTH))
                        exposedConnectors++;
                    if(componentTilesGrid.get(i, j+1).isEmpty() && !componentTilesGrid.get(i, j).get().getRightSide().equals(Side.SMOOTH))
                        exposedConnectors++;
                    if(componentTilesGrid.get(i, j-1).isEmpty() && !componentTilesGrid.get(i, j).get().getLeftSide().equals(Side.SMOOTH))
                        exposedConnectors++;
                }
            }
        }
        return exposedConnectors;
    }

    /**
     * Determines if there is a valid cannon on the right side for the given index.
     *
     * @param i The index to check in the grid.
     * @return true if a valid cannon exists on the right side, false otherwise.
     */
    public boolean isRightSideCannon (int i){
        for(int j = 6; j >= 0; j--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isRightSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    /**
     * Determines if the given index corresponds to a left-side cannon with strength greater than 0.
     *
     * @param i the index to check within the component grid
     * @return true if the index corresponds to a left-side cannon; false otherwise
     */
    public boolean isLeftSideCannon (int i){
        for(int j = 0; j < 7; j++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isLeftSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    /**
     * Determines if there is a top side cannon at the specified column index
     * within the first five rows of the component grid and checks its strength.
     *
     * @param j the column index to check for a top side cannon
     * @return true if a top side cannon is found with a strength greater than 0,
     *         false otherwise
     */
    public boolean isTopSideCannon (int j){
        for(int i = 0; i < 5; i++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isTopSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    /**
     * Determines if there is a bottom-side cannon with a strength greater than zero
     * in the column specified by the parameter.
     *
     * @param j the column index to check for a bottom-side cannon
     * @return true if a bottom-side cannon with a strength greater than zero is found, false otherwise
     */
    public boolean isBottomSideCannon (int j){
        for(int i = 4; i >= 0; i--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isBottomSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    /**
     * Checks if any component tile in the grid has its right side shielded.
     *
     * @return true if at least one component tile is present in the grid
     *         and has its right side shielded, otherwise false.
     */
    public boolean isRightSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
                if(ct.isPresent() && ct.get().isRightSideShielded())
                    return true;
        }
        return false;
    }

    /**
     * Determines if any component tile in the grid has its left side shielded.
     *
     * The method iterates through the grid of optional component tiles and checks if
     * there is any tile present that has its left side shielded.
     *
     * @return true if at least one component tile in the grid has its left side shielded, otherwise false
     */
    public boolean isLeftSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isLeftSideShielded())
                return true;
        }
        return false;
    }

    /**
     * Determines if the top side of any component tile within the grid is shielded.
     * The method iterates over the grid of component tiles and checks for the presence
     * of a tile where its top side is marked as shielded.
     *
     * @return true if the top side of at least one component tile is shielded; false otherwise
     */
    public boolean isTopSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isTopSideShielded())
                return true;
        }
        return false;
    }

    /**
     * Checks if any component tile in the grid has its bottom side shielded.
     *
     * Iterates through the grid of component tiles and determines if there is
     * at least one tile whose bottom side is shielded.
     *
     * @return true if at least one component tile's bottom side is shielded, false otherwise
     */
    public boolean isBottomSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isBottomSideShielded())
                return true;
        }
        return false;
    }

    /**
     * Determines whether an exposed connector is present on the top side of the specified column
     * within the grid of component tiles.
     *
     * @param numOfColumn the column index to check for an exposed connector on the top side
     * @return true if there is an exposed connector on the top side of the specified column;
     *         false if the top side is smooth or there are no components in the column
     */
    public boolean isExposedConnectorOnTop (int numOfColumn){
        for (int i = 0; i < 5; i++) {
            if (componentTilesGrid.get(i, numOfColumn).isPresent()) {
                return !componentTilesGrid.get(i, numOfColumn).get().getTopSide().equals(Side.SMOOTH);
            }
        }
        return false;
    }

    /**
     * Determines if there is an exposed connector on the bottom side of the column
     * specified by the given column index in the component tiles grid.
     *
     * @param numOfColumn the index of the column to check in the component tiles grid
     * @return true if there is an exposed connector on the bottom side of the specified column,
     *         false otherwise
     */
    public boolean isExposedConnectorOnBottom (int numOfColumn){
        for (int i = 4; i >= 0; i--) {
            if (componentTilesGrid.get(i, numOfColumn).isPresent()) {
                return !componentTilesGrid.get(i, numOfColumn).get().getBottomSide().equals(Side.SMOOTH);
            }
        }
        return false;
    }

    /**
     * Checks if there is an exposed connector on the left side of any component
     * in the specified row of the grid.
     *
     * @param numOfRow the row index to check for an exposed connector on the left side
     * @return true if there is at least one component in the specified row with an
     *         exposed connector on the left side; false otherwise
     */
    public boolean isExposedConnectorOnLeft(int numOfRow) {
        for (int j = 0; j < 7; j++) {
            if (componentTilesGrid.get(numOfRow, j).isPresent()) {
                return !componentTilesGrid.get(numOfRow, j).get().getLeftSide().equals(Side.SMOOTH);
            }
        }
        return false;
    }

    /**
     * Checks if the right side of the first present component in the specified row
     * is an exposed connector.
     *
     * @param numOfRow the row index to check within the grid.
     * @return true if the right side of the first present component in the row
     *         is an exposed connector; false otherwise.
     */
    public boolean isExposedConnectorOnRight(int numOfRow) {
        for (int j = 6; j >= 0; j--) {
            if (componentTilesGrid.get(numOfRow, j).isPresent()) {
                return !componentTilesGrid.get(numOfRow, j).get().getRightSide().equals(Side.SMOOTH);
            }
        }
        return false;
    }

    /**
     * Checks if a specific column in the grid is empty.
     *
     * @param numOfColumn the index of the column to check
     * @return true if the column is empty, false otherwise
     */
    public boolean columnEmpty (int numOfColumn){
        for (int i = 0; i < 5; i++) {
            if (componentTilesGrid.get(i, numOfColumn).isPresent()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a specified row in the grid is empty.
     *
     * @param numOfRow the index of the row to check
     * @return true if all elements in the specified row are empty, false otherwise
     */
    public boolean rowEmpty (int numOfRow){
        for (int i = 0; i < 7; i++) {
            if (componentTilesGrid.get(numOfRow, i).isPresent()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there is still any crew present on the component tiles grid.
     *
     * This method iterates through a collection of tiles to determine if any tile
     * contains a crew with a number greater than zero.
     *
     * @return true if at least one component tile contains a crew with a number greater than zero;
     *         false otherwise.
     */
    public boolean thereIsStillCrew() {
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().getCrewNumber() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there are still blocks available in the component tiles grid.
     * Iterates through the grid and verifies if any present component tile contains good blocks.
     *
     * @return true if at least one component tile in the grid contains good blocks, false otherwise.
     */
    public boolean thereAreStillBlocks() {
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && !ct.get().getGoodBlocks().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates and returns the total engine strength based on the components available
     * and additional conditions like the presence of a brown alien.
     *
     * @return the total calculated engine strength as an integer
     */
    public int getEngineStrength (){
        int strength = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent())
                strength += ct.get().getEngineStrength();
        }
        if(strength > 0 && isBrownAlienPresent())
            strength += 2;
        return strength;
    }

    /**
     * Calculates and returns the total cannon strength based on the component tiles present and other conditions.
     * The method aggregates the cannon strength values from all present component tiles
     * and applies additional strength if certain conditions, such as the presence of a purple alien, are met.
     *
     * @return the total cannon strength as a double value
     */
    public double getCannonStrength (){
        double strength = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent())
                strength += ct.get().getCannonStrength();
        }
        if(strength > 0 && isPurpleAlienPresent())
            strength += 2;
        return strength;
    }

    /**
     * Checks if a brown alien is present within the component tiles grid.
     *
     * @return true if a brown alien is present, false otherwise.
     */
    public boolean isBrownAlienPresent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isAlienPresent("brown"))
                return true;
        }
        return false;
    }

    /**
     * Checks if a purple alien is present within the component tiles grid.
     *
     * The method iterates through each component tile in the grid. For each tile,
     * it checks if the tile contains an alien with the specified color "purple".
     *
     * @return true if a purple alien is found in any of the tiles, false otherwise.
     */
    public boolean isPurpleAlienPresent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isAlienPresent("purple"))
                return true;
        }
        return false;
    }

    /**
     * Checks if the specified tile, identified by its row and column coordinates,
     * is connected to a cabin in the componentTilesGrid. A tile is considered connected
     * to a cabin if one of its adjacent tiles is a cabin and their matching sides align.
     *
     * @param row the row index of the tile to check
     * @param col the column index of the tile to check
     * @return true if the tile is connected to a cabin, false otherwise
     * @throws RuntimeException if no tile is found at the specified location
     */
    public boolean isConnectedToCabin(int row, int col) throws RuntimeException{
        Optional<ComponentTile> ct = componentTilesGrid.get(row, col);
        if(ct.isPresent()) {
            Optional<ComponentTile> cBot = componentTilesGrid.get(row + 1, col);
            Optional<ComponentTile> cTop = componentTilesGrid.get(row - 1, col);
            Optional<ComponentTile> cLeft = componentTilesGrid.get(row, col - 1);
            Optional<ComponentTile> cRight = componentTilesGrid.get(row, col + 1);
            if ((cBot.isPresent() && cBot.get().isCabin() && sidesMatch(ct.get().getBottomSide(), cBot.get().getTopSide())) ||
                (cTop.isPresent() && cTop.get().isCabin() && sidesMatch(ct.get().getTopSide(), cTop.get().getBottomSide())) ||
                (cLeft.isPresent() && cLeft.get().isCabin() && sidesMatch(ct.get().getLeftSide(), cLeft.get().getRightSide())) ||
                (cRight.isPresent() && cRight.get().isCabin() && sidesMatch(ct.get().getRightSide(), cRight.get().getLeftSide()))
            ) { return true;}
        }
        else {
            throw new RuntimeException("No tile found");
        }
        return false;
    }

    /**
     * Removes the most valuable good blocks up to the specified number.
     * The method attempts to remove red blocks first, followed by yellow, green, and blue blocks,
     * in that order of priority. If the specified number is still not reached, it attempts to
     * remove batteries from component tiles until the desired number or all available resources are removed.
     *
     * @param num the maximum number of good blocks to be removed, starting from the most valuable ones.
     */
    public void removeMostValuableGoodBlocks(int num){
        int stillToRemove = num;
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.REDBLOCK);
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.YELLOWBLOCK);
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.GREENBLOCK);
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.BLUEBLOCK);
        if(stillToRemove > 0) {
            for (Optional<ComponentTile> ct : componentTilesGrid) {
                while (ct.isPresent() && ct.get().isBattery() && stillToRemove > 0 && ct.get().getNumOfBatteries() > 0) {
                    ct.get().removeBatteryToken();
                    stillToRemove--;
                }
            }
        }
    }

    /**
     * Removes at most the specified number of good blocks of the given type from the component tiles grid.
     *
     * @param num the maximum number of good blocks to remove
     * @param block the type of good block to be removed
     * @return the actual number of good blocks removed
     */
    private int removeAtMostNumGoodBlocks(int num, GoodBlock block){
        int removed = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            while(ct.isPresent() && ct.get().hasGoodBlock(block) && removed < num){
                removed++;
                ct.get().removeGoodBlock(block);
                bank.depositGoodBlock(block);
            }
            if(removed == num)
                break;
        }
        return removed;
    }

    /**
     * Retrieves the current amount of cosmic credits.
     *
     * @return the number of cosmic credits as an integer
     */
    public int getCosmicCredits(){
        return CosmicCredits;
    }

    /**
     * Adds the specified amount of cosmic credits to the current balance.
     *
     * @param credit the amount of cosmic credits to be added
     */
    public void addCosmicCredits (int credit){
        CosmicCredits += credit;
    }

    /**
     * Determines if an alien can be placed on the specified tile based on its position
     * and the given color. This checks the current tile conditions, the presence of
     * an alien with the same color, and adjacency rules.
     *
     * @param i the row index of the tile in the grid
     * @param j the column index of the tile in the grid
     * @param color the color associated with the alien to be placed
     * @return true if the alien can be placed on the specified tile, false otherwise
     */
    public boolean isAlienPlaceable (int i, int j, String color){
        if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isStartingCabin())
            return false;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isAlienPresent(color))
                return false;
        }
        if(areAdjacentTilesAddons(i, j, color))
           return true;
        return false;
    }

    /**
     * Determines if any adjacent tiles to the specified coordinates contain an addon of the specified color.
     *
     * @param i the row index of the tile to check adjacency from
     * @param j the column index of the tile to check adjacency from
     * @param color the color of the addon to match against adjacent tiles
     * @return true if any adjacent tiles contain an addon of the specified color, false otherwise
     */
    private boolean areAdjacentTilesAddons(int i, int j, String color){
        return componentTilesGrid.get(i, j+1).isPresent() && componentTilesGrid.get(i, j+1).get().getAddonColor() != null && componentTilesGrid.get(i, j+1).get().getAddonColor().equals(color) ||
               componentTilesGrid.get(i, j-1).isPresent() && componentTilesGrid.get(i, j-1).get().getAddonColor() != null && componentTilesGrid.get(i, j-1).get().getAddonColor().equals(color) ||
               componentTilesGrid.get(i+1, j).isPresent() && componentTilesGrid.get(i+1, j).get().getAddonColor() != null && componentTilesGrid.get(i+1, j).get().getAddonColor().equals(color) ||
               componentTilesGrid.get(i-1, j).isPresent() && componentTilesGrid.get(i-1, j).get().getAddonColor() != null && componentTilesGrid.get(i-1, j).get().getAddonColor().equals(color);
    }

    public int getCrewNumber (){
        int crewnumber = 0;
        for (Optional<ComponentTile> ct : componentTilesGrid) {
            if (ct.isPresent())
                crewnumber += ct.get().getCrewNumber();
        }
        return crewnumber;
    }

    /**
     * Sets the status of whether the shipboard process is finished.
     *
     * @param finishedShipboard a boolean value indicating if the shipboard process is completed.
     */
    public void setFinishedShipboard(boolean finishedShipboard) {
        this.finishedShipboard = finishedShipboard;
    }

    /**
     * Checks if the shipboard process is completed.
     *
     * @return true if the shipboard process is finished, false otherwise
     */
    public boolean isFinishedShipboard(){
        return finishedShipboard;
    }

    /**
     * Highlights shipwrecks on the grid by grouping connected tiles and assigning different colors to each group.
     * The method iterates over all the tiles in the grid, colors uncolored tiles, and propagates the color to
     * connected tiles forming distinct groups.
     *
     * @return the total number of distinct colors used to highlight the shipwreck groups.
     */
    public int highlightShipWrecks(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            ct.ifPresent(c -> c.setColor(-1));
        }//reset colors for the algorithm

        int color = 0;

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++) {
                if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().getColor() == -1){ //if it's not been colored yet
                    spreadColor(i, j, color); //then color it and also all the tiles connected to it
                    color++;    //change color
                }
            }
        }

        return color; //returns the number of colors used
    }

    /**
     * Recursively sets the color of a tile and propagates the color change
     * to adjacent tiles based on their side properties.
     *
     * @param i     The row index of the tile in the grid.
     * @param j     The column index of the tile in the grid.
     * @param color The color to be set on the tile.
     */
    private void spreadColor(int i, int j, int color){
        if(componentTilesGrid.get(i, j).isEmpty() || componentTilesGrid.get(i, j).get().getColor() == color)
            return;
        componentTilesGrid.get(i, j).get().setColor(color);
        if(!componentTilesGrid.get(i, j).get().getBottomSide().equals(Side.SMOOTH)) {
            spreadColor(i+1, j, color);
        }
        if(!componentTilesGrid.get(i, j).get().getTopSide().equals(Side.SMOOTH)) {
            spreadColor(i-1, j, color);
        }
        if(!componentTilesGrid.get(i, j).get().getRightSide().equals(Side.SMOOTH)) {
            spreadColor(i, j+1, color);
        }
        if(!componentTilesGrid.get(i, j).get().getLeftSide().equals(Side.SMOOTH)) {
            spreadColor(i, j-1, color);
        }
    }

    /**
     * Selects a shipwreck based on the specified coordinates and removes all
     * shipwrecks of a different color from the grid.
     *
     * @param i the row index of the grid where the shipwreck is to be chosen
     * @param j the column index of the grid where the shipwreck is to be chosen
     * @throws IllegalArgumentException if no shipwreck is present at the specified coordinates
     */
    public void chooseShipWreck(int i, int j){
        if(componentTilesGrid.get(i, j).isEmpty())
            throw new IllegalArgumentException("There is no ship wreck in such coordinates");
        int color = componentTilesGrid.get(i, j).get().getColor();
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 7; x++) {
                if(componentTilesGrid.get(y, x).isPresent() && componentTilesGrid.get(y, x).get().getColor() != color)
                    componentTilesGrid.set(y, x, null);
            }
        }
    }

    /**
     * Retrieves a {@code ComponentTile} from the grid at the specified grid coordinates.
     *
     * @param i the row index within the grid
     * @param j the column index within the grid
     * @return an {@code Optional} containing the {@code ComponentTile} at the specified grid coordinates,
     *         or an empty {@code Optional} if no tile exists at the given location
     */
    public Optional<ComponentTile> getComponentTileFromGrid(int i, int j){
        return componentTilesGrid.get(i, j);
    }

    /**
     * Calculates and returns the total number of humans present in the component tiles grid.
     * A human is determined by the absence of specific alien types ("brown" and "purple") in a tile.
     *
     * @return the total number of humans across the component tiles grid
     */
    public int getOnlyHumanNumber(){
        int res = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && !ct.get().isAlienPresent("brown") && !ct.get().isAlienPresent("purple"))
                res += ct.get().getCrewNumber();
        }
        return res;
    }

    /**
     * Checks if the current object is marked as abandoned.
     *
     * @return true if the object is marked as abandoned, false otherwise
     */
    public boolean isAbandoned() {
        return abandoned;
    }

    /**
     * Deactivates all components present in the ship that utilize a battery.
     *
     * This method iterates through the collection of component tiles and, for each non-empty
     * tile, triggers the deactivation of the respective component. The deactivation process
     * is executed only for tiles that are not empty, ensuring that no null components
     * are accessed.
     */
    //deactivates all the components present in the ship that use a battery
    public void deactivateAllComponent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            ct.ifPresent(ComponentTile::deactivateComponent);
        }
    }

    /**
     * Sets the current tile in hand to the specified ComponentTile. If a tile
     * is already in hand, it throws an IllegalStateException to prevent holding more
     * than one tile at a time.
     *
     * @param componentTile the ComponentTile to be set as the tile currently in hand,
     *                      or null to clear the tile in hand.
     * @throws IllegalStateException if a tile is already in hand and the specified
     *                               componentTile is not null.
     */
    public void setTileInHand(ComponentTile componentTile) {
        if(componentTile != null && tileInHand != null)
            throw new IllegalStateException("Cannot hold more than two tiles at the same time");
        tileInHand = componentTile;
    }

    /**
     * Retrieves the tile currently held in hand.
     *
     * @return the tile currently in hand as a ComponentTile object
     */
    public ComponentTile getTileInHand() {
        return tileInHand;
    }

    /**
     * Checks if all cabins in the component tile grid have been populated with crew members.
     *
     * This method iterates over the list of tiles in the grid, identifying whether the tile is
     * a cabin and verifying if it has at least one crew member assigned to it. If any cabin is
     * found to have no crew members, the method returns false. If all cabins are populated, it
     * returns true.
     *
     * @return true if all cabins in the grid are populated with at least one crew member; false otherwise
     */
    public boolean allCabinsArePopulated() {
        for(Optional <ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isCabin() && ct.get().getCrewNumber() == 0)
                return false;
        }
        return true;
    }

    /**
     * Retrieves the number of discarded tiles.
     *
     * @return the count of discarded tiles as an integer
     */
    public int getDiscardedTiles() {
        return discardedTiles;
    }

    /**
     * Sets the status indicating whether the rocket has been placed.
     *
     * @param rocketPlaced a boolean value where true indicates the rocket has been placed,
     *                     and false indicates it has not been placed
     */
    public void setRocketPlaced(boolean rocketPlaced) {
        this.rocketPlaced = rocketPlaced;
    }

    /**
     * Checks if the rocket has been placed successfully.
     *
     * @return true if the rocket is placed, false otherwise
     */
    public boolean isRocketPlaced() {
        return rocketPlaced;
    }

    /**
     * Sets the correctingShip flag to indicate whether the ship is being corrected.
     *
     * @param correctingShip the new state of the correctingShip flag; true if the ship is being corrected, false otherwise
     */
    public void setCorrectingShip(boolean correctingShip) {
        this.correctingShip = correctingShip;
    }

    /**
     * Determines whether the ship is currently in the process of correcting its course.
     *
     * @return true if the ship is correcting its course, false otherwise.
     */
    public boolean isCorrectingShip() {
        return correctingShip;
    }
}

/**
 * This class represents a grid of tiles, where each tile is an instance of ComponentTile
 * wrapped in an Optional. The grid is initialized with a fixed default size of 5 rows and 7 columns.
 * It provides methods to set and retrieve tiles and allows iteration over the grid.
 *
 * The class is designed to manage and manipulate a 2D grid of tiles, with several key functionalities:
 * - Retrieve a tile at a specific grid position.
 * - Set a tile at a specific grid position.
 * - Iterate over all tiles in the grid sequentially using an iterator.
 *
 * The grid is initialized with all positions set to Optional.empty(). Each grid position
 * can store either a ComponentTile object or remain empty, ensuring safe handling of null values.
 */
class ComponentTilesGrid implements Iterable<Optional<ComponentTile>>{
    private transient Optional<ComponentTile>[][] componentTilesGrid;
    private final int rows, columns;
    public ComponentTilesGrid(){
        rows = 5;
        columns = 7;
        componentTilesGrid = (Optional<ComponentTile>[][]) new Optional[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                componentTilesGrid[i][j] = Optional.empty();
            }
        }

    }

    /**
     * Provides an iterator over the grid of tiles, allowing sequential traversal of all elements
     * in the {@code ComponentTilesGrid}. Each element in the grid is wrapped in an {@code Optional},
     * ensuring safe handling of empty spaces.
     *
     * @return an iterator that iterates through all {@code Optional<ComponentTile>} elements
     * in the grid, row by row from left to right.
     */
    @Override
    public Iterator<Optional<ComponentTile>> iterator() {
        return new ComponentTilesGridIterator();
    }

    /**
     * Updates the grid at the specified position with a given ComponentTile object.
     * If the specified indices are outside the bounds of the grid, an IndexOutOfBoundsException is thrown.
     * The ComponentTile can be null, in which case the corresponding position in the grid will be cleared.
     *
     * @param i The row index in the grid where the ComponentTile is to be set.
     * @param j The column index in the grid where the ComponentTile is to be set.
     * @param c The ComponentTile to place in the grid, or null to clear the position.
     * @throws IndexOutOfBoundsException if the specified indices are out of bounds.
     */
    public void set(int i, int j, ComponentTile c){
        if(i < 0 || j < 0 || i >= rows || j >= columns){
            throw new IndexOutOfBoundsException();
        }
        componentTilesGrid[i][j] = Optional.ofNullable(c);
    }

    /**
     * Retrieves the {@code ComponentTile} at the specified position in the grid.
     * If the provided indices are out of bounds, returns an empty {@code Optional}.
     *
     * @param i the row index in the grid
     * @param j the column index in the grid
     * @return an {@code Optional} containing the {@code ComponentTile} at the specified position,
     *         or an empty {@code Optional} if the indices are out of bounds
     */
    public Optional<ComponentTile> get(int i, int j){
        if(i < 0 || j < 0 || i >= rows || j >= columns){
            return Optional.empty();
        }
        return componentTilesGrid[i][j];
    }

    /**
     * Iterator implementation for the {@code ComponentTilesGrid} class, allowing sequential traversal over
     * all tiles within the grid. Each tile is represented as an {@code Optional<ComponentTile>} to ensure
     * safe handling of empty grid positions.
     *
     * This iterator traverses the grid row by row, starting from the top-left corner (0,0) and moving
     * left to right across each row. When the end of a row is reached, the iterator moves to the start of
     * the next row. Iteration continues until all grid elements have been visited.
     */
    private class ComponentTilesGridIterator implements Iterator<Optional<ComponentTile>>{
        private int i = 0;
        private int j = 0;

        /**
         * Determines whether there are more elements to iterate over in the grid.
         * Iteration continues as long as the current indices are within the bounds
         * of the grid's rows and columns.
         *
         * @return true if there are more elements to iterate over; false otherwise.
         */
        public boolean hasNext() {
            return i < rows && j < columns;
        }

        /**
         * Returns the next {@code Optional<ComponentTile>} in the grid during iteration.
         * The iteration proceeds from left to right across each row, advancing to the next row
         * when the end of the current row is reached.
         *
         * @return the next {@code Optional<ComponentTile>} in the traversal.
         * @throws NoSuchElementException if there are no more elements to iterate over.
         */
        public Optional<ComponentTile> next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            Optional<ComponentTile> componentTile = componentTilesGrid[i][j];
            j++;
            if(j >= columns){
                j = 0;
                i++;
            }
            return componentTile;
        }
    }
}
