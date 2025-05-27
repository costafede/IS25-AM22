package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.TUI;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketServerHandler;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The GalaxyTruckerClient class serves as the main entry point for the Galaxy Trucker client application.
 * It handles user interactions to configure the connection type and UI type, as well as initializing
 * the appropriate client (RMI or Socket) and user interface.
 */
public class GalaxyTruckerClient {
    private VirtualServer virtualServer;
    private String playerName;

    /**
     * The main entry point for the application. It initializes the required configurations,
     * allows the user to select the connection type and UI type, and starts the application
     * with the specified settings.
     *
     * @param args Command-line arguments where:
     *             args[0] (optional): The server host address (default is "localhost").
     *             args[1] (optional): The server port as an integer (default is 1234).
     *             args[2] (optional): The connection type as a case-insensitive string ("RMI" or "SOCKET").
     *             args[3] (optional): The UI type as a case-insensitive string ("TUI" or "GUI").
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientModel clientModel = new ClientModel();

        // Choose connection type
        int connectionChoice = 0;
        if (args.length <= 2) {
            while (connectionChoice != 1 && connectionChoice != 2) {
                System.out.println("\n╔══════════════════════════════╗");
                System.out.println("║    Choose Connection Type    ║");
                System.out.println("╠══════════════════════════════╣");
                System.out.println("║           1. RMI             ║");
                System.out.println("║          2. Socket           ║");
                System.out.println("╚══════════════════════════════╝");
                System.out.print("➤ ");

                if (scanner.hasNextInt()) {
                    connectionChoice = scanner.nextInt();
                } else {
                    scanner.next(); // consume invalid input
                }
                if (connectionChoice != 1 && connectionChoice != 2) {
                    System.out.println("\n╔══════════════════════════════╗");
                    System.out.println("║       ⚠Invalid Choice⚠       ║");
                    System.out.println("║     Please enter 1 or 2.     ║");
                    System.out.println("╚══════════════════════════════╝\n");
                }
            }
            scanner.nextLine(); // consume newline
        }else{
            if(args[2].equalsIgnoreCase("RMI"))
                connectionChoice = 1;
            else if(args[2].equalsIgnoreCase("SOCKET"))
                connectionChoice = 2;
            else {
                System.out.println("choose a valid connection type");
                System.exit(0);
            }

        }

        // Choose UI type
        int uiChoice = 0;
        if (args.length <= 3) {
            while (uiChoice != 1 && uiChoice != 2) {
                System.out.println("\n╔══════════════════════════════╗");
                System.out.println("║        Choose UI Type        ║");
                System.out.println("╠══════════════════════════════╣");
                System.out.println("║       1. Text-based UI       ║");
                System.out.println("║       2. Graphical UI        ║");
                System.out.println("╚══════════════════════════════╝");
                System.out.print("➤ ");

                if (scanner.hasNextInt()) {
                    uiChoice = scanner.nextInt();
                } else {
                    scanner.next(); // consume invalid input
                }
                if (uiChoice != 1 && uiChoice != 2) {
                    System.out.println("\n╔══════════════════════════════╗");
                    System.out.println("║       ⚠Invalid Choice⚠       ║");
                    System.out.println("║     Please enter 1 or 2.     ║");
                    System.out.println("╚══════════════════════════════╝\n");
                }
            }
            scanner.nextLine(); // consume newline
        } else {
            if(args[3].equalsIgnoreCase("TUI"))
                uiChoice = 1;
            else if(args[3].equalsIgnoreCase("GUI"))
                uiChoice = 2;
            else {
                System.out.println("choose a valid UI type");
                System.exit(0);
            }
        }

        // Process server connection details
        String host = "localhost";
        int port = 1234;

        if (args.length >= 1) {
            host = args[0];
        }
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("\n╔══════════════════════════════╗");
                System.out.println("║    ⚠Invalid Port Number⚠     ║");
                System.out.println("║   Using default port: 1234   ║");
                System.out.println("╚══════════════════════════════╝\n");
            }
        }

        GalaxyTruckerClient client = new GalaxyTruckerClient();

        try {
            if (connectionChoice == 1) {
                client.startRmiClient(host, port, uiChoice, scanner, clientModel);
            } else {
                // Socket connection
                // Port has to be different from the one used by RMI
                client.startSocketClient(host, port + 1, uiChoice, scanner, clientModel);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        CommandManager commandManager = new CommandManager();
        TUI tui = new TUI(commandManager, clientModel);
        clientModel.addListener(tui);
        commandManager.initializeCommandManager(client.virtualServer, tui);
        tui.run(scanner);

        if(client.virtualServer instanceof RmiClient){
            ((RmiClient) client.virtualServer).shutdown();
        }

        System.exit(0);
    }

    /**
     * Initializes and starts an RMI (Remote Method Invocation) client to connect
     * to the specified server. This method sets up the user interface based on
     * the provided choice and establishes the connection to the server using RMI.
     * It also initializes the player's name after a successful connection.
     *
     * @param host The hostname of the remote server to connect to.
     * @param port The port number the server is listening to.
     * @param uiChoice An integer representing the user's choice for UI type
     *                 (e.g., 1 for TUI, other options for GUI).
     * @param scanner A Scanner instance for handling user input, typically from
     *                the command line in TUI mode.
     * @param clientModel The ClientModel instance representing the client's
     *                    state and game data.
     * @throws RemoteException If a communication error occurs during the RMI
     *                         process.
     * @throws NotBoundException If the requested RMI server object is not found
     *                           in the registry at the given host and port.
     */
    private void startRmiClient(String host, int port, int uiChoice, Scanner scanner, ClientModel clientModel) throws RemoteException, NotBoundException {
        // Create UI based on choice
        EnhancedClientView view;

        if (uiChoice == 1) {
            view = new LobbyView(clientModel);
            // Create and run RMI client
            RmiClient client = new RmiClient(view, clientModel);
            client.connectToServer(host, port);

            // Connect to server
            virtualServer = client;

            client.run(null, scanner); // null means it will prompt for a name

            this.playerName = client.getPlayerName();
        } else {
            // GUI implementation
            GalaxyTruckerGUI.setClientModel(clientModel);
            GalaxyTruckerGUI.main(new String[]{"rmi"});
        }


    }

    /**
     * Initializes and starts a socket-based client to connect to the specified server.
     * This method sets up the user interface based on the provided choice and establishes
     * the connection to the server using sockets. It also uses the client model to represent
     * the client's state and game data.
     *
     * @param host The hostname of the remote server to connect to.
     * @param port The port number the server is listening to.
     * @param uiChoice An integer representing the user's choice for UI type
     *                 (e.g., 1 for TUI, other options for GUI).
     * @param scanner A Scanner instance for handling user input, typically from
     *                the command line in TUI mode.
     * @param clientModel The ClientModel instance representing the client's
     *                    state and game data.
     */
    private void startSocketClient(String host, int port, int uiChoice, Scanner scanner, ClientModel clientModel) {
        EnhancedClientView view;
        if (uiChoice == 1) {
            view = new LobbyView(clientModel);
            String[] socketArgs = {host, String.valueOf(port)};
            try {
                virtualServer = SocketServerHandler.connectToServerTUI(socketArgs, clientModel, scanner, view);
            } catch (Exception e) {
                System.err.println("Socket client error: " + e.getMessage());
            }
        } else {
            GalaxyTruckerGUI.setClientModel(clientModel);
            GalaxyTruckerGUI.main(new String[]{"socket"});
        }

    }
}