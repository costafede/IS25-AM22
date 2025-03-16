package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.Flightboards.Level2FlightBoard;
import it.polimi.ingsw.is25am22new.Model.Flightboards.TutorialFlightBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FlightboardTest {
    static List<String> orderedRockets = new ArrayList<>();
    static Map<String, Integer> positions = new HashMap<>();
    int f1BoardLength = 24;
    int f2BoardLength = 18;

    Level2FlightBoard f1 = new Level2FlightBoard();
    TutorialFlightBoard f2 = new TutorialFlightBoard();

    @BeforeAll
    static void setUp() {
        orderedRockets.add("Federico");
        orderedRockets.add("Alex");
        orderedRockets.add("Giuseppe");
        orderedRockets.add("Umberto");
    }

    @Test
    void getOrderedRockets() {
    }

    @Test
    void getPositions() {
    }

    @Test
    void setOrderedRockets() {
    }

    @Test
    void shiftRocket() {
    }

    @Test
    void placeRocket() {
        f1.placeRocket("Federico", 0);
        f1.placeRocket("Alex", 1);
        f1.placeRocket("Giuseppe", 2);
        f1.placeRocket("Umberto", 3);
        assertEquals(6, f1.getPositions().get("Federico"));
        assertEquals(3, f1.getPositions().get("Alex"));
        assertEquals(1, f1.getPositions().get("Giuseppe"));
        assertEquals(0, f1.getPositions().get("Umberto"));

        f2.placeRocket("Federico", 0);
        f2.placeRocket("Alex", 1);
        f2.placeRocket("Giuseppe", 2);
        f2.placeRocket("Umberto", 3);
        assertEquals(4, f2.getPositions().get("Federico"));
        assertEquals(2, f2.getPositions().get("Alex"));
        assertEquals(1, f2.getPositions().get("Giuseppe"));
        assertEquals(0, f2.getPositions().get("Umberto"));
    }
}