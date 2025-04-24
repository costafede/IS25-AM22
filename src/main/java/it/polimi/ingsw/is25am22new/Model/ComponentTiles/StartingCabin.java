package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class StartingCabin extends Cabin {

    private String color;

    public StartingCabin(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
        this.numOfAstronauts = 2;
    }

    public String getColorTile() {
        return color;
    }

    public boolean isStartingCabin(){
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Numero di astronauti: " + numOfAstronauts + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }
}
