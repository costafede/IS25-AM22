package it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StardustCardTest {

    static Map<String, Shipboard> shipboards = new HashMap<>();

    private List<ComponentTile> initializeTiles(){
        List<ComponentTile> tiles = new ArrayList<>();
        tiles.add(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleEngine("1", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new Cannon("2", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleCannon("3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("6", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new AlienAddon("7", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, "purple"));
        tiles.add(new AlienAddon("8", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "brown"));
        tiles.add(new RegularCabin("9", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new SpecialStorageCompartment("11", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2));
        tiles.add(new StorageCompartment("12", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new StorageCompartment("13", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new RegularCabin("14", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH));
        tiles.add(new RegularCabin("15", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("16", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("17", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("18", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("19", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("20", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("21", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("22", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("23", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("24", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("25", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("26", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("27", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("28", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH));
        tiles.add(new RegularCabin("29", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH));
        tiles.add(new RegularCabin("30", Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH));
        tiles.add(new RegularCabin("31", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("32", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("33", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("34", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        return tiles;
    }

    @Test
    void moves_back_more_ships_correctly_level2(){
        List<ComponentTile> tiles = initializeTiles();
        List<String> players = List.of("Federico", "Emanuele");
        Game level2Game = new Level2Game(players, null);

        Shipboard ship0 = level2Game.getShipboards().get(players.get(0));
        Shipboard ship1 = level2Game.getShipboards().get(players.get(1));

        Flightboard level2FlightBoard = level2Game.getFlightboard();

        level2FlightBoard.placeRocket("Emanuele", 0);
        level2FlightBoard.placeRocket("Federico", 1);
        // zero exposed connectors
        ship0.weldComponentTile(tiles.get(27), 2, 2);
        ship0.weldComponentTile(tiles.get(28), 2, 4);
        ship0.weldComponentTile(tiles.get(29), 1, 3);
        ship0.weldComponentTile(tiles.get(30), 3, 3);

        // some exposed connectors: 12
        ship1.weldComponentTile(tiles.get(31), 2, 2);
        ship1.weldComponentTile(tiles.get(32), 2, 4);
        ship1.weldComponentTile(tiles.get(33), 1, 3);
        ship1.weldComponentTile(tiles.get(34), 3, 3);

        StardustCard sd1 = new StardustCard("test", "test", level2Game, 2, false);
        sd1.activateEffect(null);

        assertEquals(3, level2FlightBoard.getPositions().get("Federico"));
        assertEquals(17, level2FlightBoard.getPositions().get("Emanuele"));
    }
    
    @Test
    void  moves_back_more_ships_correctly_tutorial() {
        List<ComponentTile> tiles = initializeTiles();
        List<String> players = List.of("Federico", "Emanuele");
        Game tutorialGame = new TutorialGame(players, null);

        Shipboard ship0 = tutorialGame.getShipboards().get(players.get(0));
        Shipboard ship1 = tutorialGame.getShipboards().get(players.get(1));

        Flightboard level2FlightBoard = tutorialGame.getFlightboard();

        level2FlightBoard.placeRocket("Emanuele", 0);
        level2FlightBoard.placeRocket("Federico", 1);
        // zero exposed connectors
        ship0.weldComponentTile(tiles.get(27), 2, 2);
        ship0.weldComponentTile(tiles.get(28), 2, 4);
        ship0.weldComponentTile(tiles.get(29), 1, 3);
        ship0.weldComponentTile(tiles.get(30), 3, 3);

        // some exposed connectors: 12
        ship1.weldComponentTile(tiles.get(31), 2, 2);
        ship1.weldComponentTile(tiles.get(32), 2, 4);
        ship1.weldComponentTile(tiles.get(33), 1, 3);
        ship1.weldComponentTile(tiles.get(34), 3, 3);

        StardustCard sd1 = new StardustCard("test", "test", tutorialGame, 2, false);
        sd1.activateEffect(null);

        assertEquals(2, level2FlightBoard.getPositions().get("Federico"));
        assertEquals(9, level2FlightBoard.getPositions().get("Emanuele"));
    }
}