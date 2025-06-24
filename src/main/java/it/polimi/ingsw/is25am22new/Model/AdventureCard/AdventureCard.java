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
    /**
     * Constructs a new {@code AdventureCard}.
     *
     * @param pngName   the filename of the image representing the card
     * @param name      the name of the card
     * @param game      the current game instance
     * @param level     the level or difficulty of the card
     * @param tutorial  {@code true} if this card is part of the tutorial, {@code false} otherwise
     */
    public AdventureCard(String pngName, String name, Game game, int level, boolean tutorial) {
        this.pngName = pngName;
        this.name = name;
        this.game = game;
        this.level = level;
        this.tutorial = tutorial;
        this.observableModel = game;
    }

    /**
     * Returns the name of the PNG image associated with the card.
     *
     * @return the PNG filename
     */
    public String getPngName() {
        return pngName;
    }

    /**
     * Returns the name of the card.
     *
     * @return the card name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the game instance associated with this card.
     *
     * @return the game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns the observable model associated with this card (usually the game).
     *
     * @return the observable model
     */
    public ObservableModel getObservableModel() {
        return observableModel;
    }

    /**
     * Indicates whether this card is part of the tutorial.
     *
     * @return {@code true} if it's a tutorial card; {@code false} otherwise
     */
    public boolean isTutorial() {
        return tutorial;
    }

    /**
     * Returns the level or difficulty of this card.
     *
     * @return the level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * Activates the effect of the card based on the given input command.
     *
     * @param inputCommand the input provided by the player or controller
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Returns the name of the current state associated with this card.
     *
     * @return the state name
     */
    public abstract String getStateName();
}
