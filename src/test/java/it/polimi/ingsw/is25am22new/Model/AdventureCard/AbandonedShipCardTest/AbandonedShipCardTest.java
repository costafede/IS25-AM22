package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCardTest;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCardTest {

    private Game initializeGame(){
        List<String> players = new ArrayList<>();
        players.add(0,"A");
        players.add(1,"B");
        players.add(2,"C");
        players.add(3,"D");

        Game game = new Level2Game(players, null);

        for(String player : game.getShipboards().keySet()) {
            game.getShipboards().get(player).weldComponentTile(new AlienAddon("0", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "brown"), 3, 4);
            game.getShipboards().get(player).weldComponentTile(new RegularCabin("1", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 2, 4);
            game.getShipboards().get(player).weldComponentTile(new RegularCabin("2", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE), 3, 3);
            game.getShipboards().get(player).getComponentTileFromGrid(2, 3).get().putAstronauts();
            game.getShipboards().get(player).getComponentTileFromGrid(3, 3).get().putAstronauts();
            game.getShipboards().get(player).getComponentTileFromGrid(2, 4).get().putAlien("brown");
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
    void test_should_simulate_an_execution(){
        Game game = initializeGame();
        AbandonedShipCard abandonedShipCard = new AbandonedShipCard("x","AbandonedShip", game, 1, true, 1, 4, 3);
        game.setCurrCard(abandonedShipCard);
        InputCommand inputCommand = new InputCommand();
        //player C decides to land on the ship
        inputCommand.setChoice(false);  //A doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(true);  //C lands
        abandonedShipCard.activateEffect(inputCommand);

        assertEquals("C", game.getCurrPlayer());
        assertEquals(23, game.getFlightboard().getPositions().get("C"));

        //C removes his crewmates from the right cabins -> 2 humans and 1 alien
        inputCommand = new InputCommand();
        inputCommand.setRow(3);
        inputCommand.setCol(3);
        abandonedShipCard.activateEffect(inputCommand);

        assertEquals(4, game.getShipboards().get("C").getCrewNumber());
        assertEquals(1, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getCrewNumber());

        inputCommand = new InputCommand();
        inputCommand.setRow(3);
        inputCommand.setCol(3);
        abandonedShipCard.activateEffect(inputCommand);

        assertEquals(3, game.getShipboards().get("C").getCrewNumber());
        assertEquals(0, game.getShipboards().get("C").getComponentTileFromGrid(3, 3).get().getCrewNumber());

        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);
        abandonedShipCard.activateEffect(inputCommand); //this activateEffect also triggers the end of the card

        assertEquals(2, game.getShipboards().get("C").getCrewNumber());
        assertEquals(0, game.getShipboards().get("C").getComponentTileFromGrid(2, 4).get().getCrewNumber());
        assertEquals(4, game.getShipboards().get("C").getCosmicCredits());
        assertEquals("A", game.getCurrPlayer());

    }

    @Test
    void test_should_simulate_execution_in_which_nobody_lands(){
        Game game = initializeGame();
        AbandonedShipCard abandonedShipCard = new AbandonedShipCard("x","AbandonedShip", game, 1, true, 1, 4, 3);
        game.setCurrCard(abandonedShipCard);
        InputCommand inputCommand = new InputCommand();
        //nobody lands
        inputCommand.setChoice(false);  //A doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //C doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //D doesn't land
        abandonedShipCard.activateEffect(inputCommand);

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());
        assertEquals(6, game.getFlightboard().getPositions().get("A"));
        assertEquals(3, game.getFlightboard().getPositions().get("B"));
        assertEquals(1, game.getFlightboard().getPositions().get("C"));
        assertEquals(0, game.getFlightboard().getPositions().get("D"));
    }

    @Test
    void test_should_simulate_an_execution_in_which_player_removed_all_his_crew_members_so_he_is_kicked_out(){
        Game game = initializeGame();
        AbandonedShipCard abandonedShipCard = new AbandonedShipCard("x","AbandonedShip", game, 1, true, 1, 4, 3);
        game.setCurrCard(abandonedShipCard);
        InputCommand inputCommand = new InputCommand();
        //player C decides to land on the ship
        inputCommand.setChoice(false);  //A doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(false);  //B doesn't land
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand.setChoice(true);  //C lands
        abandonedShipCard.activateEffect(inputCommand);
        //C removes his crewmates from the right cabins -> 2 humans and 1 alien
        inputCommand = new InputCommand();
        inputCommand.setRow(3);
        inputCommand.setCol(3);
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand = new InputCommand();
        inputCommand.setRow(3);
        inputCommand.setCol(3);
        abandonedShipCard.activateEffect(inputCommand);
        inputCommand = new InputCommand();
        inputCommand.setRow(2);
        inputCommand.setCol(4);
        //above there is the same execution of the first test case
        //before sending the input with activateEffect I want to remove the last two astronauts from C's starting cabin to test if he gets kicked out from the game

        game.getShipboards().get("C").getComponentTileFromGrid(2, 3).get().removeCrewMember();
        game.getShipboards().get("C").getComponentTileFromGrid(2, 3).get().removeCrewMember();

        abandonedShipCard.activateEffect(inputCommand);

        assertEquals(0, game.getShipboards().get("C").getCrewNumber());
        assertTrue(game.getShipboards().get("C").hasBeenKickedOut());
        assertTrue(game.getPlayerList().contains("C")); //is still in the players of the game even if he's been kicked out
        assertFalse(game.getFlightboard().getOrderedRockets().contains("C"));
    }

}