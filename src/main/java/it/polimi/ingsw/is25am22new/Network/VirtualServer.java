package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    void connect(VirtualView client, String nickname) throws RemoteException;
    void disconnect() throws RemoteException;
    void setNumPlayers(int numPlayers) throws IOException;
    void removePlayer(String nickname) throws IOException;
    void setPlayerReady(String nickname) throws IOException;
    void startGameByHost(String nickname) throws IOException;
    void setPlayerNotReady(String nickname) throws IOException;
    void setGameType(String gameType) throws IOException;

    void godMode(String nickname, String conf) throws IOException;
    void pickCoveredTile(String nickname) throws IOException;
    void pickUncoveredTile(String nickname, String tilePngName) throws IOException;
    void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException;
    void standbyComponentTile(String nickname) throws IOException;
    void pickStandbyComponentTile(String nickname, int index) throws IOException;
    void discardComponentTile(String nickname) throws IOException;
    void finishBuilding(String nickname) throws IOException;
    void finishBuilding(String nickname, int index) throws IOException;
    void finishedAllShipboards() throws IOException;
    void flipHourglass() throws IOException;
    void pickCard() throws IOException;
    void activateCard(InputCommand inputCommand) throws IOException;
    void playerAbandons(String nickname) throws IOException;
    void destroyComponentTile(String nickname, int i, int j) throws IOException;
    void endGame() throws IOException;

    void placeBrownAlien(String playerName, int i, int j) throws IOException;
    void placeAstronauts(String playerName, int i, int j) throws IOException;
    void placePurpleAlien(String playerName, int i, int j) throws IOException;

    void heartbeat(String playerName) throws IOException;
    void quit(String playerName) throws IOException;

    String getVirtualServerType() throws RemoteException;
}
