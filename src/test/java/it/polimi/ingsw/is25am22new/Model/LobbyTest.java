package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Controller.Lobby;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void add_Player() {
        Lobby lobby = new Lobby();
        int expected = 0;
        expected = lobby.addPlayer("player1");
        assertEquals(1, expected);
        expected = lobby.addPlayer("player2");
        assertEquals(1, expected);
        expected = lobby.addPlayer("player3");
        assertEquals(1, expected);
        expected = lobby.addPlayer("player1");
        assertEquals(-2, expected); // Player already in lobby
        expected = lobby.addPlayer("player4");
        assertEquals(1, expected);
        expected = lobby.addPlayer("player5");
        assertEquals(-1, expected);// Lobby is full
        }

    @Test
    void remove_Player() {
        Lobby lobby = new Lobby();
        lobby.addPlayer("player1");
        lobby.addPlayer("player2");
        assertEquals(1, lobby.removePlayer("player1"));
        assertEquals(-1, lobby.removePlayer("player3")); // Player not in lobby
    }

    @Test
    void set_Player_Ready() {
        Lobby lobby = new Lobby();
        lobby.addPlayer("player1");
        lobby.setPlayerReady("player1");
        assertTrue(lobby.getReadyStatus().get("player1"));
    }

    @Test
    void set_Game_Type() {
        Lobby lobby = new Lobby();
        lobby.setGameType("advanced");
        assertEquals("advanced", lobby.getGameType());
    }

}