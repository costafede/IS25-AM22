package it.polimi.ingsw.is25am22new.Model.Games;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.SpecialStorageCompartment;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    List<String> playerList = List.of("Anatoly", "Tommaso", "Federico", "Emanuele");


    @Test
    void setCurrPlayerToNext() {
        Game tutorialGame;
        Game level2Game;
        tutorialGame = new TutorialGame(playerList);
        level2Game = new Level2Game(playerList);
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
        tutorialGame = new TutorialGame(playerList);
        level2Game = new Level2Game(playerList);
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
        tutorialGame = new TutorialGame(playerList);
        level2Game = new Level2Game(playerList);
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
        tutorialGame = new TutorialGame(playerList);
        level2Game = new Level2Game(playerList);
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
        Game level2Game = new Level2Game(players);

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
    void end_tutorial_game_should_(){
        /* End game does:
            1. Calculate the partial scores : Score = Sold Goods - Discard Tiles
            2. Adding scores for position in the flightboard
            3. Adding scores for the best shipboard
            4. Sort the scores in descending order
        * */
        List<String> players = List.of("Anatoly", "Tommaso");
        Game tutorial = new TutorialGame(players);

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