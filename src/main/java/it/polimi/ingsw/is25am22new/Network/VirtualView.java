package it.polimi.ingsw.is25am22new.Network;

    import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
    import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
    import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
    import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
    import it.polimi.ingsw.is25am22new.Model.Games.Game;
    import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
    import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
    import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

    import java.rmi.Remote;
    import java.rmi.RemoteException;
    import java.util.List;
    import java.util.Map;

    /**
     * The VirtualView interface represents an abstraction for updating the game state
     * on the client-side through remote procedure calls. It is intended to be implemented
     * by classes that act as views for a remote client in a client-server architecture.
     * It provides methods to update various components of the game's state and lobby details,
     * ensuring the client remains synchronized with the server. Additionally, it supports
     * displaying messages, game phase updates, player readiness states, and other game-related data.
     */
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
    }