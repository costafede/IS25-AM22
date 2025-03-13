package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTileTest {

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

}