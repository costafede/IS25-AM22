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

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ServerSocket listenSocket = new ServerSocket(port);

        new SocketServerSide(listenSocket).runServer();
    }

    private void runServer() throws IOException, InterruptedException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            SocketClientHandler handler = new SocketClientHandler(
                    this.controller,
                    this,
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream()
            );

            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public void addHandlerToClients(SocketClientHandler handler) {
        synchronized (this.clients){
            clients.add(handler);
            System.out.println(clients);
        }
    }

    @Override
    public void updateBank(Bank bank){
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateBank(bank);
            }
        }
    }
    @Override
    public void updateTileInHand(String player, ComponentTile ct){
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateTileInHand(player, ct);
            }
        }
    }

    @Override
    public void updateUncoveredComponentTiles(List<ComponentTile> ctList) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateUncoveredComponentTiles(ctList);
            }
        }
    }
    @Override
    public void updateShipboard(String player, Shipboard shipboard){
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateShipboard(player, shipboard);
            }
        }
    }
    @Override
    public void updateFlightboard(Flightboard flightboard){
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateFlightboard(flightboard);
            }
        }
    }
    @Override
    public void updateCurrCard(AdventureCard adventureCard){
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateCurrCard(adventureCard);
            }
        }
    }
    @Override
    public void updateDices(Dices dices) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateDices(dices);
            }
        }
    }
    @Override
    public void updateCurrPlayer(String currPlayer) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateCurrPlayer(currPlayer);
            }
        }
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateGamePhase(gamePhase);
            }
        }
    }

    @Override
    public void updateCoveredComponentTiles(List<ComponentTile> ctList) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateCoveredComponentTiles(ctList);
            }
        }
    }

    @Override
    public void updateDeck(List<AdventureCard> deck) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateDeck(deck);
            }
        }
    }

    @Override
    public void updateGame(Game game) {
        System.out.println("updateGame called");
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateGame(game);
            }
        }
    }

    @Override
    public void updateHourglassSpot(int hourglassSpot) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateHourglassSpot(hourglassSpot);
            }
        }
    }

    @Override
    public void updateGameStarted() {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showGameStarted();
            }
        }
    }

    @Override
    public void updateLobby() {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showLobbyUpdate(this.controller.getPlayers(), this.controller.getReadyStatus(), this.controller.getGameType());
            }
        }
    }

    @Override
    public void updatePlayerJoined(String player) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showPlayerJoined(player);

            }
        }
    }

    @Override
    public void updateConnectionResult(boolean isHost, boolean success, String message) {

    }

    @Override
    public void updateNicknameResult(boolean valid, String message) {

    }
}
