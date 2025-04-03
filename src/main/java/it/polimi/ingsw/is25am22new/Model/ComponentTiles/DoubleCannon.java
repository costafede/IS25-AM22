package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class DoubleCannon extends Cannon {
    private boolean topSideActive;
    private boolean bottomSideActive;
    private boolean leftSideActive;
    private boolean rightSideActive;

    public DoubleCannon(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        super(pngName, topSide, bottomSide, leftSide, rightSide);
        this.topSideActive = false;
        this.bottomSideActive = false;
        this.leftSideActive = false;
        this.rightSideActive = false;
    }

    @Override
    public double getCannonStrength() {
        if(topSideActive)    return 2;
        else if (leftSideActive || bottomSideActive || rightSideActive) return 1;
        return 0;
    }

    @Override
    public void activateComponent(){
        topSideActive = topSideCannon;
        bottomSideActive = bottomSideCannon;
        rightSideActive = rightSideCannon;
        leftSideActive = leftSideCannon;
    }

    @Override
    public void deactivateComponent(){
        topSideActive = false;
        bottomSideActive = false;
        rightSideActive = false;
        leftSideActive = false;
    }
    @Override
    public boolean isDoubleCannon() {
        return true;
    }
}
