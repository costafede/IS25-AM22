package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard;

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

class CombatZoneCardTest {
    List<String> players;
    Game game;
    CombatZoneCard combatZoneCard;
    Flightboard level2FlightBoard;
    Shipboard shipA;
    Shipboard shipB;
    Shipboard shipC;
    @BeforeEach
    void test_simulate_a_game_with_different_cases() {
        players = List.of("A", "B", "C");
        game = new Level2Game(players, null);
        game.initGame();
        combatZoneCard =
                (CombatZoneCard) game.getCardArchive().stream()
                        .filter(c -> c.getName().equals("CombatZone"))
                        .findFirst().get();
        System.out.println(combatZoneCard.getPngName());

        level2FlightBoard = game.getFlightboard();

        game.setCurrCard(combatZoneCard);
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
            shipboard.weldComponentTile(new Cannon("C3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    1, 1);
            shipboard.weldComponentTile(new RegularCabin("RC", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    3, 1);
            shipboard.weldComponentTile(new AlienAddon("AA", Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, "brown"),
                    4, 1);
        }
        shipA.getComponentTileFromGrid(3, 1).ifPresent(c->c.putAlien("brown"));
        shipB.getComponentTileFromGrid(3, 1).ifPresent(ComponentTile::putAstronauts);
        shipC.getComponentTileFromGrid(3, 1).ifPresent(ComponentTile::putAstronauts);
    }

    @Test
    void test_scenario_1() {
        assertEquals(3, shipA.getCrewNumber());
        assertEquals(4, shipB.getCrewNumber());
        assertEquals(4, shipC.getCrewNumber());
        // player A has lowest crew number shift of 3 positions
        // Now player B is the leader
        // player B dont activate anything
        InputCommand i1 = new InputCommand();
        i1.setChoice(false);
        combatZoneCard.activateEffect(i1);

        assertEquals("B", game.getFlightboard().getOrderedRockets().getFirst());
        assertEquals(2, level2FlightBoard.getPositions().get("A"));
        assertEquals(1, shipB.getEngineStrength());
        assertEquals("A", game.getCurrPlayer());

        //player A activates battery
        InputCommand i2 = new InputCommand();
        i2.setChoice(true);
        i2.setRow(3);
        i2.setCol(3);
        combatZoneCard.activateEffect(i2);

        // player A activates the double engine
        InputCommand i3 = new InputCommand();
        i3.setRow(3);
        i3.setCol(4);
        combatZoneCard.activateEffect(i3);

        assertEquals(5, shipA.getEngineStrength());

        // player A stops using the batteries
        InputCommand i4 = new InputCommand();
        i4.setChoice(false);
        combatZoneCard.activateEffect(i4);

        // player C doesnt use the batteries
        // player B is the farthest so receives penalty
        InputCommand i5 = new InputCommand();
        i5.setChoice(false);
        combatZoneCard.activateEffect(i5);

        // player B removes first crew member
        InputCommand i6 = new InputCommand();
        i6.setRow(3);
        i6.setCol(1);
        combatZoneCard.activateEffect(i6);

        assertEquals(3, shipB.getCrewNumber());
        assertEquals(1, combatZoneCard.getRemovedMembers());

        // player B removes second member
        InputCommand i7 = new InputCommand();
        i7.setRow(3);
        i7.setCol(1);
        combatZoneCard.activateEffect(i7);

        assertEquals(2, combatZoneCard.getRemovedMembers());
        assertEquals(2, shipB.getCrewNumber());

        // leader is still player B
        // player B activates battery
        InputCommand i8 = new InputCommand();
        i8.setChoice(true);
        i8.setRow(3);
        i8.setCol(3);
        combatZoneCard.activateEffect(i8);

        // player B activates double cannon
        InputCommand i9 = new InputCommand();
        i9.setRow(1);
        i9.setCol(3);
        combatZoneCard.activateEffect(i9);

        // player B stops using batteries
        InputCommand i10 = new InputCommand();
        i10.setChoice(false);
        combatZoneCard.activateEffect(i10);

        assertEquals(4, shipB.getCannonStrength());

        // player A doesnt use batteries
        InputCommand i11 = new InputCommand();
        i11.setChoice(false);
        combatZoneCard.activateEffect(i11);

        assertEquals(2, shipA.getCannonStrength());

        // player C uses batteries
        // player A is in front of C so receives penalty
        InputCommand i12 = new InputCommand();
        i12.setChoice(true);
        i12.setRow(3);
        i12.setCol(3);
        combatZoneCard.activateEffect(i12);

        InputCommand i40 = new InputCommand();
        i40.setChoice(true);
        i40.setRow(2);
        i40.setCol(4);
        combatZoneCard.activateEffect(i40);

        assertEquals(4, shipC.getCannonStrength());

        InputCommand i41 = new InputCommand();
        i41.setChoice(false);
        combatZoneCard.activateEffect(i41);

        assertEquals(2, shipC.getCannonStrength());

        // player A gets shot at
        Shipboard shipTemp = copyShipboard(shipA);
        InputCommand i13 = new InputCommand();
        i13.setChoice(false);
        combatZoneCard.setDice1(3);
        combatZoneCard.setDice2(5);
        combatZoneCard.activateEffect(i13);

        assertFalse(checkShipboardIntegrity(shipTemp, shipA));
        assertTrue(shipA.getComponentTileFromGrid(3, 4).isEmpty());

        // player A gets shot at second time
        // activate battery (useless)
        shipTemp = copyShipboard(shipA);
        InputCommand i14 = new InputCommand();
        i14.setChoice(true);
        i14.setRow(3);
        i14.setCol(3);
        combatZoneCard.activateEffect(i14);

        // player A activates shield
        InputCommand i15 = new InputCommand();
        i15.setRow(2);
        i15.setCol(5);
        combatZoneCard.activateEffect(i15);

        // gets shot
        InputCommand i16 = new InputCommand();
        i16.setChoice(false);
        combatZoneCard.setDice1(6);
        combatZoneCard.setDice2(2);
        combatZoneCard.activateEffect(i16);

        assertFalse(checkShipboardIntegrity(shipTemp, shipA));
        assertTrue(shipA.getComponentTileFromGrid(2, 4).isEmpty());

        // find ship wrecks, chooses only shield generator and loses everything
        shipTemp = copyShipboard(shipA);
        InputCommand i17 = new InputCommand();
        i17.setRow(2);
        i17.setCol(5);
        combatZoneCard.activateEffect(i17);

        // card terminates
        assertTrue(allGoneExcept(shipA, 2, 5));
        assertFalse(checkShipboardIntegrity(shipTemp, shipA));
        assertNull(game.getCurrCard());
        assertEquals("B", game.getCurrPlayer());
        assertEquals(2, game.getFlightboard().getOrderedRockets().size());
    }

    // USED ONLY ONLY ONLY FOR SHIP INTEGRITY NOT COMPONENT INTERNAL STATE
    private Shipboard copyShipboard(Shipboard shipboard){
        Shipboard s = new Shipboard("tempShipboard", "nickname", null);
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(shipboard.getComponentTileFromGrid(i, j).isPresent() && i != 2 && j != 3) {
                    s.weldComponentTile(shipboard.getComponentTileFromGrid(i, j).get(), i, j);
                }
            }
        }
        return s;
    }

    private boolean checkShipboardIntegrity(Shipboard oldS, Shipboard newS) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(!oldS.getComponentTileFromGrid(i, j).equals(newS.getComponentTileFromGrid(i, j)))
                    return false;
            }
        }
        return true;
    }

    private boolean allGoneExcept(Shipboard s, int x, int y) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(s.getComponentTileFromGrid(i, j).isPresent() && i != x && j != y)
                    return false;
            }
        }
        return true;
    }
}