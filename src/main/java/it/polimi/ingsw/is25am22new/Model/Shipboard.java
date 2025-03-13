package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;

public abstract class Shipboard {

    private boolean abbandoned;
    private int daysOnFlight;
    private String color;
    private String nickname;
    private ComponentTile[][] componentTilesGrid;
    private ComponentTile[] standbyComponent;
    private int discardedTiles;
    private boolean finishedShipboard;
    private int CosmicCredits;

    public void weldComponentTile (ComponentTile ct, int x, int y){componentTilesGrid[x][y] = ct;}

    public void standbyComponentTile (ComponentTile ct){
        for (int i = 0; i < (standbyComponent.length - 1); i++ ) {
            if (standbyComponent[i].equals(null)) {
                standbyComponent[i] = ct;
            }
        }
    }

    public ComponentTile pickStandByComponentTile (int index) {return standbyComponent[index];}

    public void destroyTile (int x, int y){
        componentTilesGrid[x][y] = null;
        discardedTiles++;
    }

    public boolean checkShipboard (){
        return false; //to do
    }

    public void abandons (){
        abbandoned = true;
    }

    public int countExposedConnectors (){
        return 0; //to do
    }

    public boolean isRightSideCannon (int y){
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
