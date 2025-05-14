package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

public class PlanetsState_1 extends PlanetsState implements Serializable {
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
        if(!planetsCard.planetsFull() && !game.getCurrPlayer().equals(game.getLastPlayer())) {
            game.setCurrPlayerToNext();
            planetsCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
        }
        else {
            if(planetsCard.getPlayersWhoLanded().isEmpty()){
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                planetsCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                planetsCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                planetsCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
            else{
                for (int i = planetsCard.getPlayersWhoLanded().size() - 1; i >= 0; i--) {   //all players who have decided to land lose flight days
                    game.getFlightboard().shiftRocket(planetsCard.getPlayersWhoLanded().get(i), planetsCard.getFlightDaysLost());
                }
                planetsCard.getObservableModel().updateAllShipboardList(game.getShipboards());
                planetsCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                game.setCurrPlayer(planetsCard.getPlayersWhoLanded().getFirst());
                planetsCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                planetsCard.loadPlanet(game.getCurrPlayer());
                planetsCard.getObservableModel().updateAllBanks(game.getBank());
                transition(new PlanetsState_2(planetsCard));
            }
        }
    }

    @Override
    public String getStateName() {
        return "PlanetsState_1";
    }
}
