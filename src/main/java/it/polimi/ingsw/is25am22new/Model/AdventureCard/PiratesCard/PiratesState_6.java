package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

/**
 * Class representing the PiratesState_6, a specific state of the PiratesCard in
 * the game. This state implements game-specific logic that activates when the
 * pirates card transitions to this particular phase.
 *
 * The state defines the behavior when a player's action (input command) is executed
 * during the gameplay. It handles actions such as updating the game state, processing
 * shipboard actions, managing defeated players, advancing shot sequences, and ensuring
 * correct state transitions based on game rules.
 *
 * PiratesState_6 extends the abstract PiratesState and overrides the necessary
 * methods, defining its unique behavior and state-specific responsibilities.
 */
public class PiratesState_6 extends PiratesState implements Serializable {

    /**
     * Initializes the PiratesState_6 object with the given PiratesCard.
     * @param card
     */
    public PiratesState_6(PiratesCard card) {
        super(card);
    }

    /**
     * Activates the effect of the current state based on the input command. It processes
     * player actions, updates game and shipboard states, and transitions to the next
     * game state depending on the results of the action.
     *
     * @param inputCommand the input command containing information about the player's action,
     *                     including the selected row and column for the shipwreck action.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        shipboard.chooseShipWreck(x, y);
        piratesCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);

        if(piratesCard.getCurrDefeatedPlayer().equals(piratesCard.getLastDefeatedPlayer())) {
            piratesCard.setNextIndexOfShot();
            piratesCard.setCurrDefeatedPlayerToFirst();
            game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            piratesCard.getObservableModel().updateAllCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            if(piratesCard.thereAreStillShots()) {
                transition(new PiratesState_4(piratesCard));
            }
            else {
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                piratesCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                piratesCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
        }
        else {
            piratesCard.setCurrDefeatedPlayerToNext();
            game.setCurrPlayer(piratesCard.getCurrDefeatedPlayer());
            piratesCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new PiratesState_4(piratesCard));
        }
    }

    /**
     * Returns the name of the current state.
     * @return
     */
    @Override
    public String getStateName() {
        return "PiratesState_6";
    }
}
