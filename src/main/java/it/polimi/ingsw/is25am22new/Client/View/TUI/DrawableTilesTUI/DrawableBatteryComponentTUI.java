package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.BatteryComponent;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a drawable text-based UI (TUI) component for a battery tile in the game.
 * This class is responsible for rendering a visual representation of a battery component
 * based on its attributes and connectivity.
 *
 * This class extends DrawableComponentTileTUI to provide a specific implementation
 * for drawing a battery component, showing the pipe connections on each side (top, bottom, left, right)
 * and the current number of batteries present in the component.
 */
public class DrawableBatteryComponentTUI extends DrawableComponentTileTUI {

    BatteryComponent batteryComponent;

    public DrawableBatteryComponentTUI(BatteryComponent batteryComponent) {
        this.batteryComponent = batteryComponent;
    }

    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        Side topSide = batteryComponent.getTopSide();
        Side bottomSide = batteryComponent.getBottomSide();
        Side leftSide = batteryComponent.getLeftSide();
        Side rightSide = batteryComponent.getRightSide();
        String numOfBatteries = String.valueOf(batteryComponent.getNumOfBatteries());

        if (topSide.equals(SMOOTH)){top = "S";}
        else if (topSide.equals(ONEPIPE)){top = "1";}
        else if (topSide.equals(TWOPIPES)){top = "2";}
        else {top = "3";}

        if (bottomSide.equals(SMOOTH)){bottom = "S";}
        else if (bottomSide.equals(ONEPIPE)){bottom = "1";}
        else if (bottomSide.equals(TWOPIPES)){bottom = "2";}
        else {bottom = "3";}

        if (leftSide.equals(SMOOTH)){left = "S";}
        else if (leftSide.equals(ONEPIPE)){left = "1";}
        else if (leftSide.equals(TWOPIPES)){left = "2";}
        else {left = "3";}

        if (rightSide.equals(SMOOTH)){right = "S";}
        else if (rightSide.equals(ONEPIPE)){right = "1";}
        else if (rightSide.equals(TWOPIPES)){right = "2";}
        else {right = "3";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  BC " + right,
                "   " + numOfBatteries + "   ",
                "   " + bottom + "   ",
        };
    }
}
