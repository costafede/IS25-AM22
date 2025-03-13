package it.polimi.ingsw.is25am22new.Model;

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
    private Map<String, Integer> cardArchive;
    private Set<ComponentTile> coveredComponentTiles;
    private List<ComponentTile> uncoveredComponentTiles;
    private Map<String, Shipboard> shipboards;
    private Flightboard flightboard;

    public Game() {
        // temporaneo
        playerList = new ArrayList<>();
        bank = new Bank();
        cardArchive = new HashMap<>();
        coveredComponentTiles = new HashSet<>();
        uncoveredComponentTiles = new ArrayList<>();
        //shipboards = new HashMap<>();
    }

    public Shipboard getShipboards(String player) {
        return shipboards.get(player);
    }

    public void initGame(){
        ObjectMapper objectMapper = new ObjectMapper();
        initComponent(objectMapper);
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
