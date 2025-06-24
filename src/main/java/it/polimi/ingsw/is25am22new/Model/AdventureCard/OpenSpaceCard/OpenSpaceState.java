package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class OpenSpaceState implements Serializable {
    protected OpenSpaceCard openSpaceCard;
    protected Game game;

    /**
     * Constructs a new OpenSpaceState for the given OpenSpaceCard.
     * @param openSpaceCard
     */
    public OpenSpaceState(OpenSpaceCard openSpaceCard) {
        this.openSpaceCard = openSpaceCard;
        game = openSpaceCard.getGame();
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    public abstract String getStateName();

    /**
     * Activates the effect associated with this state.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Transitions the card to a new state.
     * @param openSpaceState
     */
    public void transition(OpenSpaceState openSpaceState) {
        openSpaceCard.setOpenSpaceState(openSpaceState);
    }
}
