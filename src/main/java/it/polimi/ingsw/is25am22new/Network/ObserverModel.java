package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.IOException;
import java.rmi.RemoteException;

public interface ObserverModel {
    void updateBank(Bank bank) throws RemoteException, IOException;
    void updateTileInHand(String player, ComponentTile ct) throws RemoteException, IOException;
    void updateUncoveredComponentTiles(ComponentTile ct) throws RemoteException, IOException;
    void updateShipboard(String player, Shipboard shipboard) throws RemoteException, IOException;
    void updateFlightboard(Flightboard flightboard) throws RemoteException, IOException;
    void updateCurrCard(AdventureCard adventureCard) throws RemoteException, IOException;
    void updateDices(Dices dices) throws RemoteException, IOException;
    void updateCurrPlayer(String currPlayer) throws RemoteException, IOException;
}
