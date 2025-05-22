package it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.SpecialStorageCompartment;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

public class DrawableSpecialStorageCompartment extends DrawableComponentTile {

    private SpecialStorageCompartment specialStorageCompartment;

    public DrawableSpecialStorageCompartment(SpecialStorageCompartment specialStorageCompartment) {
        this.specialStorageCompartment = specialStorageCompartment;
    }

    @Override
    public String draw() {
        String whatsInside = "";

        int totalBlocks = 0;
        for (GoodBlock gb : specialStorageCompartment.getGoodBlocks().keySet()) {
            totalBlocks += specialStorageCompartment.getGoodBlocks().get(gb);
        }

        if (totalBlocks == 0) {
            whatsInside = "empty";
        } else {
            whatsInside.concat(totalBlocks + " blocks (");
            // Add details about block types
            boolean first = true;
            for (GoodBlock gb : specialStorageCompartment.getGoodBlocks().keySet()) {
                int count = specialStorageCompartment.getGoodBlocks().get(gb);
                if (count > 0) {
                    if (!first) {
                        whatsInside.concat(", ");
                    }
                    whatsInside.concat(count + " " + gb.name());
                    first = false;
                }
            }
            whatsInside.concat(")");
        }

        return "Special Storage: " + whatsInside;
    }
}
