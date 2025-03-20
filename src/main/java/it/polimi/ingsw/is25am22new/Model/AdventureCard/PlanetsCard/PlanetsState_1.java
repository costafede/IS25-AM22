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
            for(int i = game.getFlightboard().getOrderedRockets().size() - 1; i >= 0; i--){   //all players who have decided to land lose flight days
                if(planetsCard.playerHasLanded(game.getFlightboard().getOrderedRockets().get(i)))
                    game.getFlightboard().shiftRocket(game.getShipboards(), game.getFlightboard().getOrderedRockets().get(i), planetsCard.getFlightDaysLost());
            }
            game.setCurrPlayerToLeader();
            planetsCard.loadPlanet(game.getCurrPlayer());
            transition(new PlanetsState_2(planetsCard));
        }
    }

    public void transition(PlanetsState planetsState){
        planetsCard.setPlanetsState(planetsState);
    }
}
