package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualViewRMI extends Remote, VirtualView {
    void showUpdateBank(Bank bank) throws RemoteException;
    void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException;
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws RemoteException;
    void showUpdateFlightboard(Flightboard flightboard) throws RemoteException;
    void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException;
    void showUpdateDices(Dices dices) throws RemoteException;
    void showUpdateCurrPlayer(String currPlayer) throws RemoteException;
}
