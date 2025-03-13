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

    public double getCannonStrength() {
        if(topSideCannoned) return cannonStrength;
        else return (cannonStrength / 2);
    }

    public void activateComponent(){
        return;
    }

    public void deactivateComponent(){
        return;
    }


    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideCannoned;
        leftSideCannoned = bottomSideCannoned;
        bottomSideCannoned = rightSideCannoned;
        rightSideCannoned = topSideCannoned;
        topSideCannoned = temp1;
    }

    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideCannoned;
        leftSideCannoned = topSideCannoned;
        topSideCannoned = rightSideCannoned;
        rightSideCannoned = bottomSideCannoned;
        bottomSideCannoned = temp1;
    }

    public boolean isTopSideCannon() {
        return topSideCannoned;
    }

    public boolean isBottomSideCannon() {
        return bottomSideCannoned;
    }

    public boolean isLeftSideCannon() {
        return leftSideCannoned;
    }

    public boolean isRightSideCannon() {
        return rightSideCannoned;
    }
}