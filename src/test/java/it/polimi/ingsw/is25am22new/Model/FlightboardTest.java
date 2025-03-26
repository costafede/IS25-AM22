package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.Flightboards.Level2FlightBoard;
import it.polimi.ingsw.is25am22new.Model.Flightboards.TutorialFlightBoard;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FlightboardTest {
    static List<String> playerList = new ArrayList<>(List.of("Cristina", "Alex", "Bianca", "Davide"));

    static List<String> orderedRockets = new ArrayList<>();
    static Map<String, Integer> tutorialPositions = new HashMap<>();
    static Map<String, Integer> level2Positions = new HashMap<>();
    int level2BoardLength = 24;
    int tutorialBoardLength = 18;

    Level2Game level2Game = new Level2Game(playerList);
    TutorialGame tutorialGame = new TutorialGame(playerList);

    Level2FlightBoard level2FlightBoard = new Level2FlightBoard(level2Game);
    TutorialFlightBoard tutorialFlightBoard = new TutorialFlightBoard(tutorialGame);

    static Map<String, Shipboard> shipboards = new HashMap<>();
    static Bank bank = new Bank();
    @BeforeAll
    static void setUp() {
        List<String> playerList = List.of("Cristina", "Alex", "Bianca", "Davide");
        List<String> colors = List.of("red", "green", "blue", "yellow");

        for(int i = 0; i < playerList.size(); i++) {
            shipboards.put(playerList.get(i), new Shipboard(colors.get(i), playerList.get(i), bank));
        }

        orderedRockets.add("Cristina");
        orderedRockets.add("Alex");
        orderedRockets.add("Bianca");
        orderedRockets.add("Davide");

        level2Positions.put("Cristina", 6);
        level2Positions.put("Alex", 3);
        level2Positions.put("Bianca", 1);
        level2Positions.put("Davide", 0);

        tutorialPositions.put("Cristina", 4);
        tutorialPositions.put("Alex", 2);
        tutorialPositions.put("Bianca", 1);
        tutorialPositions.put("Davide", 0);
    }

    @Test
    void test_get_ordered_rockets_should_return_right_list() {
        level2FlightBoard.placeRocket("Cristina", 0);
        level2FlightBoard.placeRocket("Alex", 1);
        level2FlightBoard.placeRocket("Bianca", 2);
        level2FlightBoard.placeRocket("Davide", 3);

        assertEquals(level2FlightBoard.getOrderedRockets(), orderedRockets);

        tutorialFlightBoard.placeRocket("Cristina", 0);
        tutorialFlightBoard.placeRocket("Alex", 1);
        tutorialFlightBoard.placeRocket("Bianca", 2);
        tutorialFlightBoard.placeRocket("Davide", 3);

        assertEquals(tutorialFlightBoard.getOrderedRockets(), orderedRockets);
    }

    @Test
    void test_get_positions_should_return_right_map() {
        level2FlightBoard.placeRocket("Cristina", 0);
        level2FlightBoard.placeRocket("Alex", 1);
        level2FlightBoard.placeRocket("Bianca", 2);
        level2FlightBoard.placeRocket("Davide", 3);

        assertEquals(level2Positions, level2FlightBoard.getPositions());

        tutorialFlightBoard.placeRocket("Cristina", 0);
        tutorialFlightBoard.placeRocket("Alex", 1);
        tutorialFlightBoard.placeRocket("Bianca", 2);
        tutorialFlightBoard.placeRocket("Davide", 3);

        assertEquals(tutorialPositions, tutorialFlightBoard.getPositions());
    }

    @Test
    void test_shift_rocket_shuold_shift_into_right_position_and_update_ordered_rockets_list() {
        List<String> newOrderedRockets;
        // Surpassing forward check
        // Surpassing backward check
        // Steps forward above 0 check
        // Steps backward under 0 check
        // New ordered rockets (surpassing forward and backward) check
        level2FlightBoard.placeRocket("Cristina", 0);
        level2FlightBoard.placeRocket("Alex", 1);
        level2FlightBoard.placeRocket("Bianca", 2);
        level2FlightBoard.placeRocket("Davide", 3);

        level2FlightBoard.shiftRocket("Cristina", -2);
        level2FlightBoard.shiftRocket("Alex", -1);
        level2FlightBoard.shiftRocket("Bianca", 5);
        level2FlightBoard.shiftRocket("Davide", 2);

        assertEquals(8, level2FlightBoard.getPositions().get("Cristina"));
        assertEquals(4, level2FlightBoard.getPositions().get("Alex"));
        assertEquals(19, level2FlightBoard.getPositions().get("Bianca"));
        assertEquals(22, level2FlightBoard.getPositions().get("Davide"));

        newOrderedRockets = List.of("Cristina", "Alex", "Davide", "Bianca");

        assertEquals(newOrderedRockets, level2FlightBoard.getOrderedRockets());

        tutorialFlightBoard.placeRocket("Cristina", 0);
        tutorialFlightBoard.placeRocket("Alex", 1);
        tutorialFlightBoard.placeRocket("Bianca", 2);
        tutorialFlightBoard.placeRocket("Davide", 3);

        tutorialFlightBoard.shiftRocket("Cristina", -2);
        tutorialFlightBoard.shiftRocket("Alex", -15);
        tutorialFlightBoard.shiftRocket("Bianca", 1);
        tutorialFlightBoard.shiftRocket("Davide", 3);

        assertEquals(6, tutorialFlightBoard.getPositions().get("Cristina"));
        assertEquals(2, tutorialFlightBoard.getPositions().get("Alex"));
        assertEquals(17, tutorialFlightBoard.getPositions().get("Bianca"));
        assertEquals(14, tutorialFlightBoard.getPositions().get("Davide"));

        newOrderedRockets = List.of("Alex", "Cristina", "Bianca", "Davide");

        assertEquals(newOrderedRockets, tutorialFlightBoard.getOrderedRockets());
    }

    @Test
    void test_place_rocket_should_be_right_position() {
        level2FlightBoard.placeRocket("Cristina", 0);
        level2FlightBoard.placeRocket("Alex", 1);
        level2FlightBoard.placeRocket("Davide", 2);
        level2FlightBoard.placeRocket("Bianca", 3);
        assertEquals(6, level2FlightBoard.getPositions().get("Cristina"));
        assertEquals(3, level2FlightBoard.getPositions().get("Alex"));
        assertEquals(1, level2FlightBoard.getPositions().get("Davide"));
        assertEquals(0, level2FlightBoard.getPositions().get("Bianca"));

        tutorialFlightBoard.placeRocket("Cristina", 0);
        tutorialFlightBoard.placeRocket("Alex", 1);
        tutorialFlightBoard.placeRocket("Davide", 2);
        tutorialFlightBoard.placeRocket("Bianca", 3);
        assertEquals(4, tutorialFlightBoard.getPositions().get("Cristina"));
        assertEquals(2, tutorialFlightBoard.getPositions().get("Alex"));
        assertEquals(1, tutorialFlightBoard.getPositions().get("Davide"));
        assertEquals(0, tutorialFlightBoard.getPositions().get("Bianca"));
    }
}