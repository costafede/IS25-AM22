package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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