package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.GamePhase.SetUpPhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Hourglass;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The ClientModel class is responsible for maintaining the state of the game
 * client, including player-related data, game phase, and game components.
 * It extends the ObservableModelView to provide notify methods for observers.
 */
public class ClientModel extends ObservableModelView {
    protected Bank bank;
    protected List<ComponentTile> coveredComponentTiles;
    protected List<ComponentTile> uncoveredComponentTiles;
    protected Map<String, Shipboard> shipboards;
    protected Flightboard flightboard;
    protected List<AdventureCard> cardArchive;
    protected Hourglass hourglass = new Hourglass(60);
    protected int hourglassSpot;
    protected boolean hourglassActive;
    protected List<AdventureCard> deck;
    protected String currPlayer;
    protected AdventureCard currCard;
    private Dices dices;
    protected GamePhase gamePhase;
    protected String playerName;    //name of the player using this client
    protected GameType gameType;
    protected List<CardPile> cardPiles;
    private boolean gameStartMessageReceived = false;
    protected boolean gameLoaded;


    private Map<String, Integer> leaderboard;

    /* LOCKS */
    private final Object bankLock = new Object();
    private final Object coveredComponentTilesLock = new Object();
    private final Object uncoveredComponentTilesLock = new Object();
    private final Object shipboardLock = new Object();
    private final Object flightboardLock = new Object();
    private final Object hourglassLock = new Object();
    private final Object deckLock = new Object();
    private final Object currPlayerLock = new Object();
    private final Object currCardLock = new Object();
    private final Object dicesLock = new Object();
    private final Object gamePhaseLock = new Object();
    private final Object leaderboardLock = new Object();

    /**
     * Retrieves the current leaderboard.
     *
     * @return a map of player names to their scores
     */
    public Map<String, Integer> getLeaderboard() {
        synchronized (leaderboardLock) {
            return leaderboard;
        }
    }

    /**
     * Sets the leaderboard and notifies observers.
     *
     * @param leaderboard a map of player names to their updated scores
     */
    public void setLeaderboard(Map<String, Integer> leaderboard) {
        synchronized (leaderboardLock) {
            this.leaderboard = leaderboard;
        }
        notifyAllLeaderboard(leaderboard);
    }

    /**
     * Checks whether the game start message has been received.
     *
     * @return {@code true} if the game start message was received, {@code false} otherwise
     */
    public boolean isGameStartMessageReceived() {
        return gameStartMessageReceived;
    }

    /**
     * Sets the game start message received flag and notifies observers.
     *
     * @param gameStartMessageReceived {@code true} if the message was received, {@code false} otherwise
     */
    public void setGameStartMessageReceived(boolean gameStartMessageReceived) {
        this.gameStartMessageReceived = gameStartMessageReceived;
        
        notifyGameStartMessageReceived(gameStartMessageReceived);
    }

    /**
     * Constructs a new {@code ClientModel} and initializes it to the setup phase.
     */
    public ClientModel() {
        this.gamePhase = new SetUpPhase(null);
    }

    /**
     * Sets the name of the player associated with this client.
     *
     * @param playerName the player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Retrieves the current dice values.
     *
     * @return the {@link Dices} object
     */
    public Dices getDices() {
        synchronized (dicesLock) {
            return dices;
        }
    }

    /**
     * Sets the dice values and notifies observers.
     *
     * @param dices the updated {@link Dices} object
     */
    public void setDices(Dices dices) {
        synchronized (dicesLock) {
            this.dices = dices;
        }
        notifyDices(dices);
    }

    /**
     * Retrieves the current adventure card in play.
     *
     * @return the current {@link AdventureCard}
     */
    public AdventureCard getCurrCard() {
        synchronized (currCardLock) {
            return currCard;
        }
    }

    /**
     * Sets the current adventure card and notifies observers.
     *
     * @param currCard the current {@link AdventureCard}
     */
    public void setCurrCard(AdventureCard currCard) {
        synchronized (currCardLock) {
            this.currCard = currCard;
        }
        notifyCurrCard(currCard);
    }

    /**
     * Retrieves the name of the current player.
     *
     * @return the name of the current player
     */
    public String getCurrPlayer() {
        synchronized (currPlayerLock) {
            return currPlayer;
        }
    }

    /**
     * Sets the current player's name and notifies observers.
     *
     * @param currPlayer the name of the current player
     */
    public void setCurrPlayer(String currPlayer) {
        synchronized (currPlayerLock) {
            this.currPlayer = currPlayer;
        }
        notifyCurrPlayer(currPlayer);
    }

    /**
     * Retrieves the current adventure card deck.
     *
     * @return the list of {@link AdventureCard} in the deck
     */
    public List<AdventureCard> getDeck() {
        synchronized (deckLock) {
            return deck;
        }
    }

