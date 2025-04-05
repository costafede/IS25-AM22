package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {

    void showUpdateBank(Bank bank) throws RemoteException;
    void showUpdateTileInHand(ComponentTile tile) throws RemoteException;
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws RemoteException;
    void showUpdateShipboard(Shipboard shipboard) throws RemoteException;
    void showUpdateFlightboard(Flightboard flightboard) throws RemoteException;
    void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException;
    void showUpdateDices(Dices dices) throws RemoteException;
    void showUpdateCurrPlayer(String currPlayer) throws RemoteException;
}
