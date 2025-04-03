package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class DoubleEngine extends Engine {
    private boolean bottomSideActive;

    public DoubleEngine(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.bottomSideActive = false;
    }

    // Returns the strength of the engine based on being active or not
    @Override
    public int getEngineStrength() {
        if (bottomSideActive)
            return 2;
        return 0;
    }

    // Activates the engine component --> Only the bottom side can be activated
    @Override
    public void activateComponent() {
        bottomSideActive = true;
    }

    @Override
    public void deactivateComponent() {
        bottomSideActive = false;
    }

    @Override
    public boolean isDoubleEngine() {
        return true;
    }
}
