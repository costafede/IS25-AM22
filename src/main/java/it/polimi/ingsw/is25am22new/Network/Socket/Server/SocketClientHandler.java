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
import java.util.ArrayList;
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
        try{
            while ((msg = (SocketMessage) objectInput.readObject()) != null){
                switch (msg.getCommand()) {
                    case "checkAvailability" -> {
                        int res = this.controller.addPlayer(msg.getPayload());
                        if(res == 1) {
                            server.addHandlerToClients(this);
                            showNicknameResult(true, "PlayerAdded");

                            boolean isHost = this.controller.getPlayers().size() == 1;

                            showConnectionResult(isHost, true, isHost ? "You are the host of the lobby" : "You joined an existing lobby");

                            if(!isHost)    this.controller.updateAllPlayerJoined(msg.getPayload());
                            this.controller.updateAllLobbies();
                        }
                        else if(res == -2){
                            showNicknameResult(false, "PlayerAlreadyInLobby");
                        }
                        else if(res == -1){
                            showNicknameResult(false, "LobbyFullOrOutsideLobbyState");
                        }
                        System.out.println("List of players updated: " + this.controller.getPlayers());
                    }
                    case "removePlayer" -> {
                        this.removePlayer(msg.getPayload());
                    }
                    case "setPlayerReady" -> {
                        this.setPlayerReady(msg.getPayload());
                    }
                    case "startGameByHost" -> {
                        this.startGameByHost(msg.getPayload());
                    }
                    case "setPlayerNotReady" -> {
                        this.setPlayerNotReady(msg.getPayload());
                    }
                    case "setGameType" -> {
                        this.setGameType(msg.getPayload());
                    }
                    case "pickCoveredTile" -> {
                        this.pickCoveredTile(msg.getPayload());
                    }
                    case "pickUncoveredTile" -> {
                        this.pickUncoveredTile(msg.getPayload(), (String) msg.getObject());
                    }
                    case "weldComponentTile" -> {
                        this.weldComponentTile(msg.getPayload(),
                                ((InputCommand) msg.getObject()).getRow(),
                                ((InputCommand) msg.getObject()).getCol(),
                                ((InputCommand) msg.getObject()).getIndexChosen());
                    }
                    case "standbyComponentTile" -> {
                        this.standbyComponentTile(msg.getPayload());
                    }
                    case "pickStandByComponentTile" -> {
                        this.pickStandbyComponentTile(msg.getPayload(),
                                ((InputCommand) msg.getObject()).getIndexChosen());
                    }
                    case "discardComponentTile" -> {
                        this.discardComponentTile(msg.getPayload());
                    }
                    case "finishBuilding1" -> {
                        this.finishBuilding(msg.getPayload());
                    }
                    case "finishBuilding2" -> {
                        this.finishBuilding(msg.getPayload(),
                                ((InputCommand) msg.getObject()).getIndexChosen());
                    }
                    case "finishedAllShipboards" -> {
                        this.finishedAllShipboards();
                    }
                    case "flipHourglass" -> {
                        this.flipHourglass();
                    }
                    case "pickCard" -> {
                        this.pickCard();
                    }
                    case "activateCard" -> {
                        this.activateCard((InputCommand) msg.getObject());
                    }
                    case "playerAbandons" -> {
                        this.playerAbandons(msg.getPayload());
                    }
                    case "destroyTile" -> {
                        this.destroyComponentTile(msg.getPayload(),
                                ((InputCommand) msg.getObject()).getRow(),
                                ((InputCommand) msg.getObject()).getCol());
                    }
                    case "endGame" -> {
                        this.endGame();
                    }
                    case "setNumPlayers" -> {
                        int numPlayers = ((InputCommand) msg.getObject()).getIndexChosen();
                        this.controller.setNumPlayers(numPlayers);
                    }
                    case "disconnect" -> {
                        this.removePlayer(msg.getPayload());
                        this.server.disconnect(this, msg.getPayload());
                        this.controller.updateAllLobbies();
                    }
                    case "connectionTester" -> {
                        System.out.println(msg.getPayload());
                        System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
                        showUpdateTest();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in SocketClientHandler:");
            e.printStackTrace();
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
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType)  {
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
    public void showConnectionResult(boolean isHost, boolean success, String message)  {
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
    public void showNicknameResult(boolean valid, String payload) {
        SocketMessage message = new SocketMessage(payload, valid, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error handling availability error: " + e.getMessage());
        }
    }

    @Override
    public void showGameStarted(){
        SocketMessage message = new SocketMessage("GameStarted", null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game started for client: " + e.getMessage());
        }
    }

    @Override
    public void showPlayerJoined(String player){
        SocketMessage message = new SocketMessage("PlayerJoined", null, player);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating player joined for client: " + e.getMessage());
        }
    }

    @Override
    public void setPlayerReady(String playerName) throws IOException {
        this.controller.setPlayerReady(playerName);
        this.controller.updateAllLobbies();
    }

    @Override
    public void setPlayerNotReady(String playerName) throws IOException {
        this.controller.setPlayerNotReady(playerName);
        this.controller.updateAllLobbies();
    }

    @Override
    public void startGameByHost(String playerName) throws IOException {
        if(!playerName.equals(this.controller.getLobbyCreator())) {
            showConnectionResult(false, false, "Only the host can start the game");
        }
        else {
            Map<String, Boolean> readyStatus = this.controller.getReadyStatus();
            List<String> unreadyPlayers = new ArrayList<>();

            for (Map.Entry<String, Boolean> entry : readyStatus.entrySet()) {
                if (!entry.getValue()) {
                    unreadyPlayers.add(entry.getKey());
                }
            }

            if (!unreadyPlayers.isEmpty()) {
                // Some players are not ready
                String message = "Cannot start game: " + String.join(", ", unreadyPlayers) + " not ready";
                showConnectionResult(true, false, message);
            }
            else {
                boolean result = this.controller.startGameByHost(playerName);
                if(result) {
                    this.controller.updateAllGameStarted();
                }
                else {
                    System.out.println("Error starting game");
                }
            }
        }
    }

    @Override
    public void setGameType(String gameType) throws IOException {
        this.controller.setGameType(gameType);
        this.controller.updateAllLobbies();
    }

    @Override
    public void pickCoveredTile(String playerName) throws IOException {
        this.controller.pickCoveredTile(playerName);
    }

    @Override
    public void pickUncoveredTile(String playerName, String pngName) throws IOException {
        this.controller.pickUncoveredTile(playerName, pngName);
    }

    @Override
    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) throws IOException {
        if(numOfRotation > 0){
            this.controller.rotateClockwise(playerName, numOfRotation);
        }
        else {
            this.controller.rotateCounterClockwise(playerName, -numOfRotation);
        }
        this.controller.weldComponentTile(playerName, i, j);
    }

    @Override
    public void standbyComponentTile(String playerName) throws IOException {
        this.controller.standbyComponentTile(playerName);
    }

    @Override
    public void pickStandbyComponentTile(String playerName, int index) throws IOException {
        this.controller.pickStandByComponentTile(playerName, index);
    }

    @Override
    public void discardComponentTile(String playerName) throws IOException {
        this.controller.discardComponentTile(playerName);
    }

    @Override
    public void finishBuilding(String playerName) throws IOException {
        this.controller.finishBuilding(playerName);
    }

    @Override
    public void finishBuilding(String playerName, int index) throws IOException {
        this.controller.finishBuilding(playerName, index);
    }

    @Override
    public void finishedAllShipboards() throws IOException {
        this.controller.finishedAllShipboards();
    }

    @Override
    public void flipHourglass() throws IOException {
        this.controller.flipHourglass();
    }

    @Override
    public void pickCard() throws IOException {
        this.controller.pickCard();
    }

    @Override
    public void activateCard(InputCommand inputCommand) throws IOException {
        this.controller.activateCard(inputCommand);
    }

    @Override
    public void removePlayer(String playerName) throws IOException {
        this.controller.removePlayer(playerName);
        this.controller.updateAllLobbies();
    }

    @Override
    public void playerAbandons(String playerName) throws IOException {
        this.controller.playerAbandons(playerName);
    }

    @Override
    public void destroyComponentTile(String playerName, int i, int j) throws IOException {
        this.controller.destroyTile(playerName, i, j);
    }

    @Override
    public void endGame() throws IOException {
        this.controller.endGame();
    }

    public void showMessageToEveryone(String mess) {
        SocketMessage message = new SocketMessage("MessageToEveryone", null, mess);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating message to everyone for client: " + e.getMessage());
        }
    }
}