    /**
     * Sets the deck of adventure cards and notifies observers.
     *
     * @param deck the new list of {@link AdventureCard}
     */
    public void setDeck(List<AdventureCard> deck) {
        synchronized (deckLock) {
            this.deck = deck;
        }
        notifyDeck(deck);
    }

    /**
     * Retrieves the hourglass timer.
     *
     * @return the {@link Hourglass} object
     */
    public Hourglass getHourglass() {
        synchronized (hourglassLock) {
            return hourglass;
        }
    }

    /**
     * Retrieves the flightboard.
     *
     * @return the {@link Flightboard}
     */
    public Flightboard getFlightboard() {
        synchronized (flightboardLock) {
            return flightboard;
        }
    }

    /**
     * Sets the flightboard and notifies observers.
     *
     * @param flightboard the updated {@link Flightboard}
     */
    public void setFlightboard(Flightboard flightboard) {
        synchronized (flightboardLock) {
            this.flightboard = flightboard;
        }
        notifyFlightboard(flightboard);
    }

    /**
     * Retrieves the map of all shipboards.
     *
     * @return a map of player names to their {@link Shipboard}
     */
    public Map<String, Shipboard> getShipboards() {
        synchronized (shipboardLock) {
            return shipboards;
        }
    }

    /**
     * Updates the shipboard of a specific player and notifies observers.
     *
     * @param player the name of the player
     * @param shipboard the updated {@link Shipboard}
     */
    public void setShipboard(String player, Shipboard shipboard){
        synchronized (shipboardLock) {
            this.shipboards.put(player, shipboard);
            fixShipboards(this.shipboards);
        }
        notifyShipboard(shipboard);
    }

    /**
     * Sets the entire map of shipboards and notifies observers.
     *
     * @param shipboards a map of player names to their {@link Shipboard}
     */
    public void setShipboards(Map<String, Shipboard> shipboards) {
        synchronized (shipboardLock) {
            this.shipboards = shipboards;
            fixShipboards(this.shipboards);
        }
        notifyShipboards(shipboards);
    }

    /**
     * Retrieves the list of uncovered component tiles.
     *
     * @return the list of uncovered {@link ComponentTile}
     */
    public List<ComponentTile> getUncoveredComponentTiles() {
        synchronized (uncoveredComponentTilesLock) {
            return uncoveredComponentTiles;
        }
    }

