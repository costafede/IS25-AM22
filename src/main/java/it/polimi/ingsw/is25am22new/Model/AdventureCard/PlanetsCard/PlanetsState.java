package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Games.Game;

public abstract class PlanetsState{
    protected PlanetsCard planetsCard;
    protected Game game;

    public PlanetsState(PlanetsCard planetsCard) {
        this.planetsCard = planetsCard;
        this.game = planetsCard.getGame();
    }

    public abstract void activateEffect(InputCommand inputCommand);
    public void transition(PlanetsState planetsState){
        planetsCard.setPlanetsState(planetsState);
    }

}
