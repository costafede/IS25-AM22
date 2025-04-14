package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketClientSide;

import java.util.Scanner;

public class GalaxyTruckerClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choose connection type
        int connectionChoice = 0;
        while (connectionChoice != 1 && connectionChoice != 2) {
            System.out.println("Choose connection type:");
            System.out.println("1. RMI");
            System.out.println("2. Socket");
            System.out.print("> ");
            if (scanner.hasNextInt()) {
                connectionChoice = scanner.nextInt();
            } else {
                scanner.next(); // consume invalid input
            }
            if (connectionChoice != 1 && connectionChoice != 2) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
        scanner.nextLine(); // consume newline

        // Choose UI type
        int uiChoice = 0;
        while (uiChoice != 1 && uiChoice != 2) {
            System.out.println("Choose UI type:");
            System.out.println("1. Text-based UI");
            System.out.println("2. Graphical UI");
            System.out.print("> ");
            if (scanner.hasNextInt()) {
                uiChoice = scanner.nextInt();
            } else {
                scanner.next(); // consume invalid input
            }
            if (uiChoice != 1 && uiChoice != 2) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
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
                System.out.println("Invalid port number. Using default port 1234.");
            }
        }

        try {
            if (connectionChoice == 1) {
                // RMI connection
                String[] rmiArgs = {host, String.valueOf(port), String.valueOf(uiChoice)};
                RmiClient.main(rmiArgs);
            } else {
                // Socket connection
                String[] socketArgs = {host, String.valueOf(port), String.valueOf(uiChoice)};
                SocketClientSide.main(socketArgs);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
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