//package it.polimi.ingsw.is25am22new.Network.Socket.Client;
//
//import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
//import it.polimi.ingsw.is25am22new.Network.VirtualServer;
//
//import java.io.IOException;
//
//public interface VirtualServerSocket extends VirtualServer {
//    @Override
//    void addPlayer(String nickname) throws IOException;
//    @Override
//    void removePlayer(String nickname) throws IOException;
//    @Override
//    void setPlayerReady(String nickname) throws IOException;
//    @Override
//    void startGameByHost(String nickname) throws IOException;
//    @Override
//    void setPlayerNotReady(String nickname) throws IOException;
//    @Override
//    void setGameType(String gameType) throws IOException;
//
//    @Override
//    void pickCoveredTile(String nickname) throws IOException;
//    @Override
//    void pickUncoveredTile(String nickname, int index) throws IOException;
//    @Override
//    void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException;
//    @Override
//    void standbyComponentTile(String nickname) throws IOException;
//    @Override
//    void pickStandbyComponentTile(String nickname, int index) throws IOException;
//    @Override
//    void discardComponentTile(String nickname) throws IOException;
//    @Override
//    void finishBuilding(String nickname) throws IOException;
//    @Override
//    void finishBuilding(String nickname, int index) throws IOException;
//    @Override
//    void finishedAllShipboards() throws IOException;
//    @Override
//    void flipHourglass() throws IOException;
//    @Override
//    void pickCard() throws IOException;
//    @Override
//    void activateCard(InputCommand inputCommand) throws IOException;
//    @Override
//    void playerAbandons(String nickname) throws IOException;
//    @Override
//    void destroyComponentTile(String nickname, int i, int j) throws IOException;
//    @Override
//    void endGame() throws IOException;
//}
