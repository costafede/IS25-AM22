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

public class ClientModel extends ObservableModelView {
    protected Bank bank;
    protected List<ComponentTile> coveredComponentTiles;
    protected List<ComponentTile> uncoveredComponentTiles;
    protected Map<String, Shipboard> shipboards;
    protected Flightboard flightboard;
    protected List<AdventureCard> cardArchive;
    protected Hourglass hourglass;
    protected List<AdventureCard> deck;
    protected String currPlayer;
    protected AdventureCard currCard;
    private Dices dices;
    protected GamePhase gamePhase;
    protected String playerName;    //name of the player using this client
    protected int hourglassSpot;
    protected GameType gameType;
    protected List<CardPile> cardPiles;

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
        notifyObservers(this);
    }

    public AdventureCard getCurrCard() {
        return currCard;
    }

    public void setCurrCard(AdventureCard currCard) {
        this.currCard = currCard;
        notifyObservers(this);
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
        notifyObservers(this);
    }

    public List<AdventureCard> getDeck() {
        return deck;
    }

    public void setDeck(List<AdventureCard> deck) {
        this.deck = deck;
        notifyObservers(this);
    }

    public Hourglass getHourglass() {
        return hourglass;
    }

    public void setHourglass(Hourglass hourglass) {
        this.hourglass = hourglass;
        notifyObservers(this);
    }

    public List<AdventureCard> getCardArchive() {
        return cardArchive;
    }

    public void setCardArchive(List<AdventureCard> cardArchive) {
        this.cardArchive = cardArchive;
        notifyObservers(this);
    }

    public Flightboard getFlightboard() {
        return flightboard;
    }

    public void setFlightboard(Flightboard flightboard) {
        this.flightboard = flightboard;
        notifyObservers(this);
    }

    public Map<String, Shipboard> getShipboards() {
        return shipboards;
    }

    public void setShipboard(String player, Shipboard shipboard){
        this.shipboards.put(player, shipboard);
        fixShipboards(this.shipboards);
    }

    public void setShipboards(Map<String, Shipboard> shipboards) {
        this.shipboards = shipboards;
        fixShipboards(this.shipboards);
        notifyObservers(this);
    }

    public List<ComponentTile> getUncoveredComponentTiles() {
        return uncoveredComponentTiles;
    }

    public void setUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        this.uncoveredComponentTiles = uncoveredComponentTiles;
        notifyObservers(this);
    }

    public List<ComponentTile> getCoveredComponentTiles() {
        return coveredComponentTiles;
    }

    public void setCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        this.coveredComponentTiles = coveredComponentTiles;
        notifyObservers(this);
    }

    public void setBank(Bank bank) {
        this.bank = bank;
        notifyObservers(this);
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
        notifyObservers(this);
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameType getGametype() {
        return gameType;
    }

    public void setHourglassSpot(int hourglassSpot) {
        this.hourglassSpot = hourglassSpot;
        notifyObservers(this);
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
        notifyObservers(this);
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
