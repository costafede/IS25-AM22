package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Model.Games.Game;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface EnhancedClientView extends ClientView {
    void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost);
    void displayConnectionResult(boolean isHost, boolean success, String message);
    void displayNicknameResult(boolean valid, String message);
    void displayGameStarted();
    void displayGame(Game game);
    void displayPlayerJoined(String playerName);
    void startCommandLoop(RmiClient client, String playerName, Scanner scanner);
    boolean isNicknameValid();
    void resetNicknameStatus();
}