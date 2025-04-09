package it.polimi.ingsw.is25am22new.Network.Socket.Server;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class SocketClientHandler implements VirtualView {
    final GameController controller;
    final SocketServerSide server;
    final ObjectInputStream objectInput;
    final ObjectOutputStream objectOutput;

    // CI SONO VARIE ISTANZE DI SOCKETCLIENTHANDLER, UNA PER OGNI GIOCATORE
    public SocketClientHandler(GameController controller, SocketServerSide server, InputStream is, OutputStream os) throws IOException {
        this.controller = controller;
        this.server = server;
        this.objectOutput = new ObjectOutputStream(os);
        objectOutput.flush();
        this.objectInput = new ObjectInputStream(is);
    }

    //comunicazione dal client al server
    public void runVirtualView() throws IOException, ClassNotFoundException {
        SocketMessage msg = null;
        while ((msg = (SocketMessage) objectInput.readObject()) != null){
            switch (msg.getCommand()) {
                case "checkAvailability" -> {
                    int res = this.controller.addPlayer(msg.getPayload());
                    if(res == 1) {
                        server.addHandlerToClients(this);
                    }
                    handleAvailabilityMessages(res);
                    System.out.println(this.controller.getPlayers());
                }
                case "removePlayer" -> {
                    this.controller.removePlayer(msg.getPayload());
                }
                case "setPlayerReady" -> {
                    this.controller.setPlayerReady(msg.getPayload());
                }
                case "startGameByHost" -> {
                    this.controller.startGameByHost(msg.getPayload());
                }
                case "setPlayerNotReady" -> {
                    this.controller.setPlayerNotReady(msg.getPayload());
                }
                case "setGameType" -> {
                    this.controller.setGameType(msg.getPayload());
                }
                case "pickCoveredTile" -> {
                    this.controller.pickCoveredTile(msg.getPayload());
                }
                case "pickUncoveredTile" -> {
                    this.controller.pickUncoveredTile(msg.getPayload(),
                            ((InputCommand) msg.getObject()).getIndexChosen());
                }
                case "weldComponentTile" -> {
                    if(((InputCommand) msg.getObject()).getIndexChosen() >= 0){
                        this.controller.rotateClockwise(msg.getPayload(),
                                ((InputCommand) msg.getObject()).getIndexChosen());
                    }
                    else {
                        this.controller.rotateCounterClockwise(msg.getPayload(),
                                -((InputCommand) msg.getObject()).getIndexChosen());
                    }
                    this.controller.weldComponentTile(msg.getPayload(),
                            ((InputCommand) msg.getObject()).getRow(),
                            ((InputCommand) msg.getObject()).getCol());
                }
                case "standbyComponentTile" -> {
                    this.controller.standbyComponentTile(msg.getPayload());
                }
                case "pickStandByComponentTile" -> {
                    this.controller.pickStandByComponentTile(msg.getPayload(),
                            ((InputCommand) msg.getObject()).getIndexChosen());
                }
                case "discardComponentTile" -> {
                    this.controller.discardComponentTile(msg.getPayload());
                }
                case "finishBuilding1" -> {
                    this.controller.finishBuilding(msg.getPayload());
                }
                case "finishBuilding2" -> {
                    this.controller.finishBuilding(msg.getPayload(),
                            ((InputCommand) msg.getObject()).getIndexChosen());
                }
                case "finishedAllShipboards" -> {
                    this.controller.finishedAllShipboards();
                }
                case "flipHourglass" -> {
                    this.controller.flipHourglass();
                }
                case "pickCard" -> {
                    this.controller.pickCard();
                }
                case "activateCard" -> {
                    this.controller.activateCard((InputCommand) msg.getObject());
                }
                case "playerAbandons" -> {
                    this.controller.playerAbandons(msg.getPayload());
                }
                case "destroyTile" -> {
                    this.controller.destroyTile(msg.getPayload(),
                            ((InputCommand) msg.getObject()).getRow(),
                            ((InputCommand) msg.getObject()).getCol());
                }
                case "endGame" -> {
                    this.controller.endGame();
                }
                case "connectionTester" -> {
                    System.out.println(msg.getPayload());
                    System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
                    showUpdateTest();
                }
            }
        }
    }

    public void showUpdateTest() {
        InputCommand cmd = new InputCommand();
        cmd.setIndexChosen(9033);
        SocketMessage message = new SocketMessage("updateTest", cmd, "Update message received");
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error test: " + e.getMessage());
        }
    }

    public void handleAvailabilityMessages(int res) {
        switch (res) {
            case -1 -> {
                SocketMessage message = new SocketMessage("LobbyFullOrOutsideLobbyState", null, null);
                try {
                    objectOutput.writeObject(message);
                    objectOutput.flush();
                } catch (IOException e) {
                    System.out.println("Error handling availability error: " + e.getMessage());
                }
            }
            case -2 -> {
                SocketMessage message = new SocketMessage("PlayerAlreadyInLobby", null, null);
                try {
                    objectOutput.writeObject(message);
                    objectOutput.flush();
                } catch (IOException e) {
                    System.out.println("Error handling availability error: " + e.getMessage());
                }
            }
            case 1 -> {
                SocketMessage message = new SocketMessage("PlayerAdded", null, null);
                try {
                    objectOutput.writeObject(message);
                    objectOutput.flush();
                } catch (IOException e) {
                    System.out.println("Error handling availability error: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void showUpdateBank(Bank bank){
        SocketMessage message = new SocketMessage("Bank", bank, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating bank for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) {
        SocketMessage message = new SocketMessage("TileInHand", tile, player);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating tile in hand for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList)  {
        SocketMessage message = new SocketMessage("UncoveredComponentTile", ctList, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating uncovered tile for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList)  {
        SocketMessage message = new SocketMessage("CoveredComponentTile", ctList, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating covered tile for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) {
        SocketMessage message = new SocketMessage("Shipboard", shipboard, player);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating shipboard for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard){
        SocketMessage message = new SocketMessage("Flightboard", flightboard, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating flightboard for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) {
        SocketMessage message = new SocketMessage("CurrCard", adventureCard, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating current card for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateDices(Dices dices)  {
        SocketMessage message = new SocketMessage("Dices", dices, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating dices for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer){
        SocketMessage message = new SocketMessage("CurrPlayer", null, currPlayer);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating current player for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) {
        SocketMessage message = new SocketMessage("GamePhase", gamePhase, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game phase for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck){
        SocketMessage message = new SocketMessage("Deck", deck, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating deck for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateGame(Game game)  {
        System.out.println("showUpdateGame called");
        SocketMessage message = new SocketMessage("Game", game, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot){
        SocketMessage message = new SocketMessage("Hourglass", hourglassSpot, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating hourglass for client: " + e.getMessage());
        }
    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException {
        SocketMessage message1 = new SocketMessage("LobbyUpdate", players, gameType);
        try {
            objectOutput.writeObject(message1);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating players for client: " + e.getMessage());
        }
        SocketMessage message2 = new SocketMessage("ReadyStatus", readyStatus, null);
        try {
            objectOutput.writeObject(message2);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating ready status map for client: " + e.getMessage());
        }
    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException {
        SocketMessage message1 = new SocketMessage("ConnectionResult", isHost, message);
        try {
            objectOutput.writeObject(message1);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating connection result for client: " + e.getMessage());
        }
        SocketMessage message2 = new SocketMessage("Success", success, null);
        try {
            objectOutput.writeObject(message2);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating success for client: " + e.getMessage());
        }
    }

    @Override
    public void showNicknameResult(boolean valid, String message) throws RemoteException {
        SocketMessage message1 = new SocketMessage("NicknameResult", valid, message);
        try {
            objectOutput.writeObject(message1);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating nickname result for client: " + e.getMessage());
        }
    }

    @Override
    public void showGameStarted() throws RemoteException {
        SocketMessage message = new SocketMessage("GameStarted", null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game started for client: " + e.getMessage());
        }
    }

    @Override
    public void showPlayerJoined(String player) throws RemoteException {
        SocketMessage message = new SocketMessage("PlayerJoined", null, player);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating player joined for client: " + e.getMessage());
        }
    }
}
