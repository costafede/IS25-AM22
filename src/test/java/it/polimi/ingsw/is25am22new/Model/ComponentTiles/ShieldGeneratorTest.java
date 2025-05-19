package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the ShieldGenerator component, ensuring its rotation, activation, and
 * deactivation functionalities perform as expected. The tests verify the behavior of the shield generator
 * in various scenarios, including state changes after applying clockwise and counter-clockwise rotations,
 * and the activation or deactivation of specific shield sides.
 *
 * The main aspects covered in the tests include:
 * - Correct side assignments for top, bottom, left, and right during and after rotations.
 * - Verification of shield activation on appropriate sides when the generator is activated.
 * - Ensuring no sides remain shielded after the component is deactivated.
 * - Validation of side assignments reverting correctly after counter-clockwise rotation, testing the
 *   generator's ability to return to its original orientation.
 */
class ShieldGeneratorTest {
    @Test
    void test_generator_should_rotate_and_activate_correctly(){
        ComponentTile generator = new ShieldGenerator("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);
        generator.rotateClockwise();
        generator.activateComponent();

        assertEquals(Side.SMOOTH, generator.rightSide);
        assertEquals(Side.UNIVERSALPIPE, generator.leftSide);
        assertEquals(Side.TWOPIPES, generator.topSide);
        assertEquals(Side.ONEPIPE, generator.bottomSide);
        assertTrue(generator.isRightSideShielded());
        assertTrue(generator.isBottomSideShielded());
        assertFalse(generator.isLeftSideShielded());
        assertFalse(generator.isTopSideShielded());

        generator.deactivateComponent();

        assertFalse(generator.isRightSideShielded());
        assertFalse(generator.isBottomSideShielded());
        assertFalse(generator.isLeftSideShielded());
        assertFalse(generator.isTopSideShielded());

        generator.rotateClockwise();
        generator.activateComponent();

        assertEquals(Side.SMOOTH, generator.bottomSide);
        assertEquals(Side.UNIVERSALPIPE, generator.topSide);
        assertEquals(Side.TWOPIPES, generator.rightSide);
        assertEquals(Side.ONEPIPE, generator.leftSide);
        assertTrue(generator.isBottomSideShielded());
        assertTrue(generator.isLeftSideShielded());
        assertFalse(generator.isRightSideShielded());
        assertFalse(generator.isTopSideShielded());

        generator.deactivateComponent();

        assertFalse(generator.isRightSideShielded());
        assertFalse(generator.isBottomSideShielded());
        assertFalse(generator.isLeftSideShielded());
        assertFalse(generator.isTopSideShielded());

        generator.rotateCounterClockwise();
        generator.activateComponent();

        assertEquals(Side.SMOOTH, generator.rightSide);
        assertEquals(Side.UNIVERSALPIPE, generator.leftSide);
        assertEquals(Side.TWOPIPES, generator.topSide);
        assertEquals(Side.ONEPIPE, generator.bottomSide);
        assertTrue(generator.isRightSideShielded());
        assertTrue(generator.isBottomSideShielded());
        assertFalse(generator.isLeftSideShielded());
        assertFalse(generator.isTopSideShielded());

        generator.deactivateComponent();

        assertFalse(generator.isRightSideShielded());
        assertFalse(generator.isBottomSideShielded());
        assertFalse(generator.isLeftSideShielded());
        assertFalse(generator.isTopSideShielded());
    }
}