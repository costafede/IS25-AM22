package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Network.ObservableModel;

import java.io.Serializable;

/**
 * Represents an abstract Adventure Card in the game.
 * This class serves as a base for specific types of adventure cards
 * that can be used during gameplay. Each card is associated with an image,
 * a name, a game instance, a difficulty level, and its tutorial mode status.
 * Adventure cards have customizable behavior to define their effects and state.
 * Concrete subclasses must implement specific behavior by overriding abstract methods.
 */
public abstract class AdventureCard implements Serializable {

    protected String pngName;
    protected String name;
    protected Game game;
    protected ObservableModel observableModel;
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

    public ObservableModel getObservableModel() {
        return observableModel;
    }

    public AdventureCard(String pngName, String name, Game game, int level, boolean tutorial) {
        this.pngName = pngName;
        this.name = name;
        this.game = game;
        this.level = level;
        this.tutorial = tutorial;
        this.observableModel = game;
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
