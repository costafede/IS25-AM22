package it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.BatteryComponent;

public class DrawableBatteryComponent extends DrawableComponentTile{

    private BatteryComponent batteryComponent;

    public DrawableBatteryComponent(BatteryComponent batteryComponent) {
        this.batteryComponent = batteryComponent;
    }

    @Override
    public String draw() {
        return "Battery: " + batteryComponent.getNumOfBatteries() + " remaining";
    }
}
