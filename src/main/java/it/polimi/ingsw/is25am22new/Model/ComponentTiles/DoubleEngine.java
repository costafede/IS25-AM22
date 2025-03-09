package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class DoubleEngine extends Engine {
    private boolean bottomSideActive;

    public DoubleEngine() {
        super();
        this.bottomSideActive = false;
    }

    // Returns the strength of the engine based on being active or not
    @Override
    public int getEngineStrength() {
        if (bottomSideActive)
            return 1;
        else
            return 0;
    }

    // Activates the engine component --> Only the bottom side can be activated
    @Override
    public void activateComponentTile() {
        bottomSideActive = true;
    }

    @Override
    public void deactivateComponentTile() {
        bottomSideActive = false;
    }
}
