package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketServerHandler;

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
    private int numPlayers = 0;
    private String gameType = null;
    private RmiClient rmiClient;
    private SocketServerHandler socketClient;
    private boolean autostart = false;
    private boolean gameStarted = false;
    private ClientModel clientModel;

    public LobbyView(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

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
        if (gameStarted || inGame) {
            return; // Evita stampe duplicate o successive
        }

        isHostPlayer = isHost;
        currentPlayerCount = players.size();
        this.gameType = gameType;

        // Check if the host setup is completed (game type is set)
        if (gameType != null && !gameType.isEmpty()) {
            hostSetupCompleted = true;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println(  "║                            LOBBY UPDATE                              ║");
        System.out.println(  "╠══════════════════════════════════════════════════════════════════════╣");
        String playersLine = String.format("        Players (%d%s)",
                players.size(),
                (numPlayers > 0 ? "/" + numPlayers : "")
        );
        String gameTypeStr = String.format("[Game Type] : %s",
                (gameType != null ? gameType : "Not set")
        );

        int contentWidth = playersLine.length() + gameTypeStr.length();
        int totalWidth = 60; // adjust based on your design
        int spacing = totalWidth - contentWidth;

        System.out.printf("║%s%s%s          ║\n",
                playersLine,
                " ".repeat(Math.max(0, spacing)),
                gameTypeStr
        );
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        for (String player : players) {
            String readyText = readyStatus.getOrDefault(player, false) ? "READY" : "NOT READY";
            System.out.printf("║        %-25s│  STATUS: %-9s                 ║\n", player, readyText);
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");

        if (isHost) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                 >>> YOU ARE THE HOST OF THIS LOBBY <<<               ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝\n");
        }



        // Auto-start when minimum player count (2) is reached
        if (numPlayers > 0 && currentPlayerCount == numPlayers && isHostPlayer &&
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
                        if(rmiClient != null) {
                            rmiClient.setPlayerReady(player);
                        } else {
                            socketClient.setPlayerReady(player);
                        }
                    }
                }

                // If everyone is ready, start the game
                if (allReady || players.size() >= 2) {
                    System.out.println("All players ready. Starting game...");
                    String hostName = players.getFirst();
                    if(rmiClient != null) {
                        rmiClient.startGameByHost(hostName);
                    } else {
                        socketClient.startGameByHost(hostName);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error starting game: " + e.getMessage());
            }

            // Reset the flag after a delay to allow for retries if needed
            new Thread(() -> {
                try {
                    Thread.sleep(3000);  // 3 second cooldowns
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

        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║         CONNECTION RESULT        ║");
        System.out.println("╠══════════════════════════════════╣");

        if (success) {
            System.out.println("║ ✅ Successfully connected!       ║");

            // Wrap the message if it's long in real use
            System.out.printf("║ %s%s║\n", message, " ".repeat(33 - message.length()));

            if (isHost) {
                System.out.println("║ 🛠️  As host, set up the game.    ║");
            } else {
                System.out.println("║ ⏳ Waiting for host setup...     ║");
            }
            System.out.println("╚══════════════════════════════════╝");
            nicknameValid = true;
        } else {
            System.out.println("║ ❌ Connection failed!            ║");

            // Wrap the failure message if needed
            System.out.printf("║ %s%s║\n", message, " ".repeat(33 - message.length()));

            System.out.println("╚══════════════════════════════════╝\n");
            System.exit(0);
        }

    }

    @Override
    public void displayNicknameResult(boolean valid, String errorMessage){
        if(valid){
            System.out.println("╔═════════════════════╗");
            System.out.println("║   NICKNAME RESULT   ║");
            System.out.println("╠═════════════════════╣");
            System.out.println("║  Nickname accepted  ║");
            System.out.println("╚═════════════════════╝");
            nicknameValid = true;
        } else {
            String message = "Nickname error: " + errorMessage;
            int length = message.length();
            String horizontalLine = "═".repeat(length + 4);

            System.out.println("╔" + horizontalLine + "╗");
            System.out.println("║  NICKNAME RESULT" + " ".repeat(Math.max(0, length + 4 - 17)) + "║");
            System.out.println("╠" + horizontalLine + "╣");
            System.out.println("║  " + message + "  ║");
            System.out.println("╚" + horizontalLine + "╝");
            nicknameValid = false;
        }
    }

    @Override
    public void displayGameStarted() {
        if (clientModel != null) {
            System.out.println("\n\n");

            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║  🚀🚀🚀🚀        GAME STARTED - WELCOME TO:        🚀🚀🚀🚀  ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            System.out.println(" ______   ______   __       ______   __  __   __  __       ");
            System.out.println("/\\  ___\\ /\\  __ \\ /\\ \\     /\\  __ \\ /\\_\\_\\_\\ /\\ \\_\\ \\      ");
            System.out.println("\\ \\ \\__ \\\\ \\  __ \\\\ \\ \\____\\ \\  __ \\ /_/\\_\\/_\\ \\____ \\     ");
            System.out.println(" \\ \\_____\\\\ \\_\\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\ /\\_\\/\\_\\\\ \\_____\\    ");
            System.out.println("  \\/_____/ \\/_/\\/_/ \\/_____/ \\/_/\\/_/ \\/_/\\/_/ \\/_____/    ");
            System.out.println("  ______  ______   __  __   ______   __  __   ______   ______    ");
            System.out.println(" /\\__  _\\/\\  == \\ /\\ \\/\\ \\ /\\  ___\\ /\\ \\/ /  /\\  ___\\ /\\  == \\   ");
            System.out.println(" \\/_/\\ \\/\\ \\  __< \\ \\ \\_\\ \\\\ \\ \\____\\ \\  _\"-.\\ \\  __\\ \\ \\  __<   ");
            System.out.println("    \\ \\_\\ \\ \\_\\ \\_\\\\ \\_____\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\ ");
            System.out.println("     \\/_/  \\/_/ /_/ \\/_____/ \\/_____/ \\/_/\\/_/ \\/_____/ \\/_/ /_/ ");
            System.out.println("\n══════════════════════════════════════════════════════════════════\n");

            inGame = true;

            clientModel.setGameStartMessageReceived(true);
        } else {
            System.out.println("client model in lobbyview is null.");
        }
        // displayCurrentCommands();
    }



    @Override
    public void displayPlayerJoined(String playerName) {
        System.out.println("\n>>> " + playerName + " has joined the lobby! <<<\n");
        currentPlayerCount++;

        // Auto-start if max player count reached
        if (numPlayers > 0 && currentPlayerCount >= numPlayers && isHostPlayer) {
            System.out.println("Maximum number of players reached. Starting game automatically...");
        }

        //displayCurrentCommands();
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
    public void startCommandLoopRMI(RmiClient client, String playerName, Scanner scanner) {
        boolean running = true;
        this.rmiClient = client;

        // If this is the host player, handle host setup
        if (isHostPlayer) {
            setupAsHostRMI(client, scanner);
        } else {
            // Non-host players just wait
            if(gameStarted || inGame){
                return;
            }
            System.out.println("Waiting for other players to join...");
            System.out.println("Game will start automatically when all players have joined.");
        }
//
//        while (running && !inGame && !gameStarted) {
//            System.out.println("Waiting for more players to join...");
//            System.out.println("Current players: " + currentPlayerCount +
//                    (numPlayers > 0 ? "/" + numPlayers : ""));
//            System.out.println("Type 'exit' to leave the lobby.");
//            System.out.print("> ");
//
//            // Use scanner with timeout to avoid blocking indefinitely
//            try {
//                if (scanner.hasNextLine()) {
//                    String command = scanner.nextLine().trim();
//
//                    if (command.equals("exit")) {
//                        running = false;
//                        client.playerAbandons(playerName);
//                    }
//                } else {
//                    // No input available, sleep briefly then check game status
//
//                    if (gameStarted || inGame) {
//                        break;
//                    }
//                    Thread.sleep(100);
//                }
//            } catch (Exception e) {
//                System.err.println("Error in command loop: " + e.getMessage());
//            }
//
//            // Break the loop if game started during this iteration
//            if (gameStarted || inGame) {
//                break;
//            }
//        }
    }

    public void startCommandLoopSocket(SocketServerHandler client, String playerName, Scanner scanner) {
        this.socketClient = client;
        boolean running = true;

        // If this is the host player, handle host setup
        if (isHostPlayer) {
            setupAsHostSocket(client, scanner);
        } else {
            // Non-host players just wait
            if(gameStarted){
                return;
            }
            System.out.println("Waiting for other players to join...");
            System.out.println("Game will start automatically when all players have joined.");
        }

        while (running && !inGame) {
            String command = scanner.nextLine().trim();

            try {
                if (command.equals("exit")) {
                    running = false;
                    socketClient.disconnect();
                } else {
                    // Refresh the lobby status
                    System.out.println("Waiting for more players to join...");
                    System.out.println("Current players: " + currentPlayerCount +
                            (numPlayers > 0 ? "/" + numPlayers : ""));
                    System.out.println("Type 'exit' to leave the lobby.");
                    System.out.print("> ");
                }
            } catch (IOException e) {
                System.err.println("Error executing command: " + e.getMessage());
            }
        }
    }

    private void setupAsHostSocket(SocketServerHandler client, Scanner scanner) {
        // Get max players
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                  Enter number of players (2-4)                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.print("➤ ");

        while (numPlayers < 2 || numPlayers > 4) {
            try {
                numPlayers = Integer.parseInt(scanner.nextLine().trim());

                if (numPlayers < 2 || numPlayers > 4) {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║      Invalid input. Please enter a number between 2 and 4.           ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                System.out.println("║      Invalid input. Please enter a number between 2 and 4.           ║");
                System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                System.out.print("➤ ");
            }
        }

        client.setNumPlayers(numPlayers);

        // Get game type
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║             Select game type:             ║");
        System.out.println("║               1. Tutorial                 ║");
        System.out.println("║               2. Level 2                  ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.print("➤ ");
        int typeChoice = 0;
        while (typeChoice != 1 && typeChoice != 2) {
            try {
                typeChoice = Integer.parseInt(scanner.nextLine().trim());
                if (typeChoice != 1 && typeChoice != 2) {
                    System.out.println("\n╔════════════════════════════════════════════════╗");
                    System.out.println("║     Invalid choice. Please enter 1 or 2.       ║");
                    System.out.println("╚════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n╔════════════════════════════════════════════════════════╗");
                System.out.println("║     Invalid input. Please enter a number (1 or 2).     ║");
                System.out.println("╚════════════════════════════════════════════════════════╝");
                System.out.print("➤ ");
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

    }

    private void setupAsHostRMI(RmiClient client, Scanner scanner) {
        try {
            // Get max players
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                  Enter number of players (2-4)                       ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
            System.out.print("➤ ");

            while (numPlayers < 2 || numPlayers > 4) {
                try {
                    numPlayers = Integer.parseInt(scanner.nextLine().trim());

                    if (numPlayers < 2 || numPlayers > 4) {
                        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                        System.out.println("║      Invalid input. Please enter a number between 2 and 4.           ║");
                        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                        System.out.print("➤ ");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║      Invalid input. Please enter a number between 2 and 4.           ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
                }
            }

            client.setNumPlayers(numPlayers);

            // Get game type
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║             Select game type:             ║");
            System.out.println("║               1. Tutorial                 ║");
            System.out.println("║               2. Level 2                  ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.print("➤ ");
            int typeChoice = 0;
            while (typeChoice != 1 && typeChoice != 2) {
                try {
                    typeChoice = Integer.parseInt(scanner.nextLine().trim());
                    if (typeChoice != 1 && typeChoice != 2) {
                        System.out.println("\n╔═══════════════════════════════════════════════╗");
                        System.out.println("║     Invalid choice. Please enter 1 or 2.       ║");
                        System.out.println("╚════════════════════════════════════════════════╝");
                        System.out.print("➤ ");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n╔═══════════════════════════════════════════════════════╗");
                    System.out.println("║     Invalid input. Please enter a number (1 or 2).     ║");
                    System.out.println("╚════════════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
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

    public String getGameType() {
        return gameType;
    }
}