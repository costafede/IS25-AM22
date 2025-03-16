package it.polimi.ingsw.is25am22new.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InitializingFromJsonCardsTest {

    @Test
    void init_game_should_initialize_cards_from_json_properly() {
        Game game = new TutorialGame();
        game.initGame();
        ObjectMapper objectMapper = new ObjectMapper();

        //check AbandonedShipCard initialized properly
        assertTrue(check_abandoned_ship_card(objectMapper, game), "AbandonedShipCard not initialized properly");
        assertEquals(4, game.getCardArchive().stream().filter(card -> card instanceof AbandonedShipCard).count());

        //check AbandonedStationCard initialized properly
        assertTrue(check_abandoned_station_card(objectMapper, game), "AbandonedStationCard not initialized properly");
        assertEquals(4, game.getCardArchive().stream().filter(card -> card instanceof AbandonedStationCard).count());

        //check CombatZone1Card initialized properly
        assertTrue(check_combat_zone_1_card(objectMapper, game), "CombatZone1Card not initialized properly");
        assertEquals(1, game.getCardArchive().stream().filter(card -> card instanceof CombatZoneCard && ((CombatZoneCard) card).getLostGoods() == 0).count());

        //check CombatZone2Card initialized properly
        assertTrue(check_combat_zone_2_card(objectMapper, game), "CombatZone2Card not initialized properly");
        assertEquals(1, game.getCardArchive().stream().filter(card -> card instanceof CombatZoneCard && ((CombatZoneCard) card).getLostAstronauts() == 0).count());

        //check EpidemicCard initialized properly
        assertTrue(check_epidemic_card(objectMapper, game), "EpidemicCard not initialized properly");
        assertEquals(1, game.getCardArchive().stream().filter(card -> card instanceof EpidemicCard).count());

        //check MeteorSwarmCard initialized properly
        assertTrue(check_meteor_swarm_card(objectMapper, game), "MeteorCard not initialized properly");
        assertEquals(6, game.getCardArchive().stream().filter(card -> card instanceof MeteorSwarmCard).count());

        //check openSpaceCard initialized properly
        assertTrue(check_open_space_card(objectMapper, game), "OpenSpaceCard not initialized properly");
        assertEquals(7, game.getCardArchive().stream().filter(card -> card instanceof OpenSpaceCard).count());

        //check PiratesCard initialized properly
        assertTrue(check_pirates_card(objectMapper, game), "PiratesCard not initialized properly");
        assertEquals(2, game.getCardArchive().stream().filter(card -> card instanceof PiratesCard).count());

        //check PlanetsCard initialized properly
        assertTrue(check_planets_card(objectMapper, game), "PlanetsCard not initialized properly");
        assertEquals(8, game.getCardArchive().stream().filter(card -> card instanceof PlanetsCard).count());

        //check SmugglersCard initialized properly
        assertTrue(check_smugglers_card(objectMapper, game), "SmugglersCard not initialized properly");
        assertEquals(2, game.getCardArchive().stream().filter(card -> card instanceof SmugglersCard).count());

        //check SlaversCard initialized properly
        assertTrue(check_slavers_card(objectMapper, game), "SlaversCard not initialized properly");
        assertEquals(2, game.getCardArchive().stream().filter(card -> card instanceof SlaversCard).count());

        //check StardustCard initialized properly
        assertTrue(check_stardust_card(objectMapper, game), "StardustCard not initialized properly");
        assertEquals(2, game.getCardArchive().stream().filter(card -> card instanceof StardustCard).count());

    }

    private boolean check_stardust_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/StardustCard.json"));
            for (JsonNode node : jsonNode) {
                StardustCard ec = new StardustCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof StardustCard existingCard) {
                        if (existingCard.getPngName().equals(ec.getPngName()) &&
                                existingCard.getName().equals(ec.getName()) &&
                                existingCard.getLevel() == ec.getLevel() &&
                                existingCard.isTutorial() == ec.isTutorial()) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in check_stardust_card: " + e.getMessage());
        }
        return check;
    }

    private boolean check_slavers_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;

        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/SlaversCard.json"));
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

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof SlaversCard existingCard) {
                        if (existingCard.getPngName().equals(sc.getPngName()) &&
                                existingCard.getName().equals(sc.getName()) &&
                                existingCard.getLevel() == sc.getLevel() &&
                                existingCard.isTutorial() == sc.isTutorial() &&
                                existingCard.getFlightDaysLost() == sc.getFlightDaysLost() &&
                                existingCard.getCredits() == sc.getCredits() &&
                                existingCard.getLostAstronauts() == sc.getLostAstronauts()) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in check_slavers_card: " + e.getMessage());
        }

        return check;
    }

    private boolean check_smugglers_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
        try{
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/SmugglersCard.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                int flightDaysLost = node.get("flightDaysLost").asInt();
                int cannonStrength = node.get("cannonStrength").asInt();
                int lostGoods = node.get("lostGoods").asInt();
                List<GoodBlock> goodBlocks = new ArrayList<>();
                JsonNode goodBlocksNode = node.get("goodBlocks");
                for(JsonNode goodBlockNode: goodBlocksNode){
                    GoodBlock goodBlock = GoodBlock.valueOf(goodBlockNode.asText());
                    goodBlocks.add(goodBlock);
                }

                SmugglersCard card = new SmugglersCard(pngName, name, game, level, tutorial, flightDaysLost, cannonStrength, lostGoods, goodBlocks);

                for(AdventureCard adventureCard: game.getCardArchive()){
                    if(adventureCard instanceof SmugglersCard sc){
                        if(sc.getPngName().equals(card.getPngName()) &&
                                sc.getName().equals(card.getName()) &&
                                sc.getLevel() == card.getLevel() &&
                                sc.isTutorial() == card.isTutorial() &&
                                sc.getFlightDaysLost() == card.getFlightDaysLost() &&
                                sc.getCannonStrength() == card.getCannonStrength() &&
                                sc.getLostGoods() == card.getLostGoods() &&
                                sc.getGoodBlocks().equals(goodBlocks)){
                            check = true;
                            break;
                        }
                    }
                }
            }
        }catch (IOException e){
            System.out.println("Error in reading SmugglersCard.json");
        }
        return check;
    }

    private boolean check_planets_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
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
                PlanetsCard card = new PlanetsCard(pngName, name, game, level, tutorial, planetToGoodBlocks, flightDaysLost);

                for (AdventureCard adventureCard : game.getCardArchive()) {
                    if (adventureCard instanceof PlanetsCard pc) {
                        if (pc.getPngName().equals(card.getPngName()) &&
                                pc.getName().equals(card.getName()) &&
                                pc.getLevel() == card.getLevel() &&
                                pc.isTutorial() == card.isTutorial() &&
                                pc.getPlanetToGoodBlocks().size() == numOfPlanets &&
                                pc.getFlightDaysLost() == card.getFlightDaysLost() &&
                                pc.getPlanetToGoodBlocks().equals(card.getPlanetToGoodBlocks())) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading PlanetsCard.json: " + e.getMessage());
        }
        return check;
    }

    private boolean check_pirates_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
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

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof PiratesCard pc) {
                        if (pc.getPngName().equals(pngName) &&
                                pc.getName().equals(name) &&
                                pc.getLevel() == level &&
                                pc.isTutorial() == tutorial &&
                                pc.getFlightDaysLost() == flightDaysLost &&
                                pc.getCannonStrength() == cannonStrength &&
                                pc.getCredits() == credits && check_shot(shotSize, new String[]{"BOTTOM", "BOTTOM", "BOTTOM"}, pc)) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading PiratesCard.json: " + e.getMessage());
        }
        return check;
    }

    private boolean check_shot(boolean[] shotSize, String[] shotOrientation, PiratesCard pc) {
        boolean found = false;
        for(int i = 0; i < shotSize.length; i++) {
            if((pc.getNumberToShot().get(i).isBig() == shotSize[i]) &&
                    pc.getNumberToShot().get(i).getOrientation().equals(Orientation.valueOf(shotOrientation[i]))){
                found = true;
            }else{
                break;
            }
        }
        return found;
    }

    private boolean check_open_space_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/OpenSpaceCard.json"));
            for (JsonNode node : jsonNode) {
                OpenSpaceCard ec = new OpenSpaceCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof OpenSpaceCard existingCard) {
                        if (existingCard.getPngName().equals(ec.getPngName()) &&
                                existingCard.getName().equals(ec.getName()) &&
                                existingCard.getLevel() == ec.getLevel() &&
                                existingCard.isTutorial() == ec.isTutorial()) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in check_open_space_card: " + e.getMessage());
        }
        return check;
    }

    private boolean check_meteor_swarm_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/MeteorSwarmCard.json"));
            for(JsonNode node: rootNode){
                String pngName = node.get("pngName").asText();
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                boolean tutorial = node.get("tutorial").asBoolean();
                JsonNode meteorSize = node.get("meteorSize");
                Boolean[] meteor = new Boolean[meteorSize.size()];
                for(int i = 0; i < meteorSize.size(); i++){
                    meteor[i] = meteorSize.get(i).asBoolean();
                }
                JsonNode meteorOrientation = node.get("meteorOrientation");
                String[] orientation = new String[meteorOrientation.size()];
                for(int i = 0; i < meteorOrientation.size(); i++){
                    orientation[i] = meteorOrientation.get(i).asText();
                }
                Map<Integer, Meteor> numberToMeteor = new HashMap<>();

                for(int i = 0; i < meteorSize.size(); i++){
                    numberToMeteor.put(i, new Meteor(meteor[i], Orientation.valueOf(orientation[i])));
                }

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof MeteorSwarmCard msc) {
                        if (msc.getPngName().equals(pngName) &&
                                msc.getName().equals(name) &&
                                msc.getLevel() == level &&
                                msc.isTutorial() == tutorial &&
                                check_meteor(meteor, orientation, msc)) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading MeteorSwarm.json: " + e.getMessage());
        }
        return check;
    }

    private boolean check_meteor(Boolean[] meteor, String[] orientation, MeteorSwarmCard msc) {
        boolean found = false;
        for(int i = 0; i < meteor.length; i++) {
            if((msc.getNumberToMeteor().get(i).isBig() == meteor[i]) &&
                    msc.getNumberToMeteor().get(i).getOrientation().equals(Orientation.valueOf(orientation[i]))){
                found = true;
            }else{
                break;
            }
        }
        return found;
    }

    private boolean check_epidemic_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/EpidemicCard.json"));
            for (JsonNode node : jsonNode) {
                EpidemicCard ec = new EpidemicCard(
                        node.get("pngName").asText(),
                        node.get("name").asText(),
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean()
                );

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof EpidemicCard existingCard) {
                        if (existingCard.getPngName().equals(ec.getPngName()) &&
                                existingCard.getName().equals(ec.getName()) &&
                                existingCard.getLevel() == ec.getLevel() &&
                                existingCard.isTutorial() == ec.isTutorial()) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in check_epidemic_card: " + e.getMessage());
        }
        return check;
    }

    private boolean check_combat_zone_2_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
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

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof CombatZoneCard czc) {
                        if (czc.getPngName().equals(pngName) &&
                                czc.getName().equals(name) &&
                                czc.getLevel() == level &&
                                czc.isTutorial() == tutorial &&
                                czc.getFlightDaysLost() == flightDaysLost &&
                                czc.getLostAstronauts() == 0 &&
                                czc.getLostGoods() == lostGoods && check_shot(shotSize, shotOrientation, czc)) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard2.json: " + e.getMessage());
        }
        return check;
    }

    private boolean check_combat_zone_1_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;
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

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof CombatZoneCard czc) {
                        if (czc.getPngName().equals(pngName) &&
                            czc.getName().equals(name) &&
                            czc.getLevel() == level &&
                            czc.isTutorial() == tutorial &&
                            czc.getFlightDaysLost() == flightDaysLost &&
                            czc.getLostAstronauts() == lostAstronauts &&
                            czc.getLostGoods() == 0 && check_shot(shotSize, shotOrientation, czc)) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading CombatZoneCard1.json: " + e.getMessage());
        }
        return check;
    }

    private boolean check_shot(boolean[] shotSize, String[] shotOrientation, CombatZoneCard czc){
        boolean found = false;
        for(int i = 0; i < shotSize.length; i++) {
            if((czc.getNumberToShot().get(i).isBig() == shotSize[i]) &&
                    czc.getNumberToShot().get(i).getOrientation().equals(Orientation.valueOf(shotOrientation[i]))){
                found = true;
            }else{
                break;
            }
        }
        return found;
    }

    private boolean check_abandoned_station_card(ObjectMapper objectMapper, Game game) {
        boolean check = false;

        try {
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
                        game,
                        node.get("level").asInt(),
                        node.get("tutorial").asBoolean(),
                        node.get("flightDaysLost").asInt(),
                        node.get("astronautsNumber").asInt(),
                        goodBlocks
                );
                for(AdventureCard card: game.getCardArchive()){
                    if(card instanceof AbandonedStationCard existingCard){
                        if(existingCard.getPngName().equals(asc.getPngName()) &&
                                existingCard.getName().equals(asc.getName()) &&
                                existingCard.getLevel() == asc.getLevel() &&
                                existingCard.isTutorial() == asc.isTutorial() &&
                                existingCard.getFlightDaysLost() == asc.getFlightDaysLost() &&
                                existingCard.getAstronautsNumber() == asc.getAstronautsNumber() &&
                                existingCard.getGoodBlocks().equals(asc.getGoodBlocks())){
                            check = true;
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Error in check_abandoned_station_card: " + e.getMessage());
        }

        return check;
    }

    private boolean check_abandoned_ship_card(ObjectMapper om, Game game) {
        boolean check = false;

        try {
            JsonNode jsonNode = om.readTree(new File("src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/AbandonedShipCard.json"));
            for (JsonNode node : jsonNode) {
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

                for (AdventureCard card : game.getCardArchive()) {
                    if (card instanceof AbandonedShipCard existingCard) {
                        if (existingCard.getPngName().equals(asc.getPngName()) &&
                                existingCard.getName().equals(asc.getName()) &&
                                existingCard.getLevel() == asc.getLevel() &&
                                existingCard.isTutorial() == asc.isTutorial() &&
                                existingCard.getFlightdaysLost() == asc.getFlightdaysLost() &&
                                existingCard.getCredits() == asc.getCredits() &&
                                existingCard.getLostAstronauts() == asc.getLostAstronauts()) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in check_abandoned_ship_card: " + e.getMessage());
        }
        return check;
    }


}