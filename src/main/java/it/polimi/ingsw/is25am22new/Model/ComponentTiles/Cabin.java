package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

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
        if(numOfAstronauts <= 0)
            throw new IllegalArgumentException("Cannot remove Crew member when there are none");
        numOfAstronauts--;
    }

    public void putAstronauts(){
        numOfAstronauts = 2;
    }

    public int getCrewNumber(){
        return numOfAstronauts;
    }

    public boolean isCabin() { return true;}
}
