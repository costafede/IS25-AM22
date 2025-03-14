package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Side;

import java.util.List;

public abstract class ComponentTile {
    protected Side topSide;
    protected Side bottomSide;
    protected Side leftSide;
    protected Side rightSide;
    private String pngName;

    public ComponentTile(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        this.topSide = topSide;
        this.bottomSide = bottomSide;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.pngName = pngName;
    }

    public void rotateClockwise() {
        Side tmp = topSide;
        topSide = leftSide;
        leftSide = bottomSide;
        bottomSide = rightSide;
        rightSide = tmp;
    }

    public void rotateCounterClockwise() {
        Side tmp = topSide;
        topSide = rightSide;
        rightSide = bottomSide;
        bottomSide = leftSide;
        leftSide = tmp;
    }

    public String getPngName() {
        return pngName;
    }

    public Side getTopSide(){
        return topSide;
    }

    public Side getBottomSide(){
        return bottomSide;
    }

    public Side getLeftSide(){
        return leftSide;
    }

    public Side getRightSide(){
        return rightSide;
    }

    public void addGoodBlock(GoodBlock gb){
        return;
    }

    public GoodBlock removeGoodBlock(){
        return null;
    }

    public boolean isLeftSideSmooth(){
        return leftSide == Side.SMOOTH;
    }

    public boolean isRightSideSmooth(){
        return rightSide == Side.SMOOTH;
    }

    public boolean isTopSideSmooth(){
        return topSide == Side.SMOOTH;
    }

    public boolean isBottomSideSmooth(){
        return bottomSide == Side.SMOOTH;
    }

    public void activateComponent(){
        return;
    }

    public void deactivateComponent(){
        return;
    }

    public boolean isBottomSideShielded(){
        return false;
    }

    public boolean isTopSideShielded(){
        return false;
    }

    public boolean isLeftSideShielded(){
        return false;
    }

    public boolean isRightSideShielded(){
        return false;
    }

    public boolean isLeftSideCannon(){
        return false;
    }

    public boolean isBottomSideCannon(){
        return false;
    }

    public boolean isTopSideCannon(){
        return false;
    }

    public boolean isRightSideCannon(){
        return false;
    }

    public boolean isTopSideEngine(){
        return false;
    }

    public boolean isLeftSideEngine(){
        return false;
    }

    public boolean isRightSideEngine(){
        return false;
    }

    public boolean isBottomSideEngine(){
        return false;
    }

    public int getEngineStrength(){
        return 0;
    }

    public void removeBatteryToken(){
        return;
    }

    public double getCannonStrength(){
        return 0;
    }

    public void removeCrewMember(){
        return;
    }

    public void putCrewMember(){
        return;
    }

    public boolean isBlockPlaceable(GoodBlock gb){
        return false;
    }

    public String getAddonColor(){
        return null;
    }

    public void putAlien(String color){
        return;
    }

    public int getCrewNumber(){
        return 0;
    }

    public boolean isBrownAlienPresent(){
        return false;
    }

    public boolean isPurpleAlienPresent(){
        return false;
    }

    public List<GoodBlock> getGoodBlocks(){
        return null;
    }


}
