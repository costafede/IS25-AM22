package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class PlanetsState_1 extends PlanetsState {
    public PlanetsState_1(PlanetsCard planetsCard) {
        super(planetsCard);
    }

    public void activateEffect(InputCommand inputCommand){
        if(inputCommand.getChoice())
            planetsCard.getPlanets().get(inputCommand.getIndexChosen()).setPlayer(game.getCurrPlayer());
        if(!planetsCard.planetsFull() && !game.getCurrPlayer().equals(game.getLastPlayer()))
            game.setCurrPlayerToNext();
        else {
            game.setCurrPlayerToLeader();
            transition(new PlanetsState_2(planetsCard));
        }
    }

    public void transition(PlanetsState planetsState){
        planetsCard.setPlanetsState(planetsState);
    }
}
