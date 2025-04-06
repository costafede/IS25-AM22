package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Hourglass;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.*;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;

import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Network.ObservableModel;


public abstract class Game extends ObservableModel {
    protected final List<String> playerList;
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

    public Game(List<String> playerList) {
        this.playerList = playerList;
        bank = new Bank();
        cardArchive = new ArrayList<>();
        coveredComponentTiles = new ArrayList<>();
        uncoveredComponentTiles = new ArrayList<>();
        hourglass = new Hourglass(60);
        shipboards = new HashMap<>();
        this.deck = new ArrayList<>();
        this.dices = new Dices();

        List<String> colors = List.of("red", "green", "blue", "yellow");
        for(int i = 0; i < playerList.size(); i++) {
            shipboards.put(playerList.get(i), new Shipboard(colors.get(i), playerList.get(i), bank));
        }
    }

    public Game(List<String> playerList, Bank bank, List<ComponentTile> coveredComponentTiles,
                List<ComponentTile> uncoveredComponentTiles, Map<String, Shipboard> shipboards,
                Flightboard flightboard, List<AdventureCard> cardArchive, Hourglass hourglass) { // for testing
        this.playerList = playerList;
        this.bank = bank;
        this.coveredComponentTiles = coveredComponentTiles;
        this.uncoveredComponentTiles = uncoveredComponentTiles;
        this.shipboards = shipboards;
        this.flightboard = flightboard;
        this.cardArchive = cardArchive;
        this.hourglass = hourglass;
        this.deck = new ArrayList<>();
    }

    public void initGame(){
        ObjectMapper objectMapper = new ObjectMapper();
        GameInitializer.initComponent(this, objectMapper);
        GameInitializer.initCardArchive(this, objectMapper);
        System.out.println("Game initialized");
    }

    public void pickCoveredTile(String nickname) {
        if(coveredComponentTiles.isEmpty())
            throw new IllegalStateException("There are no covered components in this game");
        shipboards.get(nickname).setTileInHand(coveredComponentTiles.remove(new Random().nextInt(coveredComponentTiles.size())));
    }

    public void pickUncoveredTile(String nickname, int index) {
        if(uncoveredComponentTiles.isEmpty())
            throw new IllegalStateException("There are no uncovered components in this game");
        shipboards.get(nickname).setTileInHand(uncoveredComponentTiles.remove(index));
    }

    public void rotateClockwise(String nickname) {
        shipboards.get(nickname).getTileInHand().rotateClockwise();
    }

    public void rotateCounterClockwise(String nickname) {
        shipboards.get(nickname).getTileInHand().rotateCounterClockwise();
    }

    public void weldComponentTile(String nickname, int i, int j) {
        ComponentTile tileInHand = shipboards.get(nickname).getTileInHand();
        if(shipboards.get(nickname).getComponentTileFromGrid(i, j).isPresent())
            throw new IllegalStateException("Component tile already present in the chosen slot");
        shipboards.get(nickname).weldComponentTile(tileInHand, i, j);
        shipboards.get(nickname).setTileInHand(null);
    }

    public void standbyComponentTile(String nickname) {
        ComponentTile tileInHand = shipboards.get(nickname).getTileInHand();
        shipboards.get(nickname).standbyComponentTile(tileInHand);
        shipboards.get(nickname).setTileInHand(null);
    }

    public void pickStandByComponentTile(String nickname, int index) {
        ComponentTile ct = shipboards.get(nickname).pickStandByComponentTile(index);
        shipboards.get(nickname).setTileInHand(ct);
    }

    public void discardComponentTile(String nickname) {
        uncoveredComponentTiles.add(shipboards.get(nickname).getTileInHand());
        shipboards.get(nickname).setTileInHand(null);
    }

