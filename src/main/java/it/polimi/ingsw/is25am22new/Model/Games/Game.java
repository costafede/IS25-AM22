package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.*;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;

import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;

import java.io.File;
import java.io.IOException;

public abstract class Game implements ModelInterface {
    protected final List<String> playerList;
    protected Bank bank;
    protected List<ComponentTile> coveredComponentTiles;
    protected List<ComponentTile> uncoveredComponentTiles;
    protected Map<String, Shipboard> shipboards;
    protected Flightboard flightboard;
    protected List<AdventureCard> cardArchive;
    protected Hourglass hourglass;
    protected List<AdventureCard> deck;

    public Game(List<String> playerList) {
        this.playerList = playerList;
        bank = new Bank();
        cardArchive = new ArrayList<>();
        coveredComponentTiles = new ArrayList<>();
        uncoveredComponentTiles = new ArrayList<>();
        hourglass = new Hourglass(60);
        shipboards = new HashMap<>();

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
        return deck.remove(new Random().nextInt(deck.size()));
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

    protected abstract Map<String, Integer> endGame();

    protected String betterShipboard() {
        //to be implemented
        //return nickname;
        return "";
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
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AbandonedShipCard.json"));
            for (JsonNode node : rootNode) {
                AbandonedShipCard asc = new AbandonedShipCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        this,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("credits").asInt(),
                        node.get("lostAstronauts").asInt()
                );
                cardArchive.add(asc);
            }
        }
        catch (IOException e){
            System.out.println("Error in reading AbandonedShipCard.json");
        }
    }

    private void initAbandonedStationCard(ObjectMapper objectMapper) {
        try{
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AbandonedStationCard.json"));
            for(JsonNode node : jsonNode){
                List<GoodBlock> goodBlocks = new ArrayList<>();
                JsonNode goodBlocksNode = node.get("goodBlocks");
                for(JsonNode goodBlockNode: goodBlocksNode){
                    GoodBlock goodBlock = GoodBlock.valueOf(goodBlockNode.asText());
                    goodBlocks.add(goodBlock);
                }
                AbandonedStationCard asc = new AbandonedStationCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        this,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("astronautsNumber").asInt(),
                        goodBlocks
                );
                cardArchive.add(asc);
            }
        }
        catch (IOException e){
            System.out.println("Error in reading AbandonedShipCard.json");
        }
    }

    private void initCombatZoneCard(ObjectMapper objectMapper) {
        initCombatZoneCard1(objectMapper);
        initCombatZoneCard2(objectMapper);
    }

    private void initCombatZoneCard1(ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/CombatZoneCard1.json"));
            for (JsonNode node : jsonNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                int lostAstronauts = node.get("lostAstronauts").asInt();
                JsonNode shotSizeNode = node.get("shotSize");
                boolean[] shotSize = new boolean[shotSizeNode.size()];
                for (int i = 0; i < shotSizeNode.size(); i++) {
                    shotSize[i] = shotSizeNode.get(i).asBoolean();
                }
                JsonNode shotOrientationNode = node.get("shotOrientation");
                String[] shotOrientation = new String[shotOrientationNode.size()];
                for (int i = 0; i < shotOrientationNode.size(); i++) {
                    shotOrientation[i] = shotOrientationNode.get(i).asText();
                }
                Map<Integer, Shot> numberToShot = new HashMap<>();
                for (int i = 0; i < shotSize.length; i++) {
                    numberToShot.put(i, new Shot(shotSize[i], Orientation.valueOf(shotOrientation[i])));
                }
                CombatZoneCard czc = new CombatZoneCard(pngName, name, this, level, tutorial, flightDaysLost, lostAstronauts, 0, numberToShot);
                cardArchive.add(czc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard1.json");
        }
    }

    private void initCombatZoneCard2(ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/CombatZoneCard2.json"));
            for (JsonNode node : jsonNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                int lostGoods = node.get("lostGoods").asInt();
                JsonNode shotSizeNode = node.get("shotSize");
                boolean[] shotSize = new boolean[shotSizeNode.size()];
                for (int i = 0; i < shotSizeNode.size(); i++) {
                    shotSize[i] = shotSizeNode.get(i).asBoolean();
                }
                JsonNode shotOrientationNode = node.get("shotOrientation");
                String[] shotOrientation = new String[shotOrientationNode.size()];
                for (int i = 0; i < shotOrientationNode.size(); i++) {
                    shotOrientation[i] = shotOrientationNode.get(i).asText();
                }
                Map<Integer, Shot> numberToShot = new HashMap<>();
                for (int i = 0; i < shotSize.length; i++) {
                    numberToShot.put(i, new Shot(shotSize[i], Orientation.valueOf(shotOrientation[i])));
                }
                CombatZoneCard czc = new CombatZoneCard(pngName, name, this, level, tutorial, flightDaysLost, 0, lostGoods, numberToShot);
                cardArchive.add(czc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard1.json");
        }
    }

    private void initEpidemicCard(ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/EpidemicCard.json"));
            for (JsonNode node : jsonNode) {
                EpidemicCard ec = new EpidemicCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        this,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );
                cardArchive.add(ec);
            }
        } catch (Exception e) {
            System.out.println("Error in reading EpidemicCard.json");
        }
    }

    private void initMeteorSwarmCard(ObjectMapper objectMapper) {
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/MeteorSwarmCard.json"));
            for(JsonNode node: rootNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                JsonNode meteorSize = node.get("meteorSize");
                Boolean[] meteor = new Boolean[meteorSize.size()];
                for (int i = 0; i < meteorSize.size(); i++) {
                    meteor[i] = meteorSize.get(i).asBoolean();
                }
                JsonNode meteorOrientation = node.get("meteorOrientation");
                String[] orientation = new String[meteorOrientation.size()];
                for (int i = 0; i < meteorOrientation.size(); i++) {
                    orientation[i] = meteorOrientation.get(i).asText();
                }
                Map<Integer, Meteor> numberToMeteor = new HashMap<>();

                for (int i = 0; i < meteorSize.size(); i++) {
                    numberToMeteor.put(i, new Meteor(meteor[i], Orientation.valueOf(orientation[i])));
                }
                MeteorSwarmCard msc = new MeteorSwarmCard(pngName, name, this, level, tutorial, numberToMeteor);
                cardArchive.add(msc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading MeteorSwarmCard.json");
        }
    }

    private void initOpenSpaceCard(ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/OpenSpaceCard.json"));
            for (JsonNode node : jsonNode) {
                OpenSpaceCard ec = new OpenSpaceCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        this,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );
                cardArchive.add(ec);
            }
        } catch (Exception e) {
            System.out.println("Error in reading OpenSpaceCard.json");
        }
    }

    private void initPiratesCard(ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/PiratesCard.json"));
            for (JsonNode node : jsonNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                int cannonStrength = node.get("cannonStrength").asInt();
                int credits = node.get("credits").asInt();
                JsonNode shotSizeNode = node.get("shotSize");
                boolean[] shotSize = new boolean[shotSizeNode.size()];
                for (int i = 0; i < shotSizeNode.size(); i++) {
                    shotSize[i] = shotSizeNode.get(i).asBoolean();
                }
                Map<Integer, Shot> numberToShot = new HashMap<>();
                for (int i = 0; i < shotSize.length; i++) {
                    numberToShot.put(i, new Shot(shotSize[i], Orientation.valueOf("BOTTOM")));
                }

                PiratesCard pc = new PiratesCard(pngName, name, this, level, tutorial, numberToShot, flightDaysLost, cannonStrength, credits);
                cardArchive.add(pc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading PiratesCard.json");
        }
    }

    private void initPlanetsCard(ObjectMapper objectMapper) {
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/PlanetsCard.json"));
            for (JsonNode node : rootNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int numOfPlanets = node.get("numOfPlanets").asInt();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                Map<Integer, List<GoodBlock>> planetToGoodBlocks = new HashMap<>();
                for (int i = 0; i < numOfPlanets; i++) {
                    List<GoodBlock> goodBlocks = new ArrayList<>();
                    JsonNode goodBlocksNode = node.get(i + "PlanetGoods");
                    for (JsonNode goodBlockNode : goodBlocksNode) {
                        GoodBlock goodBlock = GoodBlock.valueOf(goodBlockNode.asText());
                        goodBlocks.add(goodBlock);
                    }
                    planetToGoodBlocks.put(i, goodBlocks);
                }
                PlanetsCard card = new PlanetsCard(pngName, name, this, level, tutorial, planetToGoodBlocks, flightDaysLost);
                cardArchive.add(card);
            }
        } catch (IOException e) {
            System.out.println("Error in reading PlanetsCard.json");
        }
    }

    private void initSlaversCard(ObjectMapper objectMapper) {
        boolean check = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/SlaversCard.json"));
            for (JsonNode node : jsonNode) {
                SlaversCard sc = new SlaversCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        this,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("cannonStrength").asInt(),
                        node.get("lostAstronauts").asInt(),
                        node.get("credits").asInt()
                );
                cardArchive.add(sc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading SlaversCard.json");
        }
    }

    private void initSmugglersCard(ObjectMapper objectMapper) {
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/SmugglersCard.json"));
            for(JsonNode node: rootNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                int cannonStrength = node.get("cannonStrength").asInt();
                int lostGoods = node.get("lostGoods").asInt();
                List<GoodBlock> goodBlocks = new ArrayList<>();
                JsonNode goodBlocksNode = node.get("goodBlocks");
                for (JsonNode goodBlockNode : goodBlocksNode) {
                    GoodBlock goodBlock = GoodBlock.valueOf(goodBlockNode.asText());
                    goodBlocks.add(goodBlock);
                }

                SmugglersCard card = new SmugglersCard(pngName, name, this, level, tutorial, flightDaysLost, cannonStrength, lostGoods, goodBlocks);
                cardArchive.add(card);
            }
        }catch (IOException e){
            System.out.println("Error in reading SmugglersCard.json");
        }
    }

    private void initStardustCard(ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/StardustCard.json"));
            for (JsonNode node : jsonNode) {
                StardustCard ec = new StardustCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        this,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );
                cardArchive.add(ec);
            }
        } catch (Exception e) {
            System.out.println("Error in reading StardustCard.json");
        }
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
}
