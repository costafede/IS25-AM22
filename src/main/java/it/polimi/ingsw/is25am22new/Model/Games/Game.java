package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.GamePhase.*;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Hourglass;
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

        for (ObserverModel observer : observers) {
            this.addObserver(observer);
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
        if(tile.isEmpty() || tile.get().getCrewNumber() > 0 || !tile.get().isCabin() || shipboard.isAlienPlaceable(i, j, "brown"))
            throw new IllegalArgumentException("Cannot place Brown Alien at the given coordinates");
        tile.get().putAlien("brown");
        gamePhase.trySwitchToNextPhase();
        updateAllGamePhase(gamePhase);
        updateAllShipboard(nickname, shipboards.get(nickname));
    }

    public void placePurpleAlien(String nickname, int i, int j) {
        Shipboard shipboard = shipboards.get(nickname);
        Optional<ComponentTile> tile = shipboard.getComponentTileFromGrid(i, j);
        if(tile.isEmpty() || tile.get().getCrewNumber() > 0 || !tile.get().isCabin() || shipboard.isAlienPlaceable(i, j, "purple"))
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
        return hourglass.getRemainingSeconds() > 0;
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
        int shipboardNumber = Integer.parseInt(conf.substring(0, 1));
        String deckConfig = conf.length() > 1 ? conf.substring(1) : "";

        setUpShipboardConfig(player, shipboardNumber);

        setUpDeckConfig(deckConfig);

        updateAllGame(this);
    }

    private void setUpShipboardConfig(String player, int shipboardNumber) {
        Shipboard oldShipboard = shipboards.get(player);

        // Create new shipboard and store it in the map
        Shipboard newShipboard = new Shipboard(oldShipboard.getColor(), oldShipboard.getNickname(), bank);
        newShipboard.setNewStandbyComponent();

        switch (shipboardNumber) {
            case 1 -> setUpShipboard1(newShipboard); // Level2
            case 2 -> setUpShipboard2(newShipboard); // Tutorial
            default -> throw new IllegalArgumentException("Invalid shipboard number: " + shipboardNumber);
        }

        // Update the map with the new shipboard
        shipboards.put(player, newShipboard);

        // Notify observers
        updateAllShipboard(player, newShipboard);
    }

    private void setUpDeckConfig(String deckConfig) {
        deck.clear();

        if(!deckConfig.isEmpty()){
            String[] pngNames = deckConfig.split(",");

            for(String pngName : pngNames){
                for(AdventureCard card : cardArchive){
                    if(card.getPngName().equals(pngName.trim())){
                        deck.add(card);
                        break;
                    }
                }
            }
        } else {
            initDeck();
        }

        updateAllDeck(deck);
    }

    private void setUpShipboard1(Shipboard shipboard) {
        List<ComponentTile> tiles = initializeTiles();
        shipboard.setStandbyComponent(0, Optional.empty());
        shipboard.setStandbyComponent(1, Optional.empty());

        shipboard.weldComponentTile(tiles.get(3), 0, 2);
        shipboard.weldComponentTile(tiles.get(32), 0, 4);
        shipboard.weldComponentTile(tiles.get(2), 1, 1);
        shipboard.weldComponentTile(tiles.get(9), 1, 2);
        shipboard.getComponentTileFromGrid(1, 2).get().putAlien("purple");
        shipboard.weldComponentTile(tiles.get(7), 1, 3);
        shipboard.weldComponentTile(tiles.get(15), 1, 4);
        shipboard.getComponentTileFromGrid(1, 4).get().rotateClockwise();
        shipboard.weldComponentTile(tiles.get(20), 1, 5);
        shipboard.weldComponentTile(tiles.get(21), 2, 0);
        shipboard.getComponentTileFromGrid(2, 0).get().rotateCounterClockwise();
        shipboard.weldComponentTile(tiles.get(22), 2, 1);
        shipboard.weldComponentTile(tiles.get(11), 2, 2);
        shipboard.weldComponentTile(tiles.get(12), 2, 4);
        shipboard.getComponentTileFromGrid(2, 4).get().rotateClockwise();
        shipboard.weldComponentTile(tiles.get(18), 2, 5);
        shipboard.weldComponentTile(tiles.get(23), 2, 6);
        shipboard.getComponentTileFromGrid(2, 6).get().rotateClockwise();
        shipboard.weldComponentTile(tiles.get(24), 3, 0);
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(3, 0).get().rotateClockwise();
        shipboard.weldComponentTile(tiles.get(14), 3, 1);
        shipboard.getComponentTileFromGrid(3, 1).get().putAstronauts();
        shipboard.weldComponentTile(tiles.get(25), 3, 2);
        shipboard.weldComponentTile(tiles.get(0), 3, 3);
        shipboard.weldComponentTile(tiles.get(26), 3, 4);
        shipboard.getComponentTileFromGrid(3, 4).get().putAstronauts();
        shipboard.weldComponentTile(tiles.get(4), 3, 5);
        shipboard.weldComponentTile(tiles.get(27), 3, 6);
        shipboard.getComponentTileFromGrid(3, 6).get().rotateCounterClockwise();
        shipboard.weldComponentTile(tiles.get(1), 4, 1);
        shipboard.weldComponentTile(tiles.get(28), 4, 2);
        shipboard.weldComponentTile(tiles.get(29), 4, 4);
        shipboard.weldComponentTile(tiles.get(30), 4, 5);
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.getComponentTileFromGrid(4, 5).get().rotateClockwise();
        shipboard.weldComponentTile(tiles.get(31), 4, 6);

    }

    private void setUpShipboard2(Shipboard shipboard) {
        List<ComponentTile> tiles = initializeTiles();
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

    private List<ComponentTile> initializeTiles(){
        List<ComponentTile> tiles = new ArrayList<>();
        tiles.add(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE));
        tiles.add(new DoubleEngine("1", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE));
        tiles.add(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE));
        tiles.add(new DoubleCannon("3", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH));
        tiles.add(new StructuralModule("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.ONEPIPE, Side.TWOPIPES));
        tiles.add(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("6", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new AlienAddon("7", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES, "purple"));
        tiles.add(new AlienAddon("8", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, "brown"));
        tiles.add(new RegularCabin("9", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES));
        tiles.add(new StorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new SpecialStorageCompartment("11", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, 2));
        tiles.add(new StorageCompartment("12", Side.ONEPIPE, Side.ONEPIPE, Side.UNIVERSALPIPE, Side.TWOPIPES, 2));
        tiles.add(new StorageCompartment("13", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new RegularCabin("14", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH));
        tiles.add(new BatteryComponent("15", Side.TWOPIPES, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.TWOPIPES, 2));
        tiles.add(new BatteryComponent("16", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new BatteryComponent("17", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, 4));
        tiles.add(new ShieldGenerator("18", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE));
        tiles.add(new DoubleCannon("19", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH));
        tiles.add(new Cannon("20", Side.SMOOTH, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.SMOOTH));
        tiles.add(new Cannon("21", Side.SMOOTH, Side.ONEPIPE, Side.TWOPIPES, Side.SMOOTH));
        tiles.add(new BatteryComponent("22", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, 2));
        tiles.add(new Cannon("23", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.UNIVERSALPIPE));
        tiles.add(new ShieldGenerator("24", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.SMOOTH));
        tiles.add(new SpecialStorageCompartment("25", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, 1));
        tiles.add(new RegularCabin("26", Side.TWOPIPES, Side.TWOPIPES, Side.ONEPIPE, Side.ONEPIPE));
        tiles.add(new BatteryComponent("27", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, 3));
        tiles.add(new DoubleEngine("28", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH));
        tiles.add(new Engine("29", Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE));
        tiles.add(new Cannon("30", Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES, Side.TWOPIPES));
        tiles.add(new Engine("31", Side.TWOPIPES, Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH));
        tiles.add(new DoubleCannon("32", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH));

        return tiles;
    }

}
