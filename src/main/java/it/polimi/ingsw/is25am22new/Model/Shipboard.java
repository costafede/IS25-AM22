package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

import java.awt.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class Shipboard {

    private boolean abandoned;
    private int daysOnFlight;
    private String color;
    private String nickname;
    //private ComponentTilesGrid componentTilesGrid;
    private Optional<ComponentTile>[] standbyComponent;
    private int discardedTiles;
    private boolean finishedShipboard;
    private int CosmicCredits;

    public Shipboard(String color, String nickname) {
        abandoned = false;
        daysOnFlight = 0;
        this.color = color;
        this.nickname = nickname;
        //componentTilesGrid = new ComponentTilesGrid();
        standbyComponent = new Optional[2];
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

    /*public void weldComponentTile (ComponentTile ct, int i, int j){componentTilesGrid.set(i, j, ct);}

    public void standbyComponentTile (ComponentTile ct){
        for (int i = 0; i < (standbyComponent.length - 1); i++ ) {
            if (standbyComponent[i].equals(null)) {
                standbyComponent[i] = ct;
            }
        }
    }

    public ComponentTile pickStandByComponentTile (int index) {
        ComponentTile ct = standbyComponent[index];
        standbyComponent[index] = null;
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

    public int countExposedConnectors (){
        return 0; //to do
    }

    public boolean isRightSideCannon (int i){
        for(int j = 0; j < 7; j++){
            componentTilesGrid.get(i, j).isPresent().
            }

        }

        return false;
    }

    public boolean isLeftSideCannon (int y){
        return false;
    }

    public boolean isTopSideCannon (int x){
        return false;
    }

    public boolean isBottomSideCannon (int x){
        return false;
    }

    public boolean isRightSideShielded (int y){
        return false;
    }

    public boolean isLeftSideShielded (int y){
        return false;
    }

    public boolean isTopSideShielded (int x){
        return false;
    }

    public boolean isBottomSideShielded (int x){
        return false;
    }

    public boolean columnEmpty (int numOfColumn){
        for (int i = 0; i < 5; i++) {
            if (componentTilesGrid[i][numOfColumn] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean rowEmpty (int numOfRow){
        for (int i = 0; i < 7; i++) {
            if (componentTilesGrid[numOfRow][i] != null) {
                return false;
            }
        }
        return true;
    }

    public int getEngineStrengthShip (){
        int strength = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                strength += componentTilesGrid[i][j].getEngineStrength();

            }
        }
        return strength;
    }

    public double getCannonStrength (){
        double strength = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                strength += componentTilesGrid[i][j].getCannonStrength();

            }
        }
        return strength;
    }

    public boolean isBrownAlienPresentShip(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if(componentTilesGrid[i][j].isBrownAlienPresent()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPurpleAlienPresentShip(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if(componentTilesGrid[i][j].isPurpleAlienPresent()){
                    return true;
                }
            }
        }
        return false;
    }

    public void removeMostValuableGoodBlocks(){
        //bla bla
    }

    public void addCosmicCredits (int credit){
        CosmicCredits += credit;
    }

    public boolean isAlienPlaceable (int x, int y, String color){
        if(color.equals("purple")) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (componentTilesGrid[i][j].isPurpleAlienPresent()){
                        return false;
                    }
                }
            }
            return true;
        }
        if(color.equals("brown")) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (componentTilesGrid[i][j].isBrownAlienPresent()){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public int getCrewNumberShip (){
        int crewnumber = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                crewnumber += componentTilesGrid[i][j].getCrewNumber();

            }
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
            throw new IndexOutOfBoundsException();
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
    }*/
}
