package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class VirtualViewClient extends UnicastRemoteObject implements VirtualView {

    public VirtualViewClient() throws RemoteException {
        super();
    }

    @Override
    public void showUpdateBank(Bank bank) throws IOException {

    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws IOException {

    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) throws IOException {

    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) throws IOException {

    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) throws IOException {

    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws IOException {

    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws IOException {

    }

    @Override
    public void showUpdateDices(Dices dices) throws IOException {

    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws IOException {

    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) throws IOException {

    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck) throws IOException {

    }

    @Override
    public void showUpdateGame(Game game) throws IOException {

    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot) throws IOException {

    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException {

    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException {

    }

    @Override
    public void showNicknameResult(boolean valid, String message) throws RemoteException {

    }

    @Override
    public void showGameStarted() throws RemoteException {

    }

    @Override
    public void showPlayerJoined(String player) throws RemoteException {

    }
}
