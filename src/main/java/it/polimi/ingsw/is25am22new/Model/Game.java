package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;

import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class Game implements ModelInterface {
    private final List<String> playerList;
    private Bank bank;
    private List<ComponentTile> coveredComponentTiles;
    private List<ComponentTile> uncoveredComponentTiles;
    private Map<String, Shipboard> shipboards;
    private Flightboard flightboard;
    private List<AdventureCard> cardArchive;
    private Hourglass hourglass;

    public Game() {
        playerList = new ArrayList<>();
        bank = new Bank();
        cardArchive = new ArrayList<>();
        coveredComponentTiles = new ArrayList<>();
        uncoveredComponentTiles = new ArrayList<>();
        hourglass = new Hourglass(60);
        //shipboards = new HashMap<>();
    }

    public void initGame(){
        ObjectMapper objectMapper = new ObjectMapper();
        initComponent(objectMapper);
        initCardArchive(objectMapper);
    }

    public ComponentTile pickCoveredTile() {
        return coveredComponentTiles.remove(new Random().nextInt(coveredComponentTiles.size()));
    }

    public ComponentTile pickUncoveredTile(int index) {
        return uncoveredComponentTiles.remove(index);
    }

    public void weldComponentTile(String nickname, ComponentTile ct, int x, int y) {
        shipboards.get(nickname).weldComponentTile(ct, x, y);
    }

    public void standbyComponentTile(String nickname, ComponentTile ct) {
        shipboards.get(nickname).standbyComponentTile(ct);
    }

    public ComponentTile pickStandByComponentTile(String nickname, int index) {
        return shipboards.get(nickname).pickStandByComponentTile(index);
    }

    public void discardComponentTile(ComponentTile ct) {
        uncoveredComponentTiles.add(ct);
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
        hourglass.startTimer(callbackMethod);
    }

    public AdventureCard pickCard() {
        // Supposed that cardArchive is already in a random order
        // to be implemented
        return cardArchive.remove(new Random().nextInt(cardArchive.size()));
    }

    public void chooseToAbandon(String nickname) {
        shipboards.get(nickname).abandons();
    }

    //public void findShipWrecks(String nickname) {
        // to be implemented
    //}

    public void destroyTile(String nickname, int x, int y) {
        shipboards.get(nickname).destroyTile(x, y);
    }

    private void initComponent(ObjectMapper objectMapper) {
        initBatteryComponent(objectMapper);
        initStartingCabin(objectMapper);
        initRegularCabin(objectMapper);
        initAlienAddon(objectMapper);
        initStorageCompartments(objectMapper);
        initEngine(objectMapper);
        initDoubleEngine(objectMapper);
        initDoubleCannon(objectMapper);
        initCannon(objectMapper);
        initStructuralModule(objectMapper);
        initShieldGenerator(objectMapper);
        initSpecialStorageCompartment(objectMapper);
    }

    protected void initCardArchive(ObjectMapper objectMapper){
        initAbandonedShipCard(objectMapper);
        initAbandonedStationCard(objectMapper);
        initCombatZoneCard(objectMapper);
        initEpidemicCard(objectMapper);
        initMeteorSwarmCard(objectMapper);
        initOpenSpaceCard(objectMapper);
        initPiratesCard(objectMapper);
        initPlanetsCard(objectMapper);
        initSlaversCard(objectMapper);
        initSmugglersCard(objectMapper);
        initStardustCard(objectMapper);
    }

    protected abstract void initDeck(ObjectMapper objectMapper);

    private void initAbandonedShipCard(ObjectMapper objectMapper) {

    }

    private void initAbandonedStationCard(ObjectMapper objectMapper) {

    }

    private void initCombatZoneCard(ObjectMapper objectMapper) {

    }

    private void initEpidemicCard(ObjectMapper objectMapper) {

    }

    private void initMeteorSwarmCard(ObjectMapper objectMapper) {

    }

    private void initOpenSpaceCard(ObjectMapper objectMapper) {

    }

    private void initPiratesCard(ObjectMapper objectMapper) {

    }

    private void initPlanetsCard(ObjectMapper objectMapper) {

    }

    private void initSlaversCard(ObjectMapper objectMapper) {

    }

    private void initSmugglersCard(ObjectMapper objectMapper) {

    }

    private void initStardustCard(ObjectMapper objectMapper) {

    }

    private void initBatteryComponent(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/BatteryComponent.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                int numOfBatteries = node.get("numOfBatteries").asInt();
                BatteryComponent batteryComponent = new BatteryComponent(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), numOfBatteries);
                coveredComponentTiles.add(batteryComponent);
            }
        }catch (IOException e){
            System.out.println("Error in reading BatteryComponent.json");
        }
    }

    private void initStartingCabin(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/StartingCabin.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                String color = node.get("color").asText();
                ComponentTile startingCabin = new StartingCabin(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), color);
                coveredComponentTiles.add(startingCabin);
            }
        }catch (IOException e){
            System.out.println("Error in reading StartingCabin.json");
        }
    }

    private void initRegularCabin(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/RegularCabin.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile regularCabin = new RegularCabin(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(regularCabin);
            }
        }catch (IOException e){
            System.out.println("Error in reading RegularCabin.json");
        }
    }

    private void initAlienAddon(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AlienAddon.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                String color = node.get("color").asText();
                ComponentTile alienAddon = new AlienAddon(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), color);
                coveredComponentTiles.add(alienAddon);
            }
        }catch (IOException e){
            System.out.println("Error in reading AlienAddon.json");
        }
    }

    private void initStorageCompartments(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/StorageCompartment.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                int capacity = node.get("capacity").asInt();
                ComponentTile storageCompartment = new StorageCompartment(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), capacity);
                coveredComponentTiles.add(storageCompartment);
            }
        }catch (IOException e){
            System.out.println("Error in reading StorageCompartment.json");
        }
    }

    private void initEngine(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/Engine.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile engine = new Engine(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(engine);
            }
        }catch (IOException e){
            System.out.println("Error in reading Engine.json");
        }
    }

    private void initDoubleEngine(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/DoubleEngine.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile doubleEngine = new DoubleEngine(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(doubleEngine);
            }
        }catch (IOException e){
            System.out.println("Error in reading DoubleEngine.json");
        }
    }

    private void initDoubleCannon(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/DoubleCannon.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile doubleCannon = new DoubleCannon(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(doubleCannon);
            }
        }catch (IOException e){
            System.out.println("Error in reading DoubleCannon.json");
        }
    }

    private void initCannon(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/Cannon.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile cannon = new Cannon(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(cannon);
            }
        }catch (IOException e){
            System.out.println("Error in reading Cannon.json");
        }
    }

    private void initStructuralModule(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/StructuralModule.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile structuralModule = new StructuralModule(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(structuralModule);
            }
        }catch (IOException e){
            System.out.println("Error in reading StructuralModule.json");
        }
    }

    private void initShieldGenerator(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ShieldGenerator.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile shieldGenerator = new ShieldGenerator(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                coveredComponentTiles.add(shieldGenerator);
            }
        }catch (IOException e){
            System.out.println("Error in reading ShieldGenerator.json");
        }
    }

    private void initSpecialStorageCompartment(ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/SpecialStorageCompartment.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                int capacity = node.get("capacity").asInt();
                ComponentTile specialStorageCompartment = new SpecialStorageCompartment(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), capacity);
                coveredComponentTiles.add(specialStorageCompartment);
            }
        }catch (IOException e){
            System.out.println("Error in reading SpecialStorageCompartment.json");
        }
    }

    public List<ComponentTile> getCoveredComponentTiles() {
        return coveredComponentTiles;
    }

    public List<ComponentTile> getUncoveredComponentTiles() {
        return uncoveredComponentTiles;
    }

    //public List<AdventureCard> getCardArchive() {
    //    return cardArchive;
    //}

    public Flightboard getFlightboard(){
        return flightboard;
    }

    public Shipboard getShipboards(String player) {
        return shipboards.get(player);
    }

    public List<String> getPlayerList() {
        return playerList;
    }
}
