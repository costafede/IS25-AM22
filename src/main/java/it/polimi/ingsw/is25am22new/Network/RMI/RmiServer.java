package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.rmi.RemoteException;

public class RmiServer implements ObserverModel, VirtualServer {

    @Override
    public void updateBank(Bank bank) throws RemoteException {

    }

    @Override
    public void updateTileInHand(ComponentTile ct) throws RemoteException {

    }

    @Override
    public void updateUncoveredComponentTiles(ComponentTile ct) throws RemoteException {

    }

    @Override
    public void updateShipboard(Shipboard shipboard) throws RemoteException {

    }

    @Override
    public void updateFlightboard(Flightboard flightboard) throws RemoteException {

    }

    @Override
    public void updateCurrCard(AdventureCard adventureCard) throws RemoteException {

    }

    @Override
    public void updateDices(Dices dices) throws RemoteException {

    }

    @Override
    public void updateCurrPlayer(String currPlayer) throws RemoteException {

    }

    @Override
    public void connect(VirtualView client) throws RemoteException {

    }

    @Override
    public void addPlayer() {

    }

    @Override
    public void removePlayer() {

    }

    @Override
    public void setPlayerReady() {

    }

    @Override
    public void startGameByHost() {

    }

    @Override
    public void setPlayerNotReady() {

    }

    @Override
    public void setGameType() {

    }

    @Override
    public void pickCoveredTile() {

    }

    @Override
    public void pickUncoveredTile() {

    }

    @Override
    public void rotateClockwise() {

    }

    @Override
    public void rotateCounterClockwise() {

    }

    @Override
    public void weldComponentTile() {

    }

    @Override
    public void standbyComponentTile() {

    }

    @Override
    public void pickStandbyComponentTile() {

    }

    @Override
    public void discardComponentTile() {

    }

    @Override
    public void finishBuilding() {

    }

    @Override
    public void finishedAllShipboards() {

    }

    @Override
    public void flipHourglass() {

    }

    @Override
    public void pickCard() {

    }

    @Override
    public void activateCard() {

    }

    @Override
    public void playerAbandons() {

    }

    @Override
    public void destroyComponentTile() {

    }

    @Override
    public void setCurrPlayer(String currPlayer) {

    }

    @Override
    public void setCurrPlayerToLeader() {

    }

    @Override
    public void endGame() {

    }
}
