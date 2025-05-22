package it.polimi.ingsw.is25am22new.Model.ComponentTiles.Drawable;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StorageCompartment;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

public class DrawableStorageCompartment extends DrawableComponentTile{

    private StorageCompartment storageCompartment;

    public DrawableStorageCompartment(StorageCompartment storageCompartment) {
        this.storageCompartment = storageCompartment;
    }

    @Override
    public String draw() {
        String whatsInside = "";

        int totalBlocks = 0;
        for (GoodBlock gb : storageCompartment.getGoodBlocks().keySet()) {
            totalBlocks += storageCompartment.getGoodBlocks().get(gb);
        }

        if (totalBlocks == 0) {
            whatsInside.concat("Empty");
        } else {
            whatsInside.concat(totalBlocks + " blocks (");
            // Add details about block types
            boolean first = true;
            for (GoodBlock gb : storageCompartment.getGoodBlocks().keySet()) {
                int count = storageCompartment.getGoodBlocks().get(gb);
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
        return "Storage: " + whatsInside;
    }
}
