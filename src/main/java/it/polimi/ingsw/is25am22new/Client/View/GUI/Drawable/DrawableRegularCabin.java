package it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.RegularCabin;

public class DrawableRegularCabin extends DrawableComponentTile{

    private RegularCabin regularCabin;

    public DrawableRegularCabin(RegularCabin regularCabin) {
        this.regularCabin = regularCabin;
    }

    @Override
    public String draw() {
        String whatsInside;
        if (regularCabin.isBrownAlienPresent()) {
            whatsInside = "Brown alien";
        } else if(regularCabin.isPurpleAlienPresent()){
            whatsInside = "Purple alien";
        } else {
            whatsInside = regularCabin.getNumOfAstronauts() + " astronauts";
        }
        return "Cabin with " + whatsInside;
    }
}
