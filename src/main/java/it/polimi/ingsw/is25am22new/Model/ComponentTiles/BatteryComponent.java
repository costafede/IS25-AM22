package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class BatteryComponent extends ComponentTile {
    private int numOfBatteries;

    public BatteryComponent(int numOfBatteries) {
        super();
        this.numOfBatteries = numOfBatteries;
    }

    public void removeBatteryTokenTile() {
        numOfBatteries--;
    }
}