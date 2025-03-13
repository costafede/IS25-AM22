package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;

public class Cannon extends ComponentTile {
    protected boolean topSideCannon;
    protected boolean bottomSideCannon;
    protected boolean leftSideCannon;
    protected boolean rightSideCannon;

    public Cannon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideCannon = true;
        this.bottomSideCannon = false;
        this.leftSideCannon = false;
        this.rightSideCannon = false;
    }

    public double getCannonStrength() {
        if(topSideCannon) return 1;
        else return 0.5;
    }

    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideCannon;
        leftSideCannon = bottomSideCannon;
        bottomSideCannon = rightSideCannon;
        rightSideCannon = topSideCannon;
        topSideCannon = temp1;
    }

    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideCannon;
        leftSideCannon = topSideCannon;
        topSideCannon = rightSideCannon;
        rightSideCannon = bottomSideCannon;
        bottomSideCannon = temp1;
    }

    public boolean isTopSideCannon() {
        return topSideCannon;
    }

    public boolean isBottomSideCannon() {
        return bottomSideCannon;
    }

    public boolean isLeftSideCannon() {
        return leftSideCannon;
    }

    public boolean isRightSideCannon() {
        return rightSideCannon;
    }
}