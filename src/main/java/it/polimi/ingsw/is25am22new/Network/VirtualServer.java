package it.polimi.ingsw.is25am22new.Network;

import java.rmi.RemoteException;

public interface VirtualServer {
    void connect(VirtualView client) throws RemoteException;

    void addPlayer();
    void removePlayer();
    void setPlayerReady();
    void startGameByHost();
    void setPlayerNotReady();
    void setGameType();

    void pickCoveredTile();
    void pickUncoveredTile();
    void rotateClockwise();
    void rotateCounterClockwise();
    void weldComponentTile();
    void standbyComponentTile();
    void pickStandbyComponentTile();
    void discardComponentTile();
    void finishBuilding();
    void finishedAllShipboards();
    void flipHourglass();
    void pickCard();
    void activateCard();
    void playerAbandons();
    void destroyComponentTile();
    void setCurrPlayer(String currPlayer);
    void setCurrPlayerToLeader();
    void endGame();
}
