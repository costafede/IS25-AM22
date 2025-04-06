package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

public class SocketServerHandler implements VirtualServerSocket{
    final ObjectOutputStream objectOutput;

    public SocketServerHandler(OutputStream os) throws IOException {
        this.objectOutput = new ObjectOutputStream(os);
    }

    @Override
    public void addPlayer(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("addPlayer", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void removePlayer(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("removePlayer", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void setPlayerReady(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("setPlayerReady", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void startGameByHost(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("startGameByHost", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void setPlayerNotReady(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("setPlayerNotReady", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void setGameType(String gameType) throws IOException {
        SocketMessage msg = new SocketMessage("setGameType", null, gameType);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void pickCoveredTile(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("pickCoveredTile", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void pickUncoveredTile(String nickname, int index) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("pickUncoveredTile", inputCommand, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        inputCommand.setIndexChosen(numOfRotations);
        SocketMessage msg = new SocketMessage("weldComponentTile", inputCommand, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void standbyComponentTile(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("standbyComponentTile", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void pickStandbyComponentTile(String nickname, int index) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("pickStandByComponentTile", inputCommand, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void discardComponentTile(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("discardComponentTile", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void finishBuilding(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("finishBuilding1", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void finishBuilding(String nickname, int index) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("finishBuilding2", inputCommand, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void finishedAllShipboards() throws IOException {
        SocketMessage msg = new SocketMessage("finishedAllShipboards", null, null);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void flipHourglass() throws IOException {
        SocketMessage msg = new SocketMessage("flipHourglass", null, null);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void pickCard() throws IOException {
        SocketMessage msg = new SocketMessage("pickCard", null, null);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void activateCard(InputCommand inputCommand) throws IOException {
        SocketMessage msg = new SocketMessage("activateCard", inputCommand, null);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void playerAbandons(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("playerAbandons", null, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void destroyComponentTile(String nickname, int i, int j) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("destroyTile", inputCommand, nickname);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void endGame() throws IOException {
        SocketMessage msg = new SocketMessage("endGame", null, null);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    public void connectionTester(String a, int b) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(b);
        SocketMessage msg = new SocketMessage("connectionTester", inputCommand, a);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        // used only by RMI
    }
}
