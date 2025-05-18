package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketServerHandler;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This interface defines the contract for a user interface component
 * responsible for providing an enhanced view for the client in a game.
 * It includes methods for updating the UI based on game and connection state,
 * as well as managing player-related information.
 */
public interface EnhancedClientView {
    void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost);
    void displayConnectionResult(boolean isHost, boolean success, String message);
    void displayNicknameResult(boolean valid, String message);
    void displayGameStarted();
    void displayGame(Game game);
    void displayPlayerJoined(String playerName);
    boolean isNicknameValid();
    void resetNicknameStatus();
}