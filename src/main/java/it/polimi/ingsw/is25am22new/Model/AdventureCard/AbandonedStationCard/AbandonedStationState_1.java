package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

public class AbandonedStationState_1 extends AbandonedStationState implements Serializable {

    public AbandonedStationState_1(AbandonedStationCard abandonedStationCard) {
        super(abandonedStationCard);
    }
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){
            if(game.getShipboards().get(game.getCurrPlayer()).getCrewNumber() < abandonedStationCard.getAstronautsNumber())
                throw new IllegalStateException("Not Enough Astronauts");
            game.getFlightboard().shiftRocket(game.getCurrPlayer(), abandonedStationCard.getFlightDaysLost());
            abandonedStationCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            abandonedStationCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedStationCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
            abandonedStationCard.loadStation();
            abandonedStationCard.getObservableModel().updateAllBanks(game.getBank());
            transition(new AbandonedStationState_2(abandonedStationCard));
        }
        else if(!game.getCurrPlayer().equals(game.getLastPlayer())) {
            game.setCurrPlayerToNext();
            abandonedStationCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
        }
        else{
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null);
            abandonedStationCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            abandonedStationCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
            abandonedStationCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            abandonedStationCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    @Override
    public String getStateName() {
        return "AbandonedStationState_1";
    }
}
