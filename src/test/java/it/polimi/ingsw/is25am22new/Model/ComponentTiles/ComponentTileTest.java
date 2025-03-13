package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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