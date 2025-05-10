package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.GamePhase.*;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.*;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;

import java.io.Serializable;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Network.ObservableModel;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;


public abstract class Game extends ObservableModel implements Serializable {
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
    protected GamePhase gamePhase;
    protected int hourglassSpot = 0;

    public Game(List<String> playerList, List<ObserverModel> observers) {
        this.playerList = playerList;
        bank = new Bank();
        cardArchive = new ArrayList<>();
        coveredComponentTiles = new ArrayList<>();
        uncoveredComponentTiles = new ArrayList<>();
        hourglass = new Hourglass(60);
        shipboards = new HashMap<>();
        this.deck = new ArrayList<>();
        this.dices = new Dices();
        this.gamePhase = new SetUpPhase(this);

        if(observers != null) {
            for (ObserverModel observer : observers) {
                this.addObserver(observer);
            }
        }

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
        gamePhase.trySwitchToNextPhase();
    }

    public void placeAstronauts(String nickname, int i, int j) {
        Shipboard shipboard = shipboards.get(nickname);
        Optional<ComponentTile> tile = shipboard.getComponentTileFromGrid(i, j);
        if(tile.isEmpty() || tile.get().getCrewNumber() > 0 || !tile.get().isCabin())
            throw new IllegalArgumentException("Cannot place Astronaut at the given coordinates");
        tile.get().putAstronauts();
        gamePhase.trySwitchToNextPhase();
        updateAllGamePhase(gamePhase);
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void placeBrownAlien(String nickname, int i, int j) {
        Shipboard shipboard = shipboards.get(nickname);
        Optional<ComponentTile> tile = shipboard.getComponentTileFromGrid(i, j);
        if(tile.isEmpty() || tile.get().getCrewNumber() > 0 || !tile.get().isCabin() || !shipboard.isAlienPlaceable(i, j, "brown"))
            throw new IllegalArgumentException("Cannot place Brown Alien at the given coordinates");
        tile.get().putAlien("brown");
        gamePhase.trySwitchToNextPhase();
        updateAllGamePhase(gamePhase);
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void placePurpleAlien(String nickname, int i, int j) {
        Shipboard shipboard = shipboards.get(nickname);
        Optional<ComponentTile> tile = shipboard.getComponentTileFromGrid(i, j);
        if(tile.isEmpty() || tile.get().getCrewNumber() > 0 || !tile.get().isCabin() || !shipboard.isAlienPlaceable(i, j, "purple"))
            throw new IllegalArgumentException("Cannot place Purple Alien at the given coordinates");
        tile.get().putAlien("purple");
        gamePhase.trySwitchToNextPhase();
        updateAllGamePhase(gamePhase);
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void pickCoveredTile(String nickname) {
        if(coveredComponentTiles.isEmpty())
            throw new IllegalStateException("There are no covered components in this game");
        shipboards.get(nickname).setTileInHand(coveredComponentTiles.remove(new Random().nextInt(coveredComponentTiles.size())));
        updateAllTileInHand(nickname, shipboards.get(nickname).getTileInHand());
        updateAllCoveredComponentTiles(coveredComponentTiles);
    }

    public void pickUncoveredTile(String nickname, String tilePngName) {
        if(uncoveredComponentTiles.isEmpty())
            throw new IllegalStateException("There are no uncovered components in this game");
        int index = -1;
        for(ComponentTile componentTile : uncoveredComponentTiles) {
            if(tilePngName.equals(componentTile.getPngName())) {
                index = uncoveredComponentTiles.indexOf(componentTile);
                break;
            }
        }
        if(index == -1)
            throw new IllegalStateException("There is no such component tile to pick");
        shipboards.get(nickname).setTileInHand(uncoveredComponentTiles.remove(index));
        updateAllTileInHand(nickname, shipboards.get(nickname).getTileInHand());
        updateAllUncoveredComponentTiles(uncoveredComponentTiles);
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
        updateAllTileInHand(nickname, shipboards.get(nickname).getTileInHand());
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void standbyComponentTile(String nickname) {
        ComponentTile tileInHand = shipboards.get(nickname).getTileInHand();
        shipboards.get(nickname).standbyComponentTile(tileInHand);
        shipboards.get(nickname).setTileInHand(null);
        updateAllTileInHand(nickname, shipboards.get(nickname).getTileInHand());
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void pickStandByComponentTile(String nickname, int index) {
        ComponentTile ct = shipboards.get(nickname).pickStandByComponentTile(index);
        shipboards.get(nickname).setTileInHand(ct);
        updateAllTileInHand(nickname, shipboards.get(nickname).getTileInHand());
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void discardComponentTile(String nickname) {
        uncoveredComponentTiles.add(shipboards.get(nickname).getTileInHand());
        shipboards.get(nickname).setTileInHand(null);
        updateAllTileInHand(nickname, shipboards.get(nickname).getTileInHand());
        updateAllUncoveredComponentTiles(uncoveredComponentTiles);
    }

    public void finishBuilding(String nickname, int pos) {
        // pos is 0, 1, 2, 3
        flightboard.placeRocket(nickname, pos);
        shipboards.get(nickname).setFinishedShipboard(true);
        setCurrPlayerToLeader();
        gamePhase.trySwitchToNextPhase();
        updateAllGame(this);
    }


    /*these next two methods may be useless*/
    public void finishBuilding(String nickname) {
        shipboards.get(nickname).setFinishedShipboard(true);
    }

    public boolean finishedAllShipboards() {
        // called everytime a player finishes building a shipboard??
        for(Shipboard shipboard : shipboards.values()) {
            if(!shipboard.isFinishedShipboard()) { return false; }
        }
        return true;
    }
    /***************************************/


    public void flipHourglass(Runnable callbackMethod) {
        return;
    }

    public void pickCard() {
        setCurrCard(deck.remove(new Random().nextInt(deck.size())));
        updateAllDeck(deck);
        updateAllCurrCard(currCard);
    }

    public void playerAbandons(String nickname) {
        shipboards.get(nickname).abandons();
        flightboard.getOrderedRockets().remove(nickname);
        updateAllFlightboard(flightboard);
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void destroyTile(String nickname, int i, int j) {
        shipboards.get(nickname).destroyTile(i, j);
        gamePhase.trySwitchToNextPhase();
        updateAllShipboard(nickname, shipboards.get(nickname));
        updateAllGamePhase(gamePhase);
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
        if(flightboard.getOrderedRockets().isEmpty())
            gamePhase.trySwitchToNextPhase();
        else
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

    public boolean isHourglassActive() {
        return hourglass.getRemainingSeconds() < 60;
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
    public void manageInvalidPlayers() {
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

    public void activateCard(InputCommand inputCommand) {
        currCard.activateEffect(inputCommand);
        gamePhase.trySwitchToNextPhase();
        updateAllGame(this);
    }

    public void setGamePhase(GamePhase gamePhase){
        this.gamePhase = gamePhase;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public int getHourglassSpot() {
        return hourglassSpot;
    }

    public void godMode(String player, String conf) {
        conf = conf.toLowerCase().trim();
        String shipboardNumber = conf.substring(0, 1);
        String deckConfig = conf.length() > 1 ? conf.substring(1) : ",";

        setUpShipboardConfig(player, shipboardNumber);

        setUpDeckConfig(deckConfig);

        updateAllGame(this);
    }

    private void setUpShipboardConfig(String player, String shipboardNumber) {
        Shipboard oldShipboard = shipboards.get(player);

        // Create new shipboard and store it in the map
        Shipboard newShipboard = new Shipboard(oldShipboard.getColor(), oldShipboard.getNickname(), bank);
        switch (shipboardNumber) {
            case "1" -> setUpShipboard1(newShipboard); // Level2
            case "2" -> setUpShipboard2(newShipboard); // Tutorial
            case "3" -> setUpShipboard3(newShipboard); // tutorial friendly
            case "4" -> setUpShipboard4(newShipboard); // tutorial friendly
            case "5" -> setUpShipboard5(newShipboard); // tutorial friendly
            case "6" -> setUpShipboard6(newShipboard); // tutorial friendly
            case "7" -> setUpShipboard7(newShipboard); // same as shipboard1 - level2 broken shipboard to test correction method
            case "8" -> setUpShipboard8(newShipboard); // shipboard_should_be_considered_invalid_due_to_non_connected_parts to test correction method
            case "9" -> setUpShipboard9(newShipboard); // tiles in front of cannons
            case "a" -> setUpShipboardA(newShipboard); //tile_behind_engine
            case "b" -> setUpShipboardB(newShipboard); //engine not facing down
            case "c" -> setUpShipboardC(newShipboard); //tiles connected wrongly
            case "d" -> setUpShipboardD(newShipboard); //nave vuota
            case "e" -> setUpShipboardE(newShipboard);
            case "f" -> setUpShipboardF(newShipboard);
            case "g" -> setUpShipboardG(newShipboard);
            default -> throw new IllegalArgumentException("Invalid shipboard number: " + shipboardNumber);
        }

        // Update the map with the new shipboard
        shipboards.put(player, newShipboard);

        // Notify observers
        updateAllShipboard(player, newShipboard);
    }


    private void setUpDeckConfig(String deckConfig) {

        if(!deckConfig.isEmpty()){
            deck.clear();
            String[] pngNames = deckConfig.split(",");

            for(String pngName : pngNames){
                for(AdventureCard card : cardArchive){
                    if(card.getPngName().equalsIgnoreCase(pngName.trim())){
                        deck.add(card);
                        break;
                    }
                }
            }
            updateAllDeck(deck);
        }

    }

    private void setUpShipboard1(Shipboard shipboard) {
        shipboard.setStandbyComponent(0, Optional.empty());
        shipboard.setStandbyComponent(1, Optional.empty());

        shipboard.weldComponentTile(new DoubleCannon("3", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH), 0, 2);
        shipboard.weldComponentTile(new DoubleCannon("32", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH), 0, 4);
        shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE), 1, 1);
        shipboard.weldComponentTile(new RegularCabin("9", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES), 1, 2);
        shipboard.getComponentTileFromGrid(1, 2).get().putAlien("purple");
        shipboard.weldComponentTile(new AlienAddon("7", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES, "purple"), 1, 3);
        shipboard.weldComponentTile(new BatteryComponent("15", Side.TWOPIPES, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.TWOPIPES, 2), 1, 4);
        shipboard.getComponentTileFromGrid(1, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new Cannon("20", Side.SMOOTH, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.SMOOTH), 1, 5);
        shipboard.weldComponentTile(new Cannon("21", Side.SMOOTH, Side.ONEPIPE, Side.TWOPIPES, Side.SMOOTH), 2, 0);
        shipboard.getComponentTileFromGrid(2, 0).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new BatteryComponent("22", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, 2), 2, 1);
        shipboard.weldComponentTile(new SpecialStorageCompartment("11", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, 2), 2, 2);
        shipboard.weldComponentTile(new StorageCompartment("12", Side.ONEPIPE, Side.ONEPIPE, Side.UNIVERSALPIPE, Side.TWOPIPES, 2), 2, 4);
        shipboard.getComponentTileFromGrid(2, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new ShieldGenerator("18", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 2, 5);
        shipboard.weldComponentTile(new Cannon("23", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.UNIVERSALPIPE), 2, 6);
        shipboard.getComponentTileFromGrid(2, 6).get().rotateClockwise();
        shipboard.weldComponentTile(new ShieldGenerator("24", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH), 3, 0);
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.weldComponentTile(new RegularCabin("14", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH), 3, 1);
        shipboard.getComponentTileFromGrid(3, 1).get().putAstronauts();
        shipboard.weldComponentTile(new SpecialStorageCompartment("25", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, 1), 3, 2);
        shipboard.weldComponentTile(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE), 3, 3);
        shipboard.weldComponentTile(new RegularCabin("26", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 3, 4);
        shipboard.getComponentTileFromGrid(3, 4).get().putAstronauts();
        shipboard.weldComponentTile(new StructuralModule("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.ONEPIPE, Side.TWOPIPES), 3, 5);
        shipboard.weldComponentTile(new BatteryComponent("27", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, 3), 3, 6);
        shipboard.getComponentTileFromGrid(3, 6).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new DoubleEngine("1", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 1);
        shipboard.weldComponentTile(new DoubleEngine("28", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH), 4, 2);
        shipboard.weldComponentTile(new Engine("29", Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 4);
        shipboard.weldComponentTile(new Cannon("30", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES), 4, 5);
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.weldComponentTile(new Engine("31", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH), 4, 6);
    }

    private void setUpShipboard3(Shipboard shipboard) {
        shipboard.weldComponentTile(new RegularCabin("1", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 1, 3);
        shipboard.weldComponentTile(new RegularCabin("2", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 3);
        shipboard.weldComponentTile(new RegularCabin("3", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 4);
        shipboard.weldComponentTile(new RegularCabin("6", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 5);
        shipboard.weldComponentTile(new AlienAddon("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "purple"), 3, 4);
        shipboard.weldComponentTile(new AlienAddon("5", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "brown"), 1, 4);
    }

    private void setUpShipboard4(Shipboard shipboard) {
        shipboard.weldComponentTile(new DoubleCannon("0", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 1, 3);
        shipboard.weldComponentTile(new DoubleCannon("1", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 2);
        shipboard.getComponentTileFromGrid(2,2).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 5);
        shipboard.getComponentTileFromGrid(2,5).get().rotateClockwise();
        shipboard.weldComponentTile(new Cannon("3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 4, 5);
        shipboard.getComponentTileFromGrid(4,5).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(4,5).get().rotateClockwise();
        shipboard.weldComponentTile(new AlienAddon("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "purple"), 1, 4);
        shipboard.weldComponentTile(new RegularCabin("5", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 4);
        shipboard.weldComponentTile(new RegularCabin("6", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 4);
        shipboard.weldComponentTile(new BatteryComponent("7", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 5);
        shipboard.weldComponentTile(new StorageCompartment("8", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 3);
        shipboard.weldComponentTile(new StorageCompartment("9", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 4, 4);
        shipboard.weldComponentTile(new SpecialStorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 3, 2 );
    }

    private void setUpShipboard5(Shipboard shipboard) {
        shipboard.weldComponentTile(new DoubleEngine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 4, 5);
        shipboard.weldComponentTile(new DoubleEngine("1", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 3);
        shipboard.weldComponentTile(new Engine("2", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 4);
        shipboard.weldComponentTile(new Engine("3", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 2);
        shipboard.weldComponentTile(new AlienAddon("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "brown"), 1, 4);
        shipboard.weldComponentTile(new RegularCabin("5", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 1, 3);
        shipboard.weldComponentTile(new RegularCabin("6", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 4);
        shipboard.weldComponentTile(new BatteryComponent("7", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 2, 5);
        shipboard.weldComponentTile(new StorageCompartment("8", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 5);
        shipboard.weldComponentTile(new StorageCompartment("9", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 2, 2);
        shipboard.weldComponentTile(new SpecialStorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 3, 1 );
    }

    private void setUpShipboard2(Shipboard shipboard) {
        shipboard.setStandbyComponent(0, Optional.empty());
        shipboard.setStandbyComponent(1, Optional.empty());

        shipboard.weldComponentTile(new DoubleCannon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                1, 2);
        shipboard.weldComponentTile(new Cannon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                1, 3);
        shipboard.weldComponentTile(new DoubleCannon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.SMOOTH), // right
                1, 4);
        shipboard.weldComponentTile(new BatteryComponent("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE,  // right
                        2),
                2, 1);
        shipboard.weldComponentTile(new ShieldGenerator("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                2, 2);
        shipboard.weldComponentTile(new BatteryComponent("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE,
                        2), // right
                2, 4);
        shipboard.weldComponentTile(new AlienAddon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.SMOOTH,
                        "purple"), // right
                2, 5);
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                3, 1);
        shipboard.getComponentTileFromGrid(3, 1).get().putAstronauts();
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                3, 2);
        shipboard.getComponentTileFromGrid(3, 2).get().putAstronauts();
        shipboard.weldComponentTile(new StorageCompartment("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE,3), // right
                3, 3);
        shipboard.weldComponentTile(new DoubleEngine("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.SMOOTH,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                3, 4);
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.SMOOTH), // right
                3, 5);
        shipboard.getComponentTileFromGrid(3, 5).get().putAlien("purple");
        shipboard.weldComponentTile(new Engine("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.SMOOTH,  // bottom
                        Side.SMOOTH,  // left
                        Side.SMOOTH), // right
                4, 2);
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                4, 5);
        shipboard.getComponentTileFromGrid(4, 5).get().putAstronauts();
    }

    private void setUpShipboard6(Shipboard shipboard) {
        shipboard.setStandbyComponent(0, Optional.empty());
        shipboard.setStandbyComponent(1, Optional.empty());

        shipboard.weldComponentTile(new DoubleCannon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                1, 2);
        shipboard.weldComponentTile(new DoubleCannon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                1, 3);
        shipboard.weldComponentTile(new DoubleCannon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.SMOOTH), // right
                1, 4);
        shipboard.weldComponentTile(new Cannon("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE),  // right
                2, 1);
        shipboard.weldComponentTile(new ShieldGenerator("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                2, 2);
        shipboard.weldComponentTile(new BatteryComponent("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE,
                        2), // right
                2, 4);
        shipboard.weldComponentTile(new AlienAddon("1",
                        Side.SMOOTH,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.SMOOTH,
                        "purple"), // right
                2, 5);
        shipboard.weldComponentTile(new BatteryComponent("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE,
                        3), // right
                3,  1);
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                3, 2);
        shipboard.getComponentTileFromGrid(3, 2).get().putAstronauts();
        shipboard.weldComponentTile(new StorageCompartment("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE,3), // right
                3, 3);
        shipboard.weldComponentTile(new DoubleEngine("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.SMOOTH,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                3, 4);
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.SMOOTH), // right
                3, 5);
        shipboard.getComponentTileFromGrid(3, 5).get().putAlien("purple");
        shipboard.weldComponentTile(new Engine("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.SMOOTH,  // bottom
                        Side.SMOOTH,  // left
                        Side.SMOOTH), // right
                4, 2);
        shipboard.weldComponentTile(new RegularCabin("1",
                        Side.UNIVERSALPIPE,  // top
                        Side.UNIVERSALPIPE,  // bottom
                        Side.UNIVERSALPIPE,  // left
                        Side.UNIVERSALPIPE), // right
                4, 5);
        shipboard.getComponentTileFromGrid(4, 5).get().putAstronauts();
    }

    private void setUpShipboard7(Shipboard shipboard) {
        shipboard.setStandbyComponent(0, Optional.empty());
        shipboard.setStandbyComponent(1, Optional.empty());

        shipboard.weldComponentTile(new DoubleCannon("3", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH), 0, 2);
        shipboard.weldComponentTile(new DoubleCannon("32", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH), 0, 4);
        shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE), 1, 1);
        shipboard.weldComponentTile(new RegularCabin("9", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES), 1, 2);
        shipboard.getComponentTileFromGrid(1, 2).get().putAlien("purple");
        shipboard.weldComponentTile(new AlienAddon("7", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES, "purple"), 1, 3);
        shipboard.weldComponentTile(new BatteryComponent("15", Side.TWOPIPES, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.TWOPIPES, 2), 1, 4);
        shipboard.getComponentTileFromGrid(1, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new Cannon("20", Side.SMOOTH, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.SMOOTH), 1, 5);
        shipboard.weldComponentTile(new Cannon("21", Side.SMOOTH, Side.ONEPIPE, Side.TWOPIPES, Side.SMOOTH), 2, 0);
        shipboard.getComponentTileFromGrid(2, 0).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new BatteryComponent("22", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, 2), 2, 1);
        shipboard.weldComponentTile(new SpecialStorageCompartment("11", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, 2), 2, 2);
        shipboard.weldComponentTile(new StorageCompartment("12", Side.ONEPIPE, Side.ONEPIPE, Side.UNIVERSALPIPE, Side.TWOPIPES, 2), 2, 4);
        shipboard.getComponentTileFromGrid(2, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new ShieldGenerator("18", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 2, 5);
        shipboard.weldComponentTile(new Cannon("23", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.UNIVERSALPIPE), 2, 6);
        shipboard.getComponentTileFromGrid(2, 6).get().rotateClockwise();
        shipboard.weldComponentTile(new ShieldGenerator("24", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH), 3, 0);
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.weldComponentTile(new RegularCabin("14", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH), 3, 1);
        shipboard.getComponentTileFromGrid(3, 1).get().putAstronauts();
        shipboard.weldComponentTile(new SpecialStorageCompartment("25", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, 1), 3, 2);
        shipboard.weldComponentTile(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE), 3, 3);
        shipboard.weldComponentTile(new RegularCabin("26", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 3, 4);
        shipboard.getComponentTileFromGrid(3, 4).get().putAstronauts();
        shipboard.getComponentTileFromGrid(3, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new StructuralModule("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.ONEPIPE, Side.TWOPIPES), 3, 5);
        shipboard.weldComponentTile(new BatteryComponent("27", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, 3), 3, 6);
        shipboard.getComponentTileFromGrid(3, 6).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new DoubleEngine("1", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 1);
        shipboard.weldComponentTile(new DoubleEngine("28", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH), 4, 2);
        shipboard.weldComponentTile(new Engine("29", Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 4);
        shipboard.weldComponentTile(new Cannon("30", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES), 4, 5);
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.weldComponentTile(new Engine("31", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH), 4, 6);
    }

    private void setUpShipboard8(Shipboard shipboard){
        //non connected parts
        shipboard.weldComponentTile(new Engine("0", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE),4, 1);
        shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 1);
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),2, 2);
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),1, 3);
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),2, 4);
    }

    private void setUpShipboard9(Shipboard shipboard){
        //tiles in front of cannons
        shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 4);
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 3);
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),2, 4);
        shipboard.getComponentTileFromGrid(2,4).get().rotateCounterClockwise();
        shipboard.getComponentTileFromGrid(2,4).get().rotateCounterClockwise();
    }

    private void setUpShipboardA(Shipboard shipboard){
        //tile_behind_engine
        shipboard.weldComponentTile(new Engine("0", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE),2, 4);
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 3);
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 4);
    }

    private void setUpShipboardB(Shipboard shipboard){
        //engine not facing down
        shipboard.weldComponentTile(new Engine("0", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE),2, 4);
        shipboard.getComponentTileFromGrid(2,4).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 3);
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 4);
    }

    private void setUpShipboardC(Shipboard shipboard){
        //tiles connected wrongly
        shipboard.weldComponentTile(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 3);
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.getComponentTileFromGrid(3,3).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),2, 4);
        shipboard.weldComponentTile(new StructuralModule("6", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE),3, 4);
    }

    private void setUpShipboardD(Shipboard newShipboard) {
        return;
    }

    private void setUpShipboardE(Shipboard shipboard) {
        shipboard.weldComponentTile(new DoubleCannon("0", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 1, 4);
        shipboard.weldComponentTile(new DoubleCannon("1", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 1, 3);
        //shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 2);
        //shipboard.getComponentTileFromGrid(2,2).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new BatteryComponent("3", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 3, 3);
        shipboard.weldComponentTile(new AlienAddon("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "purple"), 2, 4);
        shipboard.weldComponentTile(new RegularCabin("5", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 5);
        shipboard.weldComponentTile(new DoubleEngine("6", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 2);
        shipboard.weldComponentTile(new DoubleEngine("7", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 1);
        shipboard.weldComponentTile(new StorageCompartment("8", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 4);
        shipboard.weldComponentTile(new StorageCompartment("9", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 5);
        shipboard.weldComponentTile(new SpecialStorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 4, 4 );
    }

    private void setUpShipboardF(Shipboard shipboard) {
        shipboard.weldComponentTile(new BatteryComponent("0", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 50), 2, 2);
        shipboard.weldComponentTile(new DoubleEngine("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 1);
        shipboard.weldComponentTile(new DoubleEngine("2", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 2);
        shipboard.weldComponentTile(new DoubleEngine("3", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 3);
        shipboard.weldComponentTile(new DoubleEngine("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 4);
        shipboard.weldComponentTile(new DoubleEngine("5", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 5);
    }

    private void setUpShipboardG(Shipboard shipboard) {
        shipboard.weldComponentTile(new DoubleCannon("32", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH), 0, 4);
        shipboard.weldComponentTile(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE), 1, 1);
        shipboard.weldComponentTile(new BatteryComponent("15", Side.TWOPIPES, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.TWOPIPES, 2), 1, 4);
        shipboard.getComponentTileFromGrid(1, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new Cannon("20", Side.SMOOTH, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.SMOOTH), 1, 5);
        shipboard.weldComponentTile(new Cannon("21", Side.SMOOTH, Side.ONEPIPE, Side.TWOPIPES, Side.SMOOTH), 2, 0);
        shipboard.getComponentTileFromGrid(2, 0).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new BatteryComponent("22", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, 2), 2, 1);
        shipboard.weldComponentTile(new SpecialStorageCompartment("11", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, 2), 2, 2);
        shipboard.weldComponentTile(new StorageCompartment("12", Side.ONEPIPE, Side.ONEPIPE, Side.UNIVERSALPIPE, Side.TWOPIPES, 2), 2, 4);
        shipboard.getComponentTileFromGrid(2, 4).get().rotateClockwise();
        shipboard.weldComponentTile(new ShieldGenerator("18", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 2, 5);
        shipboard.weldComponentTile(new Cannon("23", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.UNIVERSALPIPE), 2, 6);
        shipboard.getComponentTileFromGrid(2, 6).get().rotateClockwise();
        shipboard.weldComponentTile(new ShieldGenerator("24", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH), 3, 0);
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.weldComponentTile(new SpecialStorageCompartment("25", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, 1), 3, 2);
        shipboard.weldComponentTile(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE), 3, 3);
        shipboard.weldComponentTile(new RegularCabin("26", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE), 3, 4);
        shipboard.getComponentTileFromGrid(3, 4).get().putAstronauts();
        shipboard.weldComponentTile(new StructuralModule("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.ONEPIPE, Side.TWOPIPES), 3, 5);
        shipboard.weldComponentTile(new BatteryComponent("27", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, 3), 3, 6);
        shipboard.getComponentTileFromGrid(3, 6).get().rotateCounterClockwise();
        shipboard.weldComponentTile(new DoubleEngine("1", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 1);
        shipboard.weldComponentTile(new DoubleEngine("28", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH), 4, 2);
        shipboard.weldComponentTile(new Engine("29", Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 4);
        shipboard.weldComponentTile(new Cannon("30", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES), 4, 5);
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.weldComponentTile(new Engine("31", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH), 4, 6);

    }

}
