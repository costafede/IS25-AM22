package it.polimi.ingsw.is25am22new.Model.MockClasses;

public class InputCommand {
    private boolean choiceCommand; // used for yes or no choices commands
    private float xCoordClick; // stores the x coordinate of the mouse click
    private float yCoordClick; // stores the y coordinate of the mouse click
    private int xCoordGrid; // store the correspondent x coordinate of the grid
    private int yCoordGrid; // store the correspondent y coordinate of the grid

    public InputCommand() {
    }

    public boolean getChoice() { return choiceCommand;}

    public float getXCoordClick() { return xCoordClick;}

    public float getYCoordClick() { return yCoordClick;}

    public int getXCoordGrid() { return xCoordGrid;}

    public int getYCoordGrid() { return yCoordGrid;}
}
