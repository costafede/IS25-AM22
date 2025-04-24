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
        if(numOfBatteries <= 0)
            throw new IllegalArgumentException("Cannot remove batteries if there are none");
        numOfBatteries--;
    }

    public boolean isBattery() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Batteries number: " + numOfBatteries + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }
}