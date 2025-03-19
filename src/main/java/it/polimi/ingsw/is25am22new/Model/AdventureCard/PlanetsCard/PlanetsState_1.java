package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class PlanetsState_1 extends PlanetsState {
    public PlanetsState_1(PlanetsCard planetsCard) {
        super(planetsCard);
    }

    public void activateEffect(InputCommand inputCommand){
        currPlayer = planetsCard.getGame().getCurrPlayer();
        planetsCard.getPlanets().get(InputCommand.getChosenPlanet()).setPlayer(currPlayer);
        if(!planetsCard.planetsFull() && !currPlayer.equals(planetsCard.getGame().getLastPlayer())) {
            planetsCard.getGame().setCurrPlayerToNext();
            return;
        }
        else {
            planetsCard.getGame().setCurrPlayerToLeader();
            transition(new PlanetsState_2(planetsCard));
        }
    }

    public void transition(PlanetsState planetsState){
        planetsCard.setPlanetsState(planetsState);
    }
}
