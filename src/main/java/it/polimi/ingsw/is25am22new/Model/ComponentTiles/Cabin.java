package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

abstract class Cabin {

    private int numOfAstronauts;

    public void putCrewMembersTile(){
        numOfAstronauts++;
    }

    public void putAlienTile (String color){
        return;
    }

    public int getCrewNumberTile(){
        return numOfAstronauts;
    }

    public boolean isBrownAlienPresentTile(){
        return false;
    }

    public boolean isPurpleAlienPresentTile(){
        return false;
    }
}
