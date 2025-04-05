package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {

    void showUpdateBank(Bank bank) throws IOException, RemoteException;
    void showUpdateTileInHand(String player, ComponentTile tile) throws IOException, RemoteException;
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws IOException, RemoteException;
    void showUpdateShipboard(Shipboard shipboard) throws IOException, RemoteException;
    void showUpdateFlightboard(Flightboard flightboard) throws IOException, RemoteException;
    void showUpdateCurrCard(AdventureCard adventureCard) throws IOException, RemoteException;
    void showUpdateDices(Dices dices) throws IOException, RemoteException;
    void showUpdateCurrPlayer(String currPlayer) throws IOException, RemoteException;
}
