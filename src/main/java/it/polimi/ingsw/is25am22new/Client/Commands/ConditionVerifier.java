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
}
