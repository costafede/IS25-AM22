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
        return dices;
    }

    public void setDices(Dices dices) {
        this.dices = dices;
        
        notifyDices(dices);
    }

    public AdventureCard getCurrCard() {
        return currCard;
    }

    public void setCurrCard(AdventureCard currCard) {
        this.currCard = currCard;
        
        notifyCurrCard(currCard);
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
        
        notifyCurrPlayer(currPlayer);
    }

    public List<AdventureCard> getDeck() {
        return deck;
    }

    public void setDeck(List<AdventureCard> deck) {
        this.deck = deck;
        
        notifyDeck(deck);
    }

    public Hourglass getHourglass() {
        return hourglass;
    }

    public Flightboard getFlightboard() {
        return flightboard;
    }

    public void setFlightboard(Flightboard flightboard) {
        this.flightboard = flightboard;
        
        notifyFlightboard(flightboard);
    }

    public Map<String, Shipboard> getShipboards() {
        return shipboards;
    }

    public void setShipboard(String player, Shipboard shipboard){
        this.shipboards.put(player, shipboard);
        fixShipboards(this.shipboards);
        
        notifyShipboard(shipboard);
    }

    public void setShipboards(Map<String, Shipboard> shipboards) {
        this.shipboards = shipboards;
        fixShipboards(this.shipboards);
        
        notifyShipboards(shipboards);
    }

    public List<ComponentTile> getUncoveredComponentTiles() {
        return uncoveredComponentTiles;
    }

    public void setUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        this.uncoveredComponentTiles = uncoveredComponentTiles;
        
        notifyUncoveredComponentTiles(uncoveredComponentTiles);
    }

    public List<ComponentTile> getCoveredComponentTiles() {
        return coveredComponentTiles;
    }

    public void setCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        this.coveredComponentTiles = coveredComponentTiles;
        
        notifyCoveredComponentTiles(coveredComponentTiles);
    }

    public void setBank(Bank bank) {
        this.bank = bank;
        
        notifyBank(bank);
    }

    public Bank getBank() {
        return bank;
    }

    public Shipboard getShipboard(String player) {
        return this.shipboards.get(player);
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        
        notifyGamePhase(gamePhase);
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameType getGametype() {
        return gameType;
    }

    public void startHourglass(int hourglassSpot) {
        this.hourglassSpot = hourglassSpot;
        this.hourglass.startTimer(() -> {});
        this.hourglassActive = true;
        
        notifyStartHourglass(hourglassSpot);
    }

    public void stopHourglass() {
        this.hourglassActive = false;
        this.hourglass.stopTimer();
        
        notifyStopHourglass();
    }

    public boolean isHourglassActive() {
        return hourglassActive;
    }

    public int getHourglassSpot() {
        return hourglassSpot;
    }

    public List<CardPile> getCardPiles() {
        return cardPiles;
    }

    public void setCardPiles(List<CardPile> cardPiles) {
        this.cardPiles = cardPiles;
    }

    public void setGame(Game game) {
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
        
        notifyGame(this);
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
}