    /**
     * Sets the uncovered component tiles and notifies observers.
     *
     * @param uncoveredComponentTiles the new list of uncovered {@link ComponentTile}
     */
    public void setUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        synchronized (uncoveredComponentTilesLock) {
            this.uncoveredComponentTiles = uncoveredComponentTiles;
        }
        notifyUncoveredComponentTiles(uncoveredComponentTiles);
    }

    /**
     * Retrieves the list of covered component tiles.
     *
     * @return the list of covered {@link ComponentTile}
     */
    public List<ComponentTile> getCoveredComponentTiles() {
        synchronized (coveredComponentTilesLock) {
            return coveredComponentTiles;
        }
    }

    /**
     * Sets the covered component tiles and notifies observers.
     *
     * @param coveredComponentTiles the new list of covered {@link ComponentTile}
     */
    public void setCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        synchronized (coveredComponentTilesLock) {
            this.coveredComponentTiles = coveredComponentTiles;
        }
        notifyCoveredComponentTiles(coveredComponentTiles);
    }

    /**
     * Sets the bank and notifies observers.
     *
     * @param bank the updated {@link Bank}
     */
    public void setBank(Bank bank) {
        synchronized (bankLock) {
            this.bank = bank;
        }
        notifyBank(bank);
    }

    /**
     * Retrieves the current bank.
     *
     * @return the {@link Bank} object
     */
    public Bank getBank() {
        synchronized (bankLock) {
            return bank;
        }
    }

    /**
     * Retrieves the shipboard of a specific player.
     *
     * @param player the name of the player
     * @return the {@link Shipboard} of the specified player
     */
    public Shipboard getShipboard(String player) {
        synchronized (shipboardLock) {
            return this.shipboards.get(player);
        }
    }

    /**
     * Retrieves the component tile currently in hand for a specific player.
     *
     * @param player the name of the player
     * @return the {@link ComponentTile} in hand
     */
    public ComponentTile getTileInHand(String player) {
        synchronized (shipboardLock) {
            return this.shipboards.get(player).getTileInHand();
        }
    }

    /**
     * Sets the component tile in hand for a specific player and notifies observers.
     *
     * @param player the name of the player
     * @param ct the {@link ComponentTile} to set in hand
     */
    public void setTileInHand(String player, ComponentTile ct) {
        synchronized (shipboardLock) {
            this.shipboards.get(player).setTileInHand(ct);
        }
        notifyTileInHand(player, ct);
    }

    /**
     * Retrieves the current game phase.
     *
     * @return the current {@link GamePhase}
     */
    public GamePhase getGamePhase() {
        synchronized (gamePhaseLock) {
            return gamePhase;
        }
    }

    /**
     * Sets the current game phase and notifies observers.
     *
     * @param gamePhase the new {@link GamePhase}
     */
    public void setGamePhase(GamePhase gamePhase) {
        synchronized (gamePhaseLock) {
            this.gamePhase = gamePhase;
        }
        notifyGamePhase(gamePhase);
    }

    /**
     * Retrieves the name of the local player.
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retrieves the current game type (e.g., tutorial or level 2).
     *
     * @return the {@link GameType}
     */
    public GameType getGametype() {
        return gameType;
    }

    /**
     * Starts the hourglass timer at the given position and notifies observers.
     *
     * @param hourglassSpot the position on the flightboard where the hourglass is placed
     */
    public void startHourglass(int hourglassSpot) {
        synchronized (hourglassLock) {
            this.hourglassSpot = hourglassSpot;
            this.hourglass.startTimer(() -> {
            });
            this.hourglassActive = true;
        }
        
        notifyStartHourglass(hourglassSpot);
    }

    /**
     * Stops the hourglass timer and notifies observers.
     */
    public void stopHourglass() {
        synchronized (hourglassLock) {
            this.hourglassActive = false;
            this.hourglass.stopTimer();
        }
        
        notifyStopHourglass();
    }

    /**
     * Checks whether the hourglass is currently active.
     *
     * @return {@code true} if the hourglass is active, {@code false} otherwise
     */
    public boolean isHourglassActive() {
        synchronized (hourglassLock) {
            return hourglassActive;
        }
    }

    /**
     * Retrieves the position of the hourglass on the flightboard.
     *
     * @return the hourglass spot index
     */
    public int getHourglassSpot() {
        synchronized (hourglassLock) {
            return hourglassSpot;
        }
    }

    /**
     * Retrieves the list of card piles used in Level 2 games.
     *
     * @return the list of {@link CardPile}
     */
    public List<CardPile> getCardPiles() {
        return cardPiles;
    }

    /**
     * Sets the game attributes based on the provided {@link Game} instance
     * and notifies observers with the updated {@link ClientModel}.
     *
     * @param game the loaded or running game instance
     */
    public void setGame(Game game) {
        setGameAttributes(game);
        notifyGame(this);
    }

    /**
     * Populates internal fields with the data from the provided {@link Game} instance.
     *
     * @param game the game to copy attributes from
     */
    private void setGameAttributes(Game game) {
        bank = game.getBank();
        cardArchive = game.getCardArchive();
        flightboard = game.getFlightboard();
        shipboards = game.getShipboards();
        fixShipboards(this.shipboards);
        coveredComponentTiles = game.getCoveredComponentTiles();
        uncoveredComponentTiles = game.getUncoveredComponentTiles();
        deck = game.getDeck();
        currPlayer = game.getCurrPlayer();
        currCard = game.getCurrCard();
        dices = game.getDices();
        gamePhase = game.getGamePhase();
        gameType = game.getFlightboard().getFlightBoardLength() == 24 ? GameType.LEVEL2 : GameType.TUTORIAL;
        leaderboard = game.endGame();
        if(gameType.equals(GameType.LEVEL2)) {
            cardPiles = game.getCardPiles();
            hourglassSpot = game.getHourglassSpot();
            hourglassActive = game.isHourglassActive();
        }
    }

    /**
     * Ensures all {@link Shipboard} instances in the map are initialized correctly.
     *
     * @param shipboards the map of player names to shipboards to fix
     */
    public void fixShipboards(Map<String, Shipboard> shipboards) {
        for (Shipboard shipboard : shipboards.values()) {
            if(shipboard.getComponentTilesGrid() == null) {
                shipboard.setNewComponentTilesGrid();
            }
            if(shipboard.getStandbyComponent() == null) {
                shipboard.setNewStandbyComponent();
            }
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    shipboard.setComponentTileOnGrid(i, j, shipboard.getComponentTilesGridCopy(i, j));
                }
            }
            shipboard.setStandbyComponent(0, Optional.ofNullable(shipboard.getStandbyComponentCopy(0)));
            shipboard.setStandbyComponent(1, Optional.ofNullable(shipboard.getStandbyComponentCopy(1)));
        }
    }

    /**
     * Loads a game instance into the client model, sets the internal state,
     * and notifies observers that the game has been loaded.
     *
     * @param game the {@link Game} instance to load
     */
    public void setGameLoaded(Game game) {
        setGameAttributes(game);
        gameLoaded = true;
        notifyGameLoaded(this);
    }

    /**
     * Checks whether the game has been fully loaded.
     *
     * @return {@code true} if the game is loaded, {@code false} otherwise
     */
    public boolean isGameLoaded() {
        return gameLoaded;
    }
}
