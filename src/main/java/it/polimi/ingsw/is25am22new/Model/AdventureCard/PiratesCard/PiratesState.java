package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class PiratesState implements Serializable {
    protected PiratesCard piratesCard;
    protected Game game;

    /**
     * Constructs a new PiratesState for the given PiratesCard.
     * @param piratesCard
     */
    public PiratesState(PiratesCard piratesCard) {
        this.piratesCard = piratesCard;
        this.game = piratesCard.getGame();
    }

    /**
     * Activates the effect associated with this state.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Transitions the card to a new state.
     * @param piratesState
     */
    protected void transition(PiratesState piratesState) {
        piratesCard.setPiratesState(piratesState);
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    public abstract String getStateName();

}
