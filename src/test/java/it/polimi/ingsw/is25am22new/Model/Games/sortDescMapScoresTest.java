package it.polimi.ingsw.is25am22new.Model.Games;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class sortDescMapScoresTest {

    @Test
    void sortDescLevel2() {
        Level2Game level2Game = new Level2Game(List.of("player1level2", "player2level2", "player3level2"));

        // Testing sortDesc() method from Level2Game
        Map<String, Integer> scores = new java.util.HashMap<>();
        scores.put("player1level2", 10);
        scores.put("player2level2", 20);
        scores.put("player3level2", 30);
        Map<String, Integer> sortedScores = level2Game.sortDesc(scores);

        //print of the unsorted map
        System.out.println("Unsorted map:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        //print of the sorted map
        System.out.println("Sorted map:");
        for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        //Assertions for the sorted map -> the first element should be the one with the highest value using functional java
        assertTrue(sortedScores.entrySet().stream().findFirst().isPresent() && "player3level2".equals(sortedScores.entrySet().stream().findFirst().get().getKey()));
    }

    @Test
    void sortDeskTutorial(){
        TutorialGame tutorialGame = new TutorialGame(List.of("player1tutorial", "player2tutorial", "player3tutorial"));

        // Testing sortDesc() method from TutorialGame
        Map<String, Integer> scores = new java.util.HashMap<>();
        scores.put("player1tutorial", 10);
        scores.put("player2tutorial", 30);
        scores.put("player3tutorial", 30);
        Map<String, Integer> sortedScores = tutorialGame.sortDesc(scores);

        //print of the unsorted map
        System.out.println("Unsorted map:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        //print of the sorted map
        System.out.println("Sorted map:");
        for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        assertTrue(sortedScores.entrySet().stream().findFirst().isPresent() && (  "player3tutorial".equals(sortedScores.entrySet().stream().findFirst().get().getKey())|| "player2tutorial".equals(sortedScores.entrySet().stream().findFirst().get().getKey()) ));
    }

}