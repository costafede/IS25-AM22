package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

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

    @Override
    public String toString() {
        if (topSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: Double Engine" + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (bottomSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: Double Engine" + " Left: " + leftSide + " Rigth: " + rightSide;
        else if (leftSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: Double Engine" + " Rigth: " + rightSide;
        else if (rightSideEngine) return getClass().getSimpleName() + " Active: " + bottomSideActive + " Top: " + topSide + " Bottom: " + bottomSide + " Left: " + leftSide + " Rigth: Double Engine";
        else return "";
    }

    @Override
    public String[] draw(){
        String top;
        String bottom;
        String left;
        String right;
        String active = "";

        if (topSide.equals(SMOOTH) && !topSideEngine){top = "L";}
        else if (topSide.equals(ONEPIPE) && !topSideEngine){top = "1";}
        else if (topSide.equals(TWOPIPES) && !topSideEngine){top = "2";}
        else if (topSide.equals(UNIVERSALPIPE) && !topSideEngine){top = "3";}
        else {top = "#";}

        if (bottomSide.equals(SMOOTH) && !bottomSideEngine){bottom = "L";}
        else if (bottomSide.equals(ONEPIPE) && !bottomSideEngine){bottom = "1";}
        else if (bottomSide.equals(TWOPIPES) && !bottomSideEngine){bottom = "2";}
        else if (bottomSide.equals(UNIVERSALPIPE) && !bottomSideEngine){bottom = "3";}
        else {bottom = "#";}

        if (leftSide.equals(SMOOTH) && !leftSideEngine){left = "L";}
        else if (leftSide.equals(ONEPIPE) && !leftSideEngine){left = "1";}
        else if (leftSide.equals(TWOPIPES) && !leftSideEngine){left = "2";}
        else if (leftSide.equals(UNIVERSALPIPE) && !leftSideEngine){left = "3";}
        else {left = "#";}

        if (rightSide.equals(SMOOTH) && !rightSideEngine){right = "L";}
        else if (rightSide.equals(ONEPIPE) && !rightSideEngine){right = "1";}
        else if (rightSide.equals(TWOPIPES) && !rightSideEngine){right = "2";}
        else if (rightSide.equals(UNIVERSALPIPE) && !rightSideEngine){right = "3";}
        else {right = "#";}

        if (bottomSideActive) {active = "A";}
        else {active = " ";}

        return new String[]{
                "   " + top + "   ",
                "       ",
                left + "  DE " + right,
                "   " + active + "   ",
                "   " + bottom + "   ",
        };
    }
}