    public boolean finishBuilding(String nickname, int pos) {
        // pos is 1, 2, 3, 4
        flightboard.placeRocket(nickname, pos);
        boolean actuallyFinished = shipboards.get(nickname).checkShipboard();
        if(actuallyFinished){ shipboards.get(nickname).setFinishedShipboard(true); }
        return actuallyFinished;
    }

    public boolean finishBuilding(String nickname) {
        boolean actuallyFinished = shipboards.get(nickname).checkShipboard();
        if(actuallyFinished){ shipboards.get(nickname).setFinishedShipboard(true); }
        return actuallyFinished;
    }

    public boolean finishedAllShipboards() {
        // called everytime a player finishes building a shipboard??
        for(Shipboard shipboard : shipboards.values()) {
            if(!shipboard.isFinishedShipboard()) { return false; }
        }
        return true;
    }

    public void flipHourglass(Runnable callbackMethod) {
        return;
    }

    public void pickCard() {
        setCurrCard(deck.remove(new Random().nextInt(deck.size())));
    }

    public void playerAbandons(String nickname) {
        shipboards.get(nickname).abandons();
        flightboard.getOrderedRockets().remove(nickname);
    }

    public void destroyTile(String nickname, int i, int j) {
        shipboards.get(nickname).destroyTile(i, j);
    }

    public List<CardPile> getCardPiles(){
        return new ArrayList<>();
    }

    public abstract Map<String, Integer> endGame();

    //Return the nickname of the player with less exposed connectors
    protected String betterShipboard() {
        return playerList.stream()
                .min(Comparator.comparingInt(nickname -> shipboards.get(nickname).countExposedConnectors()))
                .orElse(null);
    }

    protected abstract void initDeck();

    public List<ComponentTile> getCoveredComponentTiles() {
        return coveredComponentTiles;
    }

    public List<ComponentTile> getUncoveredComponentTiles() {
        return uncoveredComponentTiles;
    }

    public List<AdventureCard> getCardArchive() {
        return cardArchive;
    }

    public Flightboard getFlightboard(){
        return flightboard;
    }

    public Map<String , Shipboard> getShipboards(){
        return shipboards;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public List<AdventureCard> getDeck() {
        return deck;
    }

    public void setCurrCard(AdventureCard card) {
        currCard = card;
    }

    public void setCurrPlayer(String nickname) {
        currPlayer = nickname;
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public AdventureCard getCurrCard() {
        return currCard;
    }

    public void setCurrPlayerToNext(){
        try{
            currPlayer = flightboard.getOrderedRockets().get(flightboard.getOrderedRockets().indexOf(currPlayer) + 1);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Error, index out of bound!");
        }
    }

    public void setCurrPlayerToLeader(){
        currPlayer = flightboard.getOrderedRockets().getFirst();
    }

    public String getLastPlayer() {
        return flightboard.getOrderedRockets().getLast();
    }

    public Bank getBank() {
        return bank;
    }

    public Dices getDices() {
        return dices;
    }

    public boolean isPlayerStillAbleToPlay(String player){
        Shipboard shipboard = shipboards.get(player);
        String leader = flightboard.getOrderedRockets().getFirst();
        if(shipboard.getOnlyHumanNumber() == 0){ //there are no more humans
            return false;
        }
        //player has been lapped
        return shipboards.get(leader).getDaysOnFlight() - shipboard.getDaysOnFlight() <= flightboard.getFlightBoardLength();
    }

    // check if active players still fulfill the conditions to play and eliminates the ones who don't (usually called at the end of the cards effects)
    public void manageInvalidPlayers(){
        Iterator<String> iterator = flightboard.getOrderedRockets().iterator();
        while(iterator.hasNext()){
            String p = iterator.next();
            if(!isPlayerStillAbleToPlay(p)){
                iterator.remove();
                flightboard.getPositions().remove(p);
                playerAbandons(p);
            }
        }
    }

    public void activateCard(InputCommand inputCommand){
        currCard.activateEffect(inputCommand);
    }

}
