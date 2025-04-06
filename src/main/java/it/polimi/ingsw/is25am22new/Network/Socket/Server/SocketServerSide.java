package it.polimi.ingsw.is25am22new.Network.Socket.Server;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServerSide implements ObserverModel {
    final ServerSocket listenSocket;
    final GameController controller;
    final List<SocketClientHandler> clients = new ArrayList<>();

    public SocketServerSide(ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        this.controller = new GameController();
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ServerSocket listenSocket = new ServerSocket(port);

        new SocketServerSide(listenSocket).runServer();
    }

    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            SocketClientHandler handler = new SocketClientHandler(
                    this.controller,
                    this,
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream()
            );

            synchronized (this.clients){
                clients.add(handler);
            }

            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public void updateBank(Bank bank) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateBank(bank);
            }
        }
    }

    public void updateTileInHand(String player, ComponentTile ct) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateTileInHand(player, ct);
            }
        }
    }

    public void updateUncoveredComponentTiles(ComponentTile ct) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateUncoveredComponentTiles(ct);
            }
        }
    }

    public void updateShipboard(Shipboard shipboard) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateShipboard(shipboard);
            }
        }
    }

    public void updateFlightboard(Flightboard flightboard) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateFlightboard(flightboard);
            }
        }
    }

    public void updateCurrCard(AdventureCard adventureCard) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateCurrCard(adventureCard);
            }
        }
    }

    public void updateDices(Dices dices) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateDices(dices);
            }
        }
    }

    public void updateCurrPlayer(String currPlayer) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateCurrPlayer(currPlayer);
            }
        }
    }
}
