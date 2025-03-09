package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class AlienAddon extends ComponentTile {
    private String color;

    //Constructor -> Need to understand what to do with the parameter
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the value from the JSON file
    public AlienAddon() {
        super();
        this.color = color;
    }

    public String getAddonColorTile() {
        return color;
    }
}
