package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

/**
 * Represents the third state in the meteor swarm card's state management within the game.
 * This state is responsible for handling the logic and effects when the MeteorSwarmCard is
 * in its third phase during gameplay.
 *
 * In this state, the current player interacts with the game mechanics to choose a position
 * on their shipboard for handling shipwrecks and updates the game state accordingly.
 * The state transitions to another state based on the game flow, and it ensures updates
 * are communicated to all relevant game components.
 */
public class MeteorSwarmState_3 extends MeteorSwarmState implements Serializable {

    /**
     * Initializes the meteor swarm state with the given adventure card.
     * @param meteorSwarmCard
     */
    public MeteorSwarmState_3(MeteorSwarmCard meteorSwarmCard) {
       super(meteorSwarmCard);
    }

    /**
     * Activates the meteor swarm state's effect based on the player's input.
     * @param inputCommand
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        int x = inputCommand.getRow();
        int y = inputCommand.getCol();

        shipboard.chooseShipWreck(x, y);
        meteorSwarmCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);

        if(game.getCurrPlayer().equals(game.getLastPlayer())) {
            meteorSwarmCard.setNextIndexOfMeteor();
            if(meteorSwarmCard.thereAreStillMeteors()) {
                game.setCurrPlayerToLeader();
                meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                transition(new MeteorSwarmState_1(meteorSwarmCard));
            }
            else {
                game.manageInvalidPlayers();
                game.setCurrPlayerToLeader();
                game.setCurrCard(null);
                meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
                meteorSwarmCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
                meteorSwarmCard.getObservableModel().updateAllShipboardList(game.getShipboards());
            }
        }
        else {
            game.setCurrPlayerToNext();
            meteorSwarmCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
            transition(new MeteorSwarmState_1(meteorSwarmCard));
        }
    }

    /**
     * Returns the name of the state, used for debugging purposes.
     * @return
     */
    @Override
    public String getStateName() {
        return "MeteorSwarmState_3";
    }
}
