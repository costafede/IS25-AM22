package it.polimi.ingsw.is25am22new.Model.AdventureCard.MeteorSwarmCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Flightboard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmCardTest {

    @Test
    void test_correct_after_activate_effect() {
        List<String> players = List.of("A", "B", "C");
        Game game = new Level2Game(players, null);
        game.initGame();
        MeteorSwarmCard meteorSwarmCard =
                (MeteorSwarmCard) game.getCardArchive().stream()
                                                       .filter(c -> c.getName().equals("MeteorSwarm"))
                                                       .findFirst().get();
        System.out.println(meteorSwarmCard.getPngName());

        Flightboard level2FlightBoard = game.getFlightboard();

        game.setCurrCard(meteorSwarmCard);
        game.setCurrPlayer(players.get(0));
        level2FlightBoard.placeRocket("A", 0);
        level2FlightBoard.placeRocket("B", 1);
        level2FlightBoard.placeRocket("C", 2);

        assertEquals(6, level2FlightBoard.getPositions().get("A"));
        assertEquals(3, level2FlightBoard.getPositions().get("B"));
        assertEquals(1, level2FlightBoard.getPositions().get("C"));

        // ship creation
        Shipboard shipA = game.getShipboards().get(players.get(0));
        Shipboard shipB = game.getShipboards().get(players.get(1));
        Shipboard shipC = game.getShipboards().get(players.get(2));
        for (String player : game.getPlayerList()) {
            Shipboard shipboard = game.getShipboards().get(player);
            shipboard.weldComponentTile(new BatteryComponent("BC", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2),
                    3, 3);
            shipboard.weldComponentTile(new Engine("E", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE),
                    3, 2);
            shipboard.weldComponentTile(new DoubleEngine("DE", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH),
                    3, 4);
            shipboard.weldComponentTile(new Cannon("C1", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 2);
            shipboard.weldComponentTile(new Cannon("C2", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 4);
            shipboard.weldComponentTile(new DoubleCannon("DC", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH),
                    1, 3);
            shipboard.weldComponentTile(new ShieldGenerator("SG", Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 5);
            shipboard.weldComponentTile(new StructuralModule("SM", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE),
                    2, 1);
        }

        // player A chooses the battery
        InputCommand i1 = new InputCommand();
        i1.setChoice(true);
        i1.setRow(3);
        i1.setCol(3);
        meteorSwarmCard.activateEffect(i1);
        // check battery usage consumption on shipA
        assertEquals(1, shipA.getComponentTileFromGrid(3, 3).get().getNumOfBatteries());

        // player A chooses double cannon to activate
        Shipboard shipBuffer = CopyShipboard(shipA);
        InputCommand i2 = new InputCommand();
        i2.setRow(1);
        i2.setCol(3);
        meteorSwarmCard.activateEffect(i2);
        // check if cannon is activated
        assertEquals(4, shipA.getCannonStrength());
        assertFalse(meteorSwarmCard.isBatteryUsed());

        // player A chooses not to use 2nd battery
        meteorSwarmCard.setDice1(3);
        meteorSwarmCard.setDice2(4);
        InputCommand i3 = new InputCommand();
        i3.setChoice(false);
        meteorSwarmCard.activateEffect(i3);
        // check if Tile is still there: component activation CHECK
        assertTrue(CheckShipboardIntegrity(shipBuffer, shipA));
        assertTrue(shipA.getComponentTileFromGrid(1, 3).get().isDoubleCannon());
        // check component deactivated
        assertEquals(0, shipA.getComponentTileFromGrid(1, 3).get().getCannonStrength());

        // currPlayer becomes player B
        shipBuffer = CopyShipboard(shipB);
        InputCommand i4 = new InputCommand();
        i4.setChoice(false);
        meteorSwarmCard.setDice1(3);
        meteorSwarmCard.setDice2(4);
        meteorSwarmCard.activateEffect(i4);
        // meteor destroys the double cannon in shipB 1,3
        assertFalse(shipB.getComponentTileFromGrid(1, 3).isPresent());
        assertFalse(CheckShipboardIntegrity(shipBuffer, shipB));

        // currPlayer becomes player C
        // Meteor should destroy tile even with shield on
        shipBuffer = CopyShipboard(shipC);
        InputCommand i5 = new InputCommand();
        i5.setChoice(true);
        i5.setRow(3);
        i5.setCol(3);
        meteorSwarmCard.activateEffect(i5);
        // check battery usage consumption
        assertEquals(1, shipC.getComponentTileFromGrid(3, 3).get().getNumOfBatteries());

        // loses SM in 2,1 in shipC
        InputCommand i6 = new InputCommand();
        i6.setRow(2);
        i6.setCol(5);
        meteorSwarmCard.activateEffect(i6);

        // playerC decides not to use battery
        InputCommand i7 = new InputCommand();
        i7.setChoice(false);
        meteorSwarmCard.setDice1(2);
        meteorSwarmCard.setDice2(3);
        meteorSwarmCard.activateEffect(i7);
        // check ship integrity
        assertTrue(shipC.getComponentTileFromGrid(2, 1).isEmpty());
        assertFalse(CheckShipboardIntegrity(shipBuffer, shipC));

        // 2ND METEOR
        // currPlayer should be player A again
        shipBuffer = CopyShipboard(shipA);
        InputCommand i8 = new InputCommand();
        i8.setChoice(true);
        i8.setRow(3);
        i8.setCol(3);
        meteorSwarmCard.activateEffect(i8);
        // check battery usage consumption on shipA
        assertEquals(0, shipA.getComponentTileFromGrid(3, 3).get().getNumOfBatteries());

        // there is nothing in 0, 0
        InputCommand i9 = new InputCommand();
        i9.setRow(0);
        i9.setCol(0);
        meteorSwarmCard.activateEffect(i9);
        // nothing is activated
        assertEquals(2, shipA.getCannonStrength());
        assertFalse(meteorSwarmCard.isBatteryUsed());

        InputCommand i10 = new InputCommand();
        i10.setChoice(false);
        meteorSwarmCard.setDice1(4);
        meteorSwarmCard.setDice2(4);
        meteorSwarmCard.activateEffect(i10);
        // nothing is destroyed because it is smooth side
        assertTrue(CheckShipboardIntegrity(shipBuffer, shipA));

        // currPlayer should be B
        shipBuffer = CopyShipboard(shipB);
        InputCommand i11 = new InputCommand();
        i11.setChoice(false);
        meteorSwarmCard.setDice1(4);
        meteorSwarmCard.setDice2(3);
        meteorSwarmCard.activateEffect(i11);
        // should destroy SM in 2, 1
        assertFalse(CheckShipboardIntegrity(shipBuffer, shipB));
        assertTrue(shipB.getComponentTileFromGrid(2, 1).isEmpty());

        // currPlayer should be C
        shipBuffer = CopyShipboard(shipC);
        InputCommand i12 = new InputCommand();
        i12.setChoice(false);
        meteorSwarmCard.setDice1(3);
        meteorSwarmCard.setDice2(3);
        meteorSwarmCard.activateEffect(i12);
        // nothing destroyed
        assertTrue(CheckShipboardIntegrity(shipBuffer, shipC));

        //currPlayer should be A
        // tries to activate shield but not work because no more batteries
        shipBuffer = CopyShipboard(shipA);
        InputCommand i13 = new InputCommand();
        i13.setChoice(true);
        i13.setRow(3);
        i13.setCol(3);
        meteorSwarmCard.activateEffect(i13);

        InputCommand i14 = new InputCommand();
        i14.setRow(2);
        i14.setCol(5);
        meteorSwarmCard.activateEffect(i14);
        assertFalse(meteorSwarmCard.isBatteryUsed());

        InputCommand i15 = new InputCommand();
        i15.setChoice(false);
        meteorSwarmCard.setDice1(6);
        meteorSwarmCard.setDice2(1);
        meteorSwarmCard.activateEffect(i15);
        // shield is not activated so it destroys shield in 2,5
        assertFalse(CheckShipboardIntegrity(shipBuffer, shipA));
        assertTrue(shipA.getComponentTileFromGrid(2, 5).isEmpty());

        // currPlayer shuold be B
        // tries and activates shield successfully
        shipBuffer = CopyShipboard(shipB);
        InputCommand i16 = new InputCommand();
        i16.setChoice(true);
        i16.setRow(3);
        i16.setCol(3);
        meteorSwarmCard.activateEffect(i16);

        InputCommand i17 = new InputCommand();
        i17.setRow(2);
        i17.setCol(5);
        meteorSwarmCard.activateEffect(i17);
        assertFalse(meteorSwarmCard.isBatteryUsed());

        InputCommand i18 = new InputCommand();
        i18.setChoice(false);
        meteorSwarmCard.setDice1(6);
        meteorSwarmCard.setDice2(1);
        meteorSwarmCard.activateEffect(i18);
        // shield is activated so it does not destroy shield in 2,5
        assertTrue(CheckShipboardIntegrity(shipBuffer, shipB));
        assertTrue(shipB.getComponentTileFromGrid(2, 5).isPresent());

        // currPlayer shuold be C
        // cannon has universal pipe in front so i know it is destroyed because it is a cannon
        shipC.weldComponentTile(new Cannon("none", Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH), 2, 6);
        shipC.getComponentTileFromGrid(2,6).ifPresent(ComponentTile::rotateClockwise);
        shipBuffer = CopyShipboard(shipC);
        InputCommand i19 = new InputCommand();
        i19.setChoice(false);
        meteorSwarmCard.setDice1(2);
        meteorSwarmCard.setDice2(5);
        meteorSwarmCard.activateEffect(i19);

        assertTrue(CheckShipboardIntegrity(shipBuffer, shipC));
        assertTrue(shipC.getComponentTileFromGrid(2, 6).isPresent());
        assertFalse(meteorSwarmCard.isBatteryUsed());

        // currCard should be null in game because card has been resolved
        assertNull(game.getCurrCard());
        assertSame(game.getCurrPlayer(), game.getFlightboard().getOrderedRockets().getFirst());

        // bonus
        shipC.destroyTile(2, 3);
        game.manageInvalidPlayers();
        assertEquals(2, game.getFlightboard().getOrderedRockets().size());
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
                        !oldS.getComponentTileFromGrid(i,j).get().isStartingCabin()) {
                    return false;
                }
            }
        }
        return true;
    }
}

// small meteor: shield on check, cannon on check, exposed connector(destroy) check, simple cannon check, unexposed connector(dont destroy) check
// big meteor: shield on check, cannon on check, simple cannon check, tile(destroy) check