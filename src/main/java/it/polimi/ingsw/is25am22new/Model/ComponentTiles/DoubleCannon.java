package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

public class DoubleCannon extends Cannon {
    private boolean topSideActive;
    private boolean bottomSideActive;
    private boolean leftSideActive;
    private boolean rightSideActive;

    public DoubleCannon(double cannonStrength, boolean topSideCannoned, boolean bottomSideCannoned,
                        boolean leftSideCannoned, boolean rightSideCannoned) {
        super();
        this.topSideActive = false;
        this.bottomSideActive = false;
        this.leftSideActive = false;
        this.rightSideActive = false;
    }

    @Override
    public double getCannonStrength() {
        if(topSideCannoned && topSideActive)    return cannonStrength;
        else if (leftSideActive || bottomSideActive || rightSideActive) return (cannonStrength / 2);
        return 0;
    }

    @Override
    public void activateComponent(){
        if(topSideCannoned) topSideActive = true;
        else if(bottomSideCannoned) bottomSideActive = true;
        else if(rightSideCannoned) rightSideActive = true;
        else if(leftSideCannoned) leftSideActive = true;
    }

    @Override
    public void deactivateComponent(){
        topSideActive = false;
        bottomSideActive = false;
        rightSideActive = false;
        leftSideActive = false;
    }
}
