package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Client.LobbyView;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import it.polimi.ingsw.is25am22new.Network.VirtualView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements VirtualView {

    private final VirtualServer server;
    private final EnhancedClientView clientView;
    private static final String SERVER_NAME = "GalaxyTruckerServer";
    private boolean isHost = false;

    protected RmiClient(String host, int port, EnhancedClientView clientView) throws RemoteException, NotBoundException {
        super();
        this.clientView = clientView;

        Registry registry = LocateRegistry.getRegistry(host, port);
        this.server = (VirtualServer) registry.lookup(SERVER_NAME);

        System.out.println("Found server: " + host + ":" + port);
    }

    public void connectWithNickname(String nickname) throws RemoteException {
        server.connect(this, nickname);
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;
        int uiChoice = 1; // Default to TUI

        if(args.length >= 1) {
            host = args[0];
        }
        if(args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Using default port 1234.");
            }
        }
        if(args.length >= 3) {
            try {
                uiChoice = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid UI choice. Defaulting to TUI.");
            }
        }

        try {
            // Create UI based on choice
            EnhancedClientView view;

            if(uiChoice == 1) {
                view = new LobbyView();
            } else {
                // GUI would be implemented here
                System.out.println("GUI not yet implemented. Defaulting to TUI.");
                view = new LobbyView();
            }

            // Create the RMI client
            RmiClient client = new RmiClient(host, port, view);

            // Interaction logic to join game, etc.
            Scanner scanner = new Scanner(System.in);

            boolean nickAccepted = false;
            String playerName = "";

            while(!nickAccepted) {
                System.out.print("Enter your name: ");
                playerName = scanner.nextLine();

                try{
                    view.resetNicknameStatus();
                    client.connectWithNickname(playerName);
                    Thread.sleep(1000);
                    nickAccepted = view.isNicknameValid();
                }catch (InterruptedException e){
                    System.err.println("Error connecting with nickname" + e.getMessage());
                }
            }

            view.startCommandLoopRMI(client, playerName, scanner);

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }

        //System.exit(0);
    }

    public void setNumPlayers(int numPlayers) throws IOException {
        server.setNumPlayers(numPlayers);
    }

    public void setPlayerReady(String playerName) throws IOException {
        server.setPlayerReady(playerName);
    }

    public void setPlayerNotReady(String playerName) throws IOException {
        server.setPlayerNotReady(playerName);
    }

    public void startGameByHost(String playerName) throws IOException {
        server.startGameByHost(playerName);
    }

    public void setGameType(String gameType) throws IOException {
        server.setGameType(gameType);
    }

    public void pickCoveredTile(String playerName) throws IOException {
        server.pickCoveredTile(playerName);
    }

    public void pickUncoveredTile(String playerName, int index) throws IOException {
        server.pickUncoveredTile(playerName, index);
    }

    public void weldComponentTile(String playerName, int i, int j, int numOfRotation) throws IOException {
        server.weldComponentTile(playerName, i, j, numOfRotation);
    }

    public void standbyComponentTile(String playerName) throws IOException {
        server.standbyComponentTile(playerName);
    }

    public void pickStandbyComponentTile(String playerName, int index) throws IOException {
        server.pickStandbyComponentTile(playerName, index);
    }

    public void discardComponentTile(String playerName) throws IOException {
        server.discardComponentTile(playerName);
    }

    public void finishBuilding(String playerName) throws IOException {
        server.finishBuilding(playerName);
    }

    public void finishBuilding(String playerName, int index) throws IOException {
        server.finishBuilding(playerName, index);
    }

    public void finishedAllShipboards() throws IOException {
        server.finishedAllShipboards();
    }

    public void flipHourglass() throws IOException {
        server.flipHourglass();
    }

    public void pickCard() throws IOException {
        server.pickCard();
    }

    public void activateCard(InputCommand inputCommand) throws IOException {
        server.activateCard(inputCommand);
    }

    public void removePlayer(String playerName) throws IOException {
        server.removePlayer(playerName);
    }

    public void playerAbandons(String playerName) throws IOException {
        server.playerAbandons(playerName);
    }

    public void destroyComponentTile(String playerName, int i, int j) throws IOException {
        server.destroyComponentTile(playerName, i, j);
    }

    public void endGame() throws IOException {
        server.endGame();
    }

    @Override
    public void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) {
        clientView.displayLobbyUpdate(players, readyStatus, gameType, isHost);
    }

    @Override
    public void showConnectionResult(boolean isHost, boolean success, String message) {
        this.isHost = isHost;
        clientView.displayConnectionResult(isHost, success, message);
    }

    @Override
    public void showNicknameResult(boolean valid, String message) {
        clientView.displayNicknameResult(valid, message);
    }

    @Override
    public void showGameStarted() {
        clientView.displayGameStarted();
    }

    @Override
    public void showPlayerJoined(String playerName) {
        clientView.displayPlayerJoined(playerName);
    }

    @Override
    public void showUpdateBank(Bank bank)  {
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile)  {
    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList)  {
    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {
        //
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard)  {
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard)  {
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
    }

    @Override
    public void showUpdateDices(Dices dices)  {
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) {

    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck) {

    }

    @Override
    public void showUpdateGame(Game game) {
        clientView.displayGame(game);
    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot) {

    }
}