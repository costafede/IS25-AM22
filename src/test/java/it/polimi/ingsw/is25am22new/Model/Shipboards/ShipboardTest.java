package it.polimi.ingsw.is25am22new.Model.Shipboards;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.*;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShipboardTest {
    private List<ComponentTile> initializeTiles(){
        List<ComponentTile> tiles = new ArrayList<>();
        tiles.add(new Engine("0", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleEngine("1", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleCannon("3", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("6", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new AlienAddon("7", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, "purple"));
        tiles.add(new AlienAddon("8", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, "brown"));
        tiles.add(new RegularCabin("9", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        return tiles;
    }

    private void clearGrid(Shipboard shipboard){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                shipboard.weldComponentTile(null, i, j);
            }
        }
    }

    @Test
    void test_shipboards_should_be_considered_valid() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(0),3, 3);
        ship.weldComponentTile(tiles.get(1),2, 4);
        ship.weldComponentTile(tiles.get(2),1, 3);
        tiles.get(3).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(3),2, 2);
        ship.weldComponentTile(tiles.get(4),1, 4);

        assertTrue(ship.checkShipboard());

        tiles = initializeTiles();
        ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(0),3, 3);
        ship.weldComponentTile(tiles.get(1),2, 4);
        ship.weldComponentTile(tiles.get(2),1, 3);
        ship.weldComponentTile(tiles.get(3),1, 4);
        tiles.get(4).rotateCounterClockwise();
        tiles.get(4).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(4),2, 4);

        assertTrue(ship.checkShipboard());
    }

    @Test
    void test_shipboard_should_be_considered_invalid_due_to_non_connected_parts() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(0),4, 1);
        ship.weldComponentTile(tiles.get(2),3, 1);
        ship.weldComponentTile(tiles.get(4),2, 2);
        ship.weldComponentTile(tiles.get(4),1, 3);
        ship.weldComponentTile(tiles.get(4),2, 4);//be careful, in these last three positions there is the same object instance, so if I color the position (2,2), I ve colored also the other three
        assertFalse(ship.checkShipboard());
    }

    @Test
    void test_shipboard_should_be_considered_invalid_due_to_tile_in_front_of_cannons() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(2),3, 1);
        tiles.get(4).rotateCounterClockwise();
        tiles.get(4).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(4),3, 3);
        ship.weldComponentTile(tiles.get(4),2, 4);
        assertFalse(ship.checkShipboard());
    }

    @Test
    void test_shipboard_should_be_considered_invalid_due_to_tile_behind_engine_and_engine_not_facing_down() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(0),2, 4);
        tiles.get(4).rotateCounterClockwise();
        tiles.get(4).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(4),3, 3);
        ship.weldComponentTile(tiles.get(5),3, 4);
        assertFalse(ship.checkShipboard());

        tiles.get(0).rotateCounterClockwise();
        assertFalse(ship.checkShipboard());
    }

    @Test
    void test_shipboard_should_be_considered_invalid_due_tiles_connected_wrongly() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(4),3, 3);
        tiles.get(4).rotateCounterClockwise();
        tiles.get(4).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(5),2, 4);
        ship.weldComponentTile(tiles.get(6),3, 4);
        assertFalse(ship.checkShipboard());

        ship.weldComponentTile(tiles.get(1),3, 4);
        assertTrue(ship.checkShipboard());

        clearGrid(ship);

        ship.weldComponentTile(tiles.get(0),3, 3);
        ship.weldComponentTile(tiles.get(5),3, 4);
        ship.weldComponentTile(tiles.get(6),2, 4);

        assertFalse(ship.checkShipboard());

    }

    @Test
    void test_shipboard_should_remove_alien_from_cabin_when_addon_destroyed() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(8),3, 4);
        ship.weldComponentTile(tiles.get(9),2, 4);
        if(ship.isAlienPlaceable(2, 4, "brown"))
            ship.getComponentTileFromGrid(2,4).putAlien("brown");

        assertTrue(ship.getComponentTileFromGrid(2, 4).isAlienPresent("brown"));

        ship.destroyTile(3, 4);

        assertFalse(ship.getComponentTileFromGrid(2, 4).isAlienPresent("brown"));
    }

}