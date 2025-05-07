package it.polimi.ingsw.is25am22new.Model.AdventureCard.PiratesCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiratesCardTest {
    List<String> players;
    Game game;
    PiratesCard piratesCard;
    Flightboard level2FlightBoard;
    Shipboard shipA;
    Shipboard shipB;
    Shipboard shipC;
    @BeforeEach
    void test_simulate_a_game_with_different_cases() {
        players = List.of("A", "B", "C");
        game = new Level2Game(players, null);
        game.initGame();
        piratesCard =
                (PiratesCard) game.getCardArchive().stream()
                        .filter(c -> c.getName().equals("Pirates"))
                        .findFirst().get();
        System.out.println(piratesCard.getPngName());

        level2FlightBoard = game.getFlightboard();

        game.setCurrCard(piratesCard);
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
            shipboard.weldComponentTile(new Cannon("C3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    2, 0);
        }
    }

    @Test
    void test_scenario_1_last_player_wins_one_ties() {
        // player A ties player B loses player C wins

        // player A activates battery: 3 -> 2
        InputCommand i1 = new InputCommand();
        i1.setChoice(true);
        i1.setRow(3);
        i1.setCol(3);
        piratesCard.activateEffect(i1);

        // player A activates double cannon
        InputCommand i2 = new InputCommand();
        i2.setRow(1);
        i2.setCol(3);
        piratesCard.activateEffect(i2);

        assertEquals(5, shipA.getCannonStrength());

        // player A stop using batteries and fights
        InputCommand i3 = new InputCommand();
        i3.setChoice(false);
        piratesCard.activateEffect(i3);

        assertEquals(3, shipA.getCannonStrength());

        // player B doesn't activate battery and loses
        InputCommand i4 = new InputCommand();
        i4.setChoice(false);
        piratesCard.activateEffect(i4);

        assertEquals(3, shipB.getCannonStrength());

        // player C activates battery
        InputCommand i5 = new InputCommand();
        i5.setChoice(true);
        i5.setRow(3);
        i5.setCol(3);
        piratesCard.activateEffect(i5);

        // player C activates double cannon
        InputCommand i6 = new InputCommand();
        i6.setRow(1);
        i6.setCol(3);
        piratesCard.activateEffect(i6);

        // player C activates second battery
        InputCommand i7 = new InputCommand();
        i7.setChoice(true);
        i7.setRow(3);
        i7.setCol(3);
        piratesCard.activateEffect(i7);

        // player C activates second cannon
        InputCommand i8 = new InputCommand();
        i8.setRow(2);
        i8.setCol(4);
        piratesCard.activateEffect(i8);

        assertEquals(7, shipC.getCannonStrength());

        // player C stops using batteries and fights
        InputCommand i9 = new InputCommand();
        i9.setChoice(false);
        piratesCard.activateEffect(i9);

        assertEquals(7, shipC.getCannonStrength());

        // player C chooses to get the prize
        InputCommand i10 = new InputCommand();
        i10.setChoice(true);
        piratesCard.activateEffect(i10);

        assertEquals(4, shipC.getCosmicCredits());
        assertEquals(0, game.getFlightboard().getPositions().get("C"));
        assertEquals(0, shipC.getDaysOnFlight());

        assertEquals(1, piratesCard.getDefeatedPlayers().size());
        assertEquals("B", piratesCard.getDefeatedPlayers().getFirst());
        assertNotNull(game.getCurrCard());
        assertEquals("B", piratesCard.getCurrDefeatedPlayer());

        // player B has to defend from shots
        // player B decides to use battery 3 -> 2
        Shipboard shipTemp = CopyShipboard(shipB);
        InputCommand i11 = new InputCommand();
        i11.setChoice(true);
        i11.setRow(3);
        i11.setCol(3);
        piratesCard.activateEffect(i11);

        // player B activates shield
        InputCommand i12 = new InputCommand();
        i12.setRow(2);
        i12.setCol(5);
        piratesCard.activateEffect(i12);

        // player B stop using batteries and gets shot
        InputCommand i13 = new InputCommand();
        i13.setChoice(false);
        piratesCard.activateEffect(i13);
        assertTrue(CheckShipboardIntegrity(shipTemp, shipB));

        // there are still shots
        // player B activates battery
        shipTemp = CopyShipboard(shipB);
        InputCommand i14 = new InputCommand();
        i14.setChoice(true);
        i14.setRow(3);
        i14.setCol(3);
        piratesCard.activateEffect(i14);

        // player B activates shield
        InputCommand i15 = new InputCommand();
        i15.setRow(2);
        i15.setCol(5);
        piratesCard.activateEffect(i15);

        // player B stops using batteries and gets shot
        InputCommand i16 = new InputCommand();
        i16.setChoice(false);
        piratesCard.setDice1(1);
        piratesCard.setDice2(6);
        piratesCard.activateEffect(i16);

        // player B loses double cannon in 1, 3
        assertFalse(CheckShipboardIntegrity(shipTemp, shipB));
        assertTrue(shipB.getComponentTileFromGrid(1, 3).isEmpty());

        // player B don't use battery and gets shot
        shipTemp = CopyShipboard(shipB);
        InputCommand i17 = new InputCommand();
        i17.setChoice(false);
        piratesCard.setDice1(3);
        piratesCard.setDice2(6);
        piratesCard.activateEffect(i17);

        // player B loses shield in 2, 5
        assertFalse(CheckShipboardIntegrity(shipTemp, shipB));
        assertTrue(shipB.getComponentTileFromGrid(2, 5).isEmpty());

        assertEquals("A", game.getCurrPlayer());
        assertNull(game.getCurrCard());

        assertEquals(3, shipA.getCannonStrength());
        assertEquals(3, shipB.getCannonStrength());
        assertEquals(3, shipC.getCannonStrength());
    }

    @Test
    void test_scenario_2_all_players_lose() {
        // player A loses
        InputCommand i1 = new InputCommand();
        i1.setChoice(false);
        piratesCard.activateEffect(i1);

        // player B loses
        InputCommand i2 = new InputCommand();
        i2.setChoice(false);
        piratesCard.activateEffect(i2);

        // player C loses
        InputCommand i3 = new InputCommand();
        i3.setChoice(false);
        piratesCard.activateEffect(i3);

        // player A gets shot -> has to choose battery
        Shipboard shipTemp = CopyShipboard(shipA);
        InputCommand i4 = new InputCommand();
        i4.setChoice(true);
        i4.setRow(3);
        i4.setCol(3);
        piratesCard.activateEffect(i4);

        InputCommand i5 = new InputCommand();
        i5.setRow(1);
        i5.setCol(3);
        piratesCard.activateEffect(i5);

        // tries to activate double cannon useless
        // loses double cannon in 1, 3
        InputCommand i6 = new InputCommand();
        i6.setChoice(false);
        piratesCard.setDice1(3);
        piratesCard.setDice2(4);
        piratesCard.activateEffect(i6);

        assertFalse(CheckShipboardIntegrity(shipTemp, shipA));
        assertTrue(shipA.getComponentTileFromGrid(1, 3).isEmpty());

        // player B activates shield
        shipTemp = CopyShipboard(shipB);
        InputCommand i7 = new InputCommand();
        i7.setChoice(true);
        i7.setRow(3);
        i7.setCol(3);
        piratesCard.activateEffect(i7);

        InputCommand i8 = new InputCommand();
        i8.setRow(2);
        i8.setCol(5);
        piratesCard.activateEffect(i8);

        InputCommand i9 = new InputCommand();
        i9.setChoice(false);
        piratesCard.activateEffect(i9);

        assertTrue(CheckShipboardIntegrity(shipTemp, shipB));

        // player C doesn't activate shield
        shipTemp = CopyShipboard(shipC);
        InputCommand i10 = new InputCommand();
        i10.setChoice(false);
        piratesCard.setDice1(5);
        piratesCard.setDice2(5);
        piratesCard.activateEffect(i10);

        assertTrue(CheckShipboardIntegrity(shipTemp, shipC));

        // back to player A
        shipTemp = CopyShipboard(shipA);
        InputCommand i11 = new InputCommand();
        i11.setChoice(false);
        piratesCard.setDice1(1);
        piratesCard.setDice2(1);
        piratesCard.activateEffect(i11);

        assertTrue(CheckShipboardIntegrity(shipTemp, shipA));

        // player B gets shot
        shipTemp = CopyShipboard(shipB);
        InputCommand i12 = new InputCommand();
        i12.setChoice(false);
        piratesCard.setDice1(1);
        piratesCard.setDice2(4);
        piratesCard.activateEffect(i12);
        assertFalse(CheckShipboardIntegrity(shipTemp, shipB));
        assertTrue(shipB.getComponentTileFromGrid(1, 1).isEmpty());

        // player C gets shot
        shipTemp = CopyShipboard(shipC);
        InputCommand i13 = new InputCommand();
        i13.setChoice(false);
        piratesCard.setDice1(2);
        piratesCard.setDice2(2);
        piratesCard.activateEffect(i13);

        assertFalse(CheckShipboardIntegrity(shipTemp, shipC));
        assertTrue(shipC.getComponentTileFromGrid(2, 0).isEmpty());

        // back to player A
        shipTemp = CopyShipboard(shipA);
        InputCommand i14 = new InputCommand();
        i14.setChoice(false);
        piratesCard.setDice1(5);
        piratesCard.setDice2(4);
        piratesCard.activateEffect(i14);

        assertFalse(CheckShipboardIntegrity(shipTemp, shipA));
        assertTrue(shipA.getComponentTileFromGrid(2, 5).isEmpty());

        // player B
        shipTemp = CopyShipboard(shipB);
        InputCommand i15 = new InputCommand();
        i15.setChoice(false);
        piratesCard.setDice1(6);
        piratesCard.setDice2(5);
        piratesCard.activateEffect(i15);

        assertTrue(CheckShipboardIntegrity(shipTemp, shipB));

        //player C
        shipTemp = CopyShipboard(shipC);
        InputCommand i16 = new InputCommand();
        i16.setChoice(false);
        piratesCard.setDice1(6);
        piratesCard.setDice2(3);
        piratesCard.activateEffect(i16);

        assertFalse(CheckShipboardIntegrity(shipTemp, shipC));
        assertTrue(shipC.getComponentTileFromGrid(2, 5).isEmpty());

        assertNull(game.getCurrCard());
        assertEquals("A", game.getCurrPlayer());
    }

    @Test
    void test_first_player_wins() {
        // player A activates battery
        InputCommand i1 = new InputCommand();
        i1.setChoice(true);
        i1.setRow(3);
        i1.setCol(3);
        piratesCard.activateEffect(i1);

        //player A activates first double cannon
        InputCommand i2 = new InputCommand();
        i2.setRow(1);
        i2.setCol(3);
        piratesCard.activateEffect(i2);

        //player A activates second battery
        InputCommand i3 = new InputCommand();
        i3.setChoice(true);
        i3.setRow(3);
        i3.setCol(3);
        piratesCard.activateEffect(i3);

        //player A activates second double cannon
        InputCommand i4 = new InputCommand();
        i4.setRow(2);
        i4.setCol(4);
        piratesCard.activateEffect(i4);

        assertEquals(7, shipA.getCannonStrength());

        // player A wins
        InputCommand i5 = new InputCommand();
        i5.setChoice(false);
        piratesCard.activateEffect(i5);

        // player A decides to take the prize
        InputCommand i6 = new InputCommand();
        i6.setChoice(true);
        piratesCard.activateEffect(i6);

        assertEquals(4, shipA.getCosmicCredits());
        assertEquals(5, shipA.getDaysOnFlight());
        assertEquals(5, game.getFlightboard().getPositions().get("A"));

        assertNull(game.getCurrCard());
        assertEquals("A", game.getCurrPlayer());
        assertEquals(3, shipA.getCannonStrength());
    }

    // USED ONLY ONLY ONLY FOR SHIP INTEGRITY NOT COMPONENT INTERNAL STATE
    private Shipboard CopyShipboard(Shipboard shipboard){
        Shipboard s = new Shipboard("tempShipboard", "nickname", null);
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(shipboard.getComponentTileFromGrid(i, j).isPresent() &&
                        !shipboard.getComponentTileFromGrid(i,j).get().isStartingCabin()) {
                    s.weldComponentTile(shipboard.getComponentTileFromGrid(i, j).get(), i, j);
                }
            }
        }
        return s;
    }

    private boolean CheckShipboardIntegrity (Shipboard oldS, Shipboard newS) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(!oldS.getComponentTileFromGrid(i, j).equals(newS.getComponentTileFromGrid(i, j)) &&
                        !oldS.getComponentTileFromGrid(i,j).get().isStartingCabin())
                    return false;
            }
        }
        return true;
    }
}