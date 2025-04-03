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

    private void setLobbyCreator(String player) {
        if(currentState == GameState.LOBBY) {
            this.lobbyCreator = player;
            System.out.println("Lobby creator set to " + player);
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
        }
        return -1; // Can't remove players outside lobby state
    }

    public void setPlayerReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobby.setPlayerReady(player);
            System.out.println("Player " + player + " is ready.");
            //checkAndStartGame();
        }
    }

    public boolean startGameByHost(String player) {
        if(currentState == GameState.LOBBY && player.equals(lobbyCreator)) {
            return checkAndStartGame();
        }
        return false;
    }

    public void setPlayerNotReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobby.setPlayerNotReady(player);
            System.out.println("Player " + player + " is not ready.");
        }
    }

    public void setGameType(String gameType) {
        if(currentState == GameState.LOBBY) {
            this.gameType = gameType;
            lobby.setGameType(gameType);
            System.out.println("Game type set to " + gameType);
        }
    }

    private boolean checkAndStartGame() {
        if(lobby.isLobbyReady()) {
            startGame();
            return true;
        }
        return false; // Not enough players or not all players are ready
    }

    private void startGame() {
        if("tutorial".equals(gameType)) {
            game = new TutorialGame(new ArrayList<>(lobby.getPlayers()));
        } else if("level2".equals(gameType)) {
            game = new Level2Game(new ArrayList<>(lobby.getPlayers()));
        } else {
            return; // Invalid game type
        }
        game.initGame();
        currentState = GameState.GAME;
        System.out.println(gameType + " game started.");
    }

    // Game methods
    public void pickCoveredTile(String player) {
        if(currentState == GameState.GAME) {
            game.pickCoveredTile(player);
        }
    }

    public void pickUncoveredTile(String player, int index) {
        if(currentState == GameState.GAME) {
            game.pickUncoveredTile(player, index);
        }
    }

    public void rotateClockwise(String player) {
        if(currentState == GameState.GAME) {
            game.rotateClockwise(player);
        }
    }

    public void rotateCounterClockwise(String player) {
        if(currentState == GameState.GAME) {
            game.rotateCounterClockwise(player);
        }
    }

    public void weldComponentTile(String player, int i, int j) {
        if(currentState == GameState.GAME) {
            game.weldComponentTile(player, i, j);
        }
    }

    public void standbyComponentTile(String player) {
        if(currentState == GameState.GAME) {
            game.standbyComponentTile(player);
        }
    }

    public void pickStandByComponentTile(String player, int index) {
        if(currentState == GameState.GAME) {
            game.pickStandByComponentTile(player, index);
        }
    }

    public void discardComponentTile(String player) {
        if(currentState == GameState.GAME) {
            game.discardComponentTile(player);
        }
    }

    public boolean finishBuilding(String player) {
        if(currentState == GameState.GAME) {
            return game.finishBuilding(player);
        }
        return false;
    }

    public boolean finishBuilding(String player, int pos) {
        if(currentState == GameState.GAME) {
            return game.finishBuilding(player, pos);
        }
        return false;
    }

    public boolean finishedAllShipboards() {
        if(currentState == GameState.GAME) {
            return game.finishedAllShipboards();
        }
        return false;
    }

    public void flipHourglass() {
        if(currentState == GameState.GAME) {
            game.flipHourglass(() -> {});
        }
    }

    public void pickCard() {
        if(currentState == GameState.GAME) {
            game.pickCard();
        }
    }

    public void activateCard(InputCommand inputCommand) {
        if(currentState == GameState.GAME) {
            game.activateCard(inputCommand);
        }
    }

    public void playerAbandons(String player) {
        if(currentState == GameState.GAME) {
            game.playerAbandons(player);
        }
    }

    public void destroyTile(String player, int i, int j) {
        if(currentState == GameState.GAME) {
            game.destroyTile(player, i, j);
        }
    }

    public void setCurrPlayer(String player) {
        if(currentState == GameState.GAME) {
            game.setCurrPlayer(player);
        }
    }

    public void setCurrPlayerToLeader() {
        if(currentState == GameState.GAME) {
            game.setCurrPlayerToLeader();
        }
    }

    public Map<String, Integer> endGame() {
        if(currentState == GameState.GAME) {
            System.out.println("Game ended.");
            return game.endGame();
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
        }
        return null;
    }

    public List<CardPile> getCardPiles() {
        if(currentState == GameState.GAME) {
            return game.getCardPiles();
        }
        return null;
    }

    public List<AdventureCard> getDeck() {
        if(currentState == GameState.GAME) {
            return game.getDeck();
        }
        return null;
    }

    public AdventureCard getCurrCard() {
        if(currentState == GameState.GAME) {
            return game.getCurrCard();
        }
        return null;
    }

    public String getLastPlayer() {
        if(currentState == GameState.GAME) {
            return game.getLastPlayer();
        }
        return null;
    }

    public List<String> getPlayers() {
        if(currentState == GameState.LOBBY) {
            return lobby.getPlayers();
        } else if(currentState == GameState.GAME) {
            return game.getPlayerList();
        }
        return null;
    }

    public Map<String, Boolean> getReadyStatus() {
        if(currentState == GameState.LOBBY) {
            return lobby.getReadyStatus();
        }
        return null;
    }

    public String getGameType() {
        if(currentState == GameState.LOBBY) {
            return lobby.getGameType();
        }
        return gameType;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}