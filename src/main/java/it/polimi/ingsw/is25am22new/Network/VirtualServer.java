package it.polimi.ingsw.is25am22new.Network;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    void connect(VirtualView client) throws RemoteException;

    void addPlayer(String nickname) throws RemoteException, IOException;
    void removePlayer(String nickname) throws RemoteException, IOException;
    void setPlayerReady(String nickname) throws RemoteException, IOException;
    void startGameByHost(String nickname) throws RemoteException, IOException;
    void setPlayerNotReady(String nickname) throws RemoteException, IOException;
    void setGameType(String gameType) throws RemoteException, IOException;

    void pickCoveredTile(String nickname) throws RemoteException, IOException;
    void pickUncoveredTile(String nickname, int index) throws RemoteException, IOException;
    void rotateClockwise(String nickname, int rotationNum) throws RemoteException, IOException;
    void rotateCounterClockwise(String nickname, int rotationNum) throws RemoteException, IOException;
    void weldComponentTile(String nickname, int i, int j) throws RemoteException, IOException;
    void standbyComponentTile(String nickname) throws RemoteException, IOException;
    void pickStandbyComponentTile(String nickname, int index) throws RemoteException, IOException;
    void discardComponentTile(String nickname) throws RemoteException, IOException;
    void finishBuilding(String nickname) throws RemoteException, IOException;
    void finishBuilding(String nickname, int index) throws RemoteException, IOException;
    void finishedAllShipboards() throws RemoteException, IOException;
    void flipHourglass() throws RemoteException, IOException;
    void pickCard() throws RemoteException, IOException;
    void activateCard(InputCommand inputCommand) throws RemoteException, IOException;
    void playerAbandons(String nickname) throws RemoteException, IOException;
    void destroyComponentTile(String nickname, int i, int j) throws RemoteException, IOException;
    void setCurrPlayer(String currPlayer) throws RemoteException, IOException;
    void setCurrPlayerToLeader() throws RemoteException, IOException;
    void endGame() throws RemoteException, IOException;
}
