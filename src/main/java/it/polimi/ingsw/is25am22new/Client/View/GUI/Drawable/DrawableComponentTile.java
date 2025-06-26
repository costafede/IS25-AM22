package it.polimi.ingsw.is25am22new.Client.View.GUI.Drawable;

public abstract class DrawableComponentTile {

    /**
     * Abstract method implemented in the subclasses of DrawableComponentTile
     * Displays information of the component when dragging the cursor over the component
     *
     * @return
     */
    public abstract String draw();

    /**
     * Returns true if the component is a component that uses battery tokens
     * @return
     */
    public boolean isActivable(){
        return false;
    }

}
