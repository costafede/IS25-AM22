package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;

/**
 * Represents the third state in the state pattern implementation for the
 * SlaversCard effect. This state allows the player to decide whether
 * to keep the acquired cosmic credits by sacrificing flight days, and
 * deactivates components on the shipboard afterward.
 *
 * This class extends the abstract SlaversState class and implements the
 * Serializable interface.
 */
public class SlaversState_3 extends SlaversState implements Serializable {
    public SlaversState_3(SlaversCard slaversCard) {
        super(slaversCard);
    }

    /**
     * Activates the effect of the current SlaversCard state. This method allows the current player
     * to decide whether to accept cosmic credits at the cost of losing flight days. It also deactivates
     * all components on the player's shipboard and updates the game state accordingly.
     *
     * @param inputCommand an InputCommand object representing the player's choice to keep cosmic credits
     *                     and lose flight days or not. The choice is indicated via the `getChoice()` method.
     */
    @Override
    public void activateEffect(InputCommand inputCommand) {
        String currentPlayer = game.getCurrPlayer();
        Shipboard shipboard = game.getShipboards().get(currentPlayer);

        // choose to keep credits and lose flight days or not
        if(inputCommand.getChoice()) {
            game.getFlightboard().shiftRocket(currentPlayer, slaversCard.getFlightDaysLost());
            shipboard.addCosmicCredits(slaversCard.getCredits());
            slaversCard.getObservableModel().updateAllShipboard(currentPlayer, shipboard);
        }

        // deactivates all components
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                game.getShipboards().get(currentPlayer).getComponentTileFromGrid(i ,j).ifPresent(ComponentTile::deactivateComponent);
            }
        }
        slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());

        // ends the card effect
        game.manageInvalidPlayers();
        game.setCurrPlayerToLeader();
        game.setCurrCard(null);
        slaversCard.getObservableModel().updateAllCurrPlayer(game.getCurrPlayer());
        slaversCard.getObservableModel().updateAllFlightboard(game.getFlightboard());
        slaversCard.getObservableModel().updateAllShipboardList(game.getShipboards());
    }

    @Override
    public String getStateName() {
        return "SlaversState_3";
    }
}
