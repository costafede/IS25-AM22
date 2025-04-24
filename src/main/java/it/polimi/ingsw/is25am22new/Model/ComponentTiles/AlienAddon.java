package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class AlienAddon extends ComponentTile {
    private final String color;

    //Constructor -> Need to understand what to do with the parameter
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the value from the JSON file
    public AlienAddon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide, String color) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.color = color;
    }

    public String getAddonColor() {
        return color;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + color + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }
}
