package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Lobby;

import java.util.List;
import java.util.Map;

public class LobbyController {
    private final Lobby lobby;

    public LobbyController(Lobby lobby) {
        this.lobby = lobby;
    }

    public int addPlayer(String player) {
        return lobby.addPlayer(player);
    }

    public int removePlayer(String player) {
        return lobby.removePlayer(player);
    }

    public void setPlayerReady(String player) {
        lobby.setPlayerReady(player);
    }

    public void setPlayerNotReady(String player) {
        lobby.setPlayerNotReady(player);
    }

    public boolean isLobbyFull() {
        return lobby.isLobbyFull();
    }

    public boolean isLobbyReady() {
        return lobby.isLobbyReady();
    }

    public void setGameType(String gameType) {
        lobby.setGameType(gameType);
    }

    public Game createGame() {
        return lobby.createGame();
    }

    public TutorialGameController startTutorialGame() {
        if(!isLobbyReady() || !"tutorial".equals(lobby.getGameType())) {
            return null;
        }

        return new TutorialGameController(lobby.getPlayers());
    }

    public List<String> getPlayers() {
        return lobby.getPlayers();
    }

    public Map<String, Boolean> getReadyStatus() {
        return lobby.getReadyStatus();
    }

    public String getGameType() {
        return lobby.getGameType();
    }

}
