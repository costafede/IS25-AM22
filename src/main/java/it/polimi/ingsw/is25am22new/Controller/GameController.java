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
    private boolean started;

    /**
     * Returns whether the game has started.
     *
     * @return {@code true} if the game has started; {@code false} otherwise.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Sets the started status of the game.
     *
     * @param started {@code true} to indicate the game has started; {@code false} otherwise.
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     * Represents the possible states of the game.
     */
    public enum GameState {
        /**
         * The game is currently in the lobby phase.
         */
        LOBBY,

        /**
         * The game is currently in progress.
         */
        GAME
    }

    /**
     * Creates a new {@code GameController} instance with a default {@link Lobby}.
     * Initializes the game state to {@link GameState#LOBBY}, sets the game type from the lobby,
     * and prepares an empty list of observers.
     */
    public GameController() {
        this.lobby = new Lobby();
        this.currentState = GameState.LOBBY;
        this.gameType = lobby.getGameType();
        this.observers = new ArrayList<>();
    }

    /**
     * Creates a new {@code GameController} instance using the provided {@link Lobby}.
     * Initializes the game state to {@link GameState#LOBBY} and sets the game type from the given lobby.
     *
     * @param lobby the lobby instance to use for initializing the game controller.
     */
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

    /**
     * Returns a string representation of the current lobby state,
     * including players, the lobby creator, ready status, and game type.
     *
     * @return a formatted string with the current lobby information.
     */
    public String getLobbyState() {
        return
                "Players: " + lobby.getPlayers() + "\n" +
                        "Lobby Creator: " + lobbyCreator + "\n" +
                        "Ready Status: " + lobby.getReadyStatus() + "\n" +
                        "Game Type: " + lobby.getGameType() + "\n";
    }

    /**
     * Returns the username of the lobby creator.
     *
     * @return the lobby creator's name.
     */
    public String getLobbyCreator() {
        return lobbyCreator;
    }

    /**
     * Sets the lobby creator if the game is in the {@link GameState#LOBBY} state.
     * This method is private and typically invoked internally when the first player joins.
     *
     * @param player the name of the player to be set as lobby creator.
     */
    private void setLobbyCreator(String player) {
        if(currentState == GameState.LOBBY) {
            this.lobbyCreator = player;
            System.out.println("Lobby creator set to " + player);
        } else {
            System.out.println("Lobby creator cannot be set outside lobby state.");
        }
    }

    /**
     * Sets the maximum number of players allowed in the lobby.
     * Can only be modified during the {@link GameState#LOBBY} phase.
     *
     * @param maxPlayers the maximum number of players.
     */
    public void setNumPlayers(int maxPlayers) {
        if(currentState == GameState.LOBBY) {
            lobby.setMaxPlayers(maxPlayers);
            System.out.println("Max players set to " + maxPlayers);
        } else {
            System.out.println("Max players cannot be set outside lobby state.");
        }
    }

    /**
     * Adds a player to the lobby if the game is in the {@link GameState#LOBBY} state.
     * If the first player is added, they are automatically set as the lobby creator.
     *
     * @param player the name of the player to add.
     * @return 1 if successfully added, -1 if lobby is full, -2 if player already exists,
     *         or -1 if not in lobby state.
     */
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
        } else {
            System.out.println("Player " + player + " cannot be added outside lobby state.");
        }
        return -1; // Can't add players outside lobby state
    }

    /**
     * Removes a player from the lobby if the game is in the {@link GameState#LOBBY} state.
     * If the lobby becomes empty, the game is reinitialized.
     * If the removed player was the creator, a new one is assigned.
     *
     * @param player the name of the player to remove.
     * @return 1 if successfully removed, 0 if player not found, or -1 if not in lobby state.
     */
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
        } else {
            System.out.println("Player " + player + " cannot be removed outside lobby state.");
        }
        return -1; // Can't remove players outside lobby state
    }

    /**
     * Marks a player as ready in the lobby.
     * Can only be called during the {@link GameState#LOBBY} phase.
     *
     * @param player the name of the player to mark as ready.
     */
    public void setPlayerReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobby.setPlayerReady(player);
            System.out.println("Player " + player + " is ready.");
        } else {
            System.out.println("Player " + player + " cannot be set to ready outside lobby state.");
        }
    }

    /**
     * Starts the game if the player is the lobby creator and the game is still in the lobby state.
     * This typically transitions the game to the active state.
     *
     * @param player the name of the player attempting to start the game.
     * @return {@code true} if the game was started, {@code false} otherwise.
     */
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

    /**
     * Marks a player as not ready in the lobby.
     * Can only be used during the {@link GameState#LOBBY} phase.
     *
     * @param player the name of the player to mark as not ready.
     */
    public void setPlayerNotReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobby.setPlayerNotReady(player);
            System.out.println("Player " + player + " is not ready.");
        } else {
            System.out.println("Player " + player + " cannot be set to not ready outside lobby state.");
        }
    }

    /**
     * Sets the type of game to be played.
     * This can only be modified during the {@link GameState#LOBBY} phase.
     *
     * @param gameType the type of game (e.g., "standard", "advanced") to be set.
     */
    public void setGameType(String gameType) {
        if(currentState == GameState.LOBBY) {
            this.gameType = gameType;
            lobby.setGameType(gameType);
            System.out.println("Game type set to " + gameType);
        } else {
            System.out.println("Game type cannot be changed outside lobby state.");
        }
    }

    /**
     * Checks if all players are ready and starts the game if possible.
     * Logs the status depending on the lobby readiness and player count.
     * Called internally when the host attempts to start the game.
     */
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

    /**
     * Loads a previously saved game state and resumes it if all lobby players match.
     *
     * @return {@code true} if the game was successfully loaded and resumed, {@code false} otherwise.
     */
    public boolean loadGame() {
        this.game = GameSaver.loadGame();
        if(game == null)
            return false;
        game.setObservers(observers);
        List<String> lobbyPlayers = lobby.getPlayers();
        List<String> previousPlayers = game.getPlayerList();
        if(lobbyPlayers.containsAll(previousPlayers) && previousPlayers.size() == lobbyPlayers.size()) {
            this.currentState = GameState.GAME;
            updateAllGameStarted();
            game.updateAllGameLoaded(game);
            return true;
        }
        return false;
    }

    /**
     * Starts a new game based on the selected game type (e.g., tutorial, level2).
     * Initializes the game, sets its state, and saves the initial state.
     */
    private void startGame() {
        GameSaver.clearFile();
        if("tutorial".equals(gameType)) {
            game = new TutorialGame(new ArrayList<>(lobby.getPlayers()), observers);
            game.initGame();
            GameSaver.saveTutorialGame(game);
            System.out.println("Tutorial Game started");
        } else if("level2".equals(gameType)) {
            game = new Level2Game(new ArrayList<>(lobby.getPlayers()), observers);
            game.initGame();
            GameSaver.saveLevel2Game(game);
            System.out.println("Level 2 Game started");
        } else {
            System.out.println("Invalid game type: " + gameType);
            return;
        }
        System.out.println("Game initialized");
        currentState = GameState.GAME;
        System.out.println(gameType + " game started.");
    }

    /**
     * Allows a player to pick a covered tile during the game.
     * This action is synchronized to avoid race conditions.
     *
     * @param player the nickname of the player picking the tile.
     */
    public void pickCoveredTile(String player) {
        synchronized (LOCK_COVEREDTILES){
            try {
                if(currentState == GameState.GAME) {
                    game.pickCoveredTile(player);
                    GameSaver.savePickCoveredTile(player);
                } else {
                    System.out.println("Player " + player + " cannot pick a covered tile outside game state.");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Allows a player to pick an uncovered tile during the game.
     * This action is synchronized to avoid race conditions.
     *
     * @param player the nickname of the player.
     * @param tilePngName the name of the image/tile to pick.
     */
    public void pickUncoveredTile(String player, String tilePngName) {
        synchronized (LOCK_UNCOVEREDTILES) {
            try {
                if (currentState == GameState.GAME) {
                    game.pickUncoveredTile(player, tilePngName);
                    GameSaver.savePickUncoveredTile(player, tilePngName);
                } else {
                    System.out.println("Player " + player + " cannot pick an uncovered tile outside game state.");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Rotates a player's current tile clockwise a specified number of times.
     *
     * @param player the nickname of the player.
     * @param rotationNum number of 90-degree rotations to perform.
     */
    public void rotateClockwise(String player, int rotationNum) {
        if(currentState == GameState.GAME) {
            for(int i = 0; i < rotationNum; i++) {
                game.rotateClockwise(player);
                GameSaver.saveRotateClockwise(player);
            }
        } else {
            System.out.println("Player " + player + " cannot rotate clockwise outside game state.");
        }
    }

    /**
     * Rotates a player's current tile counterclockwise a specified number of times.
     *
     * @param player the nickname of the player.
     * @param rotationNum number of 90-degree rotations to perform.
     */
    public void rotateCounterClockwise(String player, int rotationNum) {
        if(currentState == GameState.GAME) {
            for(int i = 0; i < rotationNum; i++) {
                game.rotateCounterClockwise(player);
                GameSaver.saveRotateCounterClockwise(player);
            }
        } else {
            System.out.println("Player " + player + " cannot rotate counterclockwise outside game state.");
        }
    }

    /**
     * Places astronauts on the ship grid at the specified coordinates.
     *
     * @param nickname the name of the player.
     * @param i the row index.
     * @param j the column index.
     */
    public void placeAstronauts(String nickname, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.placeAstronauts(nickname, i, j);
                GameSaver.savePlaceAstronauts(nickname, i, j);
            } else {
                System.out.println("Player " + nickname + " cannot place astronauts outside game state.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Places a brown alien at the given grid position.
     *
     * @param nickname the name of the player.
     * @param i the row index.
     * @param j the column index.
     */
    public void placeBrownAlien(String nickname, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.placeBrownAlien(nickname, i, j);
                GameSaver.savePlaceBrownAlien(nickname, i, j);
            } else {
                System.out.println("Player " + nickname + " cannot place brown alien outside game state.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Places a purple alien at the given grid position.
     *
     * @param nickname the name of the player.
     * @param i the row index.
     * @param j the column index.
     */
    public void placePurpleAlien(String nickname, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.placePurpleAlien(nickname, i, j);
                GameSaver.savePlacePurpleAlien(nickname, i, j);
            } else {
                System.out.println("Player " + nickname + " cannot place purple alien outside game state.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Welds a component tile onto the ship at the given coordinates.
     *
     * @param player the nickname of the player.
     * @param i the row index.
     * @param j the column index.
     */
    public void weldComponentTile(String player, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.weldComponentTile(player, i, j);
                GameSaver.saveWeldComponentTile(player, i, j);
            } else {
                System.out.println("Player " + player + " cannot weld a component tile outside game state.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends the currently selected tile into standby mode.
     *
     * @param player the nickname of the player.
     */
    public void standbyComponentTile(String player) {
        try {
            if (currentState == GameState.GAME) {
                game.standbyComponentTile(player);
                GameSaver.saveStandbyComponentTile(player);
            } else {
                System.out.println("Player " + player + " cannot standby a component tile outside game state.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Picks a tile from the player's standby list based on the index.
     *
     * @param player the nickname of the player.
     * @param index the index of the tile to pick.
     */
    public void pickStandByComponentTile(String player, int index) {
        try {
            if (currentState == GameState.GAME) {
                game.pickStandByComponentTile(player, index);
                GameSaver.savePickStandByComponentTile(player, index);
            } else {
                System.out.println("Player " + player + " cannot pick a standby component tile outside game state.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Discards the currently selected component tile.
     *
     * @param player the nickname of the player
     */
    public void discardComponentTile(String player) {
        try {
            if (currentState == GameState.GAME) {
                game.discardComponentTile(player);
                GameSaver.saveDiscardComponentTile(player);
            } else {
                System.out.println("Player " + player + " cannot discard a component tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Marks the player's shipboard as finished building.
     *
     * @param player the nickname of the player
     */
    public void finishBuilding(String player) {
        if(currentState == GameState.GAME) {
            game.finishBuilding(player);
        } else {
            System.out.println("Player " + player + " cannot finish building outside game state.");
        }
    }

    /**
     * Marks the player's shipboard as finished and selects a flight board position.
     *
     * @param player the nickname of the player
     * @param pos the position on the flight board
     */
    public void finishBuilding(String player, int pos) {
        synchronized (LOCK_FLIGHTBOARD){
            try {
                if (currentState == GameState.GAME) {
                    game.finishBuilding(player, pos);
                    GameSaver.saveFinishBuilding(player, pos);
                } else {
                    System.out.println("Player " + player + " cannot finish building outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Notifies that all players have finished building their shipboards.
     */
    public void finishedAllShipboards() {
        if(currentState == GameState.GAME) {
            game.finishedAllShipboards();
        } else {
            System.out.println("Cannot check if all shipboards are finished outside game state.");
        }
    }

    /**
     * Flips the hourglass timer during the building phase.
     */
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

    /**
     * Picks the next adventure card from the deck.
     */
    public void pickCard() {
        synchronized (LOCK_CURRCARDDECK) {
            try {
                if (currentState == GameState.GAME) {
                    game.pickCard();
                    GameSaver.savePickCard();
                } else {
                    System.out.println("Cannot pick card outside game state.");
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Activates the currently drawn adventure card using the input command.
     *
     * @param inputCommand the command from the player to activate the card
     */
    public synchronized void activateCard(InputCommand inputCommand) {
        try {
            if (currentState == GameState.GAME) {
                game.activateCard(inputCommand);
                GameSaver.saveActivateCard(inputCommand);
            } else {
                System.out.println("Cannot activate card outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows a player to abandon the game.
     *
     * @param player the nickname of the player
     */
    public void playerAbandons(String player) {
        synchronized (LOCK_FLIGHTBOARD){
            if(currentState == GameState.GAME) {
                game.playerAbandons(player);
                GameSaver.savePlayerAbandons(player);
            } else {
                System.out.println("Player " + player + " cannot abandon outside game state.");
            }
        }
    }

    /**
     * Destroys a tile from the player's ship grid.
     *
     * @param player the nickname of the player
     * @param i row index
     * @param j column index
     */
    public void destroyTile(String player, int i, int j) {
        try {
            if (currentState == GameState.GAME) {
                game.destroyTile(player, i, j);
                GameSaver.saveDestroyTile(player, i, j);
            } else {
                System.out.println("Player " + player + " cannot destroy a tile outside game state.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sets the current player for the turn.
     *
     * @param player the nickname of the player
     */
    public void setCurrPlayer(String player) {
        if(currentState == GameState.GAME) {
            game.setCurrPlayer(player);
        } else {
            System.out.println("Current player cannot be set outside game state.");
        }
    }

    /**
     * Sets the current player to the leader (first to finish building).
     */
    public void setCurrPlayerToLeader() {
        if(currentState == GameState.GAME) {
            game.setCurrPlayerToLeader();
        } else {
            System.out.println("Current player cannot be set to leader outside game state.");
        }
    }

    /**
     * Ends the game and returns the final scores.
     *
     * @return a map from player nickname to their final score
     */
    public Map<String, Integer> endGame() {
        if(currentState == GameState.GAME) {
            System.out.println("Game ended.");
            return game.endGame();
        } else {
            System.out.println("Game cannot be ended outside game state.");
        }
        return null; // Can't end game outside game state
    }

    /**
     * Resets the game and lobby to initial state.
     */
    public void reinitializeGame() {
        lobby = new Lobby();
        currentState = GameState.LOBBY;
        game = null;
        gameType = null;
        lobbyCreator = null;
        System.out.println("Game reinitialized.");
    }

    /**
     * Returns the current Game object if in GAME state.
     *
     * @return the current game or null if not in game state
     */
    public Game getGame() {
        if(currentState == GameState.GAME) {
            return game;
        } else {
            System.out.println("Game cannot be retrieved outside game state.");
        }
        return null;
    }

    /**
     * Returns the current list of card piles.
     *
     * @return list of card piles or null if not in game state
     */
    public List<CardPile> getCardPiles() {
        if(currentState == GameState.GAME) {
            return game.getCardPiles();
        } else {
            System.out.println("Card piles cannot be retrieved outside game state.");
        }
        return null;
    }

    /**
     * Returns the current deck of adventure cards.
     *
     * @return list of cards or null if not in game state
     */
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

    /**
     * Returns the currently drawn adventure card.
     *
     * @return the current card or null if not in game state
     */
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

    /**
     * Returns the last player who performed an action.
     *
     * @return the nickname of the last player or null if not in game state
     */
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

    /**
     * Returns the list of players in the lobby or current game.
     *
     * @return the list of player nicknames or null if invalid state
     */
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

    /**
     * Returns the ready status of all players in the lobby.
     *
     * @return a map from nickname to ready status, or null if not in lobby
     */
    public Map<String, Boolean> getReadyStatus() {
        if(currentState == GameState.LOBBY) {
            return lobby.getReadyStatus();
        } else {
            System.out.println("Ready status cannot be retrieved outside lobby state.");
        }
        return null;
    }

    /**
     * Returns the selected game type from the lobby.
     *
     * @return the game type or null if not in lobby
     */
    public String getGameType() {
        if(currentState == GameState.LOBBY) {
            return lobby.getGameType();
        } else {
            System.out.println("Game type cannot be retrieved outside lobby state.");
        }
        return gameType;
    }

    /**
     * Returns the current controller state.
     *
     * @return the current game state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Returns the list of observers for UI updates.
     *
     * @return list of observers
     */
    public List<ObserverModel> getObservers() {
        return observers;
    }

    /**
     * Notifies all observers to update the lobby UI.
     */
    public void updateAllLobbies() {
        for (var observer : this.observers) {
            observer.updateLobby();
        }
    }

    /**
     * Notifies all observers that the game has started.
     */
    public void updateAllGameStarted() {
        for (var observer : this.observers) {
            observer.updateGameStarted();
        }
    }

    /**
     * Notifies all observers that a player has joined.
     *
     * @param player the nickname of the player who joined
     */
    public void updateAllPlayerJoined(String player) {
        for (var observer : this.observers) {
            observer.updatePlayerJoined(player);
        }
    }

    /**
     * Enables a special debug/game manipulation mode.
     *
     * @param player the nickname of the player
     * @param conf the configuration string
     */
    public void godMode(String player, String conf) {
        game.godMode(player, conf);
    }

    /**
     * Removes a player from the game, or shuts down if last player.
     *
     * @param player the nickname of the quitting player
     */
    public synchronized void quit(String player) {
        if(game!= null && game.getPlayerList().size() > 1) {
            game.getPlayerList().remove(player);
        }
        else{
            for (var observer : this.observers) {
                observer.shutdown();
            }
            System.exit(0);
        }
    }

    /**
     * Disconnects and shuts down all observers and the application.
     */
    public synchronized void disconnect() {
        for (var observer : this.observers) {
            observer.shutdown();
        }
        System.exit(0);
    }

}