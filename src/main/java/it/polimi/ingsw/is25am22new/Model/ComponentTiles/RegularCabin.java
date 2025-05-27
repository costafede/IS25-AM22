package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableRegularCabin;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableRegularCabinTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * The RegularCabin class is a specific type of cabin that extends the functionality
 * of the Cabin superclass to include support for alien presence management. It allows
 * tracking and handling of different types of aliens (purple and brown) alongside the
 * existing astronaut crew members. This class implements methods for adding, removing,
 * and querying the presence of aliens, as well as managing and rendering the cabin's
 * state.
 */
public class RegularCabin extends Cabin {

    private boolean purpleAlienPresent;
    private boolean brownAlienPresent;

    public RegularCabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        purpleAlienPresent = false;
        brownAlienPresent = false;
    }

    //aggiunge un alieno del tipo dato nella cabina
    @Override
    public void putAlien(String color){
        if(brownAlienPresent || purpleAlienPresent)
            throw new IllegalArgumentException("Cannot place an alien where another one is already present");
        if(color.equals("brown")){
            brownAlienPresent = true;
        }
        else if(color.equals("purple")){
            purpleAlienPresent = true;
        }
    }

    @Override
    public boolean isAlienPresent(String color){
        if(color.equals("brown")){
            return brownAlienPresent;
        }
        if(color.equals("purple")){
            return purpleAlienPresent;
        }
        return false;
    }

    public int getCrewNumber(){
        if(isAlienPresent("purple") || isAlienPresent("brown")){
            return 1;
        }
        else return numOfAstronauts;
    }


    public void removeCrewMember(){
        if(isAlienPresent("purple")){
            purpleAlienPresent = false;

        } else if(isAlienPresent("brown")){
            brownAlienPresent = false;
        }
        else
            numOfAstronauts--;
    }

    public boolean isPurpleAlienPresent() {
        return purpleAlienPresent;
    }

    public boolean isBrownAlienPresent() {
        return brownAlienPresent;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Astronaut's number: " + numOfAstronauts + " Purple alien: " +
                purpleAlienPresent + " Brown alien: " + brownAlienPresent + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableRegularCabinTUI(this);
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableRegularCabin(this);
    }
}
