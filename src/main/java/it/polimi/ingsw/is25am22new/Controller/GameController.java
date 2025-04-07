package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController {
    private Lobby lobby;
    private Game game;
    private String lobbyCreator;

    private GameState currentState;
    private String gameType; // "tutorial" or "level2"

    public String getLobbyCreator() {
        return lobbyCreator;
    }

    public enum GameState {
        LOBBY,
        GAME
    }

    public GameController() {
        this.lobby = new Lobby();
        this.currentState = GameState.LOBBY;
        this.gameType = lobby.getGameType();
    }

    public GameController(Lobby lobby) {
        this.lobby = lobby;
        this.currentState = GameState.LOBBY;
        this.gameType = lobby.getGameType();
    }

    // Lobby methods

    public String getLobbyState() {
        return
                "Players: " + lobby.getPlayers() + "\n" +
                "Lobby Creator: " + lobbyCreator + "\n" +
                "Ready Status: " + lobby.getReadyStatus() + "\n" +
                "Game Type: " + lobby.getGameType() + "\n";
    }

    private void setLobbyCreator(String player) {
        if(currentState == GameState.LOBBY) {
            this.lobbyCreator = player;
            System.out.println("Lobby creator set to " + player);
        } else {
            System.out.println("Lobby creator cannot be set outside lobby state.");
        }
    }

    public int addPlayer(String player) {
        if(currentState == GameState.LOBBY) {
            int res = lobby.addPlayer(player);
            if(res == 1 && lobby.getPlayers().size() == 1) {
                setLobbyCreator(player);
            }
            System.out.println("Player " + player + " added to lobby");
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
            game = new TutorialGame(new ArrayList<>(lobby.getPlayers()));
            System.out.println("Tutorial Game started");
        } else if("level2".equals(gameType)) {
            game = new Level2Game(new ArrayList<>(lobby.getPlayers()));
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
        if(currentState == GameState.GAME) {
            game.pickCoveredTile(player);
        }else {
            System.out.println("Player " + player + " cannot pick a covered tile outside game state.");
        }
    }

    public void pickUncoveredTile(String player, int index) {
        if(currentState == GameState.GAME) {
            game.pickUncoveredTile(player, index);
        } else {
            System.out.println("Player " + player + " cannot pick an uncovered tile outside game state.");
        }
    }

    public void rotateClockwise(String player) {
        if(currentState == GameState.GAME) {
            game.rotateClockwise(player);
        } else {
            System.out.println("Player " + player + " cannot rotate clockwise outside game state.");
        }
    }

    public void rotateCounterClockwise(String player) {
        if(currentState == GameState.GAME) {
            game.rotateCounterClockwise(player);
        } else {
            System.out.println("Player " + player + " cannot rotate counterclockwise outside game state.");
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

    public void weldComponentTile(String player, int i, int j) {
        if(currentState == GameState.GAME) {
            game.weldComponentTile(player, i, j);
        } else {
            System.out.println("Player " + player + " cannot weld a component tile outside game state.");
        }
    }

    public void standbyComponentTile(String player) {
        if(currentState == GameState.GAME) {
            game.standbyComponentTile(player);
        } else {
            System.out.println("Player " + player + " cannot standby a component tile outside game state.");
        }
    }

    public void pickStandByComponentTile(String player, int index) {
        if(currentState == GameState.GAME) {
            game.pickStandByComponentTile(player, index);
        } else {
            System.out.println("Player " + player + " cannot pick a standby component tile outside game state.");
        }
    }

    public void discardComponentTile(String player) {
        if(currentState == GameState.GAME) {
            game.discardComponentTile(player);
        } else {
            System.out.println("Player " + player + " cannot discard a component tile outside game state.");
        }
    }

    public void finishBuilding(String player) {
        if(currentState == GameState.GAME) {
            game.finishBuilding(player);
        } else {
            System.out.println("Player " + player + " cannot finish building outside game state.");
        }
    }

    public void finishBuilding(String player, int pos) {
        if(currentState == GameState.GAME) {
            game.finishBuilding(player, pos);
        } else
            System.out.println("Player " + player + " cannot finish building outside game state.");{
        }
    }

    public void finishedAllShipboards() {
        if(currentState == GameState.GAME) {
            game.finishedAllShipboards();
        } else {
            System.out.println("Cannot check if all shipboards are finished outside game state.");
        }
    }

    public void flipHourglass() {
        if(currentState == GameState.GAME) {
            game.flipHourglass(() -> {
                //TO BE IMPLEMENTED
            });
        } else {
            System.out.println("Cannot flip hourglass outside game state.");
        }
    }

    public void pickCard() {
        if(currentState == GameState.GAME) {
            game.pickCard();
        } else {
            System.out.println("Cannot pick card outside game state.");
        }
    }

    public void activateCard(InputCommand inputCommand) {
        if(currentState == GameState.GAME) {
            game.activateCard(inputCommand);
        } else {
            System.out.println("Cannot activate card outside game state.");
        }
    }

    public void playerAbandons(String player) {
        if(currentState == GameState.GAME) {
            game.playerAbandons(player);
        } else {
            System.out.println("Player " + player + " cannot abandon outside game state.");
        }
    }

    public void destroyTile(String player, int i, int j) {
        if(currentState == GameState.GAME) {
            game.destroyTile(player, i, j);
        } else {
            System.out.println("Player " + player + " cannot destroy a tile outside game state.");
        }
    }

    public void setCurrPlayer(String player) {
        if(currentState == GameState.GAME) {
            game.setCurrPlayer(player);
        } else {
            System.out.println("Current player cannot be set outside game state.");
        }
    }

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
        if(currentState == GameState.GAME) {
            return game.getDeck();
        } else {
            System.out.println("Deck cannot be retrieved outside game state.");
        }
        return null;
    }

    public AdventureCard getCurrCard() {
        if(currentState == GameState.GAME) {
            return game.getCurrCard();
        } else {
            System.out.println("Current card cannot be retrieved outside game state.");
        }
        return null;
    }

    public String getLastPlayer() {
        if(currentState == GameState.GAME) {
            return game.getLastPlayer();
        } else {
            System.out.println("Last player cannot be retrieved outside game state.");
        }
        return null;
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
}