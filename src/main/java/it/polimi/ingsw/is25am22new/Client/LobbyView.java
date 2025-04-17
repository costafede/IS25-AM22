package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LobbyView implements EnhancedClientView {
    private boolean inGame = false;
    private boolean nicknameValid = false;
    private boolean isHostPlayer = false;
    private boolean hostSetupCompleted = false;
    private int currentPlayerCount = 0;
    private int maxPlayers = 0;
    private String gameType = null;
    private RmiClient rmiClient;
    private boolean autostart = false;

    public boolean isNicknameValid() {
        return nicknameValid;
    }

    public void resetNicknameStatus(){
        nicknameValid = false;
    }

    public void displayGame(Game game){
        System.out.println("Game update received");
        System.out.println(game.getPlayerList());
        System.out.println(game.getGamePhase());
        System.out.println(game.getFlightboard());
        if(game.getShipboards().get(game.getPlayerList().getFirst()).getComponentTilesGridCopy(3, 2) == null){
            System.out.println("3, 2 is empty");
        }
        System.out.println("TEST PASSED");
    }

    //@Override
    //public void displayBank(Bank bank) {
    //    System.out.println("Bank update received");
    //    displayCurrentCommands();
    //}
    //
    //@Override
    //public void displayTileInHand(String player, ComponentTile tile) {
    //    if(tile == null){
    //        System.out.println("No tile in hand");
    //        displayCurrentCommands();
    //        return;
    //    }
//
    //    System.out.println("\n=== TILE IN HAND ===");
    //    System.out.println("Player: " + player);
    //    System.out.println("Tile type: " + tile.getClass().getSimpleName());
    //    System.out.println("Sides:\n " +
    //            "- Top -> " + tile.getTopSide() + "\n " +
    //            "- Right -> " + tile.getRightSide() + "\n " +
    //            "- Bottom -> " + tile.getBottomSide() + "\n " +
    //            "- Left -> " + tile.getLeftSide() + "\n");
//
    //    System.out.println("=================\n");
    //}
//
    //@Override
    //public void displayUncoveredComponentTiles(List<ComponentTile> tiles) {
    //    System.out.println("Uncovered component tile update received");
    //    displayCurrentCommands();
    //}
//
    //@Override
    //public void displayShipboard(String player, Shipboard shipboard) {
    //    System.out.println("Shipboard update received");
    //    displayCurrentCommands();
    //}
//
    //@Override
    //public void displayFlightboard(Flightboard flightboard) {
    //    System.out.println("Flightboard update received");
    //    displayCurrentCommands();
    //}
//
    //@Override
    //public void displayCurrentCard(AdventureCard card) {
    //    System.out.println("Current card update received");
    //    displayCurrentCommands();
    //}
//
    //@Override
    //public void displayDices(Dices dices) {
    //    System.out.println("Dices update received");
    //    displayCurrentCommands();
    //}
//
    //@Override
    //public void displayCurrentPlayer(String currPlayer) {
    //    System.out.println("Current player: " + currPlayer);
    //    displayCurrentCommands();
    //}

    @Override
    public void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost) {
        isHostPlayer = isHost;
        currentPlayerCount = players.size();
        this.gameType = gameType;

        // Check if host setup is completed (game type is set)
        if (gameType != null && !gameType.isEmpty()) {
            hostSetupCompleted = true;
        }

        System.out.println("\n=== LOBBY UPDATE ===");
        System.out.println("Players (" + players.size() + (maxPlayers > 0 ? "/" + maxPlayers : "") + "):");
        for (String player : players) {
            System.out.println("  " + player + " - " + (readyStatus.getOrDefault(player, false) ? "READY" : "NOT READY"));
        }
        System.out.println("Game Type: " + (gameType != null ? gameType : "Not set"));
        if (isHost) {
            System.out.println("You are the HOST of this lobby");
        }
        System.out.println("===================\n");

        // Auto-start when minimum player count (2) is reached
        if (maxPlayers > 0 && currentPlayerCount >= 2 && isHostPlayer &&
                hostSetupCompleted && !autostart && !inGame) {

            System.out.println("Sufficient players joined. Starting game automatically...");
            autostart = true; // Prevent multiple auto-start attempts

            try {
                // Make sure all players are ready
                boolean allReady = true;
                for (String player : players) {
                    if (!readyStatus.getOrDefault(player, false)) {
                        allReady = false;
                        System.out.println("Setting player " + player + " as ready...");
                        rmiClient.setPlayerReady(player);
                    }
                }

                // If everyone is ready, start the game
                if (allReady || players.size() >= 2) {
                    System.out.println("All players ready. Starting game...");
                    String hostName = players.getFirst();
                    rmiClient.startGameByHost(hostName);
                }
            } catch (Exception e) {
                System.err.println("Error starting game: " + e.getMessage());
            }

            // Reset the flag after a delay to allow for retries if needed
            new Thread(() -> {
                try {
                    Thread.sleep(3000);  // 3 second cooldown
                    autostart = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    @Override
    public void displayConnectionResult(boolean isHost, boolean success, String message) {
        isHostPlayer = isHost;

        System.out.println("\n=== CONNECTION RESULT ===");
        if (success) {
            System.out.println("Successfully connected to the game!");
            System.out.println(message);
            if (isHost) {
                System.out.println("As the host, you need to set up the game parameters.");
            } else {
                System.out.println("Waiting for the host to complete setup...");
            }
            nicknameValid = true;
        } else {
            System.out.println("Connection failed: " + message);
            System.out.println("=========================");
            System.exit(0);
        }
    }

    @Override
    public void displayNicknameResult(boolean valid, String message){
        System.out.println("\n=== NICKNAME RESULT ===");
        if(valid){
            System.out.println("Nickname accepted!");
            nicknameValid = true;
        }else{
            System.out.println("Nickname error: " + message);
            nicknameValid = false;
        }
        System.out.println("======================\n");
    }

    @Override
    public void displayGameStarted() {
        System.out.println("\n🚀 GAME STARTED! 🚀");
        System.out.println("Welcome to Galaxy Trucker!");
        inGame = true;
        displayCurrentCommands();
    }

    @Override
    public void displayPlayerJoined(String playerName) {
        System.out.println("\n>>> " + playerName + " has joined the lobby! <<<\n");
        currentPlayerCount++;

        // Auto-start if max player count reached
        if (maxPlayers > 0 && currentPlayerCount >= maxPlayers && isHostPlayer) {
            System.out.println("Maximum number of players reached. Starting game automatically...");
        }

        displayCurrentCommands();
    }

    private void displayCurrentCommands() {
        if(!inGame){
            if (isHostPlayer) {
                System.out.println("Host Commands: setmax [number], gametype tut, gametype lvl2, ready, unready, start, exit");
            } else {
                System.out.println("Commands: ready, unready, exit");
            }
        } else {
            System.out.println("Game commands: [Enter number for commands]");
            System.out.println("6: Pick covered tile | 7: Pick uncovered tile | 8: Weld component tile | 9: Standby component tile | 10: Pick standby component tile | 11: Discard component tile \n12: Finish building | 13: Finish building with index | 14: Finished all shipboards | 15: Flip hourglass \n16: Pick card | 17: Activate card | 18: Remove player | 19: Destroy component tile \n20: Abandon game | 21: End game");
        }
        System.out.print("> ");
    }

    @Override
    public void startCommandLoop(RmiClient client, String playerName, Scanner scanner) {
        boolean running = true;
        this.rmiClient = client;

        // If this is the host player, handle host setup
        if (isHostPlayer) {
            setupAsHost(client, scanner);
        } else {
            // Non-host players just wait
            System.out.println("Waiting for other players to join...");
            System.out.println("Game will start automatically when all players have joined.");
        }

        while (running && !inGame) {
            String command = scanner.nextLine().trim();

            try {
                if (command.equals("exit")) {
                    running = false;
                    client.playerAbandons(playerName);
                } else {
                    // Just refresh the lobby status
                    System.out.println("Waiting for more players to join...");
                    System.out.println("Current players: " + currentPlayerCount +
                            (maxPlayers > 0 ? "/" + maxPlayers : ""));
                    System.out.println("Type 'exit' to leave the lobby.");
                    System.out.print("> ");
                }
            } catch (IOException e) {
                System.err.println("Error executing command: " + e.getMessage());
            }
        }
    }

    private void setupAsHost(RmiClient client, Scanner scanner) {
        try {
            // Get max players
            System.out.print("Enter maximum number of players (2-4): ");
            while (maxPlayers < 2 || maxPlayers > 4) {
                try {
                    maxPlayers = Integer.parseInt(scanner.nextLine().trim());
                    if (maxPlayers < 2 || maxPlayers > 4) {
                        System.out.print("Please enter a number between 2 and 4: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please enter a number between 2 and 4: ");
                }
            }

            // Get game type
            System.out.println("Select game type:");
            System.out.println("1. Tutorial");
            System.out.println("2. Level 2");
            System.out.print("Enter your choice (1 or 2): ");
            int typeChoice = 0;
            while (typeChoice != 1 && typeChoice != 2) {
                try {
                    typeChoice = Integer.parseInt(scanner.nextLine().trim());
                    if (typeChoice != 1 && typeChoice != 2) {
                        System.out.print("Invalid choice. Please enter 1 or 2: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please enter a number (1 or 2): ");
                }
            }

            // Set game type
            if (typeChoice == 1) {
                client.setGameType("tutorial");
                gameType = "tutorial";
            } else {
                client.setGameType("level2");
                gameType = "level2";
            }

            hostSetupCompleted = true;
            System.out.println("Lobby setup complete. Waiting for players to join...");

        } catch (IOException e) {
            System.err.println("Error setting up lobby: " + e.getMessage());
        }
    }

    private void waitForHostSetup() {
        System.out.println("Waiting for host to complete setup...");
        while (!hostSetupCompleted) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while waiting for host setup");
                break;
            }
        }
        System.out.println("Host has completed setup. Game type: " + gameType);
    }
}