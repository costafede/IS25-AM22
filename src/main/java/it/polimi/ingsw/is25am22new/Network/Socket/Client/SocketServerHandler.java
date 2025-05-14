package it.polimi.ingsw.is25am22new.Network.Socket.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SocketServerHandler implements VirtualServer {
    final ObjectOutputStream objectOutput;
    public SocketServerHandler(OutputStream os) throws IOException {
        this.objectOutput = new ObjectOutputStream(os);
    }

    public void checkAvailability(String nickname) {
        SocketMessage msg = new SocketMessage("checkAvailability", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in checkAvailability: " + e.getMessage());
        }
    }

    @Override
    public void removePlayer(String nickname) {
        SocketMessage msg = new SocketMessage("removePlayer", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in removePlayer: " + e.getMessage());
        }
    }

    @Override
    public void setPlayerReady(String nickname)  {
        SocketMessage msg = new SocketMessage("setPlayerReady", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setPlayerReady: " + e.getMessage());
        }
    }

    @Override
    public void startGameByHost(String nickname){
        SocketMessage msg = new SocketMessage("startGameByHost", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in startGameByHost: " + e.getMessage());
        }
    }

    @Override
    public void setPlayerNotReady(String nickname) {
        SocketMessage msg = new SocketMessage("setPlayerNotReady", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setPlayerNotReady: " + e.getMessage());
        }
    }

    @Override
    public void setGameType(String gameType) {
        SocketMessage msg = new SocketMessage("setGameType", null, gameType);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setGameType: " + e.getMessage());
        }
    }

    @Override
    public void godMode(String nickname, String conf) {
        SocketMessage msg = new SocketMessage("godMode", conf, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending godMode: " + e.getMessage());
        }
    }

    @Override
    public void pickCoveredTile(String nickname)  {
        SocketMessage msg = new SocketMessage("pickCoveredTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickCoveredTile: " + e.getMessage());
        }
    }

    @Override
    public void pickUncoveredTile(String nickname, String pngName) {
        SocketMessage msg = new SocketMessage("pickUncoveredTile", pngName, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickUncoveredTile: " + e.getMessage());
        }
    }

    @Override
    public void weldComponentTile(String nickname, int i, int j, int numOfRotations)  {
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
    public void standbyComponentTile(String nickname) {
        SocketMessage msg = new SocketMessage("standbyComponentTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in standbyComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void pickStandbyComponentTile(String nickname, int index)  {
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
    public void discardComponentTile(String nickname) {
        SocketMessage msg = new SocketMessage("discardComponentTile", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in discardComponentTile: " + e.getMessage());
        }
    }

    @Override
    public void finishBuilding(String nickname) {
        SocketMessage msg = new SocketMessage("finishBuilding1", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishBuilding: " + e.getMessage());
        }
    }

    @Override
    public void finishBuilding(String nickname, int index) {
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
    public void finishedAllShipboards() {
        SocketMessage msg = new SocketMessage("finishedAllShipboards", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in finishedAllShipboards: " + e.getMessage());
        }
    }

    @Override
    public void flipHourglass()  {
        SocketMessage msg = new SocketMessage("flipHourglass", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in flipHourglass: " + e.getMessage());
        }
    }

    @Override
    public void pickCard()  {
        SocketMessage msg = new SocketMessage("pickCard", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in pickCard: " + e.getMessage());
        }
    }

    @Override
    public void activateCard(InputCommand inputCommand)  {
        SocketMessage msg = new SocketMessage("activateCard", inputCommand, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in activateCard: " + e.getMessage());
        }
    }

    @Override
    public void playerAbandons(String nickname) {
        SocketMessage msg = new SocketMessage("playerAbandons", null, nickname);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in playerAbandons: " + e.getMessage());
        }
    }

    @Override
    public void destroyComponentTile(String nickname, int i, int j)  {
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
    public void endGame() {
        SocketMessage msg = new SocketMessage("endGame", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in endGame: " + e.getMessage());
        }
    }

    @Override
    public void placeBrownAlien(String playerName, int i, int j) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("placeBrownAlien", inputCommand, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending placeBrownAlien: " + e.getMessage());
        }
    }

    @Override
    public void placeAstronauts(String playerName, int i, int j) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("placeAstronauts", inputCommand, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending placeAstronauts: " + e.getMessage());
        }
    }

    @Override
    public void placePurpleAlien(String playerName, int i, int j){
        InputCommand inputCommand = new InputCommand();
        inputCommand.setRow(i);
        inputCommand.setCol(j);
        SocketMessage msg = new SocketMessage("placePurpleAlien", inputCommand, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending placePurpleAlien: " + e.getMessage());
        }
    }

    @Override
    public void heartbeat(String playerName) {
        SocketMessage msg = new SocketMessage("heartbeat", null, playerName);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending heartbeat: " + e.getMessage());
        }
    }

    @Override
    public void connect(VirtualView client, String nickname){
        //Used only for RMI
    }

    @Override
    public void disconnect() {
        SocketMessage msg = new SocketMessage("disconnect", null, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in sending disconnect: " + e.getMessage());
        }
    }

    @Override
    public void setNumPlayers(int numPlayers) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(numPlayers);
        SocketMessage msg = new SocketMessage("setNumPlayers", inputCommand, null);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in setNumPlayers: " + e.getMessage());
        }
    }

    public void connectionTester(String a, int b) {
        InputCommand inputCommand = new InputCommand();
        inputCommand.setIndexChosen(b);
        SocketMessage msg = new SocketMessage("connectionTester", inputCommand, a);
        try {
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error in connectionTester: " + e.getMessage());
        }
    }

    @Override
    public void quit(String nickname) {
        try {
            SocketMessage msg = new SocketMessage("quit", null, nickname);
            objectOutput.writeObject(msg);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Connection closed");
        }
    }
}
