package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

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
}
