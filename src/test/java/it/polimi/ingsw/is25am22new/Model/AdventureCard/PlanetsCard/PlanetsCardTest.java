package it.polimi.ingsw.is25am22new.Model.AdventureCard.PlanetsCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.SpecialStorageCompartment;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StorageCompartment;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The PlanetsCardTest class is a test suite designed to verify functionality related to the Planets Card
 * in the game. This class ensures that planets are loaded correctly with the appropriate resources and
 * that various game scenarios involving players and goods are handled as expected.
 *
 * It includes tests to validate proper placement of players, correct handling of resources, and the
 * management of situations such as lack of goods. The class uses unit testing techniques to confirm
 * the integrity of the game's behavior.
 *
 * Methods in this class include setup methods to initialize the game and adventure cards, as well as
 * specific test cases annotated with @Test to validate core game logic and scenarios.
 *
 * Superclass:
 * - java.lang.Object
 *
 * Methods:
 * - Game initializeGame(): Initializes and returns a new instance of the Game object.
 * - PlanetsCard initializeAdventureCard(Game game): Configures and returns a PlanetsCard object
 *   customized to the provided game instance.
 *
 * Test Cases:
 * - test_should_load_planets_with_resources_and_players_should_be_placed_correctly(): Verifies that
 *   planets are correctly loaded with resources and that players are placed appropriately on the flight board.
 * - goods_should_be_carried_properly(): Ensures that goods in the game are being transported as intended.
 * - test_lack_of_goods_should_be_managed_properly(): Confirms that the game can handle scenarios where goods
 *   are insufficient and that such situations are dealt with correctly.
 */
