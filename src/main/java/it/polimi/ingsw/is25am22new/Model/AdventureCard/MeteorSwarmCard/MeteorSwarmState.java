package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsState;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class MeteorSwarmState implements Serializable {
    protected MeteorSwarmCard meteorSwarmCard;
    protected Game game;

    /**
     * Constructs a new MeteorSwarmState for the given MeteorSwarmCard.
     * @param meteorSwarmCard
     */
    public MeteorSwarmState(MeteorSwarmCard meteorSwarmCard) {
        this.meteorSwarmCard = meteorSwarmCard;
        this.game = meteorSwarmCard.getGame();
    }

    /**
     * Activates the effect associated with this state.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Transitions the card to a new state.
     * @param meteorSwarmState
     */
    protected void transition(MeteorSwarmState meteorSwarmState) {
        meteorSwarmCard.setMeteorSwarmState(meteorSwarmState);
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    public abstract String getStateName();
}
