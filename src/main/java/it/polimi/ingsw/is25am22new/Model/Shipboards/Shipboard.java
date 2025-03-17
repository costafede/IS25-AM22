package it.polimi.ingsw.is25am22new.Model.Shipboards;

import it.polimi.ingsw.is25am22new.Model.Bank;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StartingCabin;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Side;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Shipboard {

    private boolean abandoned;
    private int daysOnFlight;
    private String color;
    private String nickname;
    private ComponentTilesGrid componentTilesGrid;
    private Optional<ComponentTile>[] standbyComponent;
    private int discardedTiles;
    private boolean finishedShipboard;
    private int CosmicCredits;
    private Bank bank;

    public Shipboard(String color, String nickname, Bank bank) {
        this.abandoned = false;
        this.daysOnFlight = 0;
        this.color = color;
        this.nickname = nickname;
        this.componentTilesGrid = new ComponentTilesGrid();
        this.standbyComponent = (Optional<ComponentTile>[]) new Optional[2];
        this.discardedTiles = 0;
        this.finishedShipboard = false;
        this.CosmicCredits = 0;
        this.bank = new Bank();
        weldComponentTile(new StartingCabin(colorToPngName(color), Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, color), 2, 3);
    }

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

    private String colorToPngName(String color){
        return switch (color) {
            case "red" -> "GT-new_tiles_16_for web52.png";
            case "blue" -> "GT-new_tiles_16_for web33.png";
            case "green" -> "GT-new_tiles_16_for web34.png";
            case "yellow" -> "GT-new_tiles_16_for web61.png";
            default -> null;
        };
    }

    public String getNickname() {
        return nickname;
    }

    public int getDaysOnFlight(){
        return daysOnFlight;
    }

    public void setDaysOnFlight(int daysOnFlight){
        this.daysOnFlight = daysOnFlight;
    }

    public void weldComponentTile (ComponentTile ct, int i, int j){ componentTilesGrid.set(i, j, ct);}

    public void standbyComponentTile (ComponentTile ct){
        if(standbyComponent[0].isEmpty()){
            standbyComponent[0] = Optional.of(ct);
        }
        else if (standbyComponent[1].isEmpty()) {
            standbyComponent[1] = Optional.of(ct);
        }
    }

    public ComponentTile pickStandByComponentTile (int index) {
        ComponentTile ct = standbyComponent[index].get();
        standbyComponent[index] = Optional.empty();
        return ct;
    }

    public void destroyTile (int i, int j){
        componentTilesGrid.set(i, j, null);
        discardedTiles++;
        manageAlienAddonRemoval(i-1, j);
        manageAlienAddonRemoval(i+1, j);
        manageAlienAddonRemoval(i, j-1);
        manageAlienAddonRemoval(i, j+1);
    }

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

    public boolean isShipboardEmpty(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent()){
                return false;
            }
        }
        return true;
    }

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

    private boolean tileConnectedProperly(int i, int j){
        if(componentTilesGrid.get(i, j).isEmpty() || componentTilesGrid.get(i, j).get().getColor() == 1)
            return true;
        ComponentTile ct = componentTilesGrid.get(i, j).get();
        ct.setColor(1);
        if (componentTilesGrid.get(i-1, j).isPresent() && componentTilesGrid.get(i-1, j).get().getColor() == -1 && !sidesMatch(ct.getTopSide(), componentTilesGrid.get(i-1, j).get().getBottomSide()) ||
            componentTilesGrid.get(i+1, j).isPresent() && componentTilesGrid.get(i+1, j).get().getColor() == -1 && !sidesMatch(ct.getBottomSide(), componentTilesGrid.get(i+1, j).get().getTopSide()) ||
            componentTilesGrid.get(i, j-1).isPresent() && componentTilesGrid.get(i, j-1).get().getColor() == -1 && !sidesMatch(ct.getLeftSide(), componentTilesGrid.get(i, j-1).get().getRightSide()) ||
            componentTilesGrid.get(i, j+1).isPresent() && componentTilesGrid.get(i, j+1).get().getColor() == -1 && !sidesMatch(ct.getRightSide(), componentTilesGrid.get(i, j+1).get().getLeftSide()))
            return false;
        return tileConnectedProperly(i-1, j) && tileConnectedProperly(i+1, j) && tileConnectedProperly(i, j-1) && tileConnectedProperly(i, j+1);
    }

    private boolean sidesMatch(Side s1, Side s2){
        if(s1.equals(Side.SMOOTH) && !s2.equals(Side.SMOOTH) || !s1.equals(Side.SMOOTH) && s2.equals(Side.SMOOTH)) //connector adjacent to smooth side
            return false;
        if(s1.equals(Side.ONEPIPE) && s2.equals(Side.TWOPIPES) || s1.equals(Side.TWOPIPES) && s2.equals(Side.ONEPIPE)) //connectors incompatible
            return false;
        return true;
    }

    public void abandons (){
        abandoned = true;
    }

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


    // these next four methods check if there is an active cannon (normal cannons and activated double cannons) facing in a direction at the row/column i/j
    public boolean isRightSideCannon (int i){
        for(int j = 6; j >= 0; j--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isRightSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    public boolean isLeftSideCannon (int i){
        for(int j = 0; j < 7; j++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isLeftSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    public boolean isTopSideCannon (int j){
        for(int i = 0; i < 5; i++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isTopSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    public boolean isBottomSideCannon (int j){
        for(int i = 4; i >= 0; i--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isBottomSideCannon() && componentTilesGrid.get(i, j).get().getCannonStrength() > 0)
                return true;
        }
        return false;
    }

    public boolean isRightSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
                if(ct.isPresent() && ct.get().isRightSideShielded())
                    return true;
        }
        return false;
    }

    public boolean isLeftSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isLeftSideShielded())
                return true;
        }
        return false;
    }

    public boolean isTopSideShielded (){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isTopSideShielded())
                return true;
        }
        return false;
    }

    public boolean isBottomSideShielded (int j){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isBottomSideShielded())
                return true;
        }
        return false;
    }

    public boolean columnEmpty (int numOfColumn){
        for (int i = 0; i < 5; i++) {
            if (componentTilesGrid.get(i, numOfColumn).isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean rowEmpty (int numOfRow){
        for (int i = 0; i < 7; i++) {
            if (componentTilesGrid.get(numOfRow, i).isPresent()) {
                return false;
            }
        }
        return true;
    }

    public int getEngineStrengthShip (){
        int strength = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent())
                strength += ct.get().getEngineStrength();
        }
        if(strength > 0 && isBrownAlienPresent())
            strength += 2;
        return strength;
    }

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

    public boolean isBrownAlienPresent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isAlienPresent("brown"))
                return true;
        }
        return false;
    }

    public boolean isPurpleAlienPresent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isAlienPresent("purple"))
                return true;
        }
        return false;
    }

    public void removeMostValuableGoodBlocks(int num){
        int stillToRemove = num;
        stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.REDBLOCK);
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.YELLOWBLOCK);
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.GREENBLOCK);
        if(stillToRemove > 0)
            stillToRemove -= removeAtMostNumGoodBlocks(stillToRemove, GoodBlock.BLUEBLOCK);
    }

    //tries to remove num Goodblocks of the same type of block and returns the number of GoodBlocks actually removed
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

    public void addCosmicCredits (int credit){
        CosmicCredits += credit;
    }

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

    public void setFinishedShipboard(boolean finishedShipboard) {
        this.finishedShipboard = finishedShipboard;
    }

    public boolean isFinishedShipboard(){
        return finishedShipboard;
    }

    public int highlightShipWrecks(){  //colors all the tiles belonging to the same wreck with the same color
        for(Optional<ComponentTile> ct : componentTilesGrid){
            ct.ifPresent(c -> c.setColor(-1));
        }//reset colors for the algorithm

        int color = 1;

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

    private void spreadColor(int i, int j, int color){  //method needed for the previous one to work
        if(componentTilesGrid.get(i, j).isEmpty())
            return;
        componentTilesGrid.get(i, j).get().setColor(color);
        spreadColor(i+1, j, color);
        spreadColor(i-1, j, color);
        spreadColor(i, j+1, color);
        spreadColor(i, j-1, color);
    }

    public void chooseShipWreck(int i, int j){ // keeps the ship wreck of the chosen color and eliminates the others
        int color = componentTilesGrid.get(i, j).get().getColor();
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 7; x++) {
                if(componentTilesGrid.get(y, x).isPresent() && componentTilesGrid.get(y, x).get().getColor() != color)
                    componentTilesGrid.set(y, x, null);
            }
        }
    }

    public ComponentTile getComponentTileFromGrid(int i, int j){
        return componentTilesGrid.get(i, j).orElseThrow(() -> new IllegalAccessError("No tile in this position"));
    }
}

class ComponentTilesGrid implements Iterable<Optional<ComponentTile>>{
    private Optional<ComponentTile>[][] componentTilesGrid;
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

    @Override
    public Iterator<Optional<ComponentTile>> iterator() {
        return new ComponentTilesGridIterator();
    }

    public void set(int i, int j, ComponentTile c){
        if(i < 0 || j < 0 || i >= rows || j >= columns){
            throw new IndexOutOfBoundsException();
        }
        componentTilesGrid[i][j] = Optional.ofNullable(c);
    }

    public Optional<ComponentTile> get(int i, int j){
        if(i < 0 || j < 0 || i >= rows || j >= columns){
            return Optional.empty();
        }
        return componentTilesGrid[i][j];
    }

    private class ComponentTilesGridIterator implements Iterator<Optional<ComponentTile>>{
        private int i = 0;
        private int j = 0;

        public boolean hasNext() {
            return i < rows && j < columns;
        }

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
