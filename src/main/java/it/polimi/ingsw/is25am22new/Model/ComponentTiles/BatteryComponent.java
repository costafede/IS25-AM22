package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class BatteryComponent extends ComponentTile {
    private int numOfBatteries;

    public BatteryComponent(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, int numOfBatteries) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.numOfBatteries = numOfBatteries;
    }

    public int getNumOfBatteries() {
        return numOfBatteries;
    }

    public void removeBatteryToken() {
        numOfBatteries--;
    }

    public boolean isBattery() {
        return true;
    }
}