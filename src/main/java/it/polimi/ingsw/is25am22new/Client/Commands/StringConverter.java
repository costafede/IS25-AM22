package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

public class StringConverter {
    public static GoodBlock stringToGoodBlock(String block) {
        if(block.equalsIgnoreCase("redblock"))
            return GoodBlock.REDBLOCK;
        if(block.equalsIgnoreCase("blueblock"))
            return GoodBlock.BLUEBLOCK;
        if(block.equalsIgnoreCase("greenblock"))
            return GoodBlock.GREENBLOCK;
        if(block.equalsIgnoreCase("yellowblock"))
            return GoodBlock.YELLOWBLOCK;
        else
            return null;
    }
}
