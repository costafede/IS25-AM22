package it.polimi.ingsw.is25am22new.Client.View.TUI;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

/**
 * Interface representing the operations that a user interface must implement to display
 * the various components of the game. It provides methods for visualizing the game state
 * elements and interacting with the client model.
 */
public interface ViewAdapter {
    /**
     * Shows the card pile at the specified index.
     *
     * @param idx the index of the card pile to display
     * @param clientModel the current client model state
     */
    void showCardPile(int idx, ClientModel clientModel);

    /**
     * Shows the shipboard grid for the specified player.
     *
     * @param player the nickname of the player whose shipboard is shown
     * @param clientModel the current client model state
     */
    void showShipboardGrid(String player, ClientModel clientModel);

    /**
     * Shows the standby components on the shipboard for the specified player.
     *
     * @param player the nickname of the player
     * @param clientModel the current client model state
     */
    void showShipboardStandByComponents(String player, ClientModel clientModel);

    /**
     * Shows the flightboard view.
     *
     * @param clientModel the current client model state
     */
    void showFlightboard(ClientModel clientModel);

    /**
     * Shows the specified adventure card.
     *
     * @param card the adventure card to display
     * @param clientModel the current client model state
     */
    void showCard(AdventureCard card, ClientModel clientModel);

    /**
     * Shows the uncovered component tiles.
     *
     * @param clientModel the current client model state
     */
    void showUncoveredComponentTiles(ClientModel clientModel);

    /**
     * Shows the leaderboard with player rankings/scores.
     *
     * @param clientModel the current client model state
     */
    void showLeaderboard(ClientModel clientModel);

    /**
     * Shows the list of commands currently available to the player.
     *
     * @param clientModel the current client model state
     */
    void showAvailableCommands(ClientModel clientModel);

    /**
     * Shows the component tile currently held in hand by the specified player.
     *
     * @param player the nickname of the player
     * @param clientModel the current client model state
     */
    void showTileInHand(String player, ClientModel clientModel);

    /**
     * Shows the remaining seconds of a timed phase or action.
     *
     * @param model the current client model state
     */
    void showRemainingSeconds(ClientModel model);

    /**
     * Shows the current phase of the game.
     *
     * @param model the current client model state
     */
    void showCurrPhase(ClientModel model);

    /**
     * Shows the current state of the bank.
     *
     * @param model the current client model state
     */
    void showBank(ClientModel model);

    /**
     * Quits the game or client application.
     */
    void quit();

    /**
     * Handles a player abandoning the game.
     *
     * @param player the nickname of the player who abandons the game
     */
    void abandonGame(String player);

}
