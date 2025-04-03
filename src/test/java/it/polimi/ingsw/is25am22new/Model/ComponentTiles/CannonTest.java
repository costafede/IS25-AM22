package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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