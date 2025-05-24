package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;
import it.polimi.ingsw.is25am22new.Network.RMI.Server.RmiServer;
import it.polimi.ingsw.is25am22new.Network.Socket.Server.SocketServerSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The GameController class is responsible for managing and controlling the flow of the game.
 * It facilitates operations within the lobby, starts the game, manages players' actions,
 * and executes core game functionality.
 */
public class GameController {
    private Lobby lobby;
    private Game game;
    private String lobbyCreator;
    private List<ObserverModel> observers;

    private GameState currentState;
    private String gameType; // "tutorial" or "level2"

    private final Object LOCK_COVEREDTILES = new Object();
    private final Object LOCK_UNCOVEREDTILES = new Object();
    private final Object LOCK_FLIGHTBOARD = new Object();
    private final Object LOCK_HOURGLASS = new Object();
    private final Object LOCK_CURRCARDDECK = new Object();

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    private boolean started;

    public enum GameState {
        LOBBY,
        GAME
    }

    public GameController() {
        this.lobby = new Lobby();
        this.currentState = GameState.LOBBY;
        this.gameType = lobby.getGameType();
        this.observers = new ArrayList<>();
    }

    public GameController(Lobby lobby) {
        this.lobby = lobby;
        this.currentState = GameState.LOBBY;
        this.gameType = lobby.getGameType();
    }

    /**
     * The main method responsible for initializing and starting the server components
     * including the RMI server and Socket server. This method sets up the necessary
     * configurations and begins execution of both servers in separate threads.
     *
     * @param args an array of command-line arguments where:
     *             args[0] specifies the hostname for the RMI server.
     *             args[1] specifies the port number to be used for the servers.
     */
    public static void main(String[] args) {
        GameController gameController = new GameController();
        // Starts Rmi Server
        new Thread(() -> {
            try {
                System.setProperty("java.rmi.server.hostname", args[0]);
                int port = Integer.parseInt(args[1]);
                RmiServer rmiServer = new RmiServer(gameController, port);
                gameController.getObservers().add(rmiServer);
                System.out.println("RMI Server is running... waiting for clients to connect.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Starts Socket Server
        new Thread(() -> {
            try {
                int port = Integer.parseInt(args[1]);
                ServerSocket listenSocket = new ServerSocket(++port);
                System.out.println("Socket Server is running... waiting for clients to connect.");
                System.out.println("Socket server on listen - it is running on port " + port + "...");
                SocketServerSide socketServerSide = new SocketServerSide(gameController, listenSocket);
                gameController.getObservers().add(socketServerSide);
                socketServerSide.runServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // Lobby methods
    public String getLobbyState() {
        return
                "Players: " + lobby.getPlayers() + "\n" +
                "Lobby Creator: " + lobbyCreator + "\n" +
                "Ready Status: " + lobby.getReadyStatus() + "\n" +
                "Game Type: " + lobby.getGameType() + "\n";
    }

    public String getLobbyCreator() {
        return lobbyCreator;
    }

    private void setLobbyCreator(String player) {
        if(currentState == GameState.LOBBY) {
            this.lobbyCreator = player;
            System.out.println("Lobby creator set to " + player);
        } else {
            System.out.println("Lobby creator cannot be set outside lobby state.");
        }
    }

    public void setNumPlayers(int maxPlayers) {
        if(currentState == GameState.LOBBY) {
            lobby.setMaxPlayers(maxPlayers);
            System.out.println("Max players set to " + maxPlayers);
        } else {
            System.out.println("Max players cannot be set outside lobby state.");
        }
    }

    public int addPlayer(String player) {
        if(currentState == GameState.LOBBY) {
            int res = lobby.addPlayer(player);
            if(res == 1) {
                if(lobby.getPlayers().size() == 1)  setLobbyCreator(player);
                System.out.println("Player " + player + " added to lobby");
            }
            else if(res == -1) {
                System.out.println("Lobby is full, player " + player + " cannot be added.");
            }
            else if(res == -2) {
                System.out.println("Player " + player + " already in lobby.");
            }
            return res;
        }else {
            System.out.println("Player " + player + " cannot be added outside lobby state.");
        }
        return -1; // Can't add players outside lobby state
    }

    public int removePlayer(String player) {
        if(currentState == GameState.LOBBY) {
            boolean isCreator = player.equals(lobbyCreator);
            int result = lobby.removePlayer(player);
            System.out.println("Player " + player + " removed from lobby");
            if(result == 1 && lobby.getPlayers().isEmpty()) {
                this.reinitializeGame();
                System.out.println("Lobby is empty, game reinitialized.");
            }
            if(result == 1 && isCreator && !lobby.getPlayers().isEmpty()) {
                this.setLobbyCreator(lobby.getPlayers().getFirst());
                System.out.println("Lobby creator changed to " + lobbyCreator);
            }
            return result;
        }else {

            System.out.println("Player " + player + " cannot be removed outside lobby state.");
        }
        return -1; // Can't remove players outside lobby state
    }

    public void setPlayerReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobby.setPlayerReady(player);
            System.out.println("Player " + player + " is ready.");
        }else {
            System.out.println("Player " + player + " cannot be set to ready outside lobby state.");
        }
    }

    public boolean startGameByHost(String player) {
        if(currentState == GameState.LOBBY && player.equals(lobbyCreator)) {
            checkAndStartGame();
            return true;
        } else if(currentState == GameState.LOBBY) {
            System.out.println("Only the lobby creator can start the game.");
        } else {
            System.out.println("Game cannot be started outside lobby state.");
        }
        return false;
    }

    public void setPlayerNotReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobby.setPlayerNotReady(player);
            System.out.println("Player " + player + " is not ready.");
        }else {
            System.out.println("Player " + player + " cannot be set to not ready outside lobby state.");
        }
    }

    public void setGameType(String gameType) {
        if(currentState == GameState.LOBBY) {
            this.gameType = gameType;
            lobby.setGameType(gameType);
            System.out.println("Game type set to " + gameType);
        } else {
            System.out.println("Game type cannot be changed outside lobby state.");
        }
    }

    private void checkAndStartGame() {
        if(lobby.isLobbyReady()) {
            System.out.println("Starting the game...");
            startGame();
        } else if(lobby.getPlayers().size() >= 2) {
            System.out.println("Not all players are ready.");
        } else {
            System.out.println("Not enough players to start the game.");
        }
    }

    private void startGame() {
        if("tutorial".equals(gameType)) {
            game = new TutorialGame(new ArrayList<>(lobby.getPlayers()), observers);
            System.out.println("Tutorial Game started");
        } else if("level2".equals(gameType)) {
            game = new Level2Game(new ArrayList<>(lobby.getPlayers()), observers);
            System.out.println("Level 2 Game started");
        } else {
            System.out.println("Invalid game type: " + gameType);
            return; // Invalid game type
        }
        game.initGame();
        System.out.println("Game initialized");
        currentState = GameState.GAME;
        System.out.println(gameType + " game started.");
    }

    // Game methods
    public void pickCoveredTile(String player) {
        synchronized (LOCK_COVEREDTILES){
            try {
                if(currentState == GameState.GAME) {
                    game.pickCoveredTile(player);
                }else {
                    System.out.println("Player " + player + " cannot pick a covered tile outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void pickUncoveredTile(String player, String tilePngName) {
        synchronized (LOCK_UNCOVEREDTILES) {
            try {
                if (currentState == GameState.GAME) {
                    game.pickUncoveredTile(player, tilePngName);
                } else {
                    System.out.println("Player " + player + " cannot pick an uncovered tile outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void rotateClockwise(String player, int rotationNum) {
        if(currentState == GameState.GAME) {
            for(int i = 0; i < rotationNum; i++) {
                game.rotateClockwise(player);
            }
        } else {
            System.out.println("Player " + player + " cannot rotate clockwise outside game state.");
        }
    }

    public void rotateCounterClockwise(String player, int rotationNum) {
        if(currentState == GameState.GAME) {
            for(int i = 0; i < rotationNum; i++) {
                game.rotateCounterClockwise(player);
            }
        } else {
            System.out.println("Player " + player + " cannot rotate counterclockwise outside game state.");
        }
    }

    public void placeAstronauts(String nickname, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.placeAstronauts(nickname, i, j);
            } else {
                System.out.println("Player " + nickname + " cannot place astronauts outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void placeBrownAlien(String nickname, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.placeBrownAlien(nickname, i, j);
            } else {
                System.out.println("Player " + nickname + " cannot place brown alien outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void placePurpleAlien(String nickname, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.placePurpleAlien(nickname, i, j);
            } else {
                System.out.println("Player " + nickname + " cannot place purple alien outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void weldComponentTile(String player, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.weldComponentTile(player, i, j);
            } else {
                System.out.println("Player " + player + " cannot weld a component tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void standbyComponentTile(String player) {
        try {
            if (currentState == GameState.GAME) {
                game.standbyComponentTile(player);
            } else {
                System.out.println("Player " + player + " cannot standby a component tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void pickStandByComponentTile(String player, int index) {
        try {
            if (currentState == GameState.GAME) {
                game.pickStandByComponentTile(player, index);
            } else {
                System.out.println("Player " + player + " cannot pick a standby component tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void discardComponentTile(String player) {
        try {
            if (currentState == GameState.GAME) {
                game.discardComponentTile(player);
            } else {
                System.out.println("Player " + player + " cannot discard a component tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    // NOT USED BY CLIENT?
    /*These method may be useless*/
    public void finishBuilding(String player) {
        if(currentState == GameState.GAME) {
            game.finishBuilding(player);
        } else {
            System.out.println("Player " + player + " cannot finish building outside game state.");
        }
    }
    /*********************************/

    public void finishBuilding(String player, int pos) {
        synchronized (LOCK_FLIGHTBOARD){
            try {
                if (currentState == GameState.GAME) {
                    game.finishBuilding(player, pos);
                } else {
                    System.out.println("Player " + player + " cannot finish building outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // NOT USED BY CLIENT?
    /*These method may be useless*/
    public void finishedAllShipboards() {
        if(currentState == GameState.GAME) {
            game.finishedAllShipboards();
        } else {
            System.out.println("Cannot check if all shipboards are finished outside game state.");
        }
    }
    /*********************************/

    public void flipHourglass() {
        synchronized (LOCK_HOURGLASS) {
            try {
                if (currentState == GameState.GAME) {
                    game.flipHourglass(() -> {
                    });
                } else {
                    System.out.println("Cannot flip hourglass outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void pickCard() {
        synchronized (LOCK_CURRCARDDECK) {
            try {
                if (currentState == GameState.GAME) {
                    game.pickCard();
                } else {
                    System.out.println("Cannot pick card outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public synchronized void activateCard(InputCommand inputCommand) {
        try {
            if (currentState == GameState.GAME) {
                game.activateCard(inputCommand);
            } else {
                System.out.println("Cannot activate card outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void playerAbandons(String player) {
        synchronized (LOCK_FLIGHTBOARD){
            if(currentState == GameState.GAME) {
                game.playerAbandons(player);
            } else {
                System.out.println("Player " + player + " cannot abandon outside game state.");
            }
        }
    }

    public void destroyTile(String player, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.destroyTile(player, i, j);
            } else {
                System.out.println("Player " + player + " cannot destroy a tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // NOT USED BY CLIENT?
    public void setCurrPlayer(String player) {
        if(currentState == GameState.GAME) {
            game.setCurrPlayer(player);
        } else {
            System.out.println("Current player cannot be set outside game state.");
        }
    }
    // NOT USED BY CLIENT?
    public void setCurrPlayerToLeader() {
        if(currentState == GameState.GAME) {
            game.setCurrPlayerToLeader();
        } else {
            System.out.println("Current player cannot be set to leader outside game state.");
        }
    }

    public Map<String, Integer> endGame() {
        if(currentState == GameState.GAME) {
            System.out.println("Game ended.");
            return game.endGame();
        } else {
            System.out.println("Game cannot be ended outside game state.");
        }
        return null; // Can't end game outside game state
    }

    public void reinitializeGame() {
        lobby = new Lobby();
        currentState = GameState.LOBBY;
        game = null;
        gameType = null;
        lobbyCreator = null;
        System.out.println("Game reinitialized.");
    }

    // Getters
    public Game getGame() {
        if(currentState == GameState.GAME) {
            return game;
        } else {
            System.out.println("Game cannot be retrieved outside game state.");
        }
        return null;
    }

    public List<CardPile> getCardPiles() {
        if(currentState == GameState.GAME) {
            return game.getCardPiles();
        } else {
            System.out.println("Card piles cannot be retrieved outside game state.");
        }
        return null;
    }

    public List<AdventureCard> getDeck() {
        synchronized (LOCK_CURRCARDDECK){
            if(currentState == GameState.GAME) {
                return game.getDeck();
            } else {
                System.out.println("Deck cannot be retrieved outside game state.");
            }
            return null;
        }
    }

    public AdventureCard getCurrCard() {
        synchronized (LOCK_CURRCARDDECK) {
            if(currentState == GameState.GAME) {
                return game.getCurrCard();
            } else {
                System.out.println("Current card cannot be retrieved outside game state.");
            }
            return null;
        }
    }

    public String getLastPlayer() {
        synchronized (LOCK_FLIGHTBOARD) {
            if(currentState == GameState.GAME) {
                return game.getLastPlayer();
            } else {
                System.out.println("Last player cannot be retrieved outside game state.");
            }
            return null;
        }
    }

    public List<String> getPlayers() {
        if(currentState == GameState.LOBBY) {
            return lobby.getPlayers();
        } else if(currentState == GameState.GAME) {
            return game.getPlayerList();
        } else {
            System.out.println("Players cannot be retrieved outside lobby or game state.");
        }
        return null;
    }

    public Map<String, Boolean> getReadyStatus() {
        if(currentState == GameState.LOBBY) {
            return lobby.getReadyStatus();
        } else {
            System.out.println("Ready status cannot be retrieved outside lobby state.");
        }
        return null;
    }

    public String getGameType() {
        if(currentState == GameState.LOBBY) {
            return lobby.getGameType();
        } else {
            System.out.println("Game type cannot be retrieved outside lobby state.");
        }
        return gameType;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public List<ObserverModel> getObservers() {
        return observers;
    }

    public void updateAllLobbies() {
        for (var observer : this.observers) {
            observer.updateLobby();
        }
    }

    public void updateAllGameStarted() {
        for (var observer : this.observers) {
            observer.updateGameStarted();
        }
    }

    public void updateAllPlayerJoined(String player) {
        for (var observer : this.observers) {
            observer.updatePlayerJoined(player);
        }
    }

    public void godMode(String player, String conf) {
        game.godMode(player, conf);
    }

    public synchronized void quit(String player) {
        if(game.getPlayerList().size() > 1) {
            game.getPlayerList().remove(player);
        }
        else{
            for (var observer : this.observers) {
                observer.shutdown();
            }
            System.exit(0);
        }
    }

    public synchronized void disconnect() {
        for (var observer : this.observers) {
            observer.shutdown();
        }
        System.exit(0);
    }

}