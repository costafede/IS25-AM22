package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Game;

import java.util.List;

public abstract class AdventureCard {
    protected String name;
    protected Game game;

    public AdventureCard(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public abstract void activateCard(List<String> orderedPlayers);
    public abstract void activateCard(String player);
    public abstract void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon);
    public abstract void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields);
}
