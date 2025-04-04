package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.rmi.RemoteException;

public interface ObserverModel {
    void updateBank(Bank bank) throws RemoteException;
    void updateTileInHand(ComponentTile ct) throws RemoteException;
    void updateUncoveredComponentTiles(ComponentTile ct) throws RemoteException;
    void updateShipboard(Shipboard shipboard) throws RemoteException;
    void updateFlightboard(Flightboard flightboard) throws RemoteException;
    void updateCurrCard(AdventureCard adventureCard) throws RemoteException;
    void updateDices(Dices dices) throws RemoteException;
    void updateCurrPlayer(String currPlayer) throws RemoteException;
}
