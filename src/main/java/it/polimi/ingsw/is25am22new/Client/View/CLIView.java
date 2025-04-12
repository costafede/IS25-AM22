package it.polimi.ingsw.is25am22new.Client.View;

import java.util.Objects;
import java.util.Scanner;

public class CLIView implements GameView{
    public Client client;
    Scanner scanner;

    public CLIView() {
        this.scanner = new Scanner(System.in);
        System.out.println("Select connection type: 'rmi' or 'socket'?");
        System.out.print("Enter your choice ('rmi' or 'socket'): ");

        String choice = "";

        while (!Objects.equals(choice, "rmi") && !Objects.equals(choice, "socket")) {
            choice = scanner.nextLine();
            if (!Objects.equals(choice, "rmi") && !Objects.equals(choice, "socket")) {
                System.out.print("Invalid choice. Please enter 'rmi' or 'socket': ");
            }
        }

        GalaxyTrucker.setParameters(choice, this);

    }


    public void startView() {
        System.out.println("Benvenuto in Galaxy Trucker!");

        boolean running = true;
        while(running){
            String input = scanner.nextLine();
        }
        scanner.close();
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public void initiateConnection(){
        System.out.println("Connecting to server ...");
    }

    @Override
    public void connectionSuccessful() {
        System.out.println("Connection successful!");
    }

    @Override
    public void connectionFailure() {
        System.out.println("Connection failed!");
        System.exit(1);
    }
}
