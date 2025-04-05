package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.rmi.RemoteException;
import java.util.*;

public class ObservableModel {
    List<ObserverModel> listeners = new ArrayList<>();

    public void addListener(ObserverModel ld) {
        listeners.add(ld);
    }

    public void removeListener(ObserverModel ld) {
        listeners.remove(ld);
    }

    protected void updateAllBanks(Bank bank) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateBank(bank);
    }

    protected void updateAllTileInHand(ComponentTile ct) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateTileInHand(ct);
    }

    protected void updateAllUncoveredComponentTiles(ComponentTile ct) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateUncoveredComponentTiles(ct);
    }

    protected void updateAllShipboard(Shipboard shipboard) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateShipboard(shipboard);
    }

    protected void updateAllFlightboard(Flightboard flightboard) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateFlightboard(flightboard);
    }

    protected void updateAllCurrCard(AdventureCard adventureCard) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateCurrCard(adventureCard);
    }

    protected void updateAllDices(Dices dices) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateDices(dices);
    }

    protected void updateAllCurrPlayer(String currPlayer) throws RemoteException {
        for(ObserverModel ld : listeners)
            ld.updateCurrPlayer(currPlayer);
    }
}
