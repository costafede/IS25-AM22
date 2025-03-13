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

    public void rotateClockwiseTile() {
        Side tmp = topSide;
        topSide = leftSide;
        leftSide = bottomSide;
        bottomSide = rightSide;
        rightSide = tmp;
    }

    public void rotateCounterClockwiseTile() {
        Side tmp = topSide;
        topSide = rightSide;
        rightSide = bottomSide;
        bottomSide = leftSide;
        leftSide = tmp;
    }

    public void addGoodBlockTile(GoodBlock gb){
        return;
    }

    public GoodBlock removeGoodBlockTile(){
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

    public void activateComponentTile(){
        return;
    }

    public void deactivateComponentTile(){
        return;
    }

    public boolean isBottomSideShieldedTile(){
        return false;
    }

    public boolean isTopSideShieldedTile(){
        return false;
    }

    public boolean isLeftSideShieldedTile(){
        return false;
    }

    public boolean isRightSideShieldedTile(){
        return false;
    }

    public boolean isLeftSideCannonTile(){
        return false;
    }

    public boolean isBottomSideCannonTile(){
        return false;
    }

    public boolean isTopSideCannonTile(){
        return false;
    }

    public boolean isRightSideCannonTile(){
        return false;
    }

    public boolean isTopSideEngineTile(){
        return false;
    }

    public boolean isLeftSideEngineTile(){
        return false;
    }

    public boolean isRightSideEngineTile(){
        return false;
    }

    public boolean isBottomSideEngineTile(){
        return false;
    }

    public int getEngineStrengthTile(){
        return 0;
    }

    public void removeBatteryTokenTile(){
        return;
    }

    public double getCannonStrengthTile(){
        return 0;
    }

    public void removeCrewMemberTile(){
        return;
    }

    public void putCrewMemberTile(){
        return;
    }

    public boolean isBlockPlaceableTile(GoodBlock gb){
        return false;
    }

    public String getAddonColorTile(){
        return null;
    }

    public void putAlienTile(String color){
        return;
    }

    public int getCrewNumberTile(){
        return 0;
    }

    public boolean isBrownAlienPresentTile(){
        return false;
    }

    public boolean isPurpleAlienPresentTile(){
        return false;
    }

    public List<GoodBlock> getGoodBlocksTile(){
        return null;
    }
}
