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

/**
 * The LobbyView class represents the view component of a game's multiplayer lobby.
 * It handles the display of events and updates related to the lobby,
 * such as player connections, readiness status, and game startup.
 * It also manages commands and interactions between the user and
 * the underlying client model for both RMI and socket-based connections.
 *
 * Fields:
 * - inGame: Indicates whether the game is currently in progress.
 * - nicknameValid: Tracks the validity of the current nickname.
 * - isHostPlayer: Identifies if the player is the host.
 * - hostSetupCompleted: Indicates if the host's setup process is finished.
 * - currentPlayerCount: Tracks the current count of players in the lobby.
 * - numPlayers: Represents the total number of players allowed.
 * - gameType: Specifies the type of game being played.
 * - rmiClient: The RMI client instance used for communication.
 * - socketClient: The socket client instance used for communication.
 * - gameStarted: Tracks whether the game has started.
 * - clientModel: The client model associated with this view.
 * - readyStatus: A map indicating each player's readiness state.
 * - playersList: A list of players currently in the lobby.
 */
public class LobbyView implements EnhancedClientView {
    private boolean inGame = false;
    private boolean nicknameValid = false;
    private boolean isHostPlayer = false;
    private boolean hostSetupCompleted = false;
    private int currentPlayerCount = 1;
    private int numPlayers = 0;
    private int loadGame = 0;
    private String gameType = null;
    private RmiClient rmiClient;
    private SocketServerHandler socketClient;
    private boolean gameStarted = false;
    private ClientModel clientModel;
    private Map<String, Boolean> readyStatus;
    private List<String> playersList;

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

    /**
     * Displays the current lobby update, including the list of players,
     * their readiness status, and the game type. The method will also
     * handle auto-starting the game if the required conditions are met.
     *
     * @param players      a list of player names currently in the lobby
     * @param readyStatus  a map containing each player's readiness status, where the key is the player name and the value is a Boolean indicating if they are ready
     * @param gameType     the type of game selected for the lobby, or null if not set
     * @param isHost       a boolean indicating if the current player is the host of the lobby
     */
    @Override
    public void displayLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType, boolean isHost) {
        if(players.size() == 1){
            return;
        }

        this.playersList = players;
        this.readyStatus = readyStatus;
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

        System.out.print("> ");



        // Auto-start when minimum player count (2) is reached
        if (numPlayers > 0 && currentPlayerCount == numPlayers && isHostPlayer &&
                hostSetupCompleted && !inGame) {

            try {
                // If everyone is ready, start the game
                startIfReady(players, readyStatus);
            } catch (Exception e) {
                System.err.println("Error starting game 178: " + e.getMessage());
            }

            // Reset the flag after a delay to allow for retries if needed
//            new Thread(() -> {
//                try {
//                    Thread.sleep(3000);  // 3 second cooldowns
//                    autostart = false;
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }).start();
        }
    }

    /**
     * Attempts to start the game if all players are ready and there are at least two players in the lobby.
     * If the conditions are met, the host player initiates the game start process using the appropriate client (RMI or socket).
     *
     * @param players      a list of player names in the lobby
     * @param readyStatus  a map where the keys are player names and the values are booleans indicating whether each player is ready
     */
    private void startIfReady(List<String> players, Map<String, Boolean> readyStatus) {
        boolean allReady = true;
        //System.out.println("readyStatus length: " + readyStatus.size());
        for(String player : readyStatus.keySet()) {
            if(!readyStatus.get(player)) {
                allReady = false;
                break;
            }
        }
        if (allReady && players.size() >= 2) {
            System.out.println("All players ready. Starting game...");
            String hostName = players.getFirst();
            if(rmiClient != null) {
                try {
                    rmiClient.startGameByHost(hostName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                socketClient.startGameByHost(hostName);
            }
        }
    }

    /**
     * Displays the result of a connection attempt, formatting the output
     * based on the success and whether the user is the host.
     *
     * @param isHost  a boolean indicating if the current player is the host
     * @param success a boolean indicating if the connection attempt was successful
     * @param message a string containing a message to display about the connection result
     */
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
                System.out.println("║ 🛠️  As host, set up the game.   ║");
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

    /**
     * Displays the result of a nickname validation process.
     * If the nickname is valid, it displays a formatted success message.
     * If invalid, it shows a formatted error message with the provided details.
     *
     * @param valid        a boolean indicating whether the nickname is valid
     * @param errorMessage a string containing the error message to display if the nickname is invalid
     */
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

    /**
     * Displays the "Game Started" message and updates the game state to indicate
     * that the game has begun. This method outputs a formatted visual banner
     * for the start of the game and sets the internal client state to reflect
     * the game has started.
     *
     * If the `clientModel` is not null, it updates the client model to mark
     * the game start message as received, enabling further game-related
     * interactions. If `clientModel` is null, a warning message is logged.
     *
     * Updates the `inGame` field to true, signaling the active game state.
     */
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



        //displayCurrentCommands();
    }

    //private void displayCurrentCommands() {
    //    if(!inGame){
    //        if (isHostPlayer) {
    //            System.out.println("Host Commands: setmax [number], gametype tut, gametype lvl2, ready, unready, start, exit");
    //        } else {
    //            System.out.println("Commands: ready, unready, exit");
    //        }
    //    } else {
    //        System.out.println("Game commands: [Enter number for commands]");
    //        System.out.println("6: Pick covered tile | 7: Pick uncovered tile | 8: Weld component tile | 9: Standby component tile | 10: Pick standby component tile | 11: Discard component tile \n12: Finish building | 13: Finish building with index | 14: Finished all shipboards | 15: Flip hourglass \n16: Pick card | 17: Activate card | 18: Remove player | 19: Destroy component tile \n20: Abandon game | 21: End game");
    //    }
    //    System.out.print("> ");
    //}

    /**
     * Starts the command loop for interacting with the game lobby using RMI (Remote Method Invocation).
     * The method handles both host and non-host player behaviors within the lobby, including handling setup
     * for host players, waiting for players to join, and responding to user commands.
     *
     * @param client      the RMI client used to interact with the server
     * @param playerName  the name of the player entering the lobby
     * @param scanner     the scanner object for reading user input in the command loop
     */

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
        }

        while (running && !inGame) {
            System.out.println("Waiting for more players to join...");
            System.out.println("Current players: " + currentPlayerCount +
                    (numPlayers > 0 ? "/" + numPlayers : ""));
            System.out.println("Type 'ready' to indicate you're ready.");
            System.out.println("Type 'exit' to leave the lobby.");
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            running = processLobbyInput(client, command, running);

        }

    }

    /**
     * Processes the input provided by a player in the lobby and performs the appropriate actions based on the command.
     * Commands supported are "exit", "ready", and "start". Invalid commands are handled with a warning message.
     *
     * @param client the RMI client used to interact with the server
     * @param command the command provided by the player in the lobby
     * @param running a boolean indicating whether the lobby command loop should continue running
     * @return a boolean specifying the updated running status of the lobby command loop
     */
    private boolean processLobbyInput(RmiClient client, String command, boolean running) {
        if (command.equalsIgnoreCase("exit")) {
            running = false;
            client.disconnect();
        } else if (command.equals("ready")) {
            try {
                running = false;
                rmiClient.setPlayerReady(client.getPlayerName());
            } catch (IOException e) {
                System.out.println("Error setting ready status: " + e.getMessage());
            }
        } else if (command.equals("start")) {
            handleStartGame(client.getPlayerName(), rmiClient);
        }
        else {
            System.out.println("Invalid command.");
        }
        return running;
    }

    /**
     * Processes the input provided by a player in the lobby and executes the appropriate actions
     * based on the command. Supported commands are "exit", "ready", and "start".
     * Invalid commands result in an error message.
     *
     * @param client the socket server handler used to interact with the server
     * @param command the command input provided by the player in the lobby
     * @param running a boolean indicating whether the lobby command loop should continue running
     * @return a boolean specifying the updated running status of the lobby command loop
     */
    private boolean processLobbyInput(SocketServerHandler client, String command, boolean running) {
        if (command.equalsIgnoreCase("exit")) {
            running = false;
            client.disconnect();
        } else if (command.equals("ready")) {
            running = false;
            socketClient.setPlayerReady(clientModel.getPlayerName());
        } else if (command.equals("start")) {
            handleStartGame(clientModel.getPlayerName(), rmiClient);
        }
        else {
            System.out.println("Invalid command.");
        }
        return running;
    }

    /**
     * Starts the command loop for interacting with the game lobby using a socket connection.
     * The method handles both host and non-host player behaviors within the lobby, including handling setup
     * for host players, waiting for players to join, and responding to user commands.
     *
     * @param client      the socket server handler used to interact with the server
     * @param playerName  the name of the player entering the lobby
     * @param scanner     the scanner object for reading user input in the command loop
     */
    public void startCommandLoopSocket(SocketServerHandler client, String playerName, Scanner scanner) {
        boolean running = true;
        this.socketClient = client;
        // If this is the host player, handle host setup
        if (isHostPlayer) {
            setupAsHostSocket(client, scanner);
        } else {
            // Non-host players just wait
            if(gameStarted || inGame){
                return;
            }
            System.out.println("Waiting for other players to join...");
        }

        while (running && !inGame) {
            System.out.println("Waiting for more players to join...");
            System.out.println("Current players: " + currentPlayerCount +
                    (numPlayers > 0 ? "/" + numPlayers : ""));
            System.out.println("Type 'ready' to indicate you're ready.");
            System.out.println("Type 'exit' to leave the lobby.");
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            running = processLobbyInput(client, command, running);
        }
    }

    /**
     * Sets up the socket server as a host by configuring the number of players
     * and selecting the game type. Once completed, the host setup process is marked as complete.
     *
     * @param client the SocketServerHandler object that manages the server-side socket functionality
     * @param scanner the Scanner object used to read user input for the setup configuration
     */
    private void setupAsHostSocket(SocketServerHandler client, Scanner scanner) {

        System.out.println("\n╔═════════════════════════════════════════════════╗");
        System.out.println("║             Load the previous game?             ║");
        System.out.println("║               1. Yes                            ║");
        System.out.println("║               2. No                             ║");
        System.out.println("╚═════════════════════════════════════════════════╝");
        System.out.print("➤ ");

        while (loadGame < 1 || loadGame > 2) {
            try {
                loadGame = Integer.parseInt(scanner.nextLine().trim());

                if (loadGame < 1 || loadGame > 2) {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                           Invalid input.                             ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                    System.out.print("➤ ");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                           Invalid input.                             ║");
                System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
                System.out.print("➤ ");
            }
        }

        if(loadGame == 1)


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

    /**
     * Sets up the current instance as an RMI host by allowing the user to configure
     * the number of players and game type through console inputs. The method
     * interacts with the provided RmiClient and uses the Scanner for user input.
     *
     * @param client the RmiClient instance used to configure the game settings
     *               such as the number of players and game type
     * @param scanner the Scanner used to read user input for configuring the lobby
     */
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

    /**
     * Handles the logic for starting the game from the lobby.
     * This method ensures that only the host player can initiate the game start process.
     * If the conditions allow, the request to start the game is sent to the server using the provided RmiClient instance.
     *
     * @param playerName the name of the player attempting to start the game
     * @param rmiClient  the RMI client used to communicate with the server for starting the game
     */
    public void handleStartGame(String playerName, RmiClient rmiClient) {
        try {
            // Only the host should be able to start the game
            if (isHostPlayer) {
                System.out.println("Only the host can start the game");
                return;
            }

            // The server handles the validation if all players are ready
            rmiClient.startGameByHost(playerName);
        } catch (IOException e) {
            System.out.println("Error starting game 580: " + e.getMessage());
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