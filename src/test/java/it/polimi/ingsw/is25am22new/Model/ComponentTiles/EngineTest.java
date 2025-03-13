package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    @Test
    void test_engine_should_rotate_correctly(){
        ComponentTile engine = new Engine("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);
        engine.rotateClockwise();

        assertEquals(Side.SMOOTH, engine.rightSide);
        assertEquals(Side.UNIVERSALPIPE, engine.leftSide);
        assertEquals(Side.TWOPIPES, engine.topSide);
        assertEquals(Side.ONEPIPE, engine.bottomSide);
        assertTrue(engine.isLeftSideEngine());

        engine.rotateClockwise();

        assertEquals(Side.SMOOTH, engine.bottomSide);
        assertEquals(Side.UNIVERSALPIPE, engine.topSide);
        assertEquals(Side.TWOPIPES, engine.rightSide);
        assertEquals(Side.ONEPIPE, engine.leftSide);
        assertTrue(engine.isTopSideEngine());

        engine.rotateCounterClockwise();

        assertEquals(Side.SMOOTH, engine.rightSide);
        assertEquals(Side.UNIVERSALPIPE, engine.leftSide);
        assertEquals(Side.TWOPIPES, engine.topSide);
        assertEquals(Side.ONEPIPE, engine.bottomSide);
        assertTrue(engine.isLeftSideEngine());
    }

    @Test
    void test_engine_strength_should_be_calculated_correctly(){
        ComponentTile engine = new Engine("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);
        ComponentTile doubleEngine = new DoubleEngine("x", Side.SMOOTH, Side.UNIVERSALPIPE, Side.TWOPIPES, Side.ONEPIPE);

        assertEquals(1, engine.getEngineStrength());
        assertEquals(0, doubleEngine.getEngineStrength());

        doubleEngine.activateComponent();

        assertEquals(2, doubleEngine.getEngineStrength());

        doubleEngine.deactivateComponent();

        assertEquals(0, doubleEngine.getEngineStrength());
    }
}