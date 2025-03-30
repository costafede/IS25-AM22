package it.polimi.ingsw.is25am22new.Model.AdventureCard.SmugglersCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
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

class SmugglersCardTest {
    public Game initializeGame(){
        List<String> players = new ArrayList<>();
        players.add(0,"A");
        players.add(1,"B");
        players.add(2,"C");
        players.add(3,"D");

        Game game = new Level2Game(players);

        for(String player : game.getShipboards().keySet()) {
            if(player.equals("A") || player.equals("D")) {
                game.getShipboards().get(player).weldComponentTile(new StorageCompartment("0", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 2, 4);
                game.getShipboards().get(player).weldComponentTile(new BatteryComponent("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 3);
                game.getShipboards().get(player).getComponentTileFromGrid(2, 4).get().addGoodBlock(GoodBlock.BLUEBLOCK);
                game.getBank().withdrawGoodBlock(GoodBlock.BLUEBLOCK);  //2 blocks have been withdrawn from bank (one for A and one for D)
            }
            if(player.equals("B") || player.equals("C")) {
                game.getShipboards().get(player).weldComponentTile(new Cannon("0", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 1, 3);
                game.getShipboards().get(player).weldComponentTile(new DoubleCannon("1", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 4);
                game.getShipboards().get(player).weldComponentTile(new BatteryComponent("2", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3), 3, 3);
                game.getShipboards().get(player).weldComponentTile(new DoubleCannon("3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 2);
                game.getShipboards().get(player).weldComponentTile(new StorageCompartment("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 3, 4);
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
    void test_should_simulate_a_generic_execution(){
        Game game = initializeGame();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
        theoreticalGoodBlocks.put(GoodBlock.REDBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.BLUEBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.YELLOWBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.GREENBLOCK, 1);
        SmugglersCard smugglersCard = new SmugglersCard("x", "smugglers", game, 1, true, 1, 4, 2, theoreticalGoodBlocks);
        game.setCurrCard(smugglersCard);
        InputCommand inputCommand = new InputCommand();
        //A cannot activate anything so choice is false and he loses
        inputCommand.setChoice(false);

        smugglersCard.activateEffect(inputCommand);

        //A should lose his blue block and one battery
        assertEquals(13, game.getBank().getNumGoodBlock(GoodBlock.BLUEBLOCK)); //two blocks have been withdrawn and now one has been returned
        assertEquals(0, game.getShipboards().get("A").getComponentTileFromGrid(2, 4).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));
        assertEquals(2, game.getShipboards().get("A").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("B", game.getCurrPlayer());

        //B activates only one cannon so he doesn't have enough strength and loses
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(3);
        inputCommand.setCol(3);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("B").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(1, game.getShipboards().get("B").getCannonStrength());

        //activates cannon
        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(2);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("B").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(3, game.getShipboards().get("B").getCannonStrength());

        //resolve effect
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(0, game.getShipboards().get("B").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("C", game.getCurrPlayer());

        //C activates both cannon so he wins
        inputCommand = new InputCommand();

        //removes battery
        inputCommand.setChoice(true);
        inputCommand.setRow(3);
        inputCommand.setCol(3);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(1, game.getShipboards().get("C").getCannonStrength());

        //activates cannon
        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(2);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(3, game.getShipboards().get("C").getCannonStrength());

        //removes battery
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(3);
        inputCommand.setCol(3);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(3, game.getShipboards().get("C").getCannonStrength());

        //activates cannon
        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(5, game.getShipboards().get("C").getCannonStrength());

        //resolve effect -> C wins over smugglers so he decides if he wants to lose fligth days to load blocks
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(1, smugglersCard.getActualGoodBlocks().get(GoodBlock.BLUEBLOCK));
        assertEquals(1, smugglersCard.getActualGoodBlocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(1, smugglersCard.getActualGoodBlocks().get(GoodBlock.GREENBLOCK));

        //C decides to load blocks

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(23, game.getFlightboard().getPositions().get("C"));

        //C doesn't load anything (loading goods already tested on other card)

        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        smugglersCard.activateEffect(inputCommand);

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
    }

    @Test
    void test_smugglers_defeat_every_player(){
        Game game = initializeGame();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
        theoreticalGoodBlocks.put(GoodBlock.REDBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.BLUEBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.YELLOWBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.GREENBLOCK, 1);
        SmugglersCard smugglersCard = new SmugglersCard("x", "smugglers", game, 1, true, 1, 4, 2, theoreticalGoodBlocks);
        game.setCurrCard(smugglersCard);
        InputCommand inputCommand = new InputCommand();
        //everybody decides not to do anything so they all lose
        inputCommand.setChoice(false);

        //they all lose 2 resources
        smugglersCard.activateEffect(inputCommand);

        assertEquals(13, game.getBank().getNumGoodBlock(GoodBlock.BLUEBLOCK)); //two blocks have been withdrawn and now one has been returned
        assertEquals(0, game.getShipboards().get("A").getComponentTileFromGrid(2, 4).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));
        assertEquals(2, game.getShipboards().get("A").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("B", game.getCurrPlayer());

        smugglersCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("B").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("C", game.getCurrPlayer());

        smugglersCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("D", game.getCurrPlayer());

        smugglersCard.activateEffect(inputCommand);

        assertEquals(14, game.getBank().getNumGoodBlock(GoodBlock.BLUEBLOCK)); //another blue block has been returned
        assertEquals(0, game.getShipboards().get("D").getComponentTileFromGrid(2, 4).get().getNumGoodBlocks(GoodBlock.BLUEBLOCK));
        assertEquals(2, game.getShipboards().get("D").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
    }

    @Test
    void test_B_draws_C_wins(){
        Game game = initializeGame();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
        theoreticalGoodBlocks.put(GoodBlock.REDBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.BLUEBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.YELLOWBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.GREENBLOCK, 1);
        SmugglersCard smugglersCard = new SmugglersCard("x", "smugglers", game, 1, true, 1, 1, 2, theoreticalGoodBlocks);
        game.setCurrCard(smugglersCard);
        //now the smugglers' strength is just one, so B can draw without activating any cannon
        InputCommand inputCommand = new InputCommand();
        //A cannot activate anything so choice is false and he loses
        inputCommand.setChoice(false);

        smugglersCard.activateEffect(inputCommand);

        assertEquals("B", game.getCurrPlayer());

        smugglersCard.activateEffect(inputCommand);
        //B shouldn't lose anything
        assertEquals(3, game.getShipboards().get("B").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals("C", game.getCurrPlayer());

        //C wins by activating one cannon
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setRow(3);
        inputCommand.setCol(3);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(1, game.getShipboards().get("C").getCannonStrength());

        //activates cannon
        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(2);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(2, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertEquals(3, game.getShipboards().get("C").getCannonStrength());

        //resolves effect
        inputCommand = new InputCommand();
        inputCommand.setChoice(false);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(1, smugglersCard.getActualGoodBlocks().get(GoodBlock.BLUEBLOCK));
        assertEquals(1, smugglersCard.getActualGoodBlocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(1, smugglersCard.getActualGoodBlocks().get(GoodBlock.GREENBLOCK));
        assertEquals(12, game.getBank().getNumGoodBlock(GoodBlock.GREENBLOCK));
        assertEquals(16, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));
        assertEquals(12, game.getBank().getNumGoodBlock(GoodBlock.BLUEBLOCK));

        //C decides to load blocks

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(23, game.getFlightboard().getPositions().get("C"));

        //C loads the yellow and the green block and leaves the blue one
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(3);
        inputCommand.setCol(4);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(0, smugglersCard.getActualGoodBlocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(3, 4).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(3);
        inputCommand.setCol(4);
        inputCommand.setGoodBlock(GoodBlock.GREENBLOCK);

        smugglersCard.activateEffect(inputCommand);

        assertEquals(0, smugglersCard.getActualGoodBlocks().get(GoodBlock.GREENBLOCK));
        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(3, 4).get().getNumGoodBlocks(GoodBlock.GREENBLOCK));

        inputCommand = new InputCommand();
        inputCommand.setChoice(false);// doesn't take the blue one

        smugglersCard.activateEffect(inputCommand);

        assertEquals(12, game.getBank().getNumGoodBlock(GoodBlock.GREENBLOCK));
        assertEquals(16, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));
        assertEquals(13, game.getBank().getNumGoodBlock(GoodBlock.BLUEBLOCK)); //blue block returned to the back

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
    }
}