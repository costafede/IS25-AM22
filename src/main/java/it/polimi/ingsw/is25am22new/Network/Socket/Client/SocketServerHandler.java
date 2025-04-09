package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

public class SocketServerHandler implements VirtualServer {
    final ObjectOutputStream objectOutput;

    public SocketServerHandler(OutputStream os) throws IOException {
        this.objectOutput = new ObjectOutputStream(os);
    }

    @Override
    public void addPlayer(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("addPlayer", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in addPlayer: " + e.getMessage());
        }
    }

    @Override
    public void removePlayer(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("removePlayer", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in removePlayer: " + e.getMessage());
        }
    }

    @Override
    public void setPlayerReady(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("setPlayerReady", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setPlayerReady: " + e.getMessage());
        }
    }

    @Override
    public void startGameByHost(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("startGameByHost", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in startGameByHost: " + e.getMessage());
        }
    }

    @Override
    public void setPlayerNotReady(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("setPlayerNotReady", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setPlayerNotReady: " + e.getMessage());
        }
    }

    @Override
    public void setGameType(String gameType) throws IOException {
        SocketMessage msg = new SocketMessage("setGameType", null, gameType);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setGameType: " + e.getMessage());
        }
    }

    @Override
    public void pickCoveredTile(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("pickCoveredTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickCoveredTile: " + e.getMessage());
        }
    }

    @Override
    public void pickUncoveredTile(String nickname, int index) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("pickUncoveredTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickUncoveredTile: " + e.getMessage());
        }
    }

    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        inputCommand.setIndexChosen(numOfRotations);
        SocketMessage msg = new SocketMessage("weldComponentTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in weldComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void standbyComponentTile(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("standbyComponentTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in standbyComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void pickStandbyComponentTile(String nickname, int index) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("pickStandByComponentTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickStandbyComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void discardComponentTile(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("discardComponentTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in discardComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void finishBuilding(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("finishBuilding1", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishBuilding: " + e.getMessage());
        }
    }

    @Override
    public void finishBuilding(String nickname, int index) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(index);
        SocketMessage msg = new SocketMessage("finishBuilding2", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishBuilding: " + e.getMessage());
        }
    }

    @Override
    public void finishedAllShipboards() throws IOException {
        SocketMessage msg = new SocketMessage("finishedAllShipboards", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishedAllShipboards: " + e.getMessage());
        }
    }

    @Override
    public void flipHourglass() throws IOException {
        SocketMessage msg = new SocketMessage("flipHourglass", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in flipHourglass: " + e.getMessage());
        }
    }

    @Override
    public void pickCard() throws IOException {
        SocketMessage msg = new SocketMessage("pickCard", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickCard: " + e.getMessage());
        }
    }

    @Override
    public void activateCard(InputCommand inputCommand) throws IOException {
        SocketMessage msg = new SocketMessage("activateCard", inputCommand, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in activateCard: " + e.getMessage());
        }
    }

    @Override
    public void playerAbandons(String nickname) throws IOException {
        SocketMessage msg = new SocketMessage("playerAbandons", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in playerAbandons: " + e.getMessage());
        }
    }

    @Override
    public void destroyComponentTile(String nickname, int i, int j) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("destroyTile", inputCommand, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in destroyComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void endGame() throws IOException {
        SocketMessage msg = new SocketMessage("endGame", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in endGame: " + e.getMessage());
        }
    }

    @Override
    public void connect(VirtualView client, String nickname) throws RemoteException {
        //Used only for RMI
    }

    public void connectionTester(String a, int b) throws IOException {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(b);
        SocketMessage msg = new SocketMessage("connectionTester", inputCommand, a);
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }
}
