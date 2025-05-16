package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

/**
 * Interface representing the operations that a user interface must implement to display
 * the various components of the game. It provides methods for visualizing the game state
 * elements and interacting with the client model.
 */
public interface ViewAdapter {
    void showCardPile(int idx, ClientModel clientModel);
    void showShipboardGrid(String player, ClientModel clientModel);
    void showShipboardStandByComponents(String player, ClientModel clientModel);
    void showFlightboard(ClientModel clientModel);
    void showCard(AdventureCard card, ClientModel clientModel);
    void showUncoveredComponentTiles(ClientModel clientModel);
    void showLeaderboard(ClientModel clientModel);
    void showAvailableCommands(ClientModel clientModel);
    void showTileInHand(String player, ClientModel clientModel);
    void showRemainingSeconds(ClientModel model);
    void showCurrPhase(ClientModel model);
    void showBank(ClientModel model);
    void quit();
    void abandonGame(String player);
}
