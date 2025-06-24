package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class SmugglersState implements Serializable {
    protected SmugglersCard smugglersCard;
    protected Game game;

    /**
     * Constructs a new SmugglersState object with the associated adventure card.
     * @param smugglersCard
     */
    public SmugglersState(SmugglersCard smugglersCard) {
        this.smugglersCard = smugglersCard;
        this.game = smugglersCard.getGame();
    }

    /**
     * Activates the effect of the current state.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);
    public void transition(SmugglersState smugglersState){
        smugglersCard.setSmugglersState(smugglersState);
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    public abstract String getStateName();
}
