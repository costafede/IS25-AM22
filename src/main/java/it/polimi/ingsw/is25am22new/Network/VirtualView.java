package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

public interface VirtualView {

    void showUpdateBank(Bank bank) throws Exception;
    void showUpdateTileInHand(ComponentTile tile) throws Exception;
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws Exception;
    void showUpdateShipboard(Shipboard shipboard) throws Exception;
    void showUpdateFlightboard(Flightboard flightboard) throws Exception;
    void showUpdateCurrCard(AdventureCard adventureCard) throws Exception;
    void showUpdateDices(Dices dices) throws Exception;
    void showUpdateCurrPlayer(String currPlayer) throws Exception;
}
