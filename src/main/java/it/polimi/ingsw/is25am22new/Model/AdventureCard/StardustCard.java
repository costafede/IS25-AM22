package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;

public class StardustCard extends AdventureCard{

    public StardustCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    @Override
    public void activateCard(List<String> orderedPlayers) {
        //to be implemented
    }

    @Override
    public void activateCard(String player) {
        return;
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon) {
        return;
    }

    @Override
    public void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields) {
        return;
    }
}
