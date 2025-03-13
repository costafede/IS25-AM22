package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class Engine extends ComponentTile {
    protected boolean topSideEngine;
    protected boolean bottomSideEngine;
    protected boolean leftSideEngine;
    protected boolean rightSideEngine;
    protected int engineStrength;

    //Constructor -> Need to understand what to do with the parameters
    //Maybe JSON file initialization outside the constructor? -> adding a method to set the values from the JSON file
    public Engine() {
        super();
        this.topSideEngine = topSideEngine;
        this.bottomSideEngine = bottomSideEngine;
        this.leftSideEngine = leftSideEngine;
        this.rightSideEngine = rightSideEngine;
        this.engineStrength = engineStrength;
    }

    public int getEngineStrength() {
        return engineStrength;
    }

    //It's implemented in the subclasses that need it
    public void activateComponent(){
        return;
    }

    //It's implemented in the subclasses that need it
    public void deactivateComponent(){
        return;
    }

    public void rotateClockwise(){
        super.rotateClockwise();

        boolean temp1 = leftSideEngine;
        leftSideEngine = bottomSideEngine;
        bottomSideEngine = rightSideEngine;
        rightSideEngine = topSideEngine;
        topSideEngine = temp1;
    }

    public void rotateCounterClockwise(){
        super.rotateCounterClockwise();

        boolean temp1 = leftSideEngine;
        leftSideEngine = topSideEngine;
        topSideEngine = rightSideEngine;
        rightSideEngine = bottomSideEngine;
        bottomSideEngine = temp1;
    }

    public boolean isTopSideEngine() {
        return topSideEngine;
    }

    public boolean isBottomSideEngine() {
        return bottomSideEngine;
    }

    public boolean isLeftSideEngine() {
        return leftSideEngine;
    }

    public boolean isRightSideEngine() {
        return rightSideEngine;
    }
}
