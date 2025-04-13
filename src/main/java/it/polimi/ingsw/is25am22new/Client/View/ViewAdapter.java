package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

public interface ViewAdapter {
    void showCardPile(int idx, ClientModel clientModel);
    void showShipboardGrid(String player, ClientModel clientModel);
    void showShipboardStandByComponents(String player, ClientModel clientModel);
    void showFlightboard(ClientModel clientModel);
    void showCard(AdventureCard card, ClientModel clientModel);
    void showUncoveredComponentTiles(ClientModel clientModel);
}
