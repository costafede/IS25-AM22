package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public interface ClientView {
    void displayBank(Bank bank);
    void displayTileInHand(String player, ComponentTile tile);
    void displayUncoveredComponentTiles(ComponentTile tile);
    void displayShipboard(Shipboard shipboard);
    void displayFlightboard(Flightboard flightboard);
    void displayCurrentCard(AdventureCard card);
    void displayDices(Dices dices);
    void displayCurrentPlayer(String currPlayer);

}
