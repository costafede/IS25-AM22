package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;

public abstract class AdventureCard {

    protected String pngName;
    protected String name;
    protected Game game;
    protected int level;
    protected boolean tutorial;

    public String getPngName() {
        return pngName;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public AdventureCard(String pngName, String name, Game game, int level, boolean tutorial) {
        this.pngName = pngName;
        this.name = name;
        this.game = game;
        this.level = level;
        this.tutorial = tutorial;
    }

    public AdventureCard(String name, Game game) {
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public int getLevel() {
        return level;
    }

    public abstract void activateCard(List<String> orderedPlayers);
    public abstract void activateCard(String player);
    public abstract void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields, List<String> activatingCannon);
    public abstract void activateCard(List<String> orderedPlayers, List<Integer> dicesResults, List<String> activatingShields);
}
