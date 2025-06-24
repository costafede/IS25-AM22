package it.polimi.ingsw.is25am22new.Model.Miscellaneous;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock.REDBLOCK;
import static it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock.YELLOWBLOCK;
import static it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock.GREENBLOCK;
import static it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock.BLUEBLOCK;

/**
 * The Bank class represents a repository that manages different types of GoodBlocks
 * available in a game. Each GoodBlock type in the bank has an associated count,
 * which can be incremented (deposit) or decremented (withdraw) based on specific operations.
 * The bank ensures proper handling of these blocks by implementing logical constraints
 * on their deposit and withdrawal.
 */
public class Bank implements Serializable {

    private final Map<GoodBlock, Integer> goodblockToNum;

    /**
     * Initializes a new instance of the Bank class with a predefined initial quantity
     * of each type of GoodBlock. The Bank serves as a repository managing the inventory
     * of various block types, tracking their quantities and ensuring correct handling
     * during gameplay.
     *
     * The initial quantities for the block types are:
     * - REDBLOCK: 12
     * - YELLOWBLOCK: 17
     * - GREENBLOCK: 13
     * - BLUEBLOCK: 14
     *
     * The Bank is designed to allow for operations such as depositing and withdrawing
     * blocks, as well as querying and setting their quantities.
     */
    //Initialize the bank with the number of blocks available for the game
    public Bank() {
        goodblockToNum = new HashMap<>();
        goodblockToNum.put(REDBLOCK, 12);
        goodblockToNum.put(YELLOWBLOCK, 17);
        goodblockToNum.put(GREENBLOCK, 13);
        goodblockToNum.put(BLUEBLOCK, 14);
    }

    /**
     * Increases the quantity of the specified GoodBlock type in the bank by one.
     *
     * @param gb the type of GoodBlock to be deposited into the bank. Valid types include REDBLOCK, YELLOWBLOCK, GREENBLOCK, and BLUEBLOCK.
     */
    //increase the amount of data block type in the bank
    public void depositGoodBlock(GoodBlock gb){
        if (gb.equals(REDBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
        else if (gb.equals(YELLOWBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
        else if (gb.equals(GREENBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
        else if (gb.equals(BLUEBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
    }

    /**
     * Decreases the count of the specified GoodBlock type in the bank if it is present and returns true.
     * If the specified block type does not exist or is unavailable, the method does nothing and returns false.
     *
     * @param gb the GoodBlock type to be withdrawn from the bank
     * @return true if the specified block type was successfully withdrawn, false otherwise
     */
    //decreases the amount of the given block type in the bank if it's present returning true or else does nothing and returns false
    public boolean withdrawGoodBlock(GoodBlock gb){
        if(goodblockToNum.get(gb) > 0) {
            if (gb.equals(REDBLOCK)) {
                goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
            }
            if (gb.equals(YELLOWBLOCK)) {
                goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
            }
            if (gb.equals(GREENBLOCK)) {
                goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
            }
            if (gb.equals(BLUEBLOCK)) {
                goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
            }
            return true;
        }
        return false;
    }

    /**
     * Retrieves the number of blocks of the specified type currently available in the bank.
     *
     * @param gb the GoodBlock type for which the count is to be retrieved
     * @return the number of blocks of the specified type
     */
    public int getNumGoodBlock(GoodBlock gb){
        return goodblockToNum.get(gb);
    }

    /**
     * Updates the count associated with a specific GoodBlock type in the bank.
     * This method sets a new number for the provided GoodBlock in the internal map.
     *
     * @param gb the GoodBlock type whose count is to be updated
     * @param num the new count to set for the specified GoodBlock
     */
    public void setGoodblockToNum(GoodBlock gb, int num){
        goodblockToNum.put(gb, num);
    }

    /**
     * Retrieves the mapping of each GoodBlock type to its corresponding count
     * within the bank. This map represents the current inventory of GoodBlocks.
     *
     * @return a map where each key is a GoodBlock type and each value is the count
     *         of that specific GoodBlock available in the bank.
     */
    public Map<GoodBlock, Integer> getGoodblockToNum() {
        return goodblockToNum;
    }
}