class PlanetsCardTest {
    public Game initializeGame(){
        List<String> players = new ArrayList<>();
        players.add(0,"A");
        players.add(1,"B");
        players.add(2,"C");
        players.add(3,"D");

        Game game = new Level2Game(players, null);

        for(String player : game.getShipboards().keySet()) {
            game.getShipboards().get(player).weldComponentTile(new StorageCompartment("0", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 1, 3);
            game.getShipboards().get(player).weldComponentTile(new StorageCompartment("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 2, 4);
            game.getShipboards().get(player).weldComponentTile(new StorageCompartment("2", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 3);
            game.getShipboards().get(player).weldComponentTile(new SpecialStorageCompartment("3", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 2, 2);
            game.getShipboards().get(player).weldComponentTile(new SpecialStorageCompartment("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 1, 2);
            game.getShipboards().get(player).weldComponentTile(new SpecialStorageCompartment("5", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 3, 2);
            game.getShipboards().get(player).getComponentTileFromGrid(2, 3).get().putAstronauts();
        }

        game.getFlightboard().placeRocket("A", 0);
        game.getFlightboard().placeRocket("B", 1);
        game.getFlightboard().placeRocket("C", 2);
        game.getFlightboard().placeRocket("D", 3);

        game.setCurrPlayerToLeader();
        game.setCurrPlayer("A");
        return game;
    }

    public PlanetsCard initializeAdventureCard(Game game) {
        List<Planet> planets = new ArrayList<>();
        Map<GoodBlock, Integer> goodBlocks = new HashMap<>();
        goodBlocks.put(GoodBlock.REDBLOCK, 2);
        goodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        goodBlocks.put(GoodBlock.GREENBLOCK, 0);
        goodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        planets.add(0,new Planet(goodBlocks));
        goodBlocks = new HashMap<>();
        goodBlocks.put(GoodBlock.REDBLOCK, 1);
        goodBlocks.put(GoodBlock.YELLOWBLOCK, 0);
        goodBlocks.put(GoodBlock.GREENBLOCK, 0);
        goodBlocks.put(GoodBlock.BLUEBLOCK, 2);
        planets.add(1, new Planet(goodBlocks));
        goodBlocks = new HashMap<>();
        goodBlocks.put(GoodBlock.REDBLOCK, 0);
        goodBlocks.put(GoodBlock.YELLOWBLOCK, 1);
        goodBlocks.put(GoodBlock.GREENBLOCK, 0);
        goodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        planets.add(2, new Planet(goodBlocks));

        PlanetsCard adventureCard = new PlanetsCard("x", "Planets", game, 1, true, planets, 2);
        game.setCurrCard(adventureCard);
        return adventureCard;
    }

    @Test
    void test_should_load_planets_with_resources_and_players_should_be_placed_correctly_on_flightboard(){
        Game game = initializeGame();
        PlanetsCard planetsCard = initializeAdventureCard(game);
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(0);
        planetsCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        planetsCard.activateEffect(inputCommand);
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(1);
        planetsCard.activateEffect(inputCommand);
        inputCommand.setIndexChosen(2);
        planetsCard.activateEffect(inputCommand);

        assertEquals(22, planetsCard.getGame().getFlightboard().getPositions().get("D"));
        assertEquals(23, planetsCard.getGame().getFlightboard().getPositions().get("C"));
        assertEquals(3, planetsCard.getGame().getFlightboard().getPositions().get("B"));
        assertEquals(4, planetsCard.getGame().getFlightboard().getPositions().get("A"));

        assertEquals("A", planetsCard.getPlanets().get(0).getPlayer());
        assertEquals("C", planetsCard.getPlanets().get(1).getPlayer());
        assertEquals("D", planetsCard.getPlanets().get(2).getPlayer());

        assertEquals(2, planetsCard.getPlanets().get(0).getActualGoodblocks().get(GoodBlock.REDBLOCK));
        assertEquals(0, planetsCard.getPlanets().get(1).getActualGoodblocks().get(GoodBlock.REDBLOCK));
        assertEquals(0, planetsCard.getPlanets().get(1).getActualGoodblocks().get(GoodBlock.BLUEBLOCK));
        assertEquals(0, planetsCard.getPlanets().get(2).getActualGoodblocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(0, planetsCard.getPlanets().get(0).getActualGoodblocks().get(GoodBlock.BLUEBLOCK));

        inputCommand.setChoice(false);
        //I want to verify the goods shortage so I manually remove all the 14 blue blocks
        for(int i = 0; i < 14; i++){
            game.getBank().withdrawGoodBlock(GoodBlock.BLUEBLOCK);
        }
        planetsCard.activateEffect(inputCommand);

        assertEquals(1, planetsCard.getPlanets().get(1).getActualGoodblocks().get(GoodBlock.REDBLOCK));
        assertEquals(0, planetsCard.getPlanets().get(1).getActualGoodblocks().get(GoodBlock.BLUEBLOCK));

        planetsCard.activateEffect(inputCommand);

        assertEquals(1, planetsCard.getPlanets().get(2).getActualGoodblocks().get(GoodBlock.YELLOWBLOCK));

        planetsCard.activateEffect(inputCommand);

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
    }

    @Test
    void goods_should_be_carried_properly(){
        Game game = initializeGame();
        PlanetsCard planetsCard = initializeAdventureCard(game);
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(0);
        planetsCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        planetsCard.activateEffect(inputCommand);
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(1);
        planetsCard.activateEffect(inputCommand);
        inputCommand.setIndexChosen(2);
        planetsCard.activateEffect(inputCommand);

        //I want to have only 2 red blocks in the game to test the lack of goods
        for(int i = 0; i < 10; i++){
            game.getBank().withdrawGoodBlock(GoodBlock.REDBLOCK);
        }

        //player A decides to carry all goods from planet
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("A").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.REDBLOCK));

        //then he removes one block -> the block should return to the bank
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsRemovingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("A").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.REDBLOCK));

        //A decides to end his turn -> it's C's turn
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);
        planetsCard.activateEffect(inputCommand);

        assertEquals("C", game.getCurrPlayer());

        //adds all 3 blocks
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.BLUEBLOCK);

        planetsCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(2);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.BLUEBLOCK);

        planetsCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.REDBLOCK));
        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));
        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(2, 2).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));

        //switches red and blue blocks
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagSwitchingGoodBlock();
        inputCommand.setRow(2);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.BLUEBLOCK);
        inputCommand.setRow_1(1);
        inputCommand.setCol_1(2);
        inputCommand.setGoodBlock_1(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        assertEquals(0, game.getShipboards().get("C").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.REDBLOCK));
        assertEquals(2, game.getShipboards().get("C").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));
        assertEquals(0, game.getShipboards().get("C").getComponentTileFromGrid(2, 2).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));
        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(2, 2).get().getNumGoodBlocks(GoodBlock.REDBLOCK));

        //C passes his turn to D
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);
        planetsCard.activateEffect(inputCommand);

        assertEquals("D", game.getCurrPlayer());

        //adds block
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);

        planetsCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));

        //switches to another compartment
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagSwitchingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);
        inputCommand.setRow_1(2);
        inputCommand.setCol_1(2);
        inputCommand.setGoodBlock_1(null);

        planetsCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(2, 2).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));
        assertEquals(0, game.getShipboards().get("D").getComponentTileFromGrid(1, 2).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));

        //card effect ends
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        planetsCard.activateEffect(inputCommand);

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
    }

    @Test
    void test_lack_of_goods_should_be_managed_properly(){
        Game game = initializeGame();
        PlanetsCard planetsCard = initializeAdventureCard(game);
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(0);
        planetsCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        planetsCard.activateEffect(inputCommand);
        inputCommand.setChoice(true);
        inputCommand.setIndexChosen(1);
        planetsCard.activateEffect(inputCommand);
        inputCommand.setIndexChosen(2);
        planetsCard.activateEffect(inputCommand);

        //I want to have only 2 red blocks in the game to test the lack of goods
        for(int i = 0; i < 10; i++){
            game.getBank().withdrawGoodBlock(GoodBlock.REDBLOCK);
        }

        //player A decides to carry all goods from planet
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(2);
        inputCommand.setGoodBlock(GoodBlock.REDBLOCK);

        planetsCard.activateEffect(inputCommand);

        //A decides to end his turn -> it's C's turn (with no RedBlocks left this time)
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);
        planetsCard.activateEffect(inputCommand);

        assertEquals(0, planetsCard.getPlanet("C").getActualGoodblocks().get(GoodBlock.REDBLOCK));
    }
}