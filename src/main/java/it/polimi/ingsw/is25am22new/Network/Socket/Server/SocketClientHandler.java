package it.polimi.ingsw.is25am22new.Network.Socket.Server;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
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
        this.objectInput = new ObjectInputStream(is);
    }

    //comunicazione dal client al server
    public void runVirtualView() throws IOException, ClassNotFoundException {
        SocketMessage msg = null;
        while ((msg = (SocketMessage) objectInput.readObject()) != null){
            switch (msg.getCommand()) {
                case "addPlayer" -> {
                    this.controller.addPlayer(msg.getPayload());
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

    public void showUpdateTest() throws IOException {
        InputCommand cmd = new InputCommand();
        cmd.setIndexChosen(9033);
        SocketMessage msg = new SocketMessage("updateTest", cmd, "Update message received");
        objectOutput.writeObject(msg);
        objectOutput.flush();
    }

    @Override
    public void showUpdateBank(Bank bank) throws IOException {
        SocketMessage message = new SocketMessage("Bank", bank, null);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile) throws IOException {
        SocketMessage message = new SocketMessage("TileInHand", tile, player);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateUncoveredComponentTiles(ComponentTile tile) throws IOException {
        SocketMessage message = new SocketMessage("UncoveredComponentTile", tile, null);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard) throws IOException {
        SocketMessage message = new SocketMessage("Shipboard", shipboard, player);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard) throws IOException {
        SocketMessage message = new SocketMessage("Flightboard", flightboard, null);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard) throws IOException {
        SocketMessage message = new SocketMessage("CurrCard", adventureCard, null);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateDices(Dices dices) throws IOException {
        SocketMessage message = new SocketMessage("Dices", dices, null);
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer) throws IOException {
        SocketMessage message = new SocketMessage("CurrPlayer", null, currPlayer);
        objectOutput.writeObject(message);
        objectOutput.flush();
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
