package it.polimi.ingsw.is25am22new.Network;

    import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
    import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
    import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
    import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
    import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
    import it.polimi.ingsw.is25am22new.Model.Games.Game;
    import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
    import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
    import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

    import java.io.IOException;
    import java.rmi.Remote;
    import java.rmi.RemoteException;
    import java.util.List;
    import java.util.Map;

    public interface VirtualView extends Remote {

        void showUpdateBank(Bank bank) throws RemoteException;
        void showUpdateTileInHand(String player, ComponentTile tile) throws RemoteException;
        void showUpdateUncoveredComponentTiles(List<ComponentTile> ctList) throws RemoteException;
        void showUpdateCoveredComponentTiles(List<ComponentTile> ctList) throws RemoteException;
        void showUpdateShipboard(String player, Shipboard shipboard) throws RemoteException;
        void showUpdateFlightboard(Flightboard flightboard) throws RemoteException;
        void showUpdateCurrCard(AdventureCard adventureCard) throws RemoteException;
        void showUpdateDices(Dices dices) throws RemoteException;
        void showUpdateCurrPlayer(String currPlayer) throws RemoteException;

        void showUpdateGamePhase(GamePhase gamePhase) throws RemoteException;
        void showUpdateDeck(List<AdventureCard> deck) throws RemoteException;
        void showUpdateGame(Game game) throws RemoteException;
        void showUpdateStopHourglass() throws RemoteException;
        void showUpdateStartHourglass(int hourglassSpot) throws RemoteException;

        // Methods for lobby management
        void showLobbyUpdate(List<String> players, Map<String, Boolean> readyStatus, String gameType) throws RemoteException;
        void showConnectionResult(boolean isHost, boolean success, String message) throws RemoteException;
        void showNicknameResult(boolean valid, String message) throws RemoteException;
        void showGameStarted() throws RemoteException;
        void showPlayerJoined(String player) throws RemoteException;

        void terminate() throws RemoteException;

        //Methods for game management
        //void godMode(String playerName, String conf) throws IOException;
        //void setPlayerReady(String playerName) throws IOException;
        //void setPlayerNotReady(String playerName) throws IOException;
        //void startGameByHost(String playerName) throws IOException;
        //void setGameType(String gameType) throws IOException;
        //void pickCoveredTile(String playerName) throws IOException;
        //void pickUncoveredTile(String playerName, String pngName) throws IOException;
        //void weldComponentTile(String playerName, int i, int j, int numOfRotation) throws IOException;
        //void standbyComponentTile(String playerName) throws IOException;
        //void pickStandbyComponentTile(String playerName, int index) throws IOException;
        //void discardComponentTile(String playerName) throws IOException;
        //void finishBuilding(String playerName) throws IOException;
        //void finishBuilding(String playerName, int index) throws IOException;
        //void finishedAllShipboards() throws IOException;
        //void flipHourglass() throws IOException;
        //void pickCard() throws IOException;
        //void activateCard(InputCommand inputCommand) throws IOException;
        //void removePlayer(String playerName) throws IOException;
        //void playerAbandons(String playerName) throws IOException;
        //void destroyComponentTile(String playerName, int i, int j) throws IOException;
        //void endGame() throws IOException;

        //void placeBrownAlien(String playerName, int i, int j) throws IOException;
        //void placeAstronauts(String playerName, int i, int j) throws IOException;
        //void placePurpleAlien(String playerName, int i, int j) throws IOException;
    }