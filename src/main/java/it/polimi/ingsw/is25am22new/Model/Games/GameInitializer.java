package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.*;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard.AbandonedStationCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard.CombatZoneCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2.CombatZoneCard2;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The GameInitializer class is responsible for initializing and setting up the necessary components and cards for a game.
 * This class serves as a central hub for all initialization-related methods.
 *
 * The class includes methods to initialize different types of game cards, components, and modules, ensuring that the game
 * is properly configured and ready to play. These methods are statically defined and are intended to be used internally
 * within the application.
 */
public class GameInitializer {

    /**
     * Initializes various components required for the game. This method acts as a centralized
     * initializer for different game modules and components by invoking several specific
     * initialization methods.
     *
     * @param game the Game instance for which the components are to be initialized
     * @param objectMapper an ObjectMapper instance used for processing JSON data and mapping
     *                     configurations for the game's components
     */
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

    /**
     * Initializes the card archive for a given game. This method sets up various types of cards
     * such as abandoned ships, stations, combat zones, and other special cards needed for the game.
     *
     * @param game the Game instance to which the card archive will be initialized
     * @param objectMapper the ObjectMapper instance used for handling data serialization and deserialization of cards
     */
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

    /**
     * Initializes the data for the Abandoned Ship card from a JSON file and adds the cards to the game's card archive.
     *
     * @param game The game object that contains the card archive where the Abandoned Ship cards will be added.
     * @param objectMapper The ObjectMapper instance used for reading and mapping JSON data into Java objects.
     */
    private static void initAbandonedShipCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/AbandonedShipCard.json");
            if (is == null) {
                System.out.println("Error: AbandonedShipCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the abandoned station card by reading from a predefined JSON file
     * and adding the parsed data to the game's card archive. The method extracts card
     * properties such as name, level, tutorial status, flight days lost, number of astronauts,
     * and good blocks from the JSON file and creates card objects using the data.
     *
     * @param game the game object to which the created abandoned station cards will be added.
     * @param objectMapper an instance of ObjectMapper to read and parse the JSON file containing*/
    private static void initAbandonedStationCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/AbandonedStationCard.json");
            if (is == null) {
                System.out.println("Error: AbandonedStationCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for(JsonNode node : rootNode){
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

    /**
     * Initializes the combat zone cards within the game by invoking the sub-initialization methods.
     *
     * @param game          the Game instance where combat zone cards will be initialized
     * @param objectMapper  the ObjectMapper used for data binding or JSON processing during initialization
     */
    private static void initCombatZoneCard(Game game, ObjectMapper objectMapper) {
        initCombatZoneCard1(game, objectMapper);
        initCombatZoneCard2(game, objectMapper);
    }

    /**
     * Initializes and loads a specific set of combat zone cards (CombatZoneCard1) into the game's card archive
     * from a JSON file named "CombatZoneCard1.json".
     *
     * @param game the game instance where the combat zone cards will be added
     * @param objectMapper the object mapper used for parsing the JSON file
     */
    private static void initCombatZoneCard1(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is1 = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/CombatZoneCard1.json");
            if (is1 == null) {
                System.out.println("Error: CombatZoneCard1.json resource not found");
            }
            JsonNode rootNode = objectMapper.readTree(is1);
            for (JsonNode node : rootNode) {
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
                CombatZoneCard czc = new CombatZoneCard(pngName, name, game, level, tutorial, flightDaysLost, lostAstronauts, numberToShot);
                game.getCardArchive().add(czc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard1.json");
        }
    }

    /**
     * Initializes the second Combat Zone Card by reading data from the CombatZoneCard2.json file
     * and creating card objects which are added to the game's card archive.
     *
     * @param game The game instance where the Combat Zone Card will be added.
     * @param objectMapper The ObjectMapper instance used for parsing the JSON file.
     */
    private static void initCombatZoneCard2(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is2 = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/CombatZoneCard2.json");
            if (is2 == null) {
                throw new IOException("Resource not found: CombatZoneCard2.json");
            }
            JsonNode rootNode = objectMapper.readTree(is2);
            for (JsonNode node : rootNode) {
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
                CombatZoneCard2 czc = new CombatZoneCard2(pngName, name, game, level, tutorial, flightDaysLost, lostGoods, numberToShot);
                game.getCardArchive().add(czc);
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard2.json");
        }
    }

    /**
     * Initializes the Epidemic Card objects from a JSON file and adds them to the game's card archive.
     * The JSON file is read from the resource path `/JSONfiles/AdventureCards/EpidemicCard.json`.
     * Each entry in the JSON file is parsed to create an {@code EpidemicCard} object,
     * which is then added to the {@code game}'s card archive.
     * If the JSON file is missing or an error occurs during processing, an error message is printed.
     *
     * @param game the {@code Game} instance to which the created Epidemic Cards will be added
     * @param objectMapper an instance of {@code ObjectMapper} used to read and parse the JSON file
     */
    private static void initEpidemicCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/EpidemicCard.json");
            if (is == null) {
                System.out.println("Error: EpidemicCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for (JsonNode node : rootNode) {
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

    /**
     * Initializes and loads the MeteorSwarmCard data from a JSON configuration file
     * and adds the card to the game's card archive.
     *
     * @param game the game instance where the MeteorSwarmCard will be added
     * @param objectMapper the ObjectMapper instance used for reading and parsing the JSON file
     */
    private static void initMeteorSwarmCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/MeteorSwarmCard.json");
            if (is == null) {
                System.out.println("Error: MeteorSwarmCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes OpenSpaceCard objects and adds them to the game's card archive.
     * Reads OpenSpaceCard data from a predefined JSON file and constructs
     * the corresponding card instances.
     *
     * @param game the game object to which the OpenSpaceCard instances are added
     * @param objectMapper the ObjectMapper instance used to parse the JSON file
     */
    private static void initOpenSpaceCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/OpenSpaceCard.json");
            if (is == null) {
                System.out.println("Error: OpenSpaceCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for (JsonNode node : rootNode) {
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

    /**
     * Initializes the PiratesCard objects from the specified JSON file and adds them
     * to the game's card archive. Each card is configured with its attributes.
     *
     * @param game           the game instance to which the PiratesCards will be added
     * @param objectMapper   the ObjectMapper instance used for parsing the JSON data
     */
    private static void initPiratesCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/PiratesCard.json");
            if (is == null) {
                System.out.println("Error: PiratesCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for (JsonNode node : rootNode) {
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

    /**
     * Initializes the PlanetsCard objects from a JSON file and adds them to the game's card archive.
     * The JSON file contains details about each PlanetsCard, including its properties and associated planets.
     *
     * @param game the game to which the PlanetsCards will be added
     * @param objectMapper the ObjectMapper used to parse the JSON file
     */
    private static void initPlanetsCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/PlanetsCard.json");
            if (is == null) {
                System.out.println("Error: PlanetsCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the SlaversCard objects from the JSON resource file and adds them to the game's card archive.
     * The method reads attributes for each SlaversCard from the JSON file and constructs
     * instances of the SlaversCard class, which are then added to the game's card archive.
     *
     * @param game         The game instance to which the cards will be added.
     * @param objectMapper The ObjectMapper instance used for parsing JSON files.
     */
    private static void initSlaversCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/SlaversCard.json");
            if (is == null) {
                System.out.println("Error: SlaversCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for (JsonNode node : rootNode) {
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

    /**
     * Initializes the SmugglersCard objects by reading data from the SmugglersCard.json file.
     * The method parses the JSON data, creating and adding SmugglersCard objects to the game's card archive.
     *
     * @param game the game instance to which the cards will be added
     * @param objectMapper the Jackson ObjectMapper instance used to parse the JSON data
     */
    private static void initSmugglersCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/SmugglersCard.json");
            if (is == null) {
                System.out.println("Error: SmugglersCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes StardustCard objects by reading their data from a JSON configuration file
     * and adds them to the game's card archive.
     *
     * @param game The game instance to which the StardustCard objects will be added.
     * @param objectMapper The ObjectMapper instance used for JSON parsing.
     */
    private static void initStardustCard(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/AdventureCards/StardustCard.json");
            if (is == null) {
                System.out.println("Error: StardustCard.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for (JsonNode node : rootNode) {
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

    /**
     * Initializes the BatteryComponent tiles for the game by reading configuration
     * data from a JSON file and adding the parsed components to the game's
     * covered component tiles list.
     *
     * @param game the game object to which the battery components will be added
     * @param objectMapper the ObjectMapper instance used for parsing the JSON file
     */
    private static void initBatteryComponent(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/BatteryComponent.json");
            if (is == null) {
                System.out.println("Error: BatteryComponent.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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
            e.printStackTrace();
        }
    }

    /**
     * Initializes the starting cabin components for the game by reading the definition
     * from a JSON file and adding them to the game's covered component tiles.
     *
     * @param game the Game instance where the starting cabin components will be added
     * @param objectMapper the ObjectMapper instance used to parse the JSON file
     */
    private static void initStartingCabin(Game game, ObjectMapper objectMapper){
        try{
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/StartingCabin.json");
            if (is == null) {
                throw new IOException("Resource not found: StartingCabin.json");
            }
            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the regular cabin components of the game by reading from a JSON file
     * and adding parsed data as RegularCabin component tiles to the game's covered component tiles.
     *
     * @param game         The Game instance to which the regular cabin tiles will be added.
     * @param objectMapper The ObjectMapper used to parse the JSON file containing component tile details.
     */
    private static void initRegularCabin(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/RegularCabin.json");
            if (is == null) {
                System.out.println("Error: RegularCabin.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes Alien Addon tiles from a JSON file and adds them to the game's*/
    private static void initAlienAddon(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/AlienAddon.json");
            if (is == null) {
                System.out.println("Error: AlienAddon.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes storage compartments for the game by reading and parsing data
     * from the "StorageCompartment.json" file and adding the corresponding components
     * to the game's collection of covered component tiles.
     *
     * @param game the game object to which the storage compartments will be added
     * @param objectMapper the object mapper used to parse the JSON file
     */
    private static void initStorageCompartments(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/StorageCompartment.json");
            if (is == null) {
                System.out.println("Error: StorageCompartment.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the engine components and adds them to the game's covered component tiles collection.
     * The initialization process reads configuration data from the Engine.json resource file.
     *
     * @param game the game instance to which the initialized engine components will be added
     * @param objectMapper the ObjectMapper instance used to parse the JSON configuration data
     */
    private static void initEngine(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/Engine.json");
            if (is == null) {
                System.out.println("Error: Engine.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the DoubleEngine components by reading data from the DoubleEngine.json file
     * and adding them to the game's covered components list.
     *
     * @param game the Game object in which the DoubleEngine components will be initialized
     * @param objectMapper the ObjectMapper instance used to parse the JSON data
     */
    private static void initDoubleEngine(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/DoubleEngine.json");
            if (is == null) {
                System.out.println("Error: DoubleEngine.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the DoubleCannon component tiles for the game by reading their configuration
     * from a JSON file and adding them to the game's collection of component tiles.
     *
     * @param game the game instance where the DoubleCannon tiles will be added
     * @param objectMapper the ObjectMapper used to parse the JSON file
     */
    private static void initDoubleCannon(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/DoubleCannon.json");
            if (is == null) {
                System.out.println("Error: DoubleCannon.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the cannon components in the game using data from a JSON configuration file.
     *
     * @param game         The current game instance where the cannon components will be added.
     * @param objectMapper The ObjectMapper instance used for parsing the JSON file.
     */
    private static void initCannon(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/Cannon.json");
            if (is == null) {
                System.out.println("Error: Cannon.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the structural module component tiles for the game by reading data from a JSON resource file
     * and adding the parsed components to the game's collection of covered component tiles.
     *
     * @param game the game instance to which the structural module will be added
     * @param objectMapper the ObjectMapper instance used for parsing the JSON file
     */
    private static void initStructuralModule(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/StructuralModule.json");
            if (is == null) {
                System.out.println("Error: StructuralModule.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     * Initializes the Shield Generator component tiles for the game by reading data from a JSON file
     */
    private static void initShieldGenerator(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/ShieldGenerator.json");
            if (is == null) {
                System.out.println("Error: ShieldGenerator.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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

    /**
     *
     */
    private static void initSpecialStorageCompartment(Game game, ObjectMapper objectMapper) {
        try {
            InputStream is = GameInitializer.class.getResourceAsStream("/JSONfiles/ComponentTiles/SpecialStorageCompartment.json");
            if (is == null) {
                System.out.println("Error: SpecialStorageCompartment.json resource not found");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(is);
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
