package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.Map;

public class InputCommand {
    private boolean choiceCommand; // used for yes or no choices commands
    private int indexChosen; //used to choose an element in a list or map depending on the current card
    private GoodBlock goodBlock;
    private boolean addingGoodBlock;
    private boolean removingGoodBlock;
    private int row;
    private int col;


    public InputCommand() {
        choiceCommand = false;
        indexChosen = 0;
    }

    public boolean getChoice() { return choiceCommand;}

    public int getIndexChosen() { return indexChosen;}

    public boolean isAddingGoodBlock() { return addingGoodBlock;}

    public boolean isRemovingGoodBlock() { return removingGoodBlock;}

    public int getRow() { return row;}

    public int getCol() { return col;}

    /*these setters are used to create the inputCommand objects in the model test cases,the controller will just use the constructor*/

    public GoodBlock getGoodBlock() { return goodBlock;}

    public void setGoodBlock(GoodBlock goodBlock) { this.goodBlock = goodBlock;}

    public void setIndexChosen(int indexChosen) { this.indexChosen = indexChosen;}


}
