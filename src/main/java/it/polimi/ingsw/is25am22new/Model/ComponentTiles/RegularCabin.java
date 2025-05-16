package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

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

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String alien;

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

        if (purpleAlienPresent){alien = "P";}
        else if (brownAlienPresent){alien = "B";}
        else {alien = " ";}

        return new String[]{
                "   " + top + "   ",
                "   " + alien + "   ",
                left + "  RC " + right,
                "   " + numOfAstronauts + "   ",
                "   " + bottom + "   ",
        };
    }
}
