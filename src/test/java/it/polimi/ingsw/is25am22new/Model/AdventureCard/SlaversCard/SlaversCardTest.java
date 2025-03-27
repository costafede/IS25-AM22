package it.polimi.ingsw.is25am22new.Model.AdventureCard.SlaversCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlaversCardTest {
    List<String> players;
    Game game;
    SlaversCard slaversCard;
    Flightboard level2FlightBoard;
    Shipboard shipA;
    Shipboard shipB;
    Shipboard shipC;
    @BeforeEach
    void test_simulate_a_game_with_different_cases() {
        players = List.of("A", "B", "C");
        game = new Level2Game(players);
        game.initGame();
        slaversCard =
                (SlaversCard) game.getCardArchive().stream()
                        .filter(c -> c.getName().equals("Slavers"))
                        .findFirst().get();
        System.out.println(slaversCard.getPngName());

        level2FlightBoard = game.getFlightboard();

        game.setCurrCard(slaversCard);
        game.setCurrPlayer(players.get(0));
        level2FlightBoard.placeRocket("A", 0);
        level2FlightBoard.placeRocket("B", 1);
        level2FlightBoard.placeRocket("C", 2);

        assertEquals(6, level2FlightBoard.getPositions().get("A"));
        assertEquals(3, level2FlightBoard.getPositions().get("B"));
        assertEquals(1, level2FlightBoard.getPositions().get("C"));

        // ship creation
        shipA = game.getShipboards().get(players.get(0));
        shipB = game.getShipboards().get(players.get(1));
        shipC = game.getShipboards().get(players.get(2));
        for (String player : game.getPlayerList()) {
            Shipboard shipboard = game.getShipboards().get(player);
            shipboard.weldComponentTile(new BatteryComponent("BC", Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, 3),
                    3, 3);
            shipboard.weldComponentTile(new Engine("E", Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH),
                    3, 2);
            shipboard.weldComponentTile(new DoubleEngine("DE", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH),
                    3, 4);
            shipboard.weldComponentTile(new Cannon("C1", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 2);
            shipboard.weldComponentTile(new DoubleCannon("C2", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 4);
            shipboard.weldComponentTile(new DoubleCannon("DC", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    1, 3);
            shipboard.weldComponentTile(new ShieldGenerator("SG", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH),
                    2, 5);
            shipboard.weldComponentTile(new StructuralModule("SM", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 1);
            shipboard.weldComponentTile(new Cannon("C2", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    1, 1);
            shipboard.weldComponentTile(new DoubleCannon("C2", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    2, 0);
        }
    }

    @Test
    void test_scenario_1_first_lose_second_ties_third_wins(){
        // player A uses battery
        InputCommand i1 = new InputCommand();
        i1.setChoice(true);
        i1.setRow(3);
        i1.setCol(3);
        slaversCard.activateEffect(i1);

        // player A activates double cannon
        InputCommand i2 = new InputCommand();
        i2.setRow(1);
        i2.setCol(3);
        slaversCard.activateEffect(i2);

        assertEquals(4, shipA.getCannonStrength());

        // player A loses
        InputCommand i3 = new InputCommand();
        i3.setChoice(false);
        slaversCard.activateEffect(i3);

        // player A stars to remove crew
        // not cabin so no astronauts removed
        InputCommand i4 = new InputCommand();
        i4.setRow(2);
        i4.setCol(2);
        slaversCard.activateEffect(i4);

        assertEquals(0, slaversCard.getSelectedMembers());

        // player A actually removes astronaut
        InputCommand i5 = new InputCommand();
        i5.setRow(2);
        i5.setCol(3);
        slaversCard.activateEffect(i5);

        assertEquals(1, slaversCard.getSelectedMembers());

        // player A removes from wrong tile
        InputCommand i6 = new InputCommand();
        i6.setRow(2);
        i6.setCol(4);
        slaversCard.activateEffect(i6);

        assertEquals(1, slaversCard.getSelectedMembers());

        InputCommand i7 = new InputCommand();
        i7.setRow(2);
        i7.setCol(3);
        slaversCard.activateEffect(i7);

        assertEquals(2, slaversCard.getSelectedMembers());
        assertEquals("B", game.getCurrPlayer());

        // player B activates battery
        InputCommand i8 = new InputCommand();
        i8.setChoice(true);
        i8.setRow(3);
        i8.setCol(3);
        slaversCard.activateEffect(i8);

        // player B activates double cannon
        InputCommand i9 = new InputCommand();
        i9.setRow(1);
        i9.setCol(3);
        slaversCard.activateEffect(i9);

        // player B activates second battery
        InputCommand i10 = new InputCommand();
        i10.setChoice(true);
        i10.setRow(3);
        i10.setCol(3);
        slaversCard.activateEffect(i10);

        // player B activates second cannon
        InputCommand i11 = new InputCommand();
        i11.setRow(2);
        i11.setCol(4);
        slaversCard.activateEffect(i11);

        assertEquals(6, shipB.getCannonStrength());

        // player B fights and ties
        InputCommand i12 = new InputCommand();
        i12.setChoice(false);
        slaversCard.activateEffect(i12);

        assertEquals(2, shipB.getCannonStrength());
        assertEquals("C", game.getCurrPlayer());

        // player C activates 3 batteries
        InputCommand i13 = new InputCommand();
        i13.setChoice(true);
        i13.setRow(3);
        i13.setCol(3);
        slaversCard.activateEffect(i13);

        InputCommand i14 = new InputCommand();
        i14.setRow(2);
        i14.setCol(0);
        slaversCard.activateEffect(i14);

        InputCommand i15 = new InputCommand();
        i15.setChoice(true);
        i15.setRow(3);
        i15.setCol(3);
        slaversCard.activateEffect(i15);

        InputCommand i16 = new InputCommand();
        i16.setRow(1);
        i16.setCol(3);
        slaversCard.activateEffect(i16);

        InputCommand i17 = new InputCommand();
        i17.setChoice(true);
        i17.setRow(3);
        i17.setCol(3);
        slaversCard.activateEffect(i17);

        InputCommand i18 = new InputCommand();
        i18.setRow(2);
        i18.setCol(4);
        slaversCard.activateEffect(i18);

        assertEquals(8, shipC.getCannonStrength());

        // player C fights and wins
        InputCommand i19 = new InputCommand();
        i19.setChoice(false);
        slaversCard.activateEffect(i19);

        // player C chooses not to take the prize
        InputCommand i20 = new InputCommand();
        i20.setChoice(false);
        slaversCard.activateEffect(i20);

        assertEquals(2, shipC.getCannonStrength());

        assertEquals(0, shipC.getCosmicCredits());
        assertEquals(1, game.getFlightboard().getPositions().get("C"));

        assertEquals("B", game.getCurrPlayer());
        assertNull(game.getCurrCard());
        assertEquals(2, game.getFlightboard().getOrderedRockets().size());
    }
}