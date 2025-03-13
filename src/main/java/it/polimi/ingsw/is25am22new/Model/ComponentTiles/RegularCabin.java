package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class RegularCabin extends Cabin {

    private int numOfPurpleAlien;

    private int numOfBrownAlien;

    //aggiunge un alieno del tipo dato nella cabina
    @Override
    public void putAlien(String color){
        if(color.equals("brown")){
            numOfBrownAlien++;
        }
        else if(color.equals("purple")){
            numOfPurpleAlien++;
        }
    }

    @Override
    public boolean isBrownAlienPresent(){
        if (numOfBrownAlien == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean isPurpleAlienPresent(){
        if (numOfPurpleAlien == 1){
            return true;
        }
        return false;
    }
}
