package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.io.Serializable;

public abstract class PlanetsState implements Serializable {
    protected PlanetsCard planetsCard;
    protected Game game;

    /**
     * Constructs a new PlanetsState for the given PlanetsCard.
     * @param planetsCard
     */
    public PlanetsState(PlanetsCard planetsCard) {
        this.planetsCard = planetsCard;
        this.game = planetsCard.getGame();
    }

    /**
     * Activates the effect of the current state.
     * @param inputCommand
     */
    public abstract void activateEffect(InputCommand inputCommand);

    /**
     * Transitions the current state to a new state.
     * @param planetsState
     */
    public void transition(PlanetsState planetsState){
        planetsCard.setPlanetsState(planetsState);
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    public abstract String getStateName();

}
