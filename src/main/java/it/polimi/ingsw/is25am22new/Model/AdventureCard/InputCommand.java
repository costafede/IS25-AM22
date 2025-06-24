package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

import java.io.Serializable;

/**
 * Represents a command input structure used to manage various game actions.
 * It is used within game operations to capture user inputs and flags for
 * specific actions, such as manipulating good blocks or making selections.
 * This class encapsulates the data related to these inputs and ensures
 * it can be serializable for persistence or network transmission.
 */
public class InputCommand implements Serializable {
    private boolean choiceCommand; // used for yes or no choices commands
    private int indexChosen; //used to choose an element in a list or map depending on the current card
    private GoodBlock goodBlock, goodBlock_1;
    private boolean addingGoodBlock;
    private boolean removingGoodBlock;
    private boolean switchingGoodBlock;
    private int row, row_1;
    private int col, col_1;

    /**
     * Constructs a default {@code InputCommand} with no flags and default values.
     */
    public InputCommand() {
        choiceCommand = false;
        indexChosen = 0;
    }

    /**
     * Returns the user's choice flag.
     *
     * @return {@code true} if a choice was made, {@code false} otherwise
     */
    public boolean getChoice() {
        return choiceCommand;
    }

    /**
     * Returns the index chosen by the user.
     *
     * @return the index selected
     */
    public int getIndexChosen() {
        return indexChosen;
    }

    /**
     * Returns whether this command represents adding a {@link GoodBlock}.
     *
     * @return {@code true} if adding a block, {@code false} otherwise
     */
    public boolean isAddingGoodBlock() {
        return addingGoodBlock;
    }

    /**
     * Returns whether this command represents removing a {@link GoodBlock}.
     *
     * @return {@code true} if removing a block, {@code false} otherwise
     */
    public boolean isRemovingGoodBlock() {
        return removingGoodBlock;
    }

    /**
     * Returns whether this command represents switching two {@link GoodBlock}s.
     *
     * @return {@code true} if switching blocks, {@code false} otherwise
     */
    public boolean isSwitchingGoodBlock() {
        return switchingGoodBlock;
    }

    /**
     * Returns the row index of the target tile.
     *
     * @return the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of the target tile.
     *
     * @return the column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the second row index (used for switching).
     *
     * @return the second row index
     */
    public int getRow_1() {
        return row_1;
    }

    /**
     * Returns the second column index (used for switching).
     *
     * @return the second column index
     */
    public int getCol_1() {
        return col_1;
    }

    /**
     * Returns the first {@link GoodBlock} involved in a switch operation.
     *
     * @return the first block
     */
    public GoodBlock getGoodBlock_1() {
        return goodBlock_1;
    }

    /**
     * Returns the primary {@link GoodBlock} involved in this command.
     *
     * @return the good block
     */
    public GoodBlock getGoodBlock() {
        return goodBlock;
    }

    /**
     * Sets the main {@link GoodBlock} involved in this command.
     *
     * @param goodBlock the block to set
     */
    public void setGoodBlock(GoodBlock goodBlock) {
        this.goodBlock = goodBlock;
    }

    /**
     * Sets the index chosen by the user.
     *
     * @param indexChosen the index value
     */
    public void setIndexChosen(int indexChosen) {
        this.indexChosen = indexChosen;
    }

    /**
     * Sets the choice flag for the command.
     *
     * @param choice {@code true} if a choice was made, {@code false} otherwise
     */
    public void setChoice(boolean choice) {
        this.choiceCommand = choice;
    }

    /**
     * Sets the target row for the command.
     *
     * @param row the row index
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the target column for the command.
     *
     * @param col the column index
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Sets the second row for a switching operation.
     *
     * @param row_1 the second row index
     */
    public void setRow_1(int row_1) {
        this.row_1 = row_1;
    }

    /**
     * Sets the second column for a switching operation.
     *
     * @param col_1 the second column index
     */
    public void setCol_1(int col_1) {
        this.col_1 = col_1;
    }

    /**
     * Flags this command as an "add good block" operation.
     */
    public void flagIsAddingGoodBlock() {
        this.addingGoodBlock = true;
    }

    /**
     * Flags this command as a "remove good block" operation.
     */
    public void flagIsRemovingGoodBlock() {
        this.removingGoodBlock = true;
    }

    /**
     * Flags this command as a "switch good block" operation.
     */
    public void flagSwitchingGoodBlock() {
        this.switchingGoodBlock = true;
    }

    /**
     * Sets the second {@link GoodBlock} involved in a switch operation.
     *
     * @param goodBlock_1 the block to set
     */
    public void setGoodBlock_1(GoodBlock goodBlock_1) {
        this.goodBlock_1 = goodBlock_1;
    }
}
