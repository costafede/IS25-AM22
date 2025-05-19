package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CannonTest class. This class contains test methods to verify the behavior
 * and correctness of the Cannon and DoubleCannon components, which extend the ComponentTile class.
 *
 * The following specific aspects are tested:
 *
 * 1. Rotation Behavior:
 *   Tests how a Cannon rotates clockwise and counter-clockwise, ensuring that the sides of the
 *   Cannon are updated correctly and that the cannon placement on sides is consistent after rotation.
 *
 * 2. Cannon Strength Calculation:
 *   Verifies that the cannon strength is correctly calculated after each rotation of the Cannon.
 *   Strength is calculated based on the positioning and properties of the cannon sides.
 *
 * 3. Double Cannon Activation:
 *   Tests the activation and deactivation of a DoubleCannon, ensuring that the component behaves
 *   correctly and updates its strength appropriately after these actions.
 *
 * The class utilizes assertions to validate the expected outcomes of all methods being tested.
 */
class CannonTest {

    @Test
    void test_cannon_should_rotate_correctly(){
        ComponentTile cannon = new Cannon("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);
        cannon.rotateClockwise();

        assertEquals(Side.SMOOTH, cannon.rightSide);
        assertEquals(Side.UNIVERSALPIPE, cannon.leftSide);
        assertEquals(Side.TWOPIPES, cannon.topSide);
        assertEquals(Side.ONEPIPE, cannon.bottomSide);
        assertTrue(cannon.isRightSideCannon());

        cannon.rotateClockwise();

        assertEquals(Side.SMOOTH, cannon.bottomSide);
        assertEquals(Side.UNIVERSALPIPE, cannon.topSide);
        assertEquals(Side.TWOPIPES, cannon.rightSide);
        assertEquals(Side.ONEPIPE, cannon.leftSide);
        assertTrue(cannon.isBottomSideCannon());

        cannon.rotateCounterClockwise();

        assertEquals(Side.SMOOTH, cannon.rightSide);
        assertEquals(Side.UNIVERSALPIPE, cannon.leftSide);
        assertEquals(Side.TWOPIPES, cannon.topSide);
        assertEquals(Side.ONEPIPE, cannon.bottomSide);
        assertTrue(cannon.isRightSideCannon());
    }

    @Test
    void test_cannon_strength_should_be_calculated_correctly(){
        ComponentTile cannon = new Cannon("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);

        assertEquals(1, cannon.getCannonStrength());

        cannon.rotateClockwise();

        assertEquals(0.5, cannon.getCannonStrength());

        cannon.rotateClockwise();
        cannon.rotateClockwise();

        assertEquals(0.5, cannon.getCannonStrength());

        cannon.rotateClockwise();

        assertEquals(1, cannon.getCannonStrength());
    }

    @Test
    void test_double_cannon_activation_should_work_correctly(){
        ComponentTile cannon = new DoubleCannon("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);

        assertEquals(0, cannon.getCannonStrength());

        cannon.activateComponent();

        assertEquals(2, cannon.getCannonStrength());

        cannon.deactivateComponent();
        cannon.rotateClockwise();
        cannon.activateComponent();

        assertEquals(1, cannon.getCannonStrength());

        cannon.deactivateComponent();

        assertEquals(0, cannon.getCannonStrength());
    }

}