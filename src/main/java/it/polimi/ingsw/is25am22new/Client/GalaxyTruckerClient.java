package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ClientModelObserver;
import it.polimi.ingsw.is25am22new.Client.View.ObservableModelView;
import it.polimi.ingsw.is25am22new.Client.View.TUI;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.EnhancedClientView;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketClientSide;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class GalaxyTruckerClient {
    private VirtualServer virtualServer;
    private String playerName;
    private String gameType;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choose connection type
        int connectionChoice = 0;
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

        // Choose UI type
        int uiChoice = 0;
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
                client.startRmiClient(host, port, uiChoice, scanner);
            } else {
                // Socket connection
                // Port has to be different from the one used by RMI
                client.startSocketClient(host, port + 1, uiChoice, scanner);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }

        System.out.println("!!!!! - Game Type: " + client.gameType + ", Player Name: " + client.playerName);

        CommandManager commandManager = new CommandManager(null, client.virtualServer);
        ClientModel clientModel = new ClientModel(client.playerName);
        TUI tui = new TUI(commandManager, clientModel);
        ((ObservableModelView) clientModel).addListener(tui);
        commandManager.initializeCommandManager(client.virtualServer, tui);
        tui.run();
        System.exit(0);
    }

    /*
     * Start an RMI client connection
     */
    private void startRmiClient(String host, int port, int uiChoice, Scanner scanner) throws RemoteException, NotBoundException {
        // Create UI based on choice
        EnhancedClientView view;

        if (uiChoice == 1) {
            view = new LobbyView();
        } else {
            // GUI would be implemented here
            System.out.println("GUI not yet implemented. Defaulting to TUI.");
            view = new LobbyView();
        }

        // Connect to server
        virtualServer = RmiClient.connectToServer(host, port);

        // Create and run RMI client
        RmiClient client = new RmiClient(virtualServer, view);

        System.out.println("Before client.run()");
        client.run(null, scanner); // null means it will prompt for a name
        System.out.println("After client.run()");

        this.gameType = client.getGameType();
        this.playerName = client.getPlayerName();
    }

    /*
     * Start a Socket client connection
     */
    private void startSocketClient(String host, int port, int uiChoice, Scanner scanner) {
        // For now, just use the original implementation
        String[] socketArgs = {host, String.valueOf(port), String.valueOf(uiChoice)};
        try {
            virtualServer = SocketClientSide.connectToServer(socketArgs);
        } catch (Exception e) {
            System.err.println("Socket client error: " + e.getMessage());
        }
    }
}


        /*TO DO

        Chiedi Tipo di Connessione

        Chiedi Tipo di UI

        Istanzia il giusto Virtual Server per connettersi

        Istanzia la TUI o la GUI

        Logica Lobby:

            se sei il primo -> inserisci nome, inserisci n max di giocstori, inserisci tipo di gioco (tutorial, level2)

            se non sei il primo -> attendi finché il primo non ha finito di inserire i dati sopra
                una volta finita l'attesa inserisci il nome (deve essere validato)
                entra in lobby solo se non è piena
                se è piena -> chiudi client o vedi tu che fare
                se sei l'ultimo ad entrare fai partire il gioco

        Istanzia la copia del model vuota

        Istanzia il Command Manager (con tutta la lista dei comandi)

        Iscrivi tui/gui come observer della copia del model

        Fa lavorare tui/gui

        */