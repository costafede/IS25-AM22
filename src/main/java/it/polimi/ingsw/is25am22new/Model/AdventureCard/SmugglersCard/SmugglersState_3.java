package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.Serializable;

/**
 * Represents the third state in the smuggler's card state machine. This state determines
 * the flow of actions based on the player's decision to either accept or decline the
 * smuggler's reward. The state transitions based on the player's choice.
 */
public class SmugglersState_3 extends SmugglersState implements Serializable {
    public SmugglersState_3(SmugglersCard smugglersCard) {
        super(smugglersCard);
    }

    /**
     * Activates the effect of the smuggler's card based on the player's input choice.
     * If the player accepts the reward, the flightboard is updated, and the game transitions to the next smuggler's state.
     * If the player declines, smuggled goods are unloaded, the game state is updated, and the turn ends.
     *
     * @param inputCommand the command object containing the player's decision and other input parameters.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        if(inputCommand.getChoice()){   //player decides to accept the reward
            game.getFlightboard().shiftRocket(game.getCurrPlayer(), smugglersCard.getFlightDaysLost());
            transition(new SmugglersState_4(smugglersCard));
            smugglersCard.getObservableModel().updateAllShipboard(game.getCurrPlayer(), game.getShipboards().get(game.getCurrPlayer()));
            smugglersCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
        }
        else{
            smugglersCard.unloadSmugglers();
            smugglersCard.getObservableModel().updateAllBanks(game.getBank());
            game.manageInvalidPlayers();
            game.setCurrPlayerToLeader();
            game.setCurrCard(null); //card effect has ended
            smugglersCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            smugglersCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
            smugglersCard.getObservableModel().updateAllShipboardList(game.getShipboards());
        }
    }

    @Override
    public String getStateName() {
        return "SmugglersState_3";
    }
}
