package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.Games.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    private final List<String> players;
    private final Map<String, Boolean> readyStatus;
    private final int minPlayers = 2;
    private final int maxPlayers = 4;
    private String gameType;

    public Lobby() {
        this.players = new ArrayList<>();
        this.readyStatus = new HashMap<>();
        this.gameType = "tutorial"; // Default game type
    }

    public Lobby(List<String> players, Map<String, Boolean> readyStatus) {
        this.players = players;
        this.readyStatus = readyStatus;
        this.gameType = "tutorial"; // Default game type
    }

    public int addPlayer(String player) {
        if(players.size() >= maxPlayers) {
            return -1; // Lobby is full
        }
        if(players.contains(player)) {
            return -2; // Player already in lobby
        }
        players.add(player);
        readyStatus.put(player, false);
        return 1; //ok
    }

    public int removePlayer(String player) {
        if(!players.contains(player)) {
            return -1; // Player not in lobby
        }
        players.remove(player);
        readyStatus.remove(player);
        return 1; //ok
    }

    public void setPlayerReady(String player) {
        if(readyStatus.containsKey(player)) {
            readyStatus.put(player, true);
        }
    }

    public void setPlayerNotReady(String player) {
        if(readyStatus.containsKey(player)) {
            readyStatus.put(player, false);
        }
    }

    public boolean isLobbyFull() {
        return players.size() >= maxPlayers;
    }

    public boolean isLobbyReady() {
        return players.size() >= minPlayers && readyStatus.values().stream().allMatch(Boolean::booleanValue);
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Game createGame() {
        if(!isLobbyReady()){
            return null; // Lobby is not ready
        }

        List<String> playersCopy = new ArrayList<>(players);
        return switch (gameType) {
            case "tutorial" -> new TutorialGame(playersCopy);
            case "level2" -> new Level2Game(playersCopy);
            default -> null; // Invalid game type
        };
    }

    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public Map<String, Boolean> getReadyStatus() {
        return new HashMap<>(readyStatus);
    }

    public String getGameType() {
        return gameType;
    }
}
