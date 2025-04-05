package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements VirtualView {

    final VirtualServer server;

    protected RmiClient(VirtualServer server) throws RemoteException {
        super();
        this.server = server;
    }

    @Override
    public void showUpdateBank(Bank bank) throws Exception {

    }

    @Override
    public void showUpdateTileInHand(ComponentTile tile) throws Exception {

    }

    @Override
    public void showUpdateUncoveredComponentTiles(ComponentTile tile) throws Exception {

    }

    @Override
    public void showUpdateShipboard(Shipboard shipboard) throws Exception {

    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws Exception {

    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws Exception {

    }

    @Override
    public void showUpdateDices(Dices dices) throws Exception {

    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws Exception {

    }
}
