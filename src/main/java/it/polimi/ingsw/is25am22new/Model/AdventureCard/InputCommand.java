package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.GoodBlock;

import java.util.Map;

public class InputCommand {
    private boolean choiceCommand; // used for yes or no choices commands
    private int indexChosen; //used to choose an element in a list or map depending on the current card
    private GoodBlock goodBlock, goodBlock_1;
    private boolean addingGoodBlock;
    private boolean removingGoodBlock;
    private boolean switchingGoodBlock;
    private int row, row_1;
    private int col, col_1;


    public InputCommand() {
        choiceCommand = false;
        indexChosen = 0;
    }

    public boolean getChoice() { return choiceCommand;}

    public int getIndexChosen() { return indexChosen;}

    public boolean isAddingGoodBlock() { return addingGoodBlock;}

    public boolean isRemovingGoodBlock() { return removingGoodBlock;}

    public boolean isSwitchingGoodBlock() { return switchingGoodBlock;}

    public int getRow() { return row;}

    public int getCol() { return col;}

    public int getRow_1() { return row_1;}

    public int getCol_1() { return col_1;}

    public GoodBlock getGoodBlock_1() { return goodBlock_1;}

    /*these setters are used to create the inputCommand objects in the model test cases,the controller will just use the constructor*/

    public GoodBlock getGoodBlock() { return goodBlock;}

    public void setGoodBlock(GoodBlock goodBlock) { this.goodBlock = goodBlock;}

    public void setIndexChosen(int indexChosen) { this.indexChosen = indexChosen;}

    public void setChoice(boolean choice) { this.choiceCommand = choice;}

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
