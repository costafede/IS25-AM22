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

    public Map<String, Integer> getLeaderboard() {
        synchronized (leaderboardLock) {
            return leaderboard;
        }
    }

    public void setLeaderboard(Map<String, Integer> leaderboard) {
        synchronized (leaderboardLock) {
            this.leaderboard = leaderboard;
        }
        notifyAllLeaderboard(leaderboard);
    }

    public boolean isGameStartMessageReceived() {
        return gameStartMessageReceived;
    }

    public void setGameStartMessageReceived(boolean gameStartMessageReceived) {
        this.gameStartMessageReceived = gameStartMessageReceived;
        
        notifyGameStartMessageReceived(gameStartMessageReceived);
    }

    public ClientModel() {
        this.gamePhase = new SetUpPhase(null);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Dices getDices() {
        synchronized (dicesLock) {
            return dices;
        }
    }

    public void setDices(Dices dices) {
        synchronized (dicesLock) {
            this.dices = dices;
        }
        notifyDices(dices);
    }

    public AdventureCard getCurrCard() {
        synchronized (currCardLock) {
            return currCard;
        }
    }

    public void setCurrCard(AdventureCard currCard) {
        synchronized (currCardLock) {
            this.currCard = currCard;
        }
        notifyCurrCard(currCard);
    }

    public String getCurrPlayer() {
        synchronized (currPlayerLock) {
            return currPlayer;
        }
    }

    public void setCurrPlayer(String currPlayer) {
        synchronized (currPlayerLock) {
            this.currPlayer = currPlayer;
        }
        notifyCurrPlayer(currPlayer);
    }

    public List<AdventureCard> getDeck() {
        synchronized (deckLock) {
            return deck;
        }
    }

    public void setDeck(List<AdventureCard> deck) {
        synchronized (deckLock) {
            this.deck = deck;
        }
        notifyDeck(deck);
    }

    public Hourglass getHourglass() {
        synchronized (hourglassLock) {
            return hourglass;
        }
    }

    public Flightboard getFlightboard() {
        synchronized (flightboardLock) {
            return flightboard;
        }
    }

    public void setFlightboard(Flightboard flightboard) {
        synchronized (flightboardLock) {
            this.flightboard = flightboard;
        }
        notifyFlightboard(flightboard);
    }

    public Map<String, Shipboard> getShipboards() {
        synchronized (shipboardLock) {
            return shipboards;
        }
    }

    public void setShipboard(String player, Shipboard shipboard){
        synchronized (shipboardLock) {
            this.shipboards.put(player, shipboard);
            fixShipboards(this.shipboards);
        }
        notifyShipboard(shipboard);
    }

    public void setShipboards(Map<String, Shipboard> shipboards) {
        synchronized (shipboardLock) {
            this.shipboards = shipboards;
            fixShipboards(this.shipboards);
        }
        notifyShipboards(shipboards);
    }

    public List<ComponentTile> getUncoveredComponentTiles() {
        synchronized (uncoveredComponentTilesLock) {
            return uncoveredComponentTiles;
        }
    }

    public void setUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        synchronized (uncoveredComponentTilesLock) {
            this.uncoveredComponentTiles = uncoveredComponentTiles;
        }
        notifyUncoveredComponentTiles(uncoveredComponentTiles);
    }

    public List<ComponentTile> getCoveredComponentTiles() {
        synchronized (coveredComponentTilesLock) {
            return coveredComponentTiles;
        }
    }

    public void setCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        synchronized (coveredComponentTilesLock) {
            this.coveredComponentTiles = coveredComponentTiles;
        }
        notifyCoveredComponentTiles(coveredComponentTiles);
    }

    public void setBank(Bank bank) {
        synchronized (bankLock) {
            this.bank = bank;
        }
        notifyBank(bank);
    }

    public Bank getBank() {
        synchronized (bankLock) {
            return bank;
        }
    }

    public Shipboard getShipboard(String player) {
        synchronized (shipboardLock) {
            return this.shipboards.get(player);
        }
    }

    public ComponentTile getTileInHand(String player) {
        synchronized (shipboardLock) {
            return this.shipboards.get(player).getTileInHand();
        }
    }

    public void setTileInHand(String player, ComponentTile ct) {
        synchronized (shipboardLock) {
            this.shipboards.get(player).setTileInHand(ct);
        }
        notifyTileInHand(player, ct);
    }

    public GamePhase getGamePhase() {
        synchronized (gamePhaseLock) {
            return gamePhase;
        }
    }

    public void setGamePhase(GamePhase gamePhase) {
        synchronized (gamePhaseLock) {
            this.gamePhase = gamePhase;
        }
        notifyGamePhase(gamePhase);
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameType getGametype() {
        return gameType;
    }

    public void startHourglass(int hourglassSpot) {
        synchronized (hourglassLock) {
            this.hourglassSpot = hourglassSpot;
            this.hourglass.startTimer(() -> {
            });
            this.hourglassActive = true;
        }
        
        notifyStartHourglass(hourglassSpot);
    }

    public void stopHourglass() {
        synchronized (hourglassLock) {
            this.hourglassActive = false;
            this.hourglass.stopTimer();
        }
        
        notifyStopHourglass();
    }

    public boolean isHourglassActive() {
        synchronized (hourglassLock) {
            return hourglassActive;
        }
    }

    public int getHourglassSpot() {
        synchronized (hourglassLock) {
            return hourglassSpot;
        }
    }

    public List<CardPile> getCardPiles() {
        return cardPiles;
    }

    public void setGame(Game game) {
        setGameAttributes(game);
        notifyGame(this);
    }

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
        if(gameType.equals(GameType.LEVEL2)) {
            cardPiles = game.getCardPiles();
            hourglassSpot = game.getHourglassSpot();
            hourglassActive = game.isHourglassActive();
        }
    }

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

    public void setGameLoaded(Game game) {
        setGameAttributes(game);
        notifyGameLoaded(this);
    }
}
