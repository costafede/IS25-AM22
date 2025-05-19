package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the initialization of components from JSON within the game.
 *
 * This class ensures that the process of parsing and setting up game components from
 * JSON is working as intended. It contains unit tests and validation checks for different
 * game components, ensuring correct deserialization and initialization behaviors.
 */
public class InitializingFromJsonComponentTest {
    private Game game;

    @Test
    void init_game_should_initialize_components_from_json_properly(){
        List<String> nicknames = List.of("Federico", "Alex", "Giuseppe", "Umberto");
        game = new TutorialGame(nicknames, null);
        game.initGame();
        ObjectMapper objectMapper = new ObjectMapper();

        //Check alienAddon initialized properly
        assertTrue(check_alienAddon_component(objectMapper), "AlienAddon not found in game.getCoveredComponentTiles()");
        assertEquals(12, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof AlienAddon).count(), "The number of AlienAddon is not correct");

        //Check batteryComponent initialized properly
        assertTrue(check_battery_component(objectMapper), "BatteryComponent not found in game.getCoveredComponentTiles()");
        assertEquals(17, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof BatteryComponent).count(), "The number of batteryComponent is not correct");

        //Check cannon initialized properly
        assertTrue(check_cannon_component(objectMapper), "CannonComponent not found in game.getCoveredComponentTiles()");
        assertEquals(25, game.getCoveredComponentTiles().stream().filter(ct -> (ct instanceof Cannon && !(ct instanceof DoubleCannon))).count(), "The number of Cannon is not correct");

        //Check doubleCannon initialized properly
        assertTrue(check_double_cannon_component(objectMapper), "DoubleCannonComponent not found in game.getCoveredComponentTiles()");
        assertEquals(11, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof DoubleCannon).count(), "The number of DoubleCannon is not correct");

        //Check doubleEngine initialized properly
        assertTrue(check_double_engine_component(objectMapper), "DoubleEngineComponent not found in game.getCoveredComponentTiles()");
        assertEquals(9, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof DoubleEngine).count(), "The number of DoubleEngine is not correct");

        //Check engine initialized properly
        assertTrue(check_engine_component(objectMapper), "EngineComponent not found in game.getCoveredComponentTiles()");
        assertEquals(21, game.getCoveredComponentTiles().stream().filter(ct -> (ct instanceof Engine && !(ct instanceof DoubleEngine))).count(), "The number of Engine is not correct");

        //Check regularCabin initialized properly
        assertTrue(check_regular_cabin_component(objectMapper), "RegularCabinComponent not found in game.getCoveredComponentTiles()");
        assertEquals(17, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof RegularCabin).count(), "The number of RegularCabin is not correct");

        //Check shieldGenerator initialized properly
        assertTrue(check_shield_generator_component(objectMapper), "ShieldGeneratorComponent not found in game.getCoveredComponentTiles()");
        assertEquals(8, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof ShieldGenerator).count(), "The number of ShieldGenerator is not correct");

        //Check specialStorageCompartment initialized properly
        assertTrue(check_special_storage_compartment_component(objectMapper), "SpecialStorageCompartmentComponent not found in game.getCoveredComponentTiles()");
        assertEquals(9, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof SpecialStorageCompartment).count(), "The number of SpecialStorageCompartment is not correct");

        //Check startingCabin initialized properly
        assertTrue(check_starting_cabin_component(objectMapper), "StartingCabinComponent not found in game.getCoveredComponentTiles()");
        assertEquals(4, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof StartingCabin).count(), "The number of StartingCabin is not correct");

        //Check storageCompartment initialized properly
        assertTrue(check_storage_compartment_component(objectMapper), "StorageCompartmentComponent not found in game.getCoveredComponentTiles()");
        assertEquals(15, game.getCoveredComponentTiles().stream().filter(ct -> (ct instanceof StorageCompartment && !(ct instanceof SpecialStorageCompartment))).count(), "The number of StorageCompartment is not correct");

        //Check structuralModule initialized properly
        assertTrue(check_structural_module_component(objectMapper), "StructuralModuleComponent not found in game.getCoveredComponentTiles()");
        assertEquals(8, game.getCoveredComponentTiles().stream().filter(ct -> ct instanceof StructuralModule).count(), "The number of StructuralModule is not correct");

        //Check the total number of ComponentTile
        assertEquals(156, game.getCoveredComponentTiles().size(), "The number of ComponentTile is not correct");

        //Qui svuotiamo il Set
        game.getCoveredComponentTiles().clear();
    }

    private boolean check_alienAddon_component(ObjectMapper om) {
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/AlienAddon.json"));
            for(JsonNode node : jsonNode){
                AlienAddon ad = new AlienAddon(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()), node.get("color").asText());
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(ad.getPngName()) &&
                            ct.getTopSide().equals(ad.getTopSide()) &&
                            ct.getBottomSide().equals(ad.getBottomSide()) &&
                            ct.getLeftSide().equals(ad.getLeftSide()) &&
                            ct.getRightSide().equals(ad.getRightSide()) &&
                            ((AlienAddon) ct).getAddonColor().equals(ad.getAddonColor())) {
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_battery_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/BatteryComponent.json"));
            for(JsonNode node : jsonNode){
                BatteryComponent bc = new BatteryComponent(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()), node.get("numOfBatteries").asInt());
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(bc.getPngName()) &&
                            ct.getTopSide().equals(bc.getTopSide()) &&
                            ct.getBottomSide().equals(bc.getBottomSide()) &&
                            ct.getLeftSide().equals(bc.getLeftSide()) &&
                            ct.getRightSide().equals(bc.getRightSide()) &&
                            ((BatteryComponent) ct).getNumOfBatteries() == bc.getNumOfBatteries()) {
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_cannon_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/Cannon.json"));
            for(JsonNode node : jsonNode){
                Cannon cannon = new Cannon(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(cannon.getPngName()) &&
                            ct.getTopSide().equals(cannon.getTopSide()) &&
                            ct.getBottomSide().equals(cannon.getBottomSide()) &&
                            ct.getLeftSide().equals(cannon.getLeftSide()) &&
                            ct.getRightSide().equals(cannon.getRightSide())){
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_double_cannon_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/DoubleCannon.json"));
            for(JsonNode node : jsonNode){
                DoubleCannon doublecannon = new DoubleCannon(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(doublecannon.getPngName()) &&
                            ct.getTopSide().equals(doublecannon.getTopSide()) &&
                            ct.getBottomSide().equals(doublecannon.getBottomSide()) &&
                            ct.getLeftSide().equals(doublecannon.getLeftSide()) &&
                            ct.getRightSide().equals(doublecannon.getRightSide())){
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_double_engine_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/DoubleEngine.json"));
            for(JsonNode node : jsonNode){
                DoubleEngine doubleEngine = new DoubleEngine(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(doubleEngine.getPngName()) &&
                            ct.getTopSide().equals(doubleEngine.getTopSide()) &&
                            ct.getBottomSide().equals(doubleEngine.getBottomSide()) &&
                            ct.getLeftSide().equals(doubleEngine.getLeftSide()) &&
                            ct.getRightSide().equals(doubleEngine.getRightSide())){
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_engine_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/Engine.json"));
            for(JsonNode node : jsonNode){
                Engine engine = new Engine(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(engine.getPngName()) &&
                            ct.getTopSide().equals(engine.getTopSide()) &&
                            ct.getBottomSide().equals(engine.getBottomSide()) &&
                            ct.getLeftSide().equals(engine.getLeftSide()) &&
                            ct.getRightSide().equals(engine.getRightSide())){
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_regular_cabin_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/RegularCabin.json"));
            for(JsonNode node : jsonNode){
                RegularCabin rc = new RegularCabin(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(rc.getPngName()) &&
                            ct.getTopSide().equals(rc.getTopSide()) &&
                            ct.getBottomSide().equals(rc.getBottomSide()) &&
                            ct.getLeftSide().equals(rc.getLeftSide()) &&
                            ct.getRightSide().equals(rc.getRightSide())){
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_shield_generator_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/ShieldGenerator.json"));
            for(JsonNode node : jsonNode){
                ShieldGenerator sg = new ShieldGenerator(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(sg.getPngName()) &&
                            ct.getTopSide().equals(sg.getTopSide()) &&
                            ct.getBottomSide().equals(sg.getBottomSide()) &&
                            ct.getLeftSide().equals(sg.getLeftSide()) &&
                            ct.getRightSide().equals(sg.getRightSide())){
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_special_storage_compartment_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/SpecialStorageCompartment.json"));
            for(JsonNode node : jsonNode){
                SpecialStorageCompartment spc = new SpecialStorageCompartment(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()), node.get("capacity").asInt());
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(spc.getPngName()) &&
                            ct.getTopSide().equals(spc.getTopSide()) &&
                            ct.getBottomSide().equals(spc.getBottomSide()) &&
                            ct.getLeftSide().equals(spc.getLeftSide()) &&
                            ct.getRightSide().equals(spc.getRightSide()) &&
                            ((SpecialStorageCompartment) ct).getCapacity() == spc.getCapacity()) {
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_starting_cabin_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/StartingCabin.json"));
            for(JsonNode node : jsonNode){
                StartingCabin sc = new StartingCabin(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()), node.get("color").asText());
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(sc.getPngName()) &&
                            ct.getTopSide().equals(sc.getTopSide()) &&
                            ct.getBottomSide().equals(sc.getBottomSide()) &&
                            ct.getLeftSide().equals(sc.getLeftSide()) &&
                            ct.getRightSide().equals(sc.getRightSide()) &&
                            ((StartingCabin) ct).getColorTile().equals(sc.getColorTile())) {
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_storage_compartment_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/StorageCompartment.json"));
            for(JsonNode node : jsonNode){
                StorageCompartment sc = new StorageCompartment(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()), node.get("capacity").asInt());
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(sc.getPngName()) &&
                            ct.getTopSide().equals(sc.getTopSide()) &&
                            ct.getBottomSide().equals(sc.getBottomSide()) &&
                            ct.getLeftSide().equals(sc.getLeftSide()) &&
                            ct.getRightSide().equals(sc.getRightSide()) &&
                            ((StorageCompartment) ct).getCapacity() == sc.getCapacity()) {
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }

    private boolean check_structural_module_component(ObjectMapper om){
        boolean check = false;
        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/ComponentTiles/StructuralModule.json"));
            for(JsonNode node : jsonNode){
                StructuralModule sm = new StructuralModule(node.get("pngName").asText(), Side.valueOf(node.get("topSide").asText()), Side.valueOf(node.get("bottomSide").asText()), Side.valueOf(node.get("leftSide").asText()), Side.valueOf(node.get("rightSide").asText()));
                //System.out.println(bc.getPngName());
                for(ComponentTile ct : game.getCoveredComponentTiles()) {
                    if (ct.getPngName().equals(sm.getPngName()) &&
                            ct.getTopSide().equals(sm.getTopSide()) &&
                            ct.getBottomSide().equals(sm.getBottomSide()) &&
                            ct.getLeftSide().equals(sm.getLeftSide()) &&
                            ct.getRightSide().equals(sm.getRightSide()) ) {
                        check = true;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("Error in reading the file");
        }
        return check;
    }


}
