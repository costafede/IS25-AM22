package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedStationCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.RegularCabin;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.StorageCompartment;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.GoodBlock;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationCardTest {
    public Game initializeGame(){
        List<String> players = new ArrayList<>();
        players.add(0,"A");
        players.add(1,"B");
        players.add(2,"C");
        players.add(3,"D");

        Game game = new Level2Game(players);

        for(String player : game.getShipboards().keySet()) {
            game.getShipboards().get(player).weldComponentTile(new StorageCompartment("0", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 1, 3);
            game.getShipboards().get(player).weldComponentTile(new StorageCompartment("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2), 2, 4);
            game.getShipboards().get(player).weldComponentTile(new RegularCabin("2", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 3);
            game.getShipboards().get(player).weldComponentTile(new RegularCabin("3", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 2);
            game.getShipboards().get(player).getComponentTileFromGrid(2, 3).get().putAstronauts();
            game.getShipboards().get(player).getComponentTileFromGrid(3, 3).get().putAstronauts();
            game.getShipboards().get(player).getComponentTileFromGrid(2, 2).get().putAstronauts();
        }

        game.getFlightboard().placeRocket("A", 0);
        game.getFlightboard().placeRocket("B", 1);
        game.getFlightboard().placeRocket("C", 2);
        game.getFlightboard().placeRocket("D", 3);

        game.setCurrPlayerToLeader();
        game.setCurrPlayer("A");
        return game;
    }

    @Test
    void test_should_simulate_an_execution_where_nobody_lands(){
        Game game = initializeGame();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
        theoreticalGoodBlocks.put(GoodBlock.REDBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.YELLOWBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.GREENBLOCK, 1);
        AbandonedStationCard abandonedStationCard = new AbandonedStationCard("x","AbandonedShip", game, 1, true, 1, 5, theoreticalGoodBlocks);
        game.setCurrCard(abandonedStationCard);
        InputCommand inputCommand = new InputCommand();
        //nobody lands
        inputCommand.setChoice(false);  //A doesn't land
        abandonedStationCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        abandonedStationCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //C doesn't land
        abandonedStationCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //D doesn't land
        abandonedStationCard.activateEffect(inputCommand);

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
        assertEquals(6, game.getFlightboard().getPositions().get("A"));
        assertEquals(3, game.getFlightboard().getPositions().get("B"));
        assertEquals(1, game.getFlightboard().getPositions().get("C"));
        assertEquals(0, game.getFlightboard().getPositions().get("D"));
    }

    @Test
    void test_should_simulate_a_generic_execution(){
        Game game = initializeGame();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
        theoreticalGoodBlocks.put(GoodBlock.REDBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.YELLOWBLOCK, 1);
        theoreticalGoodBlocks.put(GoodBlock.GREENBLOCK, 1);
        AbandonedStationCard abandonedStationCard = new AbandonedStationCard("x","AbandonedShip", game, 1, true, 1, 5, theoreticalGoodBlocks);
        game.setCurrCard(abandonedStationCard);
        InputCommand inputCommand = new InputCommand();
        //D lands
        inputCommand.setChoice(false);  //A doesn't land
        abandonedStationCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        abandonedStationCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //C doesn't land
        abandonedStationCard.activateEffect(inputCommand);
        inputCommand.setChoice(true);  //D lands
        abandonedStationCard.activateEffect(inputCommand);

        assertEquals("D", game.getCurrPlayer());
        assertEquals(23, game.getFlightboard().getPositions().get("D"));
        assertEquals(1, abandonedStationCard.getActualGoodBlocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(1, abandonedStationCard.getActualGoodBlocks().get(GoodBlock.GREENBLOCK));
        assertEquals(16, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));
        assertEquals(12, game.getBank().getNumGoodBlock(GoodBlock.GREENBLOCK));

        //D adds all the card's blocks
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(3);
        inputCommand.setGoodBlock(GoodBlock.GREENBLOCK);

        abandonedStationCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(1, 3).get().getNumGoodBlocks(GoodBlock.GREENBLOCK));
        assertEquals(0, abandonedStationCard.getActualGoodBlocks().get(GoodBlock.GREENBLOCK));
        assertEquals(12, game.getBank().getNumGoodBlock(GoodBlock.GREENBLOCK));

        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsAddingGoodBlock();
        inputCommand.setRow(2);
        inputCommand.setCol(4);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);

        abandonedStationCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(2, 4).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));
        assertEquals(0, abandonedStationCard.getActualGoodBlocks().get(GoodBlock.YELLOWBLOCK));
        assertEquals(16, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));

        //then he puts the yellow block in the first compartment
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagSwitchingGoodBlock();
        inputCommand.setRow(2);
        inputCommand.setCol(4);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);
        inputCommand.setRow_1(1);
        inputCommand.setCol_1(3);

        abandonedStationCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(1, 3).get().getNumGoodBlocks(GoodBlock.GREENBLOCK));
        assertEquals(0, game.getShipboards().get("D").getComponentTileFromGrid(2, 4).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));
        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(1, 3).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));

        //finally he removes his yellow block -> it returns to the bank
        inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.flagIsRemovingGoodBlock();
        inputCommand.setRow(1);
        inputCommand.setCol(3);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);

        abandonedStationCard.activateEffect(inputCommand);

        assertEquals(1, game.getShipboards().get("D").getComponentTileFromGrid(1, 3).get().getNumGoodBlocks(GoodBlock.GREENBLOCK));
        assertEquals(0, game.getShipboards().get("D").getComponentTileFromGrid(1, 3).get().getNumGoodBlocks(GoodBlock.YELLOWBLOCK));
        assertEquals(17, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));

        inputCommand.setChoice(false);  //D leaves the station
        abandonedStationCard.activateEffect(inputCommand);

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
        assertEquals(6, game.getFlightboard().getPositions().get("A"));
    }

    @Test
    void test_should_not_completely_load_station_due_to_lack_of_goods(){
        Game game = initializeGame();
        Map<GoodBlock, Integer> theoreticalGoodBlocks = new HashMap<>();
        theoreticalGoodBlocks.put(GoodBlock.REDBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.BLUEBLOCK, 0);
        theoreticalGoodBlocks.put(GoodBlock.YELLOWBLOCK, 1000); //way too many yellow blocks
        theoreticalGoodBlocks.put(GoodBlock.GREENBLOCK, 1);
        AbandonedStationCard abandonedStationCard = new AbandonedStationCard("x","AbandonedShip", game, 1, true, 1, 5, theoreticalGoodBlocks);
        game.setCurrCard(abandonedStationCard);
        InputCommand inputCommand = new InputCommand();

        inputCommand.setChoice(true);  //A lands
        abandonedStationCard.activateEffect(inputCommand);

        assertEquals(5, game.getFlightboard().getPositions().get("A"));
        assertEquals(0, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));
        assertEquals(17, abandonedStationCard.getActualGoodBlocks().get(GoodBlock.YELLOWBLOCK)); //there are max 17 yellow blocks in the game
        assertEquals(12, game.getBank().getNumGoodBlock(GoodBlock.GREENBLOCK));
        assertEquals(1, abandonedStationCard.getActualGoodBlocks().get(GoodBlock.GREENBLOCK));

        inputCommand.setChoice(false);  //A leaves
        abandonedStationCard.activateEffect(inputCommand);
        //no blocks were taken so the bank returns to the default state and the blocks (they are not removed from the card because there is no need to do so since it is going to be never used again)
        assertEquals(17, game.getBank().getNumGoodBlock(GoodBlock.YELLOWBLOCK));
        assertEquals(13, game.getBank().getNumGoodBlock(GoodBlock.GREENBLOCK));
    }

}