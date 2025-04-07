package it.polimi.ingsw.is25am22new.Network.RMI;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.rmi.RemoteException;

public interface VirtualServerRMI extends VirtualServer {
    void connect(VirtualView client, String nickname) throws RemoteException;

    void addPlayer(String nickname) throws RemoteException;
    void removePlayer(String nickname) throws RemoteException;
    void setPlayerReady(String nickname) throws RemoteException;
    void startGameByHost(String nickname) throws RemoteException;
    void setPlayerNotReady(String nickname) throws RemoteException;
    void setGameType(String gameType) throws RemoteException;

    void pickCoveredTile(String nickname) throws RemoteException;
    void pickUncoveredTile(String nickname, int index) throws RemoteException;
    void rotateClockwise(String nickname, int rotationNum) throws RemoteException;
    void rotateCounterClockwise(String nickname, int rotationNum) throws RemoteException;
    void weldComponentTile(String nickname, int i, int j) throws RemoteException;
    void standbyComponentTile(String nickname) throws RemoteException;
    void pickStandbyComponentTile(String nickname, int index) throws RemoteException;
    void discardComponentTile(String nickname) throws RemoteException;
    void finishBuilding(String nickname) throws RemoteException;
    void finishBuilding(String nickname, int index) throws RemoteException;
    void finishedAllShipboards() throws RemoteException;
    void flipHourglass() throws RemoteException;
    void pickCard() throws RemoteException;
    void activateCard(InputCommand inputCommand) throws RemoteException;
    void playerAbandons(String nickname) throws RemoteException;
    void destroyComponentTile(String nickname, int i, int j) throws RemoteException;
    void setCurrPlayer(String currPlayer) throws RemoteException;
    void setCurrPlayerToLeader() throws RemoteException;
    void endGame() throws RemoteException;

}
