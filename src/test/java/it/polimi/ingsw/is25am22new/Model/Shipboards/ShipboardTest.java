package it.polimi.ingsw.is25am22new.Model.Shipboards;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The ShipboardTest class is a test suite designed to verify the functionality, validity, and behavior
 * of the Shipboard and associated components. This class uses unit tests to ensure that specific scenarios
 * within the shipboard mechanics operate correctly. The test cases assess various aspects, such as connectivity,
 * configuration validity, scoring calculations, and whether the Shipboard behaves as expected under certain conditions.
 *
 * Key functionalities tested include:
 * - Checking if cabins are properly connected.
 * - Determining the validity of a shipboard based on correct configuration and placement of components.
 * - Ensuring that rules related to ship parts like cannons, engines, and their orientation are properly adhered to.
 * - Evaluating the removal of aliens from components under specific scenarios.
 * - Calculating strengths of engines and cannons along with exposed connector counts.
 * - Verifying shipwreck recognition and selection logic.
 * - Proper management of valuable blocks within the Shipboard.
 *
 * The class applies annotations like @Test to designate test methods and uses helper methods for initialization
 * and grid clearing tasks.
 */
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
        tiles.add(new StorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new SpecialStorageCompartment("11", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2));
        tiles.add(new StorageCompartment("12", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new StorageCompartment("13", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new RegularCabin("14", Side.ONEPIPE, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        return tiles;
    }

    private void clearGrid(Shipboard shipboard){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if(shipboard.getComponentTileFromGrid(i, j).isPresent())
                    shipboard.destroyTile(i, j);
            }
        }
    }

    @Test
    void test_two_cabins_are_connected_return_true() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship0 = new Shipboard("red", "Tom", null);
        ship0.weldComponentTile(tiles.get(14),2, 2);
        ship0.weldComponentTile(tiles.get(14),2, 4);
        ship0.weldComponentTile(tiles.get(14),1, 3);
        ship0.weldComponentTile(tiles.get(14),3, 3);
        ship0.weldComponentTile(tiles.get(14),4, 6);
        ship0.weldComponentTile(tiles.get(13),3, 6);
        ship0.weldComponentTile(tiles.get(13),2, 6);
        ship0.weldComponentTile(tiles.get(14),4, 0);
        ship0.weldComponentTile(tiles.get(14),3, 0);
        assertTrue(ship0.isConnectedToCabin(2, 2));
        assertTrue(ship0.isConnectedToCabin(2, 4));
        assertTrue(ship0.isConnectedToCabin(1, 3));
        assertTrue(ship0.isConnectedToCabin(3, 3));
        assertFalse(ship0.isConnectedToCabin(4, 6));
        assertTrue(ship0.isConnectedToCabin(3, 6));
        assertFalse(ship0.isConnectedToCabin(2, 6));
        assertFalse(ship0.isConnectedToCabin(4, 0));
        assertFalse(ship0.isConnectedToCabin(3, 0));
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
        ship.weldComponentTile(tiles.get(2),3, 4);
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

        ship.destroyTile(3, 4);
        ship.weldComponentTile(tiles.get(1),3, 4);
        assertTrue(ship.checkShipboard());

        clearGrid(ship);

        ship.weldComponentTile(tiles.get(0),3, 3);
        ship.weldComponentTile(tiles.get(5),3, 4);
        ship.weldComponentTile(tiles.get(6),2, 4);

        assertFalse(ship.checkShipboard());
    }

    @Test
    void test_shipboard_should_be_considered_invalid_due_to_adjacent_smooth_sides_not_creating_a_connected_ship() {
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(new StructuralModule("x", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 1, 4);
        ship.weldComponentTile(new StructuralModule("y", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH), 2, 4);

        assertFalse(ship.checkShipboard());
    }

    @Test
    void test_shipboard_should_remove_alien_from_cabin_when_addon_destroyed() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(8),3, 4);
        ship.weldComponentTile(tiles.get(9),2, 4);
        if(ship.isAlienPlaceable(2, 4, "brown"))
            ship.getComponentTileFromGrid(2,4).ifPresent(ct -> ct.putAlien("brown"));

        AtomicBoolean isAlien = new AtomicBoolean(false);
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct -> isAlien.set(ct.isAlienPresent("brown")));
        assertTrue(isAlien.get());

        ship.destroyTile(3, 4);

        isAlien.set(false);
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct -> isAlien.set(ct.isAlienPresent("brown")));
        assertFalse(isAlien.get());
    }

    @Test
    void test_shipboard_should_not_remove_alien_from_cabin_when_addon_destroyed_because_there_is_another_addon() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(8),3, 4);
        ship.weldComponentTile(new AlienAddon("x", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, "brown"),2, 5);
        ship.getComponentTileFromGrid(2, 5).ifPresent(ComponentTile::rotateClockwise);
        ship.weldComponentTile(tiles.get(9),2, 4);
        if(ship.isAlienPlaceable(2, 4, "brown"))
            ship.getComponentTileFromGrid(2,4).ifPresent(ct -> ct.putAlien("brown"));

        AtomicBoolean isAlien = new AtomicBoolean(false);
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct -> isAlien.set(ct.isAlienPresent("brown")));
        assertTrue(isAlien.get());

        ship.destroyTile(3, 4);

        isAlien.set(false);
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct -> isAlien.set(ct.isAlienPresent("brown")));
        assertTrue(isAlien.get());
    }

    @Test
    void test_should_count_exposed_connectors_correctly(){
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(8),3, 4);
        tiles.get(8).rotateCounterClockwise();
        tiles.get(8).rotateCounterClockwise();
        ship.weldComponentTile(tiles.get(4),2, 5);
        ship.weldComponentTile(tiles.get(9),2, 4);

        assertEquals(7, ship.countExposedConnectors());
    }

    @Test
    void test_should_calculate_engine_strength_correctly(){
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);

        ship.weldComponentTile(tiles.get(1),3, 5);
        ship.weldComponentTile(tiles.get(8),2, 5);
        ship.weldComponentTile(tiles.get(9),2, 4);
        ship.getComponentTileFromGrid(2,4).ifPresent(ct -> ct.putAlien("brown"));

        assertEquals(0, ship.getEngineStrength());

        ship.weldComponentTile(tiles.get(0),3, 4);

        assertEquals(3, ship.getEngineStrength());

        ship.getComponentTileFromGrid(3, 5).ifPresent(ComponentTile::activateComponent);

        assertEquals(5, ship.getEngineStrength());

        ship.destroyTile(2, 5);

        assertEquals(3, ship.getEngineStrength());

        ship.getComponentTileFromGrid(3, 5).ifPresent(ComponentTile::deactivateComponent);

        assertEquals(1, ship.getEngineStrength());
    }

    @Test
    void test_should_calculate_cannon_strength_correctly(){
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);

        ship.weldComponentTile(tiles.get(3),2, 4);
        ship.weldComponentTile(tiles.get(7),2, 5);
        ship.weldComponentTile(tiles.get(9),3, 5);
        ship.getComponentTileFromGrid(3, 5).ifPresent(ComponentTile::rotateCounterClockwise);
        ship.getComponentTileFromGrid(3, 5).ifPresent(ComponentTile::rotateCounterClockwise);

        ship.getComponentTileFromGrid(3,5).ifPresent(ct -> ct.putAlien("purple"));

        assertEquals(0, ship.getCannonStrength());

        ship.weldComponentTile(tiles.get(2),3, 4);
        ship.getComponentTileFromGrid(3, 4).ifPresent(ComponentTile::rotateCounterClockwise);
        ship.getComponentTileFromGrid(3, 4).ifPresent(ComponentTile::rotateCounterClockwise);

        assertEquals(2.5, ship.getCannonStrength());

        ship.getComponentTileFromGrid(2, 4).ifPresent(ComponentTile::activateComponent);

        assertEquals(4.5, ship.getCannonStrength());

        ship.destroyTile(2, 5);

        assertEquals(2.5, ship.getCannonStrength());

        ship.getComponentTileFromGrid(2, 4).ifPresent(ComponentTile::deactivateComponent);

        assertEquals(0.5, ship.getCannonStrength());
        assertTrue(ship.checkShipboard());
    }

    @Test
    void test_get_score_calculates_the_correct_score() {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);

        ship.weldComponentTile(tiles.get(10), 1, 3);
        ship.getComponentTileFromGrid(1, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.YELLOWBLOCK));
        ship.getComponentTileFromGrid(1, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.GREENBLOCK));
        assertEquals(5, ship.getScore());

        ship.weldComponentTile(tiles.get(12), 2, 4);
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct->ct.addGoodBlock(GoodBlock.GREENBLOCK));
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct->ct.addGoodBlock(GoodBlock.BLUEBLOCK));
        assertEquals(8, ship.getScore());

        ship.weldComponentTile(tiles.get(13), 2, 2);
        ship.getComponentTileFromGrid(2, 2).ifPresent(ct->ct.addGoodBlock(GoodBlock.BLUEBLOCK));
        ship.getComponentTileFromGrid(2, 2).ifPresent(ct->ct.addGoodBlock(GoodBlock.BLUEBLOCK));
        assertEquals(10, ship.getScore());

        ship.weldComponentTile(tiles.get(11), 3, 3);
        ship.getComponentTileFromGrid(3, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.REDBLOCK));
        ship.getComponentTileFromGrid(3, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.REDBLOCK));
        assertEquals(18, ship.getScore());
    }

    @Test
    void test_remove_most_valuable_blocks() {
        List<ComponentTile> tiles = initializeTiles();
        Bank bank1 = new Bank();
        Shipboard ship = new Shipboard("red", "Emanuele", bank1);

        // check add more boxes than capacity
        ship.weldComponentTile(tiles.get(10), 1, 3);
        ship.getComponentTileFromGrid(1, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.YELLOWBLOCK));
        ship.getComponentTileFromGrid(1, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.GREENBLOCK));
        ship.getComponentTileFromGrid(1, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.YELLOWBLOCK));
        assertThrows(IllegalArgumentException.class, () -> {
                ship.getComponentTileFromGrid(1, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.YELLOWBLOCK));
        });

        assertEquals(2, ship.getComponentTileFromGrid(1, 3).get().getGoodBlocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(0, ship.getComponentTileFromGrid(1, 3).get().getGoodBlocks().get(GoodBlock.REDBLOCK));
        assertEquals(1, ship.getComponentTileFromGrid(1, 3).get().getGoodBlocks().get(GoodBlock.GREENBLOCK));
        assertEquals(0, ship.getComponentTileFromGrid(1, 3).get().getGoodBlocks().get(GoodBlock.BLUEBLOCK));

        // check remove two equal boxes from same tile
        ship.removeMostValuableGoodBlocks(2);
        assertEquals(2, ship.getScore());

        // check remove from two different tiles
        ship.weldComponentTile(tiles.get(12), 2, 4);
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct->ct.addGoodBlock(GoodBlock.GREENBLOCK));
        ship.getComponentTileFromGrid(2, 4).ifPresent(ct->ct.addGoodBlock(GoodBlock.BLUEBLOCK));

        ship.removeMostValuableGoodBlocks(2);
        assertEquals(1, ship.getScore());

        // check remove two different boxes from same tile
        ship.weldComponentTile(tiles.get(13), 2, 2);
        ship.getComponentTileFromGrid(2, 2).ifPresent(ct->ct.addGoodBlock(GoodBlock.YELLOWBLOCK));
        ship.getComponentTileFromGrid(2, 2).ifPresent(ct->ct.addGoodBlock(GoodBlock.GREENBLOCK));
        ship.getComponentTileFromGrid(2, 2).ifPresent(ct->ct.addGoodBlock(GoodBlock.BLUEBLOCK));

        ship.removeMostValuableGoodBlocks(2);
        assertEquals(2, ship.getScore());

        // check remove from specialStorageCompartment
        ship.weldComponentTile(tiles.get(11), 3, 3);
        ship.getComponentTileFromGrid(3, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.REDBLOCK));
        ship.getComponentTileFromGrid(3, 3).ifPresent(ct->ct.addGoodBlock(GoodBlock.REDBLOCK));

        ship.removeMostValuableGoodBlocks(1);
        assertEquals(6, ship.getScore());

        ship.removeMostValuableGoodBlocks(1);
        assertEquals(2, ship.getScore());

        ship.removeMostValuableGoodBlocks(1);
        assertEquals(1, ship.getScore());

        // check remove more than available boxes
        ship.removeMostValuableGoodBlocks(52);
        assertEquals(0, ship.getScore());
    }

    // To test again
    @Test
    void test_should_recognise_three_shipwrecks_and_choose_the_third_one(){
        List<ComponentTile> tiles = initializeTiles();
        Shipboard ship = new Shipboard("red", "Emanuele", null);
        ship.weldComponentTile(tiles.get(0), 4, 1);
        ship.weldComponentTile(tiles.get(1), 4, 5);
        ship.weldComponentTile(tiles.get(2), 3, 1);
        ship.weldComponentTile(tiles.get(4), 1, 3);
        ship.weldComponentTile(tiles.get(5), 3, 5);
        ship.weldComponentTile(tiles.get(6), 3, 4);

        assertEquals(3, ship.highlightShipWrecks());
        assertEquals(0, tiles.get(4).getColor());
        assertEquals(0, ship.getComponentTileFromGrid(2, 3).get().getColor());
        assertEquals(1, tiles.get(2).getColor());
        assertEquals(1, tiles.get(0).getColor());
        assertEquals(2, tiles.get(1).getColor());
        assertEquals(2, tiles.get(5).getColor());
        assertEquals(2, tiles.get(6).getColor());

        ship.chooseShipWreck(3, 5);

        assertEquals(tiles.get(6), ship.getComponentTileFromGrid(3, 4).get());
        assertEquals(tiles.get(5), ship.getComponentTileFromGrid(3, 5).get());
        assertEquals(tiles.get(1), ship.getComponentTileFromGrid(4, 5).get());
        ship.destroyTile(3, 4);
        ship.destroyTile(3, 5);
        ship.destroyTile(4, 5);
        assertTrue(ship.isShipboardEmpty());
        assertTrue(ship.checkShipboard());
    }
}