package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;

/**
 * The StringConverter class provides utility methods to convert string representations
 * into specific objects or values used within the application.
 */
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

    /**
     * translates the coordinates of the shipboard into the ones of the component tiles grid
     */
    public static int stringToGridRow(String row) throws NumberFormatException {
        int i;
        i = switch (row) {
            case "5" -> 0;
            case "6" -> 1;
            case "7" -> 2;
            case "8" -> 3;
            case "9" -> 4;
            default -> throw new NumberFormatException();
        };
        return i;
    }
    /**
     * Converts a string representing a column label (from "4" to "10")
     * into the corresponding zero-based grid column index.
     *
     * The mapping is as follows:
     *
     *     "4" → 0
     *     "5" → 1
     *     "6" → 2
     *     "7" → 3
     *     "8" → 4
     *     "9" → 5
     *     "10" → 6
     * @param col the string representing the column number (from "4" to "10")
     * @return the zero-based index of the column in the grid
     * @throws NumberFormatException if the input string is not between "4" and "10"
     */
    public static int stringToGridCol(String col) {
        int j;
        j = switch (col) {
            case "4" -> 0;
            case "5" -> 1;
            case "6" -> 2;
            case "7" -> 3;
            case "8" -> 4;
            case "9" -> 5;
            case "10" -> 6;
            default -> throw new NumberFormatException();
        };
        return j;
    }

}
