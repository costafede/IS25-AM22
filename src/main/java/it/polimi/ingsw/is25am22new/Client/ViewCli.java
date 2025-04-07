package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ViewCli {

    private VirtualServer server;

    public ViewCli(VirtualServer server) throws Exception{
        this.server = server;
    }

    public void startGame(){

    }

    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Scegli il protocollo: 'rmi' oppure 'socket'");
        String choice = scanner.nextLine().toLowerCase();

        try{
            VirtualServer server;
            if(choice.equals("rmi")){
                server = new ClientRMI();
            } else if(choice.equals("socket")){
                server = new ClientSocket();
            } else {
                System.out.println("Scelta non valida. Scegli 'rmi' oppure 'socket'");
                return;
            }

            ViewCli view = new ViewCli(server);
            view.startGame();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
