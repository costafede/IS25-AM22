package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class AdventureCard implements Serializable {

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

    public boolean isTutorial() {
        return tutorial;
    }

    public int getLevel() {
        return level;
    }

    public abstract void activateEffect(InputCommand inputCommand);

    public abstract String getStateName();
}
