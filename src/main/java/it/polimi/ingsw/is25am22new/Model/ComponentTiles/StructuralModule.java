package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableComponentTile;
import it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable.DrawableStructuralModule;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableComponentTileTUI;
import it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI.DrawableStructuralModuleTUI;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.TWOPIPES;

/**
 * The StructuralModule class represents a specialized component tile in the game.
 * It extends the ComponentTile class and provides additional functionality specific
 * to structural modules in the game mechanics. StructuralModule does not implement
 * storage-related or active functionalities but focuses on structural connectivity.
 */
public class StructuralModule extends ComponentTile {
    public StructuralModule(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide){
        super(pngName, topSide, bottomSide, leftSide, rightSide);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
    }

    @Override
    public DrawableComponentTileTUI getDrawableTUI() {
        return new DrawableStructuralModuleTUI(this);
    }

    @Override
    public DrawableComponentTile getDrawable() {
        return new DrawableStructuralModule();
    }
}
