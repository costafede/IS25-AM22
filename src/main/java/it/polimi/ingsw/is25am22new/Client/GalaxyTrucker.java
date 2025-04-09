package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Client.View.CLIView;
import it.polimi.ingsw.is25am22new.Client.View.GUIView;
import it.polimi.ingsw.is25am22new.Client.View.GameView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class GalaxyTrucker {

    public static GameView gameView;
    public static Client client;
    public static String protocolType;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String playerName;

        System.out.println("=== GALAXY TRUCKER GAME ===");
        System.out.println("Select CLI or GUI:");
        System.out.println("1. CLI");
        System.out.println("2. GUI");
        System.out.print("Enter your choice (1 or 2): ");

        int choice = 0;
        while (choice != 1 && choice != 2) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice != 1 && choice != 2) {
                    System.out.print("Invalid choice. Please enter 1 or 2: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number (1 or 2): ");
            }
        }

        String host = "localhost";
        int port = 1234;

        if (args.length >= 1) {
            host = args[0];
        }
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number, using default: 1234");
            }
        }

        try{
            if(choice == 1) {
                gameView = new CLIView();
            } else {
                gameView = new GUIView();
            }
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        gameView.startView();

    }

    public void setClient(Client client) {
        GalaxyTrucker.client = client;
    }

    public static void setParameters(String protocolType, GameView gameView) {
        GalaxyTrucker.protocolType = protocolType;
        GalaxyTrucker.gameView = gameView;
        switch (protocolType) {
            case "rmi" -> {
                try {
                    client = new ClientRMI();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            case "tcp" -> {
                try {
                    client = new ClientSocket();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                System.err.println("Invalid protocol: " + protocolType + ". Use 'rmi' or 'tcp'.");
                System.exit(1);
            }
        }
        client.setView(gameView);
        gameView.setClient(client);
    }

}
