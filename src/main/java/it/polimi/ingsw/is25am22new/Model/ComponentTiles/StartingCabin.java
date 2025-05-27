package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableStartingCabin;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableStartingCabinTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * Represents a specialized type of cabin in the game that serves as the starting point.
 * The StartingCabin sets predefined characteristics, including a default number of astronauts
 * and a color attribute that differentiates it from other cabins.
 * It inherits the base functionalities of the Cabin class.
 */
public class StartingCabin extends Cabin {

    private String color;

    public StartingCabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
        this.numOfAstronauts = 2;
    }

    public String getColorTile() {
        return color;
    }

    public boolean isStartingCabin(){
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Astronaut's number: " + numOfAstronauts + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableStartingCabinTUI(this);
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableStartingCabin(this);
    }
}
