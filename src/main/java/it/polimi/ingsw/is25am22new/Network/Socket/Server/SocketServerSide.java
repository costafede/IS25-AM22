package it.polimi.ingsw.is25am22new.Network.Socket.Server;

import it.polimi.ingsw.is25am22new.Controller.GameController;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketServerHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketServerSide implements ObserverModel {
    final ServerSocket listenSocket;
    final GameController controller;
    final Map<SocketClientHandler, Thread> clients;

    public SocketServerSide(GameController gameController, ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        this.controller = gameController;
        this.clients = new HashMap<>();
    }

    public void runServer() throws IOException{
        System.out.println("Server started, waiting for clients...");
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            SocketClientHandler handler = new SocketClientHandler(
                    this.controller,
                    this,
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream()
            );

            Thread t = new Thread(() -> {
                try {
                    handler.runVirtualView(Thread.currentThread());
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Thread interrupted: " + e.getMessage());
                }
            });
            t.start();
        }
    }

    public void addHandlerToClients(SocketClientHandler handler, Thread thread) {
        synchronized (this.clients.keySet()){
            clients.put(handler, thread);
        }
    }

    @Override
    public void updateBank(Bank bank){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateBank(bank);
            }
        }
    }
    @Override
    public void updateTileInHand(String player, ComponentTile ct){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateTileInHand(player, ct);
            }
        }
    }

    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> ctList) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateUncoveredComponentTiles(ctList);
            }
        }
    }
    @Override
    public void updateShipboard(String player, Shipboard shipboard){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateShipboard(player, shipboard);
            }
        }
    }
    @Override
    public void updateFlightboard(Flightboard flightboard){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateFlightboard(flightboard);
            }
        }
    }
    @Override
    public void updateCurrCard(AdventureCard adventureCard){
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateCurrCard(adventureCard);
            }
        }
    }
    @Override
    public void updateDices(Dices dices) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateDices(dices);
            }
        }
    }
    @Override
    public void updateCurrPlayer(String currPlayer) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateCurrPlayer(currPlayer);
            }
        }
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateGamePhase(gamePhase);
            }
        }
    }

    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> ctList) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateCoveredComponentTiles(ctList);
            }
        }
    }

    @Override
    public void updateDeck(List<AdventureCard> deck) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateDeck(deck);
            }
        }
    }

    @Override
    public void updateGame(Game game) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateGame(game);
            }
        }
    }

    @Override
    public void updateStartHourglass(int hourglassSpot) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateStartHourglass(hourglassSpot);
            }
        }
    }

    @Override
    public void updateStopHourglass() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showUpdateStopHourglass();
            }
        }
    }

    @Override
    public void updateGameStarted() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showGameStarted();
            }
        }
    }

    @Override
    public void updateLobby() {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showLobbyUpdate(this.controller.getPlayers(), this.controller.getReadyStatus(), this.controller.getGameType());
            }
        }
    }

    @Override
    public void updatePlayerJoined(String player) {
        synchronized (this.clients.keySet()) {
            for (var client : this.clients.keySet()) {
                client.showPlayerJoined(player);
            }
        }
    }

   public void disconnect(SocketClientHandler handler, String nickname) {
        synchronized (this.clients.keySet()) {
            System.out.println("Player disconnected: " + nickname);
            this.clients.get(handler).interrupt();
            this.clients.remove(handler);
            //for (var client : this.clients.keySet()) {
            //    client.showMessageToEveryone("Player " + nickname + " has disconnected");
            //}
        }
   }
}
