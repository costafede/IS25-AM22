package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard.MeteorSwarmCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard.PiratesCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.Planet;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard.PlanetsCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard.SlaversCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard.SmugglersCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard.StardustCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInitializer {

    protected static void initComponent(Game game, ObjectMapper objectMapper) {
        initBatteryComponent(game, objectMapper);
        //initStartingCabin(game, objectMapper);
        initRegularCabin(game, objectMapper);
        initAlienAddon(game, objectMapper);
        initStorageCompartments(game, objectMapper);
        initEngine(game, objectMapper);
        initDoubleEngine(game, objectMapper);
        initDoubleCannon(game, objectMapper);
        initCannon(game, objectMapper);
        initStructuralModule(game, objectMapper);
        initShieldGenerator(game, objectMapper);
        initSpecialStorageCompartment(game, objectMapper);
    }

    protected static void initCardArchive(Game game, ObjectMapper objectMapper){
        initAbandonedShipCard(game, objectMapper);
        initAbandonedStationCard(game, objectMapper);
        initCombatZoneCard(game, objectMapper);
        initEpidemicCard(game, objectMapper);
        initMeteorSwarmCard(game, objectMapper);
        initOpenSpaceCard(game, objectMapper);
        initPiratesCard(game, objectMapper);
        initPlanetsCard(game, objectMapper);
        initSlaversCard(game, objectMapper);
        initSmugglersCard(game, objectMapper);
        initStardustCard(game, objectMapper);
    }

    private static void initAbandonedShipCard(Game game, ObjectMapper objectMapper) {
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/AbandonedShipCard.json"));
            for (JsonNode node : rootNode) {
                AbandonedShipCard asc = new AbandonedShipCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("credits").asInt(),
                        node.get("lostAstronauts").asInt()
                );
                game.getCardArchive().add(asc);
            }
        }
        catch (IOException e){
            System.out.println("Error in reading AbandonedShipCard.json");
        }
    }

    private static void initAbandonedStationCard(Game game, ObjectMapper objectMapper) {
        try{
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/AbandonedStationCard.json"));
            for(JsonNode node : jsonNode){
                Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
                JsonNode goodBlocksNode = node.get("goodBlocks");
                for(JsonNode goodBlockNode: goodBlocksNode){
                    GoodBlock goodBlock = GoodBlock.valueOf(goodBlockNode.asText());
                    theoreticalGoodBlocks.put(goodBlock, theoreticalGoodBlocks.getOrDefault(goodBlock, 0) + 1);
                }
                AbandonedStationCard asc = new AbandonedStationCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("astronautsNumber").asInt(),
                        theoreticalGoodBlocks

                );
                game.getCardArchive().add(asc);
            }
        }
        catch (IOException e){
            System.out.println("Error in reading AbandonedShipCard.json");
        }
    }

    private static void initCombatZoneCard(Game game, ObjectMapper objectMapper) {
        initCombatZoneCard1(game, objectMapper);
        initCombatZoneCard2(game, objectMapper);
    }

    private static void initCombatZoneCard1(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/CombatZoneCard1.json"));
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
                CombatZoneCard czc = new CombatZoneCard(pngName, name, game, level, tutorial, flightDaysLost, lostAstronauts, 0, numberToShot);
                game.getCardArchive().add(czc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard1.json");
        }
    }

    private static void initCombatZoneCard2(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/CombatZoneCard2.json"));
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
                CombatZoneCard czc = new CombatZoneCard(pngName, name, game, level, tutorial, flightDaysLost, 0, lostGoods, numberToShot);
                game.getCardArchive().add(czc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard1.json");
        }
    }

    private static void initEpidemicCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/EpidemicCard.json"));
            for (JsonNode node : jsonNode) {
                EpidemicCard ec = new EpidemicCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );
                game.getCardArchive().add(ec);
            }
        } catch (Exception e) {
            System.out.println("Error in reading EpidemicCard.json");
        }
    }

    private static void initMeteorSwarmCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/MeteorSwarmCard.json"));
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
                MeteorSwarmCard msc = new MeteorSwarmCard(pngName, name, game, level, tutorial, numberToMeteor);
                game.getCardArchive().add(msc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading MeteorSwarmCard.json");
        }
    }

    private static void initOpenSpaceCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/OpenSpaceCard.json"));
            for (JsonNode node : jsonNode) {
                OpenSpaceCard ec = new OpenSpaceCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );
                game.getCardArchive().add(ec);
            }
        } catch (Exception e) {
            System.out.println("Error in reading OpenSpaceCard.json");
        }
    }

    private static void initPiratesCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/PiratesCard.json"));
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

                PiratesCard pc = new PiratesCard(pngName, name, game, level, tutorial, numberToShot, flightDaysLost, cannonStrength, credits);
                game.getCardArchive().add(pc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading PiratesCard.json");
        }
    }

    private static void initPlanetsCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/PlanetsCard.json"));
            for (JsonNode node : rootNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int numOfPlanets = node.get("numOfPlanets").asInt();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                List<Planet> planets = new ArrayList<>();

                // Create planets with their goods
                for (int i = 0; i < numOfPlanets; i++) {
                    String planetGoodsKey = i + "PlanetGoods";
                    if (node.has(planetGoodsKey)) {
                        Map<GoodBlock, Integer> planetGoods = new HashMap<>();
                        JsonNode goodsArray = node.get(planetGoodsKey);

                        // Count occurrences of each GoodBlock
                        for (JsonNode goodNode : goodsArray) {
                            GoodBlock goodBlock = GoodBlock.valueOf(goodNode.asText());
                            planetGoods.put(goodBlock, planetGoods.getOrDefault(goodBlock, 0) + 1);
                        }

                        Planet planet = new Planet(planetGoods);
                        planets.add(planet);
                    }
                }

                PlanetsCard card = new PlanetsCard(pngName, name, game, level, tutorial, planets, flightDaysLost);
                game.getCardArchive().add(card);
            }
        } catch (IOException e) {
            System.out.println("Error in reading PlanetsCard.json");
        }
    }

    private static void initSlaversCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/SlaversCard.json"));
            for (JsonNode node : jsonNode) {
                SlaversCard sc = new SlaversCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("cannonStrength").asInt(),
                        node.get("lostAstronauts").asInt(),
                        node.get("credits").asInt()
                );
                game.getCardArchive().add(sc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading SlaversCard.json");
        }
    }

    private static void initSmugglersCard(Game game, ObjectMapper objectMapper) {
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/SmugglersCard.json"));
            for(JsonNode node: rootNode) {
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                int cannonStrength = node.get("cannonStrength").asInt();
                int lostGoods = node.get("lostGoods").asInt();
                Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
                JsonNode goodBlocksNode = node.get("goodBlocks");
                for(JsonNode goodBlockNode: goodBlocksNode){
                    GoodBlock goodBlock = GoodBlock.valueOf(goodBlockNode.asText());
                    theoreticalGoodBlocks.put(goodBlock, theoreticalGoodBlocks.getOrDefault(goodBlock, 0) + 1);
                }

                SmugglersCard card = new SmugglersCard(pngName, name, game, level, tutorial, flightDaysLost, cannonStrength, lostGoods, theoreticalGoodBlocks);
                game.getCardArchive().add(card);
            }
        }catch (IOException e){
            System.out.println("Error in reading SmugglersCard.json");
        }
    }

    private static void initStardustCard(Game game, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AdventureCards/StardustCard.json"));
            for (JsonNode node : jsonNode) {
                StardustCard ec = new StardustCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );
                game.getCardArchive().add(ec);
            }
        } catch (Exception e) {
            System.out.println("Error in reading StardustCard.json");
        }
    }

    private static void initBatteryComponent(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/BatteryComponent.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                int numOfBatteries = node.get("numOfBatteries").asInt();
                BatteryComponent batteryComponent = new BatteryComponent(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), numOfBatteries);
                game.getCoveredComponentTiles().add(batteryComponent);
            }
        }catch (IOException e){
            System.out.println("Error in reading BatteryComponent.json");
        }
    }

    private static void initStartingCabin(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/StartingCabin.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                String color = node.get("color").asText();
                ComponentTile startingCabin = new StartingCabin(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), color);
                game.getCoveredComponentTiles().add(startingCabin);
            }
        }catch (IOException e){
            System.out.println("Error in reading StartingCabin.json");
        }
    }

    private static void initRegularCabin(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/RegularCabin.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile regularCabin = new RegularCabin(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(regularCabin);
            }
        }catch (IOException e){
            System.out.println("Error in reading RegularCabin.json");
        }
    }

    private static void initAlienAddon(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/AlienAddon.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                String color = node.get("color").asText();
                ComponentTile alienAddon = new AlienAddon(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), color);
                game.getCoveredComponentTiles().add(alienAddon);
            }
        }catch (IOException e){
            System.out.println("Error in reading AlienAddon.json");
        }
    }

    private static void initStorageCompartments(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/StorageCompartment.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                int capacity = node.get("capacity").asInt();
                ComponentTile storageCompartment = new StorageCompartment(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), capacity);
                game.getCoveredComponentTiles().add(storageCompartment);
            }
        }catch (IOException e){
            System.out.println("Error in reading StorageCompartment.json");
        }
    }

    private static void initEngine(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/Engine.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile engine = new Engine(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(engine);
            }
        }catch (IOException e){
            System.out.println("Error in reading Engine.json");
        }
    }

    private static void initDoubleEngine(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/DoubleEngine.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile doubleEngine = new DoubleEngine(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(doubleEngine);
            }
        }catch (IOException e){
            System.out.println("Error in reading DoubleEngine.json");
        }
    }

    private static void initDoubleCannon(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/DoubleCannon.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile doubleCannon = new DoubleCannon(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(doubleCannon);
            }
        }catch (IOException e){
            System.out.println("Error in reading DoubleCannon.json");
        }
    }

    private static void initCannon(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/Cannon.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile cannon = new Cannon(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(cannon);
            }
        }catch (IOException e){
            System.out.println("Error in reading Cannon.json");
        }
    }

    private static void initStructuralModule(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/StructuralModule.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile structuralModule = new StructuralModule(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(structuralModule);
            }
        }catch (IOException e){
            System.out.println("Error in reading StructuralModule.json");
        }
    }

    private static void initShieldGenerator(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/ShieldGenerator.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                ComponentTile shieldGenerator = new ShieldGenerator(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide));
                game.getCoveredComponentTiles().add(shieldGenerator);
            }
        }catch (IOException e){
            System.out.println("Error in reading ShieldGenerator.json");
        }
    }

    private static void initSpecialStorageCompartment(Game game, ObjectMapper objectMapper){
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/SpecialStorageCompartment.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String topSide = node.get("topSide").asText();
                String bottomSide = node.get("bottomSide").asText();
                String leftSide = node.get("leftSide").asText();
                String rightSide = node.get("rightSide").asText();
                int capacity = node.get("capacity").asInt();
                ComponentTile specialStorageCompartment = new SpecialStorageCompartment(pngName, Side.valueOf(topSide), Side.valueOf(bottomSide), Side.valueOf(leftSide), Side.valueOf(rightSide), capacity);
                game.getCoveredComponentTiles().add(specialStorageCompartment);
            }
        }catch (IOException e){
            System.out.println("Error in reading SpecialStorageCompartment.json");
        }
    }
}
