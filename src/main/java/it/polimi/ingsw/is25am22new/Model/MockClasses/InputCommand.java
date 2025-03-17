package it.polimi.ingsw.is25am22new.Model.MockClasses;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;

public class InputCommand {
    private boolean choiceCommand; // used for yes or no choices commands
    private float xCoordClick; // stores the x coordinate of the mouse click
    private float yCoordClick; // stores the y coordinate of the mouse click
    private int xCoordGrid; // store the correspondent x coordinate of the grid
    private int yCoordGrid; // store the correspondent y coordinate of the grid
    private GoodBlock goodBlock; // used for list choices commands

    public InputCommand() {
        choiceCommand = false;
        xCoordClick = 0;
        yCoordClick = 0;
        xCoordGrid = 0;
        yCoordGrid = 0;
    }

    public boolean getChoice() { return choiceCommand;}

    public float getXCoordClick() { return xCoordClick;}

    public float getYCoordClick() { return yCoordClick;}

    public int getXCoordGrid() { return xCoordGrid;}

    public int getYCoordGrid() { return yCoordGrid;}

    public GoodBlock getGoodBlock() { return goodBlock;}

    public void setGoodBlock(GoodBlock goodBlock) { this.goodBlock = goodBlock;}
}
