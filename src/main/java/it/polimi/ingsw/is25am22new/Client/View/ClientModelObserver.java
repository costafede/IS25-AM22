package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.List;
import java.util.Map;

/**
 * The ClientModelObserver interface is designed to observe the state changes in a client-side game model
 * and to update the view or perform actions based on the changes in the game state.
 * It acts as a listener for updates from the game model and provides methods to handle various aspects
 * of the game's state and events.
 */
public interface ClientModelObserver {
    /**
     * Updates the entire game state with the provided client model.
     *
     * @param model the current state of the client model
     */
    void updateGame(ClientModel model);

    /**
     * Updates the view/state to reflect that the hourglass has stopped.
     */
    void updateStopHourglass();

    /**
     * Starts the hourglass animation or timer at the specified spot.
     *
     * @param hourglassSpot the position/index of the hourglass to start
     */
    void updateStartHourglass(int hourglassSpot);

    /**
     * Updates the current phase of the game.
     *
     * @param gamePhase the current phase of the game
     */
    void updateGamePhase(GamePhase gamePhase);

    /**
     * Updates the bank state.
     *
     * @param bank the current state of the bank
     */
    void updateBank(Bank bank);

    /**
     * Updates the list of covered component tiles.
     *
     * @param coveredComponentTiles list of component tiles that are still covered
     */
    void updateCoveredComponentTiles(List<ComponentTile> coveredComponentTiles);

    /**
     * Updates the list of uncovered component tiles.
     *
     * @param uncoveredComponentTiles list of component tiles that are uncovered and available
     */
    void updateUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles);

    /**
     * Updates all players' shipboards.
     *
     * @param shipboards map associating player nicknames to their shipboards
     */
    void updateShipboards(Map<String, Shipboard> shipboards);

    /**
     * Updates the flightboard state.
     *
     * @param flightboard the current flightboard
     */
    void updateFlightboard(Flightboard flightboard);

    /**
     * Updates a single shipboard's state.
     *
     * @param shipboard the shipboard to update
     */
    void updateShipboard(Shipboard shipboard);

    /**
     * Updates the deck of adventure cards.
     *
     * @param deck the current list of adventure cards in the deck
     */
    void updateDeck(List<AdventureCard> deck);

    /**
     * Updates the current player in the game.
     *
     * @param player the nickname of the current player
     */
    void updateCurrPlayer(String player);

    /**
     * Updates the current adventure card in play.
     *
     * @param currCard the current adventure card
     */
    void updateCurrCard(AdventureCard currCard);

    /**
     * Updates the dice state.
     *
     * @param dices the current dices state
     */
    void updateDices(Dices dices);

    /**
     * Updates whether the game start message has been received.
     *
     * @param gameStartMessageReceived true if the message has been received, false otherwise
     */
    void updateGameStartMessageReceived(boolean gameStartMessageReceived);

    /**
     * Updates the component tile currently held in hand by a specific player.
     *
     * @param player the nickname of the player holding the tile
     * @param ct the component tile in the player's hand
     */
    void updateTileInHand(String player, ComponentTile ct);

    /**
     * Updates the entire leaderboard.
     *
     * @param leaderboard map associating player nicknames to their scores or rankings
     */
    void updateAllLeaderboard(Map<String, Integer> leaderboard);

    /**
     * Updates the game state when all game data has been loaded.
     *
     * @param clientModel the client model containing the full loaded game state
     */
    void updateAllGameLoaded(ClientModel clientModel);

}
