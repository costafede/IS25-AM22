package it.polimi.ingsw.is25am22new.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
    }

    @Test
    void test_Add_Remove_Players() {
        assertEquals(1, gameManager.addPlayer("player1"));
        assertEquals(1, gameManager.addPlayer("player2"));
        assertEquals(-2, gameManager.addPlayer("player1")); // Already in lobby
        assertEquals(1, gameManager.removePlayer("player1"));
        assertEquals(-1, gameManager.removePlayer("player3")); // Not in lobby
    }

    @Test
    void test_Tutorial_Game_Start() {
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setGameType("tutorial");
        gameManager.setPlayerReady("player1");
        gameManager.setPlayerReady("player2");

        // Game should have started
        assertEquals(GameManager.GameState.TUTORIAL, gameManager.getCurrentState());
        assertNotNull(gameManager.getTutorialGameController());
    }

    @Test
    void test_Level2_Game_Start() {
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setGameType("level2");
        gameManager.setPlayerReady("player1");
        gameManager.setPlayerReady("player2");

        // Game should have started
        assertEquals(GameManager.GameState.LEVEL2, gameManager.getCurrentState());
        assertNotNull(gameManager.getLevel2Controller());
    }

    @Test
    void test_End_Game() {
        // Setup and start a game
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setGameType("tutorial");
        gameManager.setPlayerReady("player1");
        gameManager.setPlayerReady("player2");

        // End the game
        gameManager.endGame();

        // Should be back in lobby state
        assertEquals(GameManager.GameState.LOBBY, gameManager.getCurrentState());
    }

    @Test
    void test_Operations_In_Non_Lobby_State() {
        // Start a game first
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setPlayerReady("player1");
        gameManager.setPlayerReady("player2");

        // Now in tutorial state
        assertEquals(-1, gameManager.addPlayer("player3")); // Should fail
        assertEquals(-1, gameManager.removePlayer("player1")); // Should fail
    }

    @Test
    void test_Player_Readiness() {
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setPlayerReady("player1");

        // Only one player ready, game shouldn't start
        assertEquals(GameManager.GameState.LOBBY, gameManager.getCurrentState());

        // Make player not ready and ready
        gameManager.setPlayerNotReady("player1");
        gameManager.setPlayerReady("player2");

        // Still should be in lobby
        assertEquals(GameManager.GameState.LOBBY, gameManager.getCurrentState());
    }

    @Test
    void test_Controller_Access() {
        // Start with lobby controller
        assertNotNull(gameManager.getLobbyController());

        // Start tutorial game
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setPlayerReady("player1");
        gameManager.setPlayerReady("player2");

        // In tutorial state, only tutorial controller should be available
        assertNotNull(gameManager.getTutorialGameController());
        assertNull(gameManager.getLevel2Controller());
    }

    @Test
    void test_Game_Doesnt_Start_Without_Ready_Players() {
        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setGameType("tutorial");
        gameManager.setPlayerReady("player1");
        // Only one player ready, game shouldn't start
        assertEquals(GameManager.GameState.LOBBY, gameManager.getCurrentState());

        // Make player not ready
        gameManager.setPlayerNotReady("player1");
        assertEquals(GameManager.GameState.LOBBY, gameManager.getCurrentState());
    }

    @Test
    void test_restrictions_from_state() {

        gameManager.addPlayer("player1");
        gameManager.addPlayer("player2");
        gameManager.setPlayerReady("player1");
        gameManager.setPlayerReady("player2");

        // Now operations should be restricted
        assertEquals(-1, gameManager.addPlayer("player3"));
        assertEquals(-1, gameManager.removePlayer("player1"));
    }

}