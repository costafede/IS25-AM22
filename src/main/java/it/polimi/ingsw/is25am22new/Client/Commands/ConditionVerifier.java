package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GameType;

import java.util.List;

public class ConditionVerifier {
    public static boolean coordinatesAreNotOutOfBound(int i, int j, ClientModel model) {
        GameType gameType = model.getGametype();
        switch (gameType) {
            case TUTORIAL:
                return List.of(                     List.of(5,7),
                                      List.of(6,6), List.of(6,7), List.of(6,8),
                        List.of(7,5), List.of(7,6), List.of(7,7), List.of(7,8), List.of(7,9),
                        List.of(8,5), List.of(8,6), List.of(8,7), List.of(8,8), List.of(8,9),
                        List.of(9,5), List.of(9,6),               List.of(9,8), List.of(9,9)).contains(List.of(i,j));
            case LEVEL2:
                return List.of(                       List.of(5, 6),                List.of(5, 8),
                                       List.of(6, 5), List.of(6, 6), List.of(6, 7), List.of(6, 8), List.of(6, 9),
                        List.of(7, 4), List.of(7, 5), List.of(7, 6), List.of(7, 7), List.of(7, 8), List.of(7, 9), List.of(7,10),
                        List.of(8, 4), List.of(8, 5), List.of(8, 6), List.of(8, 7), List.of(8, 8), List.of(8, 9), List.of(8,10),
                        List.of(9, 4), List.of(9, 5), List.of(9, 6),                List.of(9, 8), List.of(9, 9), List.of(9,10)).contains(List.of(i,j));
            default:
                throw new IllegalStateException("Model has no GameType (LEVEL2 or TUTORIAL)");
        }
    }

    public static boolean gridCoordinatesAreNotOutOfBound(int i, int j, ClientModel model) {
        GameType gameType = model.getGametype();
        switch (gameType) {
            case TUTORIAL:
                return List.of(                     List.of(0,3),
                                      List.of(1,2), List.of(1,3), List.of(1,4),
                        List.of(2,1), List.of(2,2), List.of(2,3), List.of(2,4), List.of(2,5),
                        List.of(3,1), List.of(3,2), List.of(3,3), List.of(3,4), List.of(3,5),
                        List.of(4,1), List.of(4,2),               List.of(4,4), List.of(4,5)).contains(List.of(i,j));
            case LEVEL2:
                return List.of(                       List.of(0, 2),                List.of(0, 4),
                                       List.of(1, 1), List.of(1, 2), List.of(1, 3), List.of(1, 4), List.of(1, 5),
                        List.of(2, 0), List.of(2, 1), List.of(2, 2), List.of(2, 3), List.of(2, 4), List.of(2, 5), List.of(2,6),
                        List.of(3, 0), List.of(3, 1), List.of(3, 2), List.of(3, 3), List.of(3, 4), List.of(3, 5), List.of(3,6),
                        List.of(4, 0), List.of(4, 1), List.of(4, 2),                List.of(4, 4), List.of(4, 5), List.of(4,6)).contains(List.of(i,j));
            default:
                throw new IllegalStateException("Model has no GameType (LEVEL2 or TUTORIAL)");
        }
    }

    public static boolean stringIsGoodBlock(String block) {
        return block.equalsIgnoreCase("redblock") ||
                block.equalsIgnoreCase("blueblock") ||
                block.equalsIgnoreCase("greenblock") ||
                block.equalsIgnoreCase("yellowblock");
    }
}
