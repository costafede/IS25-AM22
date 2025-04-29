package it.polimi.ingsw.is25am22new.Network.RMI.Client;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
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

    ClientModel clientModel;
    private final VirtualServer server;
    private final EnhancedClientView clientView;
    private static final String SERVER_NAME = "GalaxyTruckerServer";
    private boolean isHost = false;
    private String playerName;
    private String gameType;

    public RmiClient(VirtualServer server, EnhancedClientView clientView) throws RemoteException {
        super();
        this.server = server;
        this.clientView = clientView;
    }

    /*
     * Creates a VirtualServer connection through RMI
     */
    public static VirtualServer connectToServer(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        VirtualServer server = (VirtualServer) registry.lookup(SERVER_NAME);
        System.out.println("Found server: " + host + ":" + port);
        return server;
    }

    /*
     * Run the RMI client
     */
    public void run(String playerName, Scanner scanner) {
        try {
            boolean nickAccepted = false;

            while(!nickAccepted) {
                if (playerName == null || playerName.isEmpty()) {
                    System.out.print("Enter your name: ");
                    playerName = scanner.nextLine();
                }

                try {
                    clientView.resetNicknameStatus();
                    connectWithNickname(playerName);
                    Thread.sleep(1000);
                    nickAccepted = clientView.isNicknameValid();
                    if (!nickAccepted) {
                        playerName = null; // Reset to prompt again
                    }
                } catch (InterruptedException e) {
                    System.err.println("Error connecting with nickname: " + e.getMessage());
                }
            }
            this.playerName = playerName;
            clientView.startCommandLoopRMI(this, playerName, scanner);
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }

    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGameType() {
        return gameType;
    }

    public void connectWithNickname(String nickname) throws RemoteException {
        server.connect(this, nickname);
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

    public void pickUncoveredTile(String playerName, String pngName) throws IOException {
        server.pickUncoveredTile(playerName, pngName);
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
        this.gameType = gameType;
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
        clientModel.setBank(bank);
    }

    @Override
    public void showUpdateTileInHand(String player, ComponentTile tile)  {
        clientModel.getShipboard(player).setTileInHand(tile);
    }

    @Override
    public void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList)  {
        clientModel.setUncoveredComponentTiles(ctList);
    }

    @Override
    public void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) {
        clientModel.setCoveredComponentTiles(ctList);
    }

    @Override
    public void showUpdateShipboard(String player, Shipboard shipboard)  {
        clientModel.getShipboards().put(player, shipboard);
    }

    @Override
    public void showUpdateFlightboard(Flightboard flightboard)  {
        clientModel.setFlightboard(flightboard);
    }

    @Override
    public void showUpdateCurrCard(AdventureCard adventureCard)  {
        clientModel.setCurrCard(adventureCard);
    }

    @Override
    public void showUpdateDices(Dices dices)  {
        clientModel.setDices(dices);
    }

    @Override
    public void showUpdateCurrPlayer(String currPlayer)  {
        clientModel.setCurrPlayer(currPlayer);
    }

    @Override
    public void showUpdateGamePhase(GamePhase gamePhase) {
        clientModel.setGamePhase(gamePhase);
    }

    @Override
    public void showUpdateDeck(List<AdventureCard> deck) {
        clientModel.setDeck(deck);
    }

    @Override
    public void showUpdateGame(Game game) {
        clientModel.setGame(game);
    }

    @Override
    public void showUpdateHourglassSpot(int hourglassSpot) {
        System.out.println("Hourglass spot: " + hourglassSpot);
    }

    public void setPlayerName(String testPlayer) {
        this.playerName = testPlayer;
    }
}