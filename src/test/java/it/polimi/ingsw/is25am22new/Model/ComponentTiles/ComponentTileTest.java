package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the behavior of the {@link ComponentTile} class and its subclasses.
 * Specifically, this test focuses on the rotation functionality provided by the component tile.
 *
 * This test ensures that:
 * - ComponentTile instances correctly rotate clockwise and counterclockwise.
 * - Side properties (top, bottom, left, right) are updated correctly after rotations.
 *
 * The {@code test_tile_should_rotate_correctly} method validates the integrity of the rotation logic
 * by checking the side configurations against expected results after each rotation step.
 *
 * Key functionalities tested:
 * - Proper clockwise rotation of sides.
 * - Proper counterclockwise rotation of sides.
 * - Consistency of rotations when alternating between clockwise and counterclockwise methods.
 *
 * Dependencies under test:
 * - {@link ComponentTile#rotateClockwise()}
 * - {@link ComponentTile#rotateCounterClockwise()}
 */
class ComponentTileTest {

    @Test
    void test_tile_should_rotate_correctly(){
        ComponentTile tile = new StructuralModule("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);
        tile.rotateClockwise();

        assertEquals(Side.SMOOTH, tile.rightSide);
        assertEquals(Side.UNIVERSALPIPE, tile.leftSide);
        assertEquals(Side.TWOPIPES, tile.topSide);
        assertEquals(Side.ONEPIPE, tile.bottomSide);

        tile.rotateClockwise();

        assertEquals(Side.SMOOTH, tile.bottomSide);
        assertEquals(Side.UNIVERSALPIPE, tile.topSide);
        assertEquals(Side.TWOPIPES, tile.rightSide);
        assertEquals(Side.ONEPIPE, tile.leftSide);

        tile.rotateCounterClockwise();

        assertEquals(Side.SMOOTH, tile.rightSide);
        assertEquals(Side.UNIVERSALPIPE, tile.leftSide);
        assertEquals(Side.TWOPIPES, tile.topSide);
        assertEquals(Side.ONEPIPE, tile.bottomSide);
    }

}