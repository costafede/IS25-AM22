package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

public class SmugglersState_3 extends SmugglersState implements Serializable {
    public SmugglersState_3(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){   //player decides to accept the reward
            game.getFlightboard().shiftRocket(game.getCurrPlayer(), smugglersCard.getFlightDaysLost());
            transition(new SmugglersState_4(smugglersCard));
            smugglersCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
            smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            smugglersCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
        }
        else{
            smugglersCard.unloadSmugglers();
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null); //card effect has ended
            smugglersCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            smugglersCard.getObservableModel().updateAllCurrCard(game.getCurrCard());
            smugglersCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            smugglersCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    @Override
    public String getStateName() {
        return "SmugglersState_3";
    }
}
