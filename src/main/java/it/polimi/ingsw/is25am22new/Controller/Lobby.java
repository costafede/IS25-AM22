package it.polimi.ingsw.is25am22new.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Lobby class manages a game lobby where players can join, set their ready status,
 * and configure game settings. The lobby ensures that the number of players conforms
 * to specific limits and tracks the readiness of all players before a game can be initiated.
 */
public class Lobby {
    private final List<String> players;
    private final Map<String, Boolean> readyStatus;
    private int maxPlayers;
    private String gameType;

    /**
     * Creates a new lobby with default settings:
     * maxPlayers = 4 and gameType = "tutorial".
     * Initializes empty player list and ready status map.
     */
    public Lobby() {
        this.players = new ArrayList<>();
        this.readyStatus = new HashMap<>();
        this.maxPlayers = 4; // Default max players
        this.gameType = "tutorial"; // Default game type
    }

    /**
     * Sets the maximum number of players allowed in the lobby.
     * Must be between 2 and 4 inclusive.
     *
     * @param maxPlayers the maximum number of players
     * @throws IllegalArgumentException if maxPlayers is not between 2 and 4
     */
    public void setMaxPlayers(int maxPlayers) {
        if(maxPlayers < 2 || maxPlayers > 4){
            throw new IllegalArgumentException("Max players must be between 2 and 4");
        } else {
            this.maxPlayers = maxPlayers;
        }
    }

    /**
     * Returns the current maximum number of players allowed in the lobby.
     *
     * @return the max players count
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Creates a lobby with a predefined list of players and their ready statuses.
     * Sets default game type to "tutorial".
     *
     * @param players list of player nicknames
     * @param readyStatus map of player nicknames to their ready status
     */
    public Lobby(List<String> players, Map<String, Boolean> readyStatus) {
        this.players = players;
        this.readyStatus = readyStatus;
        this.gameType = "tutorial"; // Default game type
    }

    /**
     * Attempts to add a player to the lobby.
     *
     * @param player the nickname of the player to add
     * @return  1 if the player was successfully added,
     *         -1 if the lobby is full,
     *         -2 if the player is already in the lobby
     */
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

    /**
     * Attempts to remove a player from the lobby.
     *
     * @param player the nickname of the player to remove
     * @return 1 if the player was removed,
     *        -1 if the player was not in the lobby
     */
    public int removePlayer(String player) {
        if(!players.contains(player)) {
            return -1; // Player not in lobby
        }
        players.remove(player);
        readyStatus.remove(player);
        return 1; //ok
    }

    /**
     * Marks the specified player as ready.
     *
     * @param player the player to mark ready
     */
    public void setPlayerReady(String player) {
        if(readyStatus.containsKey(player)) {
            readyStatus.put(player, true);
        }
    }

    /**
     * Marks the specified player as not ready.
     *
     * @param player the player to mark not ready
     */
    public void setPlayerNotReady(String player) {
        if(readyStatus.containsKey(player)) {
            readyStatus.put(player, false);
        }
    }

    /**
     * Checks if the lobby is full.
     *
     * @return true if the number of players is equal or greater than maxPlayers, false otherwise
     */
    public boolean isLobbyFull() {
        return players.size() >= maxPlayers;
    }


    /**
     * Checks if the lobby is ready to start the game.
     * The lobby is ready if it has at least 2 players, not more than maxPlayers,
     * and all players are marked as ready.
     *
     * @return true if lobby is ready, false otherwise
     */
    public boolean isLobbyReady() {
        //can be converted to a local variable
        int minPlayers = 2;
        return players.size() >= minPlayers && players.size() <= maxPlayers && readyStatus.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Sets the type of the game to be played in this lobby.
     *
     * @param gameType the game type (e.g., "tutorial", "level2")
     */
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * Returns a copy of the list of players currently in the lobby.
     *
     * @return a list of player nicknames
     */
    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Returns a copy of the map that tracks the ready status of each player.
     *
     * @return a map from player nickname to ready status
     */
    public Map<String, Boolean> getReadyStatus() {
        return new HashMap<>(readyStatus);
    }

    /**
     * Returns the current game type set for the lobby.
     *
     * @return the game type string
     */
    public String getGameType() {
        return gameType;
    }
}
