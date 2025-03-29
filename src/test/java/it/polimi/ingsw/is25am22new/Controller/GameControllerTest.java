package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    @Test
    void testInitialState() {
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());
        assertNull(gameController.getGame());
    }

    @Test
    void testAddRemovePlayers() {
        // Add players
        assertEquals(1, gameController.addPlayer("Player1"));
        assertEquals(1, gameController.addPlayer("Player2"));

        // Check players were added
        assertEquals(2, gameController.getPlayers().size());
        assertTrue(gameController.getPlayers().contains("Player1"));
        assertTrue(gameController.getPlayers().contains("Player2"));

        // Try to add duplicate player
        assertEquals(-2, gameController.addPlayer("Player1"));

        // Remove player
        assertEquals(1, gameController.removePlayer("Player2"));
        assertFalse(gameController.getPlayers().contains("Player2"));
        assertEquals(1, gameController.getPlayers().size());

        // Try to remove non-existent player
        assertEquals(-1, gameController.removePlayer("NonExistentPlayer"));

        // Try to add more than max players
        gameController.addPlayer("Player2");
        gameController.addPlayer("Player3");
        gameController.addPlayer("Player4");
        assertEquals(-1, gameController.addPlayer("Player5")); // Should return -1 (lobby full)
        assertEquals(4, gameController.getPlayers().size()); // Should still be 4 players
    }

    @Test
    void testLobbyCreator(){
        // Add players - first player should be the lobby creator
        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");

        // Check lobby creator
        assertEquals("Player1", gameController.getLobbyCreator());

        // Set players ready
        gameController.setPlayerReady("Player1");
        gameController.setPlayerReady("Player2");

        // Game should NOT start automatically when all players are ready
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());

        // Non-creator can't start the game
        assertFalse(gameController.startGameByHost("Player2"));
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());

        // Player1 should be the lobby creator and able to start the game
        assertTrue(gameController.startGameByHost("Player1"));
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());

        //reinitialize game
        gameController.reinitializeGame();
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());
        assertNull(gameController.getGame());
        assertEquals(0, gameController.getPlayers().size());
        assertNull(gameController.getLobbyCreator());
        assertEquals(0, gameController.getReadyStatus().size());

        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");
        gameController.setPlayerReady("Player1");
        gameController.setPlayerReady("Player2");
        gameController.setGameType("level2");
        assertTrue(gameController.startGameByHost("Player1"));
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());
        assertNotNull(gameController.getGame());
        assertEquals("level2", gameController.getGameType());
    }

    @Test
    void testLonelyPlayerLeaves() {
        // Add a player
        gameController.addPlayer("Player1");
        assertEquals("Player1", gameController.getLobbyCreator());
        assertEquals(1, gameController.getPlayers().size());

        // Remove the player
        assertEquals(1, gameController.removePlayer("Player1"));

        // Verify game is reinitialized
        assertEquals(0, gameController.getPlayers().size());
        assertNull(gameController.getLobbyCreator());
        assertNull(gameController.getGame());
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());
        assertEquals("tutorial", gameController.getGameType()); // Should reset to default
        assertEquals(0, gameController.getReadyStatus().size());
    }

    @Test
    void testLobbyCreatorTransfer() {
        // Add players
        gameController.addPlayer("Player1"); // First player is automatically the creator
        gameController.addPlayer("Player2");
        gameController.addPlayer("Player3");

        // Verify Player1 is the creator
        assertEquals("Player1", gameController.getLobbyCreator());

        // Remove the creator
        gameController.removePlayer("Player1");

        // Creator role should transfer to Player2
        assertEquals("Player2", gameController.getLobbyCreator());

        // Verify Player2 can now start the game
        gameController.setPlayerReady("Player2");
        gameController.setPlayerReady("Player3");
        assertTrue(gameController.startGameByHost("Player2"));
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());
    }

    @Test
    void testPlayerReadyStatus() {
        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");

        // Check initial ready status
        Map<String, Boolean> readyStatus = gameController.getReadyStatus();
        assertFalse(readyStatus.get("Player1"));
        assertFalse(readyStatus.get("Player2"));

        // Set player ready
        gameController.setPlayerReady("Player1");
        readyStatus = gameController.getReadyStatus();
        assertTrue(readyStatus.get("Player1"));
        assertFalse(readyStatus.get("Player2"));

        // Set player not ready
        gameController.setPlayerNotReady("Player1");
        readyStatus = gameController.getReadyStatus();
        assertFalse(readyStatus.get("Player1"));
    }

    @Test
    void testGameTypeSelection() {

        // Test default game type
        assertEquals("tutorial", gameController.getGameType());

        gameController.setGameType("tutorial");
        assertEquals("tutorial", gameController.getGameType());

        gameController.setGameType("level2");
        assertEquals("level2", gameController.getGameType());
    }

    @Test
    void testGameStartTutorial() {
        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");
        //gameController.setGameType("tutorial");

        // Game shouldn't start until all players are ready
        gameController.setPlayerReady("Player1");
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());

        // All players ready, game should start
        gameController.setPlayerReady("Player2");
        gameController.startGameByHost("Player1");
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());
        assertNotNull(gameController.getGame());
    }

    @Test
    void testGameStartLevel2() {
        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");
        gameController.setGameType("level2");

        gameController.setPlayerReady("Player1");
        // Game shouldn't start until all players are ready
        gameController.setPlayerReady("Player1");
        gameController.startGameByHost("Player1");
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());


        gameController.setPlayerReady("Player2");
        gameController.startGameByHost("Player1");
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());
        assertNotNull(gameController.getGame());
    }

    @Test
    void testGameFunctions() {
        // Setup game
        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");
        gameController.addPlayer("Player3");
        gameController.setPlayerReady("Player1");
        gameController.setPlayerReady("Player2");
        gameController.setPlayerReady("Player3");

        // Start game
        gameController.startGameByHost("Player1");

        // Test game is initialized
        Game game = gameController.getGame();
        assertNotNull(game);

        // Test setting current player
        gameController.setCurrPlayer("Player1");
        assertEquals("Player1", game.getCurrPlayer());

        // Test ending the game
        Map<String, Integer> score = gameController.endGame();
        assertNotNull(score);
        assertEquals(3, score.size());
        assertTrue(score.containsKey("Player1"));
        assertTrue(score.containsKey("Player2"));
        assertTrue(score.containsKey("Player3"));
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());


        // Test reinitializing
        gameController.reinitializeGame();
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());
        assertNull(gameController.getGame());
    }

    @Test
    void testReinitializeGame() {
        // Setup and start game
        gameController.addPlayer("Player1");
        gameController.addPlayer("Player2");
        gameController.setGameType("tutorial");
        gameController.setPlayerReady("Player1");
        gameController.setPlayerReady("Player2");
        gameController.startGameByHost("Player1");

        // Verify game started
        assertEquals(GameController.GameState.GAME, gameController.getCurrentState());

        // Reinitialize
        gameController.reinitializeGame();

        // Verify back to lobby state
        assertEquals(GameController.GameState.LOBBY, gameController.getCurrentState());
        assertEquals(0, gameController.getPlayers().size());
        assertNull(gameController.getGame());
    }
}