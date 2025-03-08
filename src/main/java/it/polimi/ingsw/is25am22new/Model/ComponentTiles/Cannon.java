package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class Cannon extends ComponentTile {
    protected boolean topSideCannoned;
    protected boolean bottomSideCannoned;
    protected boolean leftSideCannoned;
    protected boolean rightSideCannoned;
    protected double cannonStrength;

    public Cannon() {
        super();
        this.cannonStrength = cannonStrength;
        this.topSideCannoned = topSideCannoned;
        this.bottomSideCannoned = bottomSideCannoned;
        this.leftSideCannoned = leftSideCannoned;
        this.rightSideCannoned = rightSideCannoned;
    }

    public double getCannonStrengthTile() {
        if(topSideCannoned) return cannonStrength;
        else return (cannonStrength / 2);
    }

    public void activateComponentTile(){
        return;
    }

    public void deactivateComponentTile(){
        return;
    }


    public void rotateClockwiseTile(){
        super.rotateClockwiseTile();

        boolean temp1 = leftSideCannoned;
        leftSideCannoned = bottomSideCannoned;
        bottomSideCannoned = rightSideCannoned;
        rightSideCannoned = topSideCannoned;
        topSideCannoned = temp1;
    }

    public void rotateCounterClockwiseTile(){
        super.rotateCounterClockwiseTile();

        boolean temp1 = leftSideCannoned;
        leftSideCannoned = topSideCannoned;
        topSideCannoned = rightSideCannoned;
        rightSideCannoned = bottomSideCannoned;
        bottomSideCannoned = temp1;
    }

    public boolean isTopSideCannonTile() {
        return topSideCannoned;
    }

    public boolean isBottomSideCannonTile() {
        return bottomSideCannoned;
    }

    public boolean isLeftSideCannonTile() {
        return leftSideCannoned;
    }

    public boolean isRightSideCannonTile() {
        return rightSideCannoned;
    }
}