package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class ShieldGenerator extends ComponentTile {
    private boolean topSideShieldable, bottomSideShieldable, leftSideShieldable, rightSideShieldable;
    private boolean topSideShielded, bottomSideShielded, leftSideShielded, rightSideShielded;

    /*DEVO COSTRUIRE IL COMPONENT TILE CON IL FILE JSON*/


    /*DA FARE IL COSTRUTTORE (NON SO COME)*/

    public void rotateClockwiseTile() {
        super.rotateClockwiseTile();
        boolean tmp = topSideShieldable;
        topSideShieldable = leftSideShieldable;
        leftSideShieldable = bottomSideShieldable;
        bottomSideShieldable = rightSideShieldable;
        rightSideShieldable = tmp;
    }

    public void rotateCounterClockwiseTile() {
        super.rotateCounterClockwiseTile();
        boolean tmp = topSideShieldable;
        topSideShieldable = rightSideShieldable;
        rightSideShieldable = bottomSideShieldable;
        bottomSideShieldable = leftSideShieldable;
        leftSideShieldable = tmp;
    }

    public void activateComponentTile(){
        topSideShielded = topSideShieldable;
        bottomSideShielded = bottomSideShieldable;
        leftSideShielded = leftSideShieldable;
        rightSideShielded = rightSideShieldable;
    }

    public void deactivateComponentTile() {
        topSideShielded = false;
        bottomSideShielded = false;
        leftSideShielded = false;
        rightSideShielded = false;
    }

    public boolean isTopSideShieldedTile() {
        return topSideShielded;
    }

    public boolean isBottomSideShieldedTile() {
        return bottomSideShielded;
    }

    public boolean isLeftSideShieldedTile() {
        return leftSideShielded;
    }

    public boolean isRightSideShieldedTile() {
        return rightSideShielded;
    }
}
