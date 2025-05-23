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
import it.polimi.ingsw.is25am22new.Network.HeartbeatManager;
import it.polimi.ingsw.is25am22new.Network.Socket.SocketMessage;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SocketClientHandler implements VirtualView {
    private final GameController controller;
    private final SocketServerSide server;
    private final ObjectInputStream objectInput;
    private final ObjectOutputStream objectOutput;
    private final HeartbeatManager heartbeatManager;
    private Map<String, Consumer<SocketMessage>> commandMap;
    private Thread thisThread;
    // CI SONO VARIE ISTANZE DI SOCKETCLIENTHANDLER, UNA PER OGNI GIOCATORE
    public SocketClientHandler(GameController controller, SocketServerSide server, InputStream is, OutputStream os) throws IOException {
        this.controller = controller;
        this.server = server;
        this.objectOutput = new ObjectOutputStream(os);
        objectOutput.flush();
        this.objectInput = new ObjectInputStream(is);
        this.heartbeatManager = new HeartbeatManager(5000, this::handleHeartbeatDisconnect);
        commandMap = new HashMap<>();
        initializeCommandMap();
    }

    private void initializeCommandMap() {
        commandMap.put("checkAvailability", this::handleCheckAvailability);
        commandMap.put("removePlayer", msg -> removePlayer(msg.getPayload()));
        commandMap.put("setPlayerReady", msg -> setPlayerReady(msg.getPayload()));
        commandMap.put("setPlayerNotReady", msg -> setPlayerNotReady(msg.getPayload()));
        commandMap.put("startGameByHost", msg -> startGameByHost(msg.getPayload()));
        commandMap.put("setGameType", msg -> setGameType(msg.getPayload()));
        commandMap.put("pickCoveredTile", msg -> pickCoveredTile(msg.getPayload()));
        commandMap.put("pickUncoveredTile", msg -> pickUncoveredTile(msg.getPayload(), (String) msg.getObject()));
        commandMap.put("weldComponentTile", this::handleWeldComponentTile);
        commandMap.put("standbyComponentTile", msg -> standbyComponentTile(msg.getPayload()));
        commandMap.put("pickStandByComponentTile", this::handlePickStandbyComponentTile);
        commandMap.put("discardComponentTile", msg -> discardComponentTile(msg.getPayload()));
        commandMap.put("finishBuilding1", msg -> finishBuilding(msg.getPayload()));
        commandMap.put("finishBuilding2", this::handleFinishBuilding2);
        commandMap.put("finishedAllShipboards", msg -> finishedAllShipboards());
        commandMap.put("flipHourglass", msg -> flipHourglass());
        commandMap.put("pickCard", msg -> pickCard());
        commandMap.put("activateCard", msg -> activateCard((InputCommand) msg.getObject()));
        commandMap.put("playerAbandons", msg -> playerAbandons(msg.getPayload()));
        commandMap.put("destroyTile", this::handleDestroyTile);
        commandMap.put("endGame", msg -> endGame());
        commandMap.put("setNumPlayers", this::handleSetNumPlayers);
        commandMap.put("quit", this::handleQuit);
        commandMap.put("godMode", this::handleGodMode);
        commandMap.put("placeBrownAlien", this::handlePlaceBrownAlien);
        commandMap.put("placePurpleAlien", this::handlePlacePurpleAlien);
        commandMap.put("heartbeat", msg -> heartbeat(msg.getPayload()));
        commandMap.put("placeAstronauts", this::handlePlaceAstronauts);
        commandMap.put("disconnect", msg -> controller.disconnect());
        commandMap.put("connectionTester", this::handleConnectionTester);
    }

    //comunicazione dal client al server
    public void runVirtualView(Thread thread) {
        SocketMessage msg;
        thisThread = thread;
        try {
            while ((msg = (SocketMessage) objectInput.readObject()) != null) {
                commandMap.getOrDefault(msg.getCommand(), m -> System.out.println("Unknown command: " + m.getCommand())).accept(msg);
            }
        } catch (Exception e) {
            System.out.println("Connection closed on ServerClientHandler: " + this);
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (objectInput != null) objectInput.close();
                if (objectOutput != null) objectOutput.close();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
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

    public void heartbeat(String nickname) {
        System.out.println("Received heartbeat from: " + nickname);
        heartbeatManager.heartbeat(nickname);
    }

    private void handleHeartbeatDisconnect(String nickname) {
        try {
            System.out.println("Heartbeat timeout for client: " + nickname);

            this.controller.disconnect();
        } catch (Exception e) {
            System.err.println("Error handling client disconnect: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateBank(Bank bank){
        SocketMessage message = new SocketMessage("Bank", bank, null);
        try {
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
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
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating deck for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateGame(Game game)  {
        SocketMessage message = new SocketMessage("Game", game, null);
        try {
            objectOutput.reset();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating game for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateStopHourglass() {
        SocketMessage message = new SocketMessage("StopHourglass",null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating hourglass for client: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateStartHourglass(int hourglassSpot){
        SocketMessage message = new SocketMessage("StartHourglass",hourglassSpot, null);
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
    public void showUpdateLeaderboard(Map<String, Integer> leaderboard) throws RemoteException {
        SocketMessage message = new SocketMessage("Leaderboard", leaderboard, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error updating leaderboard for client: " + e.getMessage());
        }
    }

    @Override
    public void terminate(){
        showWaitResult();
    }

    private void showWaitResult() {
        SocketMessage message = new SocketMessage("waitResult", null, null);
        try {
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            System.out.println("Error sending wait result from server: " + e.getMessage());
        }
    }

    private void godMode(String playerName, String conf){
        this.controller.godMode(playerName, conf);
    }


    private void setPlayerReady(String playerName) {
        this.controller.setPlayerReady(playerName);
        this.controller.updateAllLobbies();
    }


    private void setPlayerNotReady(String playerName){
        this.controller.setPlayerNotReady(playerName);
        this.controller.updateAllLobbies();
    }


    public void startGameByHost(String playerName) {
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


    public void setGameType(String gameType) {
        this.controller.setGameType(gameType);
        this.controller.updateAllLobbies();
        this.controller.setStarted(true);
    }


    public void pickCoveredTile(String playerName){
        this.controller.pickCoveredTile(playerName);
    }


    public void pickUncoveredTile(String playerName, String pngName)  {
        this.controller.pickUncoveredTile(playerName, pngName);
    }


    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) {
        if(numOfRotation > 0){
            this.controller.rotateClockwise(playerName, numOfRotation);
        }
        else {
            this.controller.rotateCounterClockwise(playerName, -numOfRotation);
        }
        this.controller.weldComponentTile(playerName, i, j);
    }


    private void standbyComponentTile(String playerName) {
        this.controller.standbyComponentTile(playerName);
    }


    private void pickStandbyComponentTile(String playerName, int index) {
        this.controller.pickStandByComponentTile(playerName, index);
    }


    private void discardComponentTile(String playerName) {
        this.controller.discardComponentTile(playerName);
    }


    private void finishBuilding(String playerName) {
        this.controller.finishBuilding(playerName);
    }


    private void finishBuilding(String playerName, int index) {
        this.controller.finishBuilding(playerName, index);
    }


    private void finishedAllShipboards()  {
        this.controller.finishedAllShipboards();
    }


    private void flipHourglass(){
        this.controller.flipHourglass();
    }


    private void pickCard() {
        this.controller.pickCard();
    }


    private void activateCard(InputCommand inputCommand) {
        this.controller.activateCard(inputCommand);
    }


    private void removePlayer(String playerName) {
        this.controller.removePlayer(playerName);
        this.controller.updateAllLobbies();
    }


    private void playerAbandons(String playerName) {
        this.controller.playerAbandons(playerName);
    }


    private void destroyComponentTile(String playerName, int i, int j)  {
        this.controller.destroyTile(playerName, i, j);
    }


    private void endGame() {
        this.controller.endGame();
    }


    private void placeBrownAlien(String playerName, int i, int j) {
        this.controller.placeBrownAlien(playerName, i, j);
    }


    private void placeAstronauts(String playerName, int i, int j) {
        this.controller.placeAstronauts(playerName, i, j);
    }


    private void placePurpleAlien(String playerName, int i, int j) {
        this.controller.placePurpleAlien(playerName, i, j);
    }

    //public void showMessageToEveryone(String mess) {
    //    SocketMessage message = new SocketMessage("MessageToEveryone", null, mess);
    //    try {
    //        objectOutput.writeObject(message);
    //        objectOutput.flush();
    //    } catch (IOException e) {
    //        System.out.println("Error updating message to everyone for client: " + e.getMessage());
    //    }
    //}

    public ObjectInputStream getObjectInput() {
        return objectInput;
    }

    public ObjectOutputStream getObjectOutput() {
        return objectOutput;
    }

    public HeartbeatManager getHeartbeatManager() {
        return heartbeatManager;
    }
    private void handleWeldComponentTile(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        weldComponentTile(msg.getPayload(), cmd.getRow(), cmd.getCol(), cmd.getIndexChosen());
    }

    private void handlePickStandbyComponentTile(SocketMessage msg) {
        int indexChosen = ((InputCommand) msg.getObject()).getIndexChosen();
        pickStandbyComponentTile(msg.getPayload(), indexChosen);
    }

    private void handleFinishBuilding2(SocketMessage msg) {
        int indexChosen = ((InputCommand) msg.getObject()).getIndexChosen();
        finishBuilding(msg.getPayload(), indexChosen);
    }

    private void handleDestroyTile(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        destroyComponentTile(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    private void handleSetNumPlayers(SocketMessage msg) {
        int numPlayers = ((InputCommand) msg.getObject()).getIndexChosen();
        controller.setNumPlayers(numPlayers);
    }

    private void handleQuit(SocketMessage msg) {
        controller.quit(msg.getPayload());
        heartbeatManager.unregisterClient(msg.getPayload());
    }

    private void handleGodMode(SocketMessage msg) {
        godMode(msg.getPayload(), (String) msg.getObject());
    }

    private void handlePlaceBrownAlien(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        placeBrownAlien(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    private void handlePlacePurpleAlien(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        placePurpleAlien(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    private void handlePlaceAstronauts(SocketMessage msg) {
        InputCommand cmd = (InputCommand) msg.getObject();
        placeAstronauts(msg.getPayload(), cmd.getRow(), cmd.getCol());
    }

    private void handleConnectionTester(SocketMessage msg) {
        System.out.println(msg.getPayload());
        System.out.println(((InputCommand) msg.getObject()).getIndexChosen());
        showUpdateTest();
    }

    private void handleCheckAvailability(SocketMessage msg) {
        if (controller.isStarted() || controller.getPlayers().isEmpty()) {
            int res = controller.addPlayer(msg.getPayload());
            if (res == 1) {
                heartbeatManager.registerClient(msg.getPayload());
                server.addHandlerToClients(this, thisThread);
                showNicknameResult(true, "PlayerAdded");

                boolean isHost = controller.getPlayers().size() == 1;
                showConnectionResult(isHost, true, isHost ? "You are the host of the lobby" : "You joined an existing lobby");

                if (!isHost) controller.updateAllPlayerJoined(msg.getPayload());
                controller.updateAllLobbies();
            } else if (res == -2) {
                showNicknameResult(false, "PlayerAlreadyInLobby");
            } else if (res == -1) {
                showNicknameResult(false, "LobbyFullOrOutsideLobbyState");
            }
            System.out.println("List of players updated: " + controller.getPlayers());
        } else {
            terminate();
        }
    }
}
