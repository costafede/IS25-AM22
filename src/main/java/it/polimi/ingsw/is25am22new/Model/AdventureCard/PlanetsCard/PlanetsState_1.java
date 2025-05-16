package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * Represents the first state in the PlanetsState sequence, managing the interactions
 * for exploring planets in the game. This state ensures proper sequencing of player
 * actions, validations, and transitions to subsequent states based on game logic.
 */
public class PlanetsState_1 extends PlanetsState implements Serializable {
    public PlanetsState_1(PlanetsCard planetsCard) {
        super(planetsCard);
    }

    /**
     * Activates the effect of the current state, managing player actions, game state updates,
     * and transition logic based on the player's input and the current game conditions.
     *
     * @param inputCommand the command object containing player choices and parameters
     *                      necessary for performing the action. It includes the player's
     *                      decision to participate and the index of the chosen planet.
     */
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
