package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Side;

import java.util.List;
import java.util.Map;

public abstract class ComponentTile {
    protected Side topSide;
    protected Side bottomSide;
    protected Side leftSide;
    protected Side rightSide;
    private String pngName;
    private int color; //needed for the algorithm of checkShipboard

    public ComponentTile(String pngName, Side topSide, Side bottomSide, Side leftSide, Side rightSide) {
        this.topSide = topSide;
        this.bottomSide = bottomSide;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.pngName = pngName;
        this.color = 0;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public GoodBlock removeGoodBlock(GoodBlock goodBlock){
        return null;
    }

    public boolean isStorageCompartment(){ return false; }

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

    public int getNumOfBatteries() {
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

    public void putAstronauts(){
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

    public boolean isAlienPresent(String color){
        return false;
    }

    public Map<GoodBlock, Integer> getGoodBlocks(){
        return null;
    }

    public boolean isStartingCabin(){
        return false;
    }

    public boolean hasGoodBlock(GoodBlock gb) {
        return false;
    }

    public boolean isCabin() {
        return false;
    }

    public boolean isShieldGenerator() {
        return false;
    }

    public boolean isDoubleCannon() {
        return false;
    }

    public boolean isDoubleEngine() {
        return false;
    }

    public boolean isBattery() {
        return false;
    }

    public boolean isPurpleAlienPresent(){ return false; }

    public boolean isBrownAlienPresent(){ return false; }

    public int getNumGoodBlocks(GoodBlock gb) {
        return 0;
    }

}
