package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Hourglass;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.List;
import java.util.Map;

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

    public ClientModel(Game game, String playerName) {
        bank = game.getBank();
        cardArchive = game.getCardArchive();
        flightboard = game.getFlightboard();
        shipboards = game.getShipboards();
        coveredComponentTiles = game.getCoveredComponentTiles();
        uncoveredComponentTiles = game.getUncoveredComponentTiles();
        hourglass = new Hourglass(60);
        deck = game.getDeck();
        currPlayer = game.getCurrPlayer();
        currCard = game.getCurrCard();
        dices = game.getDices();
        gamePhase = game.getGamePhase();
        this.playerName = playerName;
    }

    public Dices getDices() {
        return dices;
    }

    public void setDices(Dices dices) {
        this.dices = dices;
    }

    public AdventureCard getCurrCard() {
        return currCard;
    }

    public void setCurrCard(AdventureCard currCard) {
        this.currCard = currCard;
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
    }

    public List<AdventureCard> getDeck() {
        return deck;
    }

    public void setDeck(List<AdventureCard> deck) {
        this.deck = deck;
    }

    public Hourglass getHourglass() {
        return hourglass;
    }

    public void setHourglass(Hourglass hourglass) {
        this.hourglass = hourglass;
    }

    public List<AdventureCard> getCardArchive() {
        return cardArchive;
    }

    public void setCardArchive(List<AdventureCard> cardArchive) {
        this.cardArchive = cardArchive;
    }

    public Flightboard getFlightboard() {
        return flightboard;
    }

    public void setFlightboard(Flightboard flightboard) {
        this.flightboard = flightboard;
    }

    public Map<String, Shipboard> getShipboards() {
        return shipboards;
    }

    public void setShipboards(Map<String, Shipboard> shipboards) {
        this.shipboards = shipboards;
    }

    public List<ComponentTile> getUncoveredComponentTiles() {
        return uncoveredComponentTiles;
    }

    public void setUncoveredComponentTiles(List<ComponentTile> uncoveredComponentTiles) {
        this.uncoveredComponentTiles = uncoveredComponentTiles;
    }

    public List<ComponentTile> getCoveredComponentTiles() {
        return coveredComponentTiles;
    }

    public void setCoveredComponentTiles(List<ComponentTile> coveredComponentTiles) {
        this.coveredComponentTiles = coveredComponentTiles;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
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
    }

    public String getPlayerName() {
        return playerName;
    }
}
