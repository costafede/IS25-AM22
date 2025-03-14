package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class Shipboard {

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
        abandoned = false;
        daysOnFlight = 0;
        this.color = color;
        this.nickname = nickname;
        componentTilesGrid = new ComponentTilesGrid();
        standbyComponent = (Optional<ComponentTile>[]) new Optional[2];
        discardedTiles = 0;
        finishedShipboard = false;
        CosmicCredits = 0;
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

    public void weldComponentTile (ComponentTile ct, int i, int j){componentTilesGrid.set(i, j, ct);}

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
    }

    public boolean checkShipboard (){
        return false; //to do
    }

    public void abandons (){
        abandoned = true;
    }

    /*public int countExposedConnectors (){
        int exposedConnectors = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && !ct.get().getTopSide().equals(Side.SMOOTH) &&){}
        }

    }*/

    public boolean isRightSideCannon (int i){
        for(int j = 6; j >= 0; j--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isRightSideCannon())
                return true;
        }
        return false;
    }

    public boolean isLeftSideCannon (int i){
        for(int j = 0; j < 7; j++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isLeftSideCannon())
                return true;
        }
        return false;
    }

    public boolean isTopSideCannon (int j){
        for(int i = 0; i < 5; i++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isTopSideCannon())
                return true;
        }
        return false;
    }

    public boolean isBottomSideCannon (int j){
        for(int i = 4; i >= 0; i--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isBottomSideCannon())
                return true;
        }
        return false;
    }

    public boolean isRightSideShielded (int i){
        for(int j = 6; j >= 0; j--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isRightSideShielded())
                return true;
        }
        return false;
    }

    public boolean isLeftSideShielded (int i){
        for(int j = 0; j < 7; j++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isLeftSideShielded())
                return true;
        }
        return false;
    }

    public boolean isTopSideShielded (int j){
        for(int i = 0; i < 5; i++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isTopSideShielded())
                return true;
        }
        return false;
    }

    public boolean isBottomSideShielded (int j){
        for(int i = 4; i >= 0; i--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isBottomSideShielded())
                return true;
        }
        return false;
    }

    public boolean isRightSideEngine (int i){
        for(int j = 6; j >= 0; j--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isRightSideEngine())
                return true;
        }
        return false;
    }

    public boolean isLeftSideEngine (int i){
        for(int j = 0; j < 7; j++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isLeftSideEngine())
                return true;
        }
        return false;
    }

    public boolean isTopSideEngine (int j){
        for(int i = 0; i < 5; i++){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isTopSideEngine())
                return true;
        }
        return false;
    }

    public boolean isBottomSideEngine (int j){
        for(int i = 4; i >= 0; i--){
            if(componentTilesGrid.get(i, j).isPresent() && componentTilesGrid.get(i, j).get().isBottomSideEngine())
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
        return strength;
    }

    public double getCannonStrength (){
        double strength = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent())
                strength += ct.get().getCannonStrength();
        }
        return strength;
    }

    public boolean isBrownAlienPresent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isBrownAlienPresent())
                return true;
        }
        return false;
    }

    public boolean isPurpleAlienPresent(){
        for(Optional<ComponentTile> ct : componentTilesGrid){
            if(ct.isPresent() && ct.get().isPurpleAlienPresent())
                return true;
        }
        return false;
    }

    public void removeMostValuableGoodBlocks(int num){
        int stillToRemove = num;
        stillToRemove = num - removeAtMostNumGoodBlocks(num, GoodBlock.REDBLOCK);
        if(stillToRemove > 0)
            stillToRemove = num - removeAtMostNumGoodBlocks(num, GoodBlock.YELLOWBLOCK);
        if(stillToRemove > 0)
            stillToRemove = num - removeAtMostNumGoodBlocks(num, GoodBlock.GREENBLOCK);
        if(stillToRemove > 0)
            stillToRemove = num - removeAtMostNumGoodBlocks(num, GoodBlock.BLUEBLOCK);
    }

    //tries to remove num Goodblocks of the same type of block and returns the number of GoodBlocks actually removed
    private int removeAtMostNumGoodBlocks(int num, GoodBlock block){
        int removed = 0;
        for(Optional<ComponentTile> ct : componentTilesGrid){
            while(ct.isPresent() && ct.get().isGoodBlock(block) && removed < num){
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

        if(color.equals("purple")) {
            for(Optional<ComponentTile> ct : componentTilesGrid){
                if(ct.isPresent() && ct.get().isPurpleAlienPresent())
                    return false;
            }
            if(areAdjacentTilesAddons(i, j, "purple"))
               return true;
        }
        if(color.equals("brown")) {
            for (Optional<ComponentTile> ct : componentTilesGrid) {
                if (ct.isPresent() && ct.get().isBrownAlienPresent())
                    return false;
            }
            if(areAdjacentTilesAddons(i, j, "brown"))
                return true;
        }
        return false;
    }

    private boolean areAdjacentTilesAddons(int i, int j, String color){
        return componentTilesGrid.get(i, j+1).isPresent() && componentTilesGrid.get(i, j+1).get().getAddonColor().equals(color) ||
               componentTilesGrid.get(i, j-1).isPresent() && componentTilesGrid.get(i, j-1).get().getAddonColor().equals(color) ||
               componentTilesGrid.get(i+1, j).isPresent() && componentTilesGrid.get(i+1, j).get().getAddonColor().equals(color) ||
               componentTilesGrid.get(i-1, j).isPresent() && componentTilesGrid.get(i-1, j).get().getAddonColor().equals(color);
    }

    public int getCrewNumberShip (){
        int crewnumber = 0;
        for (Optional<ComponentTile> ct : componentTilesGrid) {
            if (ct.isPresent())
                crewnumber += ct.get().getCrewNumber();
        }
        return crewnumber;
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
            if(columns >= j){
                j = 0;
                i++;
            }
            return componentTile;
        }
    }
}
