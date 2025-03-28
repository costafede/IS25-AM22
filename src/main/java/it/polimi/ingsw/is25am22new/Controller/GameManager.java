package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Lobby;

public class GameManager {
    private Lobby lobby;
    private LobbyController lobbyController;
    private Game currentGame;
    private Level2Controller level2Controller;
    private TutorialGameController tutorialGameController;

    public enum GameState {
        LOBBY,
        TUTORIAL,
        LEVEL2
    }

    private GameState currentState;

    public GameManager() {
        this.lobby = new Lobby();
        this.lobbyController = new LobbyController(lobby);
        this.currentState = GameState.LOBBY;
    }

    public GameManager(Lobby lobby) {
        this.lobby = lobby;
        this.lobbyController = new LobbyController(lobby);
        this.currentState = GameState.LOBBY;
    }

    public int addPlayer(String player) {
        if(currentState == GameState.LOBBY) {
            return lobbyController.addPlayer(player);
        }
        return -1; // Can't remove players outside lobby state
    }

    public int removePlayer(String player) {
        if(currentState == GameState.LOBBY) {
            return lobbyController.removePlayer(player);
        }
        return -1; // Can't remove players outside lobby state
    }

    public void setPlayerReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobbyController.setPlayerReady(player);
            System.out.println("Player " + player + " is ready.");
            checkAndStartGame();
        }
    }

    public void setPlayerNotReady(String player) {
        if(currentState == GameState.LOBBY) {
            lobbyController.setPlayerNotReady(player);
            System.out.println("Player " + player + " is not ready.");
        }
    }

    public void setGameType(String gameType) {
        if(currentState == GameState.LOBBY) {
            lobbyController.setGameType(gameType);
            System.out.println("Game type set to " + gameType);
        }
    }

    private void checkAndStartGame() {
        if(lobbyController.isLobbyReady()) {
            String gameType = lobbyController.getGameType();
            if("tutorial".equals(gameType)) {
                startTutorialGame();
            } else if("level2".equals(gameType)) {
                startLevel2Game();
            }
        }
    }

    private void startTutorialGame() {
        tutorialGameController = lobbyController.startTutorialGame();
        if(tutorialGameController != null) {
            currentState = GameState.TUTORIAL;
            System.out.println("Tutorial game started.");
        }
    }

    private void startLevel2Game() {
        currentGame = lobbyController.createGame();
        if(currentGame != null) {
            level2Controller = new Level2Controller(currentGame);
            level2Controller.initGame();
            currentState = GameState.LEVEL2;
            System.out.println("Level2 game started.");
        }
    }

    public void endGame() {
        if(currentState == GameState.TUTORIAL) {
            tutorialGameController.endGame();
        } else if(currentState == GameState.LEVEL2) {
            level2Controller.endGame();
        }

        lobby = new Lobby();
        lobbyController = new LobbyController(lobby);
        currentState = GameState.LOBBY;
        System.out.println("Game ended, returned to lobby.");
    }

    public TutorialGameController getTutorialGameController() {
        if(currentState == GameState.TUTORIAL) {
            return tutorialGameController;
        }
        return null;
    }

    public Level2Controller getLevel2Controller() {
        if(currentState == GameState.LEVEL2) {
            return level2Controller;
        }
        return null;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}

