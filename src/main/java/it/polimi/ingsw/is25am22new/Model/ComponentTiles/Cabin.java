package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;

abstract class Cabin extends ComponentTile {

    public int getNumOfAstronauts() {
        return numOfAstronauts;
    }

    protected int numOfAstronauts;

    public Cabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        numOfAstronauts = 0;
    }

    public void removeCrewMember() {
        System.out.print("remove crew member di cabin/startingcabin ->");
        if(numOfAstronauts > 0) numOfAstronauts--;
    }

    public void putAstronauts(){
        numOfAstronauts = 2;
    }

    public int getCrewNumber(){
        return numOfAstronauts;
    }

    public boolean isCabin() { return true;}
}
