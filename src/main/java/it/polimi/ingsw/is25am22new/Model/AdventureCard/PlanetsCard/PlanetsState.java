package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public abstract class PlanetsState{
    protected PlanetsCard planetsCard;
    protected Game game;

    public PlanetsState(PlanetsCard planetsCard) {
        this.planetsCard = planetsCard;
    }

    public abstract void activateEffect(InputCommand inputCommand);
    public abstract void transition(PlanetsState planetsState);

}
