package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class RegularCabin extends Cabin {

    private int numOfPurpleAlien;

    private int numOfBrownAlien;

    //aggiunge un alieno del tipo dato nella cabina
    @Override
    public void putAlienTile(String color){
        if(color.equals("brown")){
            numOfBrownAlien++;
        }
        else if(color.equals("purple")){
            numOfPurpleAlien++;
        }
    }

    @Override
    public boolean isBrownAlienPresentTile(){
        if (numOfBrownAlien == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean isPurpleAlienPresentTile(){
        if (numOfPurpleAlien == 1){
            return true;
        }
        return false;
    }
}
