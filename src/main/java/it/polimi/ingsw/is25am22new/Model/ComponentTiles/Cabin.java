package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;

abstract class Cabin extends ComponentTile {

    protected int numOfAstronauts;

    public Cabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        numOfAstronauts = 0;
    }

    public void putAstronauts(){
        numOfAstronauts = 2;
    }

    public int getCrewNumber(){
        return numOfAstronauts;
    }

}
