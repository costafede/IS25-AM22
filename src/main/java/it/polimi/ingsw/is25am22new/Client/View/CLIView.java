package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Network.ClientInterface;

import java.util.Scanner;

public class CLIView {
    private final ClientInterface client;
    private final Scanner scanner;

    public CLIView(ClientInterface client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
    }

    public void startGameLoop() {
        System.out.println("Benvenuto in Galaxy Trucker!");

        boolean running = true;
        while(running){
            System.out.println("\nInserisci il comando (action/exit/status):");
            String input = scanner.nextLine();


        }
        scanner.close();
    }
}
