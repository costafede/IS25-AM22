package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@code OpenSpaceCard} functionality in the context of a game.
 * It ensures that the game mechanics related to the 'Open Space' adventure card are functioning as expected.
 *
 * The tests validate interactions between the OpenSpaceCard, the Game, and the players' shipboards and flightboards.
 * Various scenarios are tested to confirm the correct behavior of engine activation, player movement,
 * overlapping players, and handling of players with 0 engine strength.
 *
 * Key features tested include:
 * 1. Proper execution of the generic 'Open Space' scenario with multiple players and card effects.
 * 2. Verification that players are moved correctly based on engine strength activation.
 * 3. Accurate handling of battery and engine consumption when activating engines.
 * 4. Ensuring players with zero engine strength are correctly kicked out of the game at the end of the scenario.
 * 5. Handling of overlapping players and confirmation that the correct players are removed or retained.
 * 6. Ensuring that kicked-out players are no longer active on the flightboard but remain in the overall player list.
 * 7. Confirmation that the current player updates appropriately and the card is cleared once the effect ends.
 */
class OpenSpaceCardTest {
    public Game initializeGame(){
        List<String> players = new ArrayList<>();
        players.add(0,"A");
        players.add(1,"B");
        players.add(2,"C");
        players.add(3,"D");

        Game game = new Level2Game(players, null);

        for(String player : game.getShipboards().keySet()) {
            if(!player.equals("D")) {
                game.getShipboards().get(player).weldComponentTile(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 3);
                game.getShipboards().get(player).weldComponentTile(new DoubleEngine("1", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 4);
                game.getShipboards().get(player).weldComponentTile(new DoubleEngine("2", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 2);
                game.getShipboards().get(player).weldComponentTile(new BatteryComponent("3", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 1, 3);
            }
            game.getShipboards().get(player).getComponentTileFromGrid(2, 3).get().putAstronauts();
        }

        game.getFlightboard().placeRocket("A", 0);
        game.getFlightboard().placeRocket("B", 1);
        game.getFlightboard().placeRocket("C", 2);
        game.getFlightboard().placeRocket("D", 3);

        game.setCurrPlayerToLeader();
        return game;
    }

    @Test
    void test_generic_execution_and_D_should_be_kicked_out_at_the_end_because_0_engine_strength(){
        Game game = initializeGame();
        AdventureCard openSpaceCard = new OpenSpaceCard("x", "OpenSpace", game, 1, true);
        InputCommand inputCommand = new InputCommand();
        //A doesn't activate any cannon -> he shifts of one position forward
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(7, game.getFlightboard().getPositions().get("A"));
        assertEquals(7, game.getShipboards().get("A").getDaysOnFlight());
        assertEquals("B", game.getCurrPlayer());

        //B activates one engine
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(1);
        inputCommand.setCol(3);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("B").getComponentTileFromGrid(1, 3).get().getNumOfBatteries());

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(3, game.getShipboards().get("B").getEngineStrength());

        //B resolves effect
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("B").getEngineStrength());
        assertEquals(6, game.getFlightboard().getPositions().get("B"));
        assertEquals(6, game.getShipboards().get("B").getDaysOnFlight());
        assertEquals("C", game.getCurrPlayer());

        //C activates both engines
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(1);
        inputCommand.setCol(3);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("C").getComponentTileFromGrid(1, 3).get().getNumOfBatteries());

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(3, game.getShipboards().get("C").getEngineStrength());

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(1);
        inputCommand.setCol(3);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(1, 3).get().getNumOfBatteries());

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(2);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(5, game.getShipboards().get("C").getEngineStrength());

        //C resolves effect
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("C").getEngineStrength());
        assertEquals(8, game.getFlightboard().getPositions().get("C"));
        assertEquals(8, game.getShipboards().get("C").getDaysOnFlight());
        assertEquals("D", game.getCurrPlayer());

        //D cannot activate anything so he can only resolve the card effect

        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        //D is the last player and has 0 engine strength so he gets kicked out because the card effect ended

        assertTrue(game.getShipboards().get("D").hasBeenKickedOut());
        assertTrue(game.getPlayerList().contains("D")); //is still in the players of the game even if he's been kicked out
        assertFalse(game.getFlightboard().getOrderedRockets().contains("D"));   //D is no more on the flight board

        assertEquals("C", game.getCurrPlayer());    //C is the leader now
        assertNull(game.getCurrCard());
    }

    @Test
    void test_should_kick_out_overlapped_player(){
        Game game = initializeGame();
        AdventureCard openSpaceCard = new OpenSpaceCard("x", "OpenSpace", game, 1, true);
        InputCommand inputCommand = new InputCommand();
        //In this scenario A, C and D are in the same position but with a lap of advantage, so at the end B and D get kicked out, because B has been overlapped while D has no engine strength
        game.getFlightboard().shiftRocket("A", - 21);
        game.getFlightboard().shiftRocket("C", - 21);       //Lap of advantage
        game.getFlightboard().shiftRocket("D", - 21);

        //now the same instructions of the previous test
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(1);
        inputCommand.setCol(3);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(1);
        inputCommand.setCol(3);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(2);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(1);
        inputCommand.setCol(3);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);

        openSpaceCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        openSpaceCard.activateEffect(inputCommand);

        //now I verify that B and D got Kicked out

        assertTrue(game.getShipboards().get("B").hasBeenKickedOut());
        assertTrue(game.getPlayerList().contains("B")); //is still in the players of the game even if he's been kicked out
        assertFalse(game.getFlightboard().getOrderedRockets().contains("B"));   //B is no more on the flight board

        assertTrue(game.getShipboards().get("D").hasBeenKickedOut());
        assertTrue(game.getPlayerList().contains("D")); //is still in the players of the game even if he's been kicked out
        assertFalse(game.getFlightboard().getOrderedRockets().contains("D"));   //D is no more on the flight board

        //C and A are still playing
        assertFalse(game.getShipboards().get("C").hasBeenKickedOut());
        assertFalse(game.getShipboards().get("A").hasBeenKickedOut());
        assertTrue(game.getFlightboard().getOrderedRockets().contains("C"));
        assertTrue(game.getFlightboard().getOrderedRockets().contains("A"));

        assertEquals("C", game.getCurrPlayer());    //C is the leader now
        assertNull(game.getCurrCard());

    }
}