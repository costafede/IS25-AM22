package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;

public class ShieldGenerator extends ComponentTile {
    private boolean topSideShieldable, bottomSideShieldable, leftSideShieldable, rightSideShieldable;   //sono i lati su cui si può attivare lo scudo
    private boolean topSideShielded, bottomSideShielded, leftSideShielded, rightSideShielded; //booleani che servono a capire che lati sono effettivamente attivi

    public ShieldGenerator(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideShieldable = true;
        this.bottomSideShieldable = false;
        this.leftSideShieldable = false;
        this.rightSideShieldable = true;
        this.topSideShielded = false;
        this.bottomSideShielded = false;
        this.leftSideShielded = false;
        this.rightSideShielded = false;
    }

    public void rotateClockwise() {
        super.rotateClockwise();
        boolean tmp = topSideShieldable;
        topSideShieldable = leftSideShieldable;
        leftSideShieldable = bottomSideShieldable;
        bottomSideShieldable = rightSideShieldable;
        rightSideShieldable = tmp;
    }

    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        boolean tmp = topSideShieldable;
        topSideShieldable = rightSideShieldable;
        rightSideShieldable = bottomSideShieldable;
        bottomSideShieldable = leftSideShieldable;
        leftSideShieldable = tmp;
    }

    public void activateComponent(){
        topSideShielded = topSideShieldable;
        bottomSideShielded = bottomSideShieldable;
        leftSideShielded = leftSideShieldable;
        rightSideShielded = rightSideShieldable;
    }

    public void deactivateComponent() {
        topSideShielded = false;
        bottomSideShielded = false;
        leftSideShielded = false;
        rightSideShielded = false;
    }

    public boolean isTopSideShielded() {
        return topSideShielded;
    }

    public boolean isBottomSideShielded() {
        return bottomSideShielded;
    }

    public boolean isLeftSideShielded() {
        return leftSideShielded;
    }

    public boolean isRightSideShielded() {
        return rightSideShielded;
    }
}
