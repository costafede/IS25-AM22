package it.polimi.ingsw.is25am22new.Model.ComponentTiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The EngineTest class is a set of unit tests to validate the functionality of
 * engine-related behaviors in the system. It ensures correctness in the
 * rotation mechanics and engine strength calculations for different component tiles.
 *
 * The functionality being tested includes:
 *
 * 1. Rotation Behavior:
 *    - Verifies that the engine rotates correctly in both clockwise and
 *      counter-clockwise directions.
 *    - Confirms that the sides of the engine adjust appropriately with each
 *      rotation.
 *    - Checks specific side properties post-rotation, such as whether an
 *      engine is present on a given side.
 *
 * 2. Engine Strength:
 *    - Ensures that engine strength calculations are accurate for both
 *      single-engine and double-engine component tiles.
 *    - Covers scenarios where the engine is activated and deactivated to ensure
 *      proper strength updates.
 *
 * These tests validate the correct interaction between rotation mechanisms, side
 * properties, and engine-specific functionalities across different component implementations.
 */
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