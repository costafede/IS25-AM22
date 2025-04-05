package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class PlanetsState_1 extends PlanetsState {
    public PlanetsState_1(PlanetsCard planetsCard) {
        super(planetsCard);
    }

    public void activateEffect(InputCommand inputCommand){
        if(inputCommand.getChoice()) {
            if(planetsCard.getPlayersWhoLanded().contains(game.getCurrPlayer()))
                throw new IllegalArgumentException("Player has already landed on a planet");
            planetsCard.getPlanets().get(inputCommand.getIndexChosen()).setPlayer(game.getCurrPlayer());
            planetsCard.getPlayersWhoLanded().add(game.getCurrPlayer());
        }
        if(!planetsCard.planetsFull() && !game.getCurrPlayer().equals(game.getLastPlayer()))
            game.setCurrPlayerToNext();
        else {
            for(int i = planetsCard.getPlayersWhoLanded().size() - 1; i >= 0; i--){   //all players who have decided to land lose flight days
                game.getFlightboard().shiftRocket(planetsCard.getPlayersWhoLanded().get(i), planetsCard.getFlightDaysLost());
            }
            game.setCurrPlayerToLeader();
            planetsCard.loadPlanet(game.getCurrPlayer());
            transition(new PlanetsState_2(planetsCard));
        }
    }
}
