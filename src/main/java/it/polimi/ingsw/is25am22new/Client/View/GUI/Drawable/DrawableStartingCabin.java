package it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StartingCabin;

public class DrawableStartingCabin extends DrawableComponentTile{
    private StartingCabin startingCabin;

    public DrawableStartingCabin(StartingCabin startingCabin) {
        this.startingCabin = startingCabin;
    }

    @Override
    public String draw() {
        return "Cabin with " + startingCabin.getNumOfAstronauts() + " astronauts";
    }
}
