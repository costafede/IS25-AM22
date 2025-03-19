package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

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

    // for tests??
    public AdventureCard(String name, Game game) {
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public int getLevel() {
        return level;
    }

    // the check is made before asking the input to the player
    // returns true if actionable, false otherwise
    public abstract boolean checkActivationConditions(String nickname);

    // returning false means it has to go the next player turn
    // returning true means it has to stay in the same turn, next phase
    public abstract boolean activateCardPhase(String nickname, InputCommand inputCommand);
    public abstract boolean receiveInputPhase(String nickname, InputCommand inputCommand);
    public abstract void resolveCardEffectPhase(String nickname);
}
