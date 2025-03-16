package it.polimi.ingsw.is25am22new.Model.Shipboards;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.*;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ShipboardTest {
    private List<ComponentTile> initializeTiles(){
        List<ComponentTile> tiles = new ArrayList<>();
        tiles.add(new Engine("0", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleEngine("1", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new Cannon("2", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleCannon("3", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("4", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        return tiles;
    }
    @Test
    void test_shipboard_should_be_considered_valid() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(0),3, 3);
        ship.weldComponentTile(tiles.get(1),2, 4);
        ship.weldComponentTile(tiles.get(2),1, 3);
        tiles.get(3).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(3),2, 2);
        ship.weldComponentTile(tiles.get(4),1, 4);

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
        ship.weldComponentTile(tiles.get(4),2, 4);
        ship.checkShipboard();
        assertEquals(1, ship.getComponentTileFromGrid(1, 3).getColor());
        assertEquals(-1, tiles.get(0).getColor());
        assertFalse(ship.checkShipboard());
    }


}