package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Flightboards.TutorialFlightBoard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.*;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The GameTest class contains a set of test methods and utility methods for testing different scenarios
 * in the gameplay of various game types (e.g. Level 2 game, Tutorial game).
 * This class primarily focuses on verifying player management, gameplay transitions,
 * and score calculation functionality.
 */
class GameTest {

    List<String> playerList = List.of("Anatoly", "Tommaso", "Federico", "Emanuele");

    Game game;
    @BeforeEach
    void setUp() {
        game = new TutorialGame(playerList, null);
        game.getShipboards().get("Tommaso").weldComponentTile(
                new RegularCabin("1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 3, 3);
    }

    @Test
    void setCurrPlayerToNext() {
        Game tutorialGame;
        Game level2Game;
        tutorialGame = new TutorialGame(playerList, null);
        level2Game = new Level2Game(playerList, null);
        setCurrentPlayerToNextInTutorialGame(tutorialGame);
        setCurrentPlayerToNextInLevel2(level2Game);
    }

    private void setCurrentPlayerToNextInLevel2(Game level2Game) {
        //Test for the level 2 game
        level2Game.getFlightboard().placeRocket("Anatoly", 0);
        level2Game.getFlightboard().placeRocket("Tommaso", 1);
        level2Game.getFlightboard().placeRocket("Federico", 2);
        level2Game.getFlightboard().placeRocket("Emanuele", 3);
        level2Game.setCurrPlayer(level2Game.getFlightboard().getOrderedRockets().getFirst());

        try {
            assertEquals("Anatoly", level2Game.getCurrPlayer());
            level2Game.setCurrPlayerToNext();
            assertEquals("Tommaso", level2Game.getCurrPlayer());
            level2Game.setCurrPlayerToNext();
            assertEquals("Federico", level2Game.getCurrPlayer());
            level2Game.setCurrPlayerToNext();
            assertEquals("Emanuele", level2Game.getCurrPlayer());
            level2Game.setCurrPlayerToNext();
        } catch (Exception e) {
            //The exception should be thrown when the last player is reached
            assertEquals(NullPointerException.class, e.getClass());
            assertNull(level2Game.getCurrPlayer());
        }
    }

    private void setCurrentPlayerToNextInTutorialGame(Game tutorialGame) {
        //Test for the tutorial game
        tutorialGame.getFlightboard().placeRocket("Anatoly", 0);
        tutorialGame.getFlightboard().placeRocket("Tommaso", 1);
        tutorialGame.getFlightboard().placeRocket("Federico", 2);
        tutorialGame.getFlightboard().placeRocket("Emanuele", 3);
        tutorialGame.setCurrPlayer(tutorialGame.getFlightboard().getOrderedRockets().getFirst());

        try {
            assertEquals("Anatoly", tutorialGame.getCurrPlayer());
            tutorialGame.setCurrPlayerToNext();
            assertEquals("Tommaso", tutorialGame.getCurrPlayer());
            tutorialGame.setCurrPlayerToNext();
            assertEquals("Federico", tutorialGame.getCurrPlayer());
            tutorialGame.setCurrPlayerToNext();
            assertEquals("Emanuele", tutorialGame.getCurrPlayer());
            tutorialGame.setCurrPlayerToNext();

        } catch (Exception e) {
            //The exception should be thrown when the last player is reached
            assertEquals(NullPointerException.class, e.getClass());
            assertNull(tutorialGame.getCurrPlayer());
        }
    }

    @Test
    void setCurrPlayerToLeader() {
        Game tutorialGame;
        Game level2Game;
        tutorialGame = new TutorialGame(playerList, null);
        level2Game = new Level2Game(playerList, null);
        setCurrPlayerToLeaderInTutorialGame(tutorialGame);
        setCurrPlayerToLeaderInLevel2Game(level2Game);
    }

    private void setCurrPlayerToLeaderInLevel2Game(Game level2Game) {
        level2Game.getFlightboard().placeRocket("Anatoly", 0);
        level2Game.getFlightboard().placeRocket("Tommaso", 1);
        level2Game.getFlightboard().placeRocket("Federico", 2);
        level2Game.getFlightboard().placeRocket("Emanuele", 3);
        level2Game.setCurrPlayer(level2Game.getFlightboard().getOrderedRockets().getLast());

        level2Game.setCurrPlayerToLeader();
        assertEquals("Anatoly", level2Game.getCurrPlayer());
    }

    private void setCurrPlayerToLeaderInTutorialGame(Game tutorialGame) {
        tutorialGame.getFlightboard().placeRocket("Anatoly", 0);
        tutorialGame.getFlightboard().placeRocket("Tommaso", 1);
        tutorialGame.getFlightboard().placeRocket("Federico", 2);
        tutorialGame.getFlightboard().placeRocket("Emanuele", 3);
        tutorialGame.setCurrPlayer(tutorialGame.getFlightboard().getOrderedRockets().getLast());

        tutorialGame.setCurrPlayerToLeader();
        assertEquals("Anatoly", tutorialGame.getCurrPlayer());
    }

    @Test
    void isPlayerStillAbleToPlay() {
        Game tutorialGame;
        Game level2Game;
        tutorialGame = new TutorialGame(playerList,null);
        level2Game = new Level2Game(playerList,null);
        isPlayerStillAbleToPlayInTutorialGame(tutorialGame);
        isPlayerStillAbleToPlayInLevel2Game(level2Game);

    }

    private void isPlayerStillAbleToPlayInLevel2Game(Game level2Game) {
        level2Game.getFlightboard().placeRocket("Anatoly", 0);
        level2Game.getFlightboard().placeRocket("Tommaso", 1);
        level2Game.getFlightboard().placeRocket("Federico", 2);
        level2Game.getFlightboard().placeRocket("Emanuele", 3);
        level2Game.setCurrPlayer(level2Game.getFlightboard().getOrderedRockets().getFirst());

        assertTrue(level2Game.isPlayerStillAbleToPlay("Anatoly"));
        assertTrue(level2Game.isPlayerStillAbleToPlay("Tommaso"));
        assertTrue(level2Game.isPlayerStillAbleToPlay("Federico"));
        assertTrue(level2Game.isPlayerStillAbleToPlay("Emanuele"));

        for(String player : playerList){
            if(player.equals("Anatoly") || player.equals("Tommaso")){
                level2Game.getShipboards().get(player).destroyTile(2, 3);
            }
        }

        assertFalse(level2Game.isPlayerStillAbleToPlay("Anatoly"));
        assertFalse(level2Game.isPlayerStillAbleToPlay("Tommaso"));

        level2Game.getShipboards().get("Federico").setDaysOnFlight(10);
        level2Game.getShipboards().get("Emanuele").setDaysOnFlight(100);
        level2Game.getFlightboard().shiftRocket("Federico", 1);

        assertFalse(level2Game.isPlayerStillAbleToPlay("Federico"));
    }

    private void isPlayerStillAbleToPlayInTutorialGame(Game tutorialGame) {
        //Test for the tutorial game
        tutorialGame.getFlightboard().placeRocket("Anatoly", 0);
        tutorialGame.getFlightboard().placeRocket("Tommaso", 1);
        tutorialGame.getFlightboard().placeRocket("Federico", 2);
        tutorialGame.getFlightboard().placeRocket("Emanuele", 3);
        tutorialGame.setCurrPlayer(tutorialGame.getFlightboard().getOrderedRockets().getFirst());

        assertTrue(tutorialGame.isPlayerStillAbleToPlay("Anatoly"));
        assertTrue(tutorialGame.isPlayerStillAbleToPlay("Tommaso"));
        assertTrue(tutorialGame.isPlayerStillAbleToPlay("Federico"));
        assertTrue(tutorialGame.isPlayerStillAbleToPlay("Emanuele"));

        for(String player : playerList){
            if(player.equals("Anatoly") || player.equals("Tommaso")){
                tutorialGame.getShipboards().get(player).destroyTile(2, 3);
            }
        }

        assertFalse(tutorialGame.isPlayerStillAbleToPlay("Anatoly"));
        assertFalse(tutorialGame.isPlayerStillAbleToPlay("Tommaso"));

        tutorialGame.getShipboards().get("Federico").setDaysOnFlight(10);
        tutorialGame.getShipboards().get("Emanuele").setDaysOnFlight(100);
        tutorialGame.getFlightboard().shiftRocket("Federico", 1);

        assertFalse(tutorialGame.isPlayerStillAbleToPlay("Federico"));

    }

    @Test
    void manageInvalidPlayers() {
        Game tutorialGame;
        Game level2Game;
        tutorialGame = new TutorialGame(playerList, null);
        level2Game = new Level2Game(playerList, null);
        manageInvalidPlayersInTutorialGame(tutorialGame);
        manageInvalidPlayersInLevel2Game(level2Game);
    }

    private void manageInvalidPlayersInLevel2Game(Game level2Game) {
        level2Game.getFlightboard().placeRocket("Anatoly", 0);
        level2Game.getFlightboard().placeRocket("Tommaso", 1);
        level2Game.getFlightboard().placeRocket("Federico", 2);
        level2Game.getFlightboard().placeRocket("Emanuele", 3);
        level2Game.setCurrPlayer(level2Game.getFlightboard().getOrderedRockets().getFirst());

        for(String player : playerList){
            if(player.equals("Anatoly") || player.equals("Tommaso")){
                level2Game.getShipboards().get(player).destroyTile(2, 3);
            }
        }

        assertFalse(level2Game.isPlayerStillAbleToPlay("Anatoly"));
        assertFalse(level2Game.isPlayerStillAbleToPlay("Tommaso"));

        level2Game.getShipboards().get("Federico").setDaysOnFlight(10);
        level2Game.getShipboards().get("Emanuele").setDaysOnFlight(100);
        level2Game.getFlightboard().shiftRocket("Federico", 1);

        level2Game.manageInvalidPlayers();

        assertTrue(level2Game.getShipboards().get("Anatoly").isAbandoned());
        assertTrue(level2Game.getShipboards().get("Tommaso").isAbandoned());
        assertTrue(level2Game.getShipboards().get("Federico").isAbandoned());
        assertFalse(level2Game.getShipboards().get("Emanuele").isAbandoned());
    }

    private void manageInvalidPlayersInTutorialGame(Game tutorialGame) {
        tutorialGame.getFlightboard().placeRocket("Anatoly", 0);
        tutorialGame.getFlightboard().placeRocket("Tommaso", 1);
        tutorialGame.getFlightboard().placeRocket("Federico", 2);
        tutorialGame.getFlightboard().placeRocket("Emanuele", 3);
        tutorialGame.setCurrPlayer(tutorialGame.getFlightboard().getOrderedRockets().getFirst());

        for(String player : playerList){
            if(player.equals("Anatoly") || player.equals("Tommaso")){
                tutorialGame.getShipboards().get(player).destroyTile(2, 3);
            }
        }

        assertFalse(tutorialGame.isPlayerStillAbleToPlay("Anatoly"));
        assertFalse(tutorialGame.isPlayerStillAbleToPlay("Tommaso"));

        tutorialGame.getShipboards().get("Federico").setDaysOnFlight(10);
        tutorialGame.getShipboards().get("Emanuele").setDaysOnFlight(100);
        tutorialGame.getFlightboard().shiftRocket("Federico", 1);

        tutorialGame.manageInvalidPlayers();

        assertTrue(tutorialGame.getShipboards().get("Anatoly").isAbandoned());
        assertTrue(tutorialGame.getShipboards().get("Tommaso").isAbandoned());
        assertTrue(tutorialGame.getShipboards().get("Federico").isAbandoned());
        assertFalse(tutorialGame.getShipboards().get("Emanuele").isAbandoned());
    }


    @Test
    void end_level_2_game_should_calculate_the_scores_properly() {
        /* End game does:
            1. Calculate the partial scores : Score = Sold Goods - Discard Tiles
            2. Adding scores for position in the flightboard
            3. Adding scores for the best shipboard
            4. Sort the scores in descending order
        * */
        List<String> players = List.of("Anatoly", "Tommaso");
        Game level2Game = new Level2Game(players, null);

        level2Game.getFlightboard().placeRocket("Anatoly", 0);
        level2Game.getFlightboard().placeRocket("Tommaso", 1);
        level2Game.setCurrPlayer(level2Game.getFlightboard().getOrderedRockets().getFirst());

        SpecialStorageCompartment rc = new SpecialStorageCompartment("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 5);
        rc.addGoodBlock(GoodBlock.REDBLOCK);
        rc.addGoodBlock(GoodBlock.REDBLOCK);
        rc.addGoodBlock(GoodBlock.YELLOWBLOCK);
        rc.addGoodBlock(GoodBlock.GREENBLOCK);
        rc.addGoodBlock(GoodBlock.BLUEBLOCK);
        level2Game.getShipboards().get("Anatoly").weldComponentTile(rc, 2, 4);
        level2Game.getShipboards().get("Anatoly").destroyTile(2, 3);

        SpecialStorageCompartment rc2 = new SpecialStorageCompartment("1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, 4);
        rc2.addGoodBlock(GoodBlock.REDBLOCK);
        rc2.addGoodBlock(GoodBlock.YELLOWBLOCK);
        rc2.addGoodBlock(GoodBlock.GREENBLOCK);
        rc2.addGoodBlock(GoodBlock.BLUEBLOCK);
        level2Game.getShipboards().get("Tommaso").weldComponentTile(rc2, 2, 4);

        Map<String, Integer> scores = level2Game.endGame();

        // Selling goods:
        // score = 2 red + 1 yellow + 1 green + 1 blue =
        //       = 2*4 + 1*3 + 1*2 + 1*1
        //       = 14
        // Discard tiles: 1, so 14 - 1 = 13
        // Position in the flightboard: 2*(4-0) = 8, so 13 + 8 = 21
        // Best shipboard: 0, so 21 + 0 = 21
        // The score should be 21
        assertEquals(21, scores.get("Anatoly"));

        //Selling goods:
        // score = 1 red + 1 yellow + 1 green + 1 blue =
        //       = 1*4 + 1*3 + 1*2 + 1*1
        //       = 10
        // Discard tiles: 0, so 10 - 0 = 10
        // Position in the flightboard: 2*(4-1) = 6, so 10 + 6 = 16
        // Best shipboard: 1, so 16 + 4 = 20
        // The score should be 20
        assertEquals(20, scores.get("Tommaso"));

        // now Anatoly abandons so when i calculate the score it should count the good blocks divided by 2
        level2Game.getShipboards().get("Anatoly").abandons();
        // so the score should be 14/2 -1 + 8 + 0 = 14
        assertEquals(14, level2Game.endGame().get("Anatoly"));

    }

    @Test
    public void testPlaceAstronautsSuccessfully() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        game.placeAstronauts("Tommaso", 3, 3);
        ComponentTile tile = game.getShipboards().get("Tommaso").getComponentTileFromGrid(3,3).get();
        assertEquals(2, tile.getCrewNumber());
    }

    @Test
    public void testPlaceAstronautsWrongPhase() {
        game.setGamePhase(new BuildingPhase(game)); // Una fase che NON è PlaceCrewMembersPhase
        assertThrows(IllegalStateException.class, () -> {
            game.placeAstronauts("Tommaso", 3, 3);
        });
    }

    @Test
    public void testPlaceAstronautsOnNonCabinTile() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        game.getShipboards().get("Tommaso").weldComponentTile(
                new DoubleEngine("1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 4, 4);
        assertThrows(IllegalArgumentException.class, () -> {
            game.placeAstronauts("Tommaso", 4, 4);
        });
    }

    @Test
    public void testPlaceAstronautsOnOccupiedTile() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        game.placeAstronauts("Tommaso", 3, 3); // piazzamento valido
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // riporta la fase (per il secondo tentativo)
        assertThrows(IllegalArgumentException.class, () -> {
            game.placeAstronauts("Tommaso", 3, 3); // già occupata
        });
    }

    @Test
    public void testPlaceBrownAlienSuccessfully() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Prepara tile cabina alienabile a (2,2) con un AlienAddon vicino
        shipboard.weldComponentTile(new RegularCabin("C1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 2, 2);
        shipboard.weldComponentTile(new AlienAddon("A1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, "brown"), 3, 2);

        game.placeBrownAlien("Tommaso", 2, 2);
        ComponentTile tile = shipboard.getComponentTileFromGrid(2, 2).get();
        assertTrue(tile.isAlienPresent("brown"));
    }

    @Test
    public void testPlaceBrownAlienWrongPhase() {
        game.setGamePhase(new BuildingPhase(game)); // Fase errata
        assertThrows(IllegalStateException.class, () -> {
            game.placeBrownAlien("Tommaso", 2, 2);
        });
    }

    @Test
    public void testPlaceBrownAlienOnNonCabinTile() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Posiziona un AlienAddon ma nessuna cabina a (4,4)
        shipboard.weldComponentTile(new AlienAddon("A1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, "brown"), 4, 4);

        assertThrows(IllegalArgumentException.class, () -> {
            game.placeBrownAlien("Tommaso", 4, 4); // Non è una cabina
        });
    }

    @Test
    public void testPlaceBrownAlienWhenNotPlaceable() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Cabina ma senza AlienAddon adiacente → non alienabile
        shipboard.weldComponentTile(new RegularCabin("C2", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 1, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            game.placeBrownAlien("Tommaso", 5, 5); // Non c'è AlienAddon vicino
        });
    }

    @Test
    public void testPlacePurpleAlienSuccessfully() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Prepara tile cabina alienabile a (2,2) con AlienAddon adiacente
        shipboard.weldComponentTile(new RegularCabin("C1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 2, 2);
        shipboard.weldComponentTile(new AlienAddon("A1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, "purple"), 3, 2);

        game.placePurpleAlien("Tommaso", 2, 2);
        ComponentTile tile = shipboard.getComponentTileFromGrid(2, 2).get();
        assertTrue(tile.isAlienPresent("purple"));
    }

    @Test
    public void testPlacePurpleAlienWrongPhase() {
        game.setGamePhase(new BuildingPhase(game)); // Fase non valida
        assertThrows(IllegalStateException.class, () -> {
            game.placePurpleAlien("Tommaso", 2, 2);
        });
    }

    @Test
    public void testPickCoveredTileSuccessfully() {
        game.setGamePhase(new BuildingPhase(game));
        game.getCoveredComponentTiles().clear();

        // Aggiunge alcune tile coperte, incluse cabin e addon
        game.getCoveredComponentTiles().add(new RegularCabin("C1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH));
        game.getCoveredComponentTiles().add(new DoubleEngine("E1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH));

        game.pickCoveredTile("Tommaso");

        ComponentTile tileInHand = game.getShipboards().get("Tommaso").getTileInHand();
        assertNotNull(tileInHand);
        assertFalse(game.getCoveredComponentTiles().contains(tileInHand)); // Verifica che sia stata rimossa dal pool
    }

    @Test
    public void testPickCoveredTileWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // Fase errata

        assertThrows(IllegalStateException.class, () -> {
            game.pickCoveredTile("Tommaso");
        });
    }

    @Test
    public void testPickCoveredTileEmptyPool() {
        game.setGamePhase(new BuildingPhase(game));
        game.getCoveredComponentTiles().clear(); // Vuoto

        assertThrows(IllegalStateException.class, () -> {
            game.pickCoveredTile("Tommaso");
        });
    }

    @Test
    public void testPickCoveredTileSkipsAlienAddonInTutorial() {
        game.setGamePhase(new BuildingPhase(game));
        game.getCoveredComponentTiles().clear();

        game.getCoveredComponentTiles().add(new AlienAddon("A1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, "brown"));
        game.getCoveredComponentTiles().add(new RegularCabin("C2", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH));

        game.pickCoveredTile("Tommaso");

        ComponentTile tileInHand = game.getShipboards().get("Tommaso").getTileInHand();
        assertNotNull(tileInHand);
        assertFalse(tileInHand.isAlienAddon()); // L'alien addon è saltato
    }

    @Test
    public void testPickUncoveredTileSuccessfully() {
        game.setGamePhase(new BuildingPhase(game));

        ComponentTile tile = new DoubleEngine("E1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);

        game.getUncoveredComponentTiles().add(tile);

        game.pickUncoveredTile("Tommaso", "E1");

        assertEquals(tile, game.getShipboards().get("Tommaso").getTileInHand());
        assertFalse(game.getUncoveredComponentTiles().contains(tile));
    }

    @Test
    public void testPickUncoveredTileWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        assertThrows(IllegalStateException.class, () -> {
            game.pickUncoveredTile("Tommaso", "any.png");
        });
    }

    @Test
    public void testPickUncoveredTileEmptyList() {
        game.setGamePhase(new BuildingPhase(game));
        game.getUncoveredComponentTiles().clear(); // assicura lista vuota

        assertThrows(IllegalStateException.class, () -> {
            game.pickUncoveredTile("Tommaso", "engine.png");
        });
    }

    @Test
    public void testPickUncoveredTileNotFound() {
        game.setGamePhase(new BuildingPhase(game));
        ComponentTile tile = new DoubleEngine("E1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);

        game.getUncoveredComponentTiles().add(tile);

        assertThrows(IllegalStateException.class, () -> {
            game.pickUncoveredTile("Tommaso", "fake_tile.png");
        });
    }

    @Test
    public void testPlacePurpleAlienOnNonCabinTile() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Posiziona solo AlienAddon a (4,4), senza cabina
        shipboard.weldComponentTile(new AlienAddon("A1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, "purple"), 4, 4);

        assertThrows(IllegalArgumentException.class, () -> {
            game.placePurpleAlien("Tommaso", 4, 4); // Non è una cabina
        });
    }

    @Test
    public void testWeldComponentTileSuccessfully() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        ComponentTile tile = new DoubleEngine("E1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.setTileInHand(tile);

        game.weldComponentTile("Tommaso", 2, 2);

        assertTrue(shipboard.getComponentTileFromGrid(2, 2).isPresent());
        assertEquals(tile, shipboard.getComponentTileFromGrid(2, 2).get());
        assertNull(shipboard.getTileInHand());
    }


    @Test
    public void testPlacePurpleAlienWhenNotPlaceable() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Posiziona solo una cabina, nessun AlienAddon vicino → non piazzabile
        shipboard.weldComponentTile(new RegularCabin("C2", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH),1 , 1);

        assertThrows(IllegalArgumentException.class, () -> {
            game.placePurpleAlien("Tommaso", 5, 5); // Non piazzabile (manca AlienAddon purple vicino)
        });
    }

    @Test
    public void testWeldComponentTileWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // fase errata
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        ComponentTile tile = new RegularCabin("R1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.setTileInHand(tile);

        assertThrows(IllegalStateException.class, () -> {
            game.weldComponentTile("Tommaso", 2, 2);
        });
    }

    @Test
    public void testWeldComponentTileOnOccupiedPosition() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        ComponentTile existing = new RegularCabin("C1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.weldComponentTile(existing, 1, 1); // già presente

        ComponentTile newTile = new DoubleEngine("E1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.setTileInHand(newTile);

        assertThrows(IllegalStateException.class, () -> {
            game.weldComponentTile("Tommaso", 1, 1);
        });
    }

    @Test
    public void testStandbyComponentTileSuccessfully() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        ComponentTile tile = new RegularCabin("CAB", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.setTileInHand(tile);

        game.standbyComponentTile("Tommaso");

        assertNull(shipboard.getTileInHand());
        assertEquals(shipboard.getStandbyComponent()[0].get(), tile);
    }

    @Test
    public void testStandbyComponentTileWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // fase sbagliata
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        ComponentTile tile = new DoubleEngine("ENG", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.setTileInHand(tile);

        assertThrows(IllegalStateException.class, () -> {
            game.standbyComponentTile("Tommaso");
        });
    }

    @Test
    public void testPickStandByComponentTileSuccessfully() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Aggiungo una tile in standby
        ComponentTile standbyTile = new RegularCabin("CAB", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.standbyComponentTile(standbyTile);

        // Controllo che tileInHand sia null prima
        assertNull(shipboard.getTileInHand());

        // Provo a prendere la tile standby all'indice 0
        game.pickStandByComponentTile("Tommaso", 0);

        // Verifico che la tile in mano sia quella appena presa dallo standby
        assertEquals(standbyTile, shipboard.getTileInHand());
        // La tile non deve più essere in standby
        assertTrue(shipboard.getStandbyComponent()[0].isEmpty());
    }

    @Test
    public void testPickStandByComponentTileWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // fase sbagliata
        assertThrows(IllegalStateException.class, () -> {
            game.pickStandByComponentTile("Tommaso", 0);
        });
    }

    @Test
    public void testPickStandByComponentTileInvalidIndex() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Nessuna tile in standby
        assertThrows(IllegalStateException.class, () -> {
            game.pickStandByComponentTile("Tommaso", 0);
        });

        // Aggiungo una tile in standby e provo a prenderla con indice fuori range
        ComponentTile standbyTile = new RegularCabin("CAB", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.standbyComponentTile(standbyTile);

        assertThrows(IllegalStateException.class, () -> {
            game.pickStandByComponentTile("Tommaso", 1); // indice non valido
        });
    }

    @Test
    public void testDiscardComponentTileSuccessfully() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Preparo una tile in mano da scartare
        ComponentTile tileInHand = new RegularCabin("CAB", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH);
        shipboard.setTileInHand(tileInHand);

        int initialUncoveredSize = game.getUncoveredComponentTiles().size();

        // Scarto la tile
        game.discardComponentTile("Tommaso");

        // La tile in mano deve essere null
        assertNull(shipboard.getTileInHand());

        // La tile deve essere stata aggiunta alla lista uncoveredComponentTiles
        assertEquals(initialUncoveredSize + 1, game.getUncoveredComponentTiles().size());
        assertTrue(game.getUncoveredComponentTiles().contains(tileInHand));
    }

    @Test
    public void testDiscardComponentTileWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // fase sbagliata
        assertThrows(IllegalStateException.class, () -> {
            game.discardComponentTile("Tommaso");
        });
    }

    @Test
    public void testDiscardComponentTileWithNullTileInHand() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");

        // Tile in mano è null
        shipboard.setTileInHand(null);

        // Scartare tile null potrebbe essere gestito o no a seconda della logica
        // Se vuoi testare, puoi vedere se viene sollevata eccezione o meno
        // Supponiamo che non sollevi eccezione ma semplicemente non faccia nulla
        game.discardComponentTile("Tommaso");

        assertNull(shipboard.getTileInHand());
        // uncoveredComponentTiles non cambia perché nulla è stato aggiunto
    }

    @Test
    public void testFinishBuildingSuccessfullyInBuildingPhase() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setRocketPlaced(false);
        shipboard.setFinishedShipboard(false);

        game.finishBuilding("Tommaso", 0);

        // Verifica che il razzo sia posizionato nel flightboard
        assertTrue(game.getFlightboard().getOrderedRockets().contains("Tommaso"));
        assertEquals(4, game.getFlightboard().getPositions().get("Tommaso"));
        // Verifica stato shipboard aggiornato
        assertTrue(shipboard.isRocketPlaced());
        assertTrue(shipboard.isFinishedShipboard());
        assertFalse(shipboard.isCorrectingShip());
        // Verifica che il giocatore corrente sia aggiornato al leader
        assertEquals(game.getCurrPlayer(), game.getFlightboard().getOrderedRockets().getFirst());
    }

    @Test
    public void testFinishBuildingSuccessfullyInCorrectingShipPhase() {
        game.setGamePhase(new CorrectingShipPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setRocketPlaced(false);
        shipboard.setCorrectingShip(true);
        shipboard.setFinishedShipboard(false);

        game.finishBuilding("Tommaso", 2);

        assertTrue(game.getFlightboard().getOrderedRockets().contains("Tommaso"));
        assertEquals(1, game.getFlightboard().getPositions().get("Tommaso"));
        assertTrue(shipboard.isRocketPlaced());
        assertTrue(shipboard.isFinishedShipboard());
        assertFalse(shipboard.isCorrectingShip());
    }

    @Test
    public void testFinishBuildingThrowsExceptionIfWrongPhase() {
        game.setGamePhase(new PlaceCrewMembersPhase(game)); // fase sbagliata
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setRocketPlaced(false);

        assertThrows(IllegalStateException.class, () -> {
            game.finishBuilding("Tommaso", 0);
        });
    }

    @Test
    public void testFinishBuildingThrowsExceptionIfRocketAlreadyPlacedInCorrectingShip() {
        game.setGamePhase(new CorrectingShipPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setRocketPlaced(true);
        shipboard.setCorrectingShip(true);

        assertThrows(IllegalStateException.class, () -> {
            game.finishBuilding("Tommaso", 3);
        });
    }
    @Test
    public void testDestroyTileSuccessfullyInCorrectingShipPhase() {
        game.setGamePhase(new CorrectingShipPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setCorrectingShip(false); // anche se false, la fase è CorrectingShip quindi ok

        game.destroyTile("Tommaso", 3, 3);

        // Verifica che il tile sia stato distrutto (dipende da implementazione di destroyTile, supponiamo cambi stato)
        assertFalse(shipboard.getComponentTileFromGrid(1, 1).isPresent());
        // Verifica che la fase possa essere cambiata (se previsto)
        // (puoi aggiungere ulteriori assert a seconda del comportamento della fase)
    }

    @Test
    public void testDestroyTileSuccessfullyInBuildingPhaseWithCorrectingShipTrue() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setCorrectingShip(true);

        game.destroyTile("Tommaso", 3, 3);

        assertFalse(shipboard.getComponentTileFromGrid(2, 2).isPresent());
    }

    @Test
    public void testPickCardThrowsExceptionIfWrongPhase() {
        game.setGamePhase(new BuildingPhase(game)); // fase sbagliata
        assertThrows(IllegalStateException.class, () -> {
            game.pickCard();
        });
    }

    @Test
    public void testDestroyTileThrowsExceptionIfNotCorrectPhaseOrNotCorrectingShip() {
        game.setGamePhase(new BuildingPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setCorrectingShip(false); // Non in fase di correzione

        assertThrows(IllegalStateException.class, () -> {
            game.destroyTile("Tommaso", 1, 1);
        });
    }

    @Test
    public void testDestroyTileThrowsExceptionIfPhaseNotCorrectingShipOrBuilding() {
        game.setGamePhase(new PlaceCrewMembersPhase(game));
        Shipboard shipboard = game.getShipboards().get("Tommaso");
        shipboard.setCorrectingShip(true);

        assertThrows(IllegalStateException.class, () -> {
            game.destroyTile("Tommaso", 1, 1);
        });
    }


    @Test
    void end_tutorial_game_should_(){
        /* End game does:
            1. Calculate the partial scores : Score = Sold Goods - Discard Tiles
            2. Adding scores for position in the flightboard
            3. Adding scores for the best shipboard
            4. Sort the scores in descending order
        * */
        List<String> players = List.of("Anatoly", "Tommaso");
        Game tutorial = new TutorialGame(players, null);

        tutorial.getFlightboard().placeRocket("Anatoly", 0);
        tutorial.getFlightboard().placeRocket("Tommaso", 1);
        tutorial.setCurrPlayer(tutorial.getFlightboard().getOrderedRockets().getFirst());

        SpecialStorageCompartment rc = new SpecialStorageCompartment("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 5);
        rc.addGoodBlock(GoodBlock.REDBLOCK);
        rc.addGoodBlock(GoodBlock.REDBLOCK);
        rc.addGoodBlock(GoodBlock.YELLOWBLOCK);
        rc.addGoodBlock(GoodBlock.GREENBLOCK);
        rc.addGoodBlock(GoodBlock.BLUEBLOCK);
        tutorial.getShipboards().get("Anatoly").weldComponentTile(rc, 2, 4);
        tutorial.getShipboards().get("Anatoly").destroyTile(2, 3);

        SpecialStorageCompartment rc2 = new SpecialStorageCompartment("1", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, 4);
        rc2.addGoodBlock(GoodBlock.REDBLOCK);
        rc2.addGoodBlock(GoodBlock.YELLOWBLOCK);
        rc2.addGoodBlock(GoodBlock.GREENBLOCK);
        rc2.addGoodBlock(GoodBlock.BLUEBLOCK);
        tutorial.getShipboards().get("Tommaso").weldComponentTile(rc2, 2, 4);

        Map<String, Integer> scores = tutorial.endGame();

        // Selling goods:
        // score = 2 red + 1 yellow + 1 green + 1 blue =
        //       = 2*4 + 1*3 + 1*2 + 1*1
        //       = 14
        // Discard tiles: 1, so 14 - 1 = 13
        // Position in the flightboard: (4-0) = 4, so 13 + 4 = 17
        // Best shipboard: 0, so 17 + 0 = 17
        // The score should be 17
        assertEquals(17, scores.get("Anatoly"));

        //Selling goods:
        // score = 1 red + 1 yellow + 1 green + 1 blue =
        //       = 1*4 + 1*3 + 1*2 + 1*1
        //       = 10
        // Discard tiles: 0, so 10 - 0 = 10
        // Position in the flightboard: (4-1) = 3, so 10 + 3 = 13
        // Best shipboard: 1, so 13 + 2 = 15
        // The score should be 15
        assertEquals(15, scores.get("Tommaso"));

        // now Anatoly abandons so when i calculate the score it should count the good blocks divided by 2
        tutorial.getShipboards().get("Anatoly").abandons();
        // so the score should be 14/2 -1 + 4 + 0 = 10
        assertEquals(10, tutorial.endGame().get("Anatoly"));

    }


}