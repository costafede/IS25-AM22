package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;

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
        if(color.equals("brown")){
            brownAlienPresent = true;
        }
        else if(color.equals("purple")){
            purpleAlienPresent = true;
        }
    }

    @Override
    public boolean isBrownAlienPresent(){
        return brownAlienPresent;
    }

    @Override
    public boolean isPurpleAlienPresent(){
        return purpleAlienPresent;
    }

    public int getCrewNumber(){
        if(isBrownAlienPresent() || isPurpleAlienPresent()){
            return 1;
        }
        else return numOfAstronauts;
    }
}
