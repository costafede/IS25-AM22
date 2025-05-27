package it.polimi.ingsw.is25am22new.Model.AdventureCard.CombatZoneCard2;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneCard2Test {
    List<String> players;
    Game game;
    CombatZoneCard2 combatZoneCard2;
    Flightboard level2FlightBoard;
    Shipboard shipA;
    Shipboard shipB;
    Shipboard shipC;
    @BeforeEach
    void test_simulate_a_game_with_different_cases() {
        players = List.of("A", "B", "C");
        game = new Level2Game(players, null);
        game.initGame();
        combatZoneCard2 =
                (CombatZoneCard2) game.getCardArchive().stream()
                        .filter(c -> c.getPngName().equals("GT-cards_II_IT_0116.jpg"))
                        .findFirst().get();

        level2FlightBoard = game.getFlightboard();

        game.setCurrCard(combatZoneCard2);
        game.setCurrPlayer(players.getFirst());
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
            shipboard.weldComponentTile(new Cannon("C3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.SMOOTH),
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

        // Player A pass the turn
        InputCommand i0 = new InputCommand();
        i0.setChoice(false);
        combatZoneCard2.activateEffect(i0);

        assertEquals("B", game.getCurrPlayer());

        // Player B chooses to activate
        InputCommand i1 = new InputCommand();
        i1.setChoice(true);
        i1.setRow(3);
        i1.setCol(3);
        combatZoneCard2.activateEffect(i1);
        assertTrue(shipB.getComponentTileFromGrid(3, 3).get().isBattery());
        assertEquals(2, shipB.getComponentTileFromGrid(3, 3).get().getNumOfBatteries());
        assertTrue(combatZoneCard2.isBatteryUsed());
        assertEquals(2, shipB.getCannonStrength());

        InputCommand i5 = new InputCommand();
        i5.setRow(1);
        i5.setCol(3);
        assertEquals("CombatZoneState2_2", combatZoneCard2.getStateName());
        combatZoneCard2.activateEffect(i5);
        assertTrue(shipB.getComponentTileFromGrid(1, 3).get().isDoubleCannon());
        assertTrue(shipB.getComponentTileFromGrid(1, 3).get().isActivated());
        assertEquals(4, shipB.getCannonStrength());

        // playerB pass the turn
        InputCommand i2 = new InputCommand();
        i2.setChoice(false);
        combatZoneCard2.activateEffect(i2);

        assertEquals("C", game.getCurrPlayer());

        // player c pass the turn
        InputCommand i6 = new InputCommand();
        i6.setChoice(false);
        combatZoneCard2.activateEffect(i6);

        // resolves the effect
        assertEquals(0, level2FlightBoard.getPositions().get("A"));

        // player B turn
        // player b decides to activate the battery again
        assertEquals("B", game.getCurrPlayer());
        InputCommand i3 = new InputCommand();
        i3.setChoice(true);
        i3.setRow(3);
        i3.setCol(3);
        combatZoneCard2.activateEffect(i3);

        assertEquals("B", game.getCurrPlayer());
        // player b activate double engine
        InputCommand i4 = new InputCommand();
        i4.setRow(3);
        i4.setCol(4);
        combatZoneCard2.activateEffect(i4);

        assertEquals("B", game.getCurrPlayer());
        //player b pass the turn
        InputCommand i7 = new InputCommand();
        i7.setChoice(false);
        combatZoneCard2.activateEffect(i7);

        assertEquals("C", game.getCurrPlayer());
        // player C turn pass the turn
        InputCommand i8 = new InputCommand();
        i8.setChoice(false);
        combatZoneCard2.activateEffect(i8);

        assertEquals("A", game.getCurrPlayer());
        //player A turn pass the turn
        InputCommand i9 = new InputCommand();
        i9.setChoice(false);
        combatZoneCard2.activateEffect(i9);

        // resolves the effect player with fewest crew members
        assertEquals("CombatZoneState2_6", combatZoneCard2.getStateName());
        InputCommand i10 = new InputCommand();
        i10.setChoice(false);
        combatZoneCard2.activateEffect(i10);
        assertEquals(3, shipA.getCrewNumber());
        assertEquals(4, shipB.getCrewNumber());
        assertEquals(4, shipC.getCrewNumber());

        assertEquals("CombatZoneState2_7", combatZoneCard2.getStateName());
        assertEquals("A", game.getCurrPlayer());

        Shipboard copyShipA = copyShipboard(shipA);
        assertTrue(checkShipboardIntegrity(copyShipA, shipA));
        // player A chooses to activate the battery
        InputCommand i11 = new InputCommand();
        i11.setChoice(true);
        i11.setRow(3);
        i11.setCol(3);
        combatZoneCard2.activateEffect(i11);

        // player A activates the shield
        InputCommand i12 = new InputCommand();
        i12.setRow(2);
        i12.setCol(5);
        combatZoneCard2.activateEffect(i12);
        assertTrue(shipA.getComponentTileFromGrid(2, 5).get().isShieldGenerator());
        assertTrue(shipA.getComponentTileFromGrid(2, 5).get().isActivated());

        assertTrue(checkShipboardIntegrity(copyShipA, shipA));

        // player a resolve effect
        InputCommand i13 = new InputCommand();
        i13.setChoice(false);
        combatZoneCard2.activateEffect(i13);

        assertTrue(checkShipboardIntegrity(copyShipA, shipA));

        combatZoneCard2.setDice1(3);
        combatZoneCard2.setDice2(3);

        InputCommand i14 = new InputCommand();
        i14.setChoice(false);
        combatZoneCard2.activateEffect(i14);
        // should destroy the component at (1, 1)
        assertFalse(checkShipboardIntegrity(copyShipA, shipA));
        assertTrue(shipA.getComponentTileFromGrid(1, 1).isEmpty());

        InputCommand i15 = new InputCommand();
        i15.setChoice(false);
        combatZoneCard2.setDice1(4);
        combatZoneCard2.setDice2(4);
        combatZoneCard2.activateEffect(i15);

        assertTrue(shipA.getComponentTileFromGrid(3, 4).isEmpty());

        InputCommand i16 = new InputCommand();
        i16.setChoice(false);
        combatZoneCard2.setDice1(4);
        combatZoneCard2.setDice2(4);
        combatZoneCard2.activateEffect(i16);

        assertTrue(shipA.getComponentTileFromGrid(2, 4).isEmpty());

        InputCommand i17 = new InputCommand();
        i17.setRow(2);
        i17.setCol(5);
        combatZoneCard2.activateEffect(i17);
        assertTrue(allGoneExcept(shipA, 2, 5));
    }

    // USED ONLY ONLY ONLY FOR SHIP INTEGRITY NOT COMPONENT INTERNAL STATE
    private Shipboard copyShipboard(Shipboard shipboard){
        Shipboard s = new Shipboard("tempShipboard", "nickname", null);
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(shipboard.getComponentTileFromGrid(i, j).isPresent() &&
                        !shipboard.getComponentTileFromGrid(i, j).get().isStartingCabin()) {
                    s.weldComponentTile(shipboard.getComponentTileFromGrid(i, j).get(), i, j);
                }
            }
        }
        return s;
    }

    private boolean checkShipboardIntegrity(Shipboard oldS, Shipboard newS) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                if(oldS.getComponentTileFromGrid(i, j).isPresent() &&
                        !oldS.getComponentTileFromGrid(i, j).get().isStartingCabin() &&
                        !oldS.getComponentTileFromGrid(i, j).equals(newS.getComponentTileFromGrid(i, j)))
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