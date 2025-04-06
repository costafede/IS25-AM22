package it.polimi.ingsw.is25am22new.Network.Socket.Server;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualViewSocket extends VirtualView {
    @Override
    void showUpdateBank(Bank bank) throws IOException;
    @Override
    void showUpdateTileInHand(String player, ComponentTile tile) throws IOException;
    @Override
    void showUpdateUncoveredComponentTiles(ComponentTile tile) throws IOException;
    @Override
    void showUpdateShipboard(String player, Shipboard shipboard) throws IOException;
    @Override
    void showUpdateFlightboard(Flightboard flightboard) throws IOException;
    @Override
    void showUpdateCurrCard(AdventureCard adventureCard) throws IOException;
    @Override
    void showUpdateDices(Dices dices) throws IOException;
    @Override
    void showUpdateCurrPlayer(String currPlayer) throws IOException;
}
