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

    //Initialize the bank with the number of blocks available for the game
    public Bank() {
        goodblockToNum = new HashMap<>();
        goodblockToNum.put(REDBLOCK, 12);
        goodblockToNum.put(YELLOWBLOCK, 17);
        goodblockToNum.put(GREENBLOCK, 13);
        goodblockToNum.put(BLUEBLOCK, 14);
    }

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

    public int getNumGoodBlock(GoodBlock gb){
        return goodblockToNum.get(gb);
    }

    public void setGoodblockToNum(GoodBlock gb, int num){
        goodblockToNum.put(gb, num);
    }

    public Map<GoodBlock, Integer> getGoodblockToNum() {
        return goodblockToNum;
    }
}
