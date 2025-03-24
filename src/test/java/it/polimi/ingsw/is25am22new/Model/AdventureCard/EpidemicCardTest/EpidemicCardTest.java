package it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCardTest;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpidemicCardTest {

    private List<ComponentTile> initializeTiles(){
        List<ComponentTile> tiles = new ArrayList<>();
        tiles.add(new Engine("0", Side.UNIVERSALPIPE, Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleEngine("1", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new Cannon("2", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new DoubleCannon("3", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("4", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("5", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StructuralModule("6", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE));
        tiles.add(new AlienAddon("7", Side.SMOOTH, Side.TWOPIPES, Side.ONEPIPE, Side.UNIVERSALPIPE, "purple"));
        tiles.add(new AlienAddon("8", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "brown"));
        tiles.add(new RegularCabin("9", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new StorageCompartment("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new SpecialStorageCompartment("11", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 2));
        tiles.add(new StorageCompartment("12", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new StorageCompartment("13", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, 3));
        tiles.add(new RegularCabin("14", Side.SMOOTH, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH));
        tiles.add(new RegularCabin("15", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("16", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("17", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("18", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("19", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("20", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("21", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("22", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("23", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("24", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("25", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        tiles.add(new RegularCabin("26", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE));
        return tiles;
    }

    @Test
    void simple_test () {
        List<ComponentTile> tiles = initializeTiles();
        Shipboard s = new Shipboard("red", "name", null);

        s.weldComponentTile(tiles.get(9),2, 2);
        assertTrue(s.getComponentTileFromGrid(2, 3).get().isCabin());
        assertTrue(s.getComponentTileFromGrid(2, 2).get().isCabin());
        assertEquals(2, s.getCrewNumber());
        s.getComponentTileFromGrid(2,2).ifPresent(ComponentTile::putAstronauts);
        assertEquals(4, s.getCrewNumber());
    }

    @Test
    void RemovesCrewFromConnectedCabins(){

        List<ComponentTile> tiles = initializeTiles();
        List<String> players = List.of("Federico", "Anatoly");
        Game game = new Level2Game(players);

        Shipboard ship0 = game.getShipboards().get(players.get(0));
        // check connection with starting cabin
        ship0.weldComponentTile(tiles.get(9),2, 2);
        ship0.getComponentTileFromGrid(2,2).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(15),3, 3);
        ship0.getComponentTileFromGrid(3,3).ifPresent(ComponentTile::putAstronauts);
        // check square of 4 (grouped)
        ship0.weldComponentTile(tiles.get(16),3, 0);
        ship0.getComponentTileFromGrid(3,0).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(17),3, 1);
        ship0.getComponentTileFromGrid(3,1).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(18),4, 0);
        ship0.getComponentTileFromGrid(4,0).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(19),4, 1);
        ship0.getComponentTileFromGrid(4,1).ifPresent(ComponentTile::putAstronauts);
        // check line of 3
        ship0.weldComponentTile(tiles.get(20),0, 3);
        ship0.getComponentTileFromGrid(0,3).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(21),0, 4);
        ship0.getComponentTileFromGrid(0,4).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(22),0, 5);
        // check connected with something else
        // check for disconnected cabins
        ship0.getComponentTileFromGrid(0,5).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(23),2, 5);
        ship0.getComponentTileFromGrid(2,5).ifPresent(ComponentTile::putAstronauts);
        ship0.weldComponentTile(tiles.get(12),3, 5);

        // check for different ships
        Shipboard ship1 = game.getShipboards().get(players.get(1));
        ship1.weldComponentTile(tiles.get(14),2, 4);
        ship1.getComponentTileFromGrid(2,4).ifPresent(ComponentTile::putAstronauts);
        ship1.weldComponentTile(tiles.get(26),3, 3);
        ship1.getComponentTileFromGrid(3,3).ifPresent(ComponentTile::putAstronauts);
        // check alien removal
        ship1.weldComponentTile(tiles.get(24),1, 3);
        ship1.getComponentTileFromGrid(1,3).ifPresent(componentTile -> componentTile.putAlien("brown"));
        ship1.weldComponentTile(tiles.get(25),0, 0);
        ship1.getComponentTileFromGrid(0,0).ifPresent(componentTile -> componentTile.putAlien("purple"));

        EpidemicCard ec = new EpidemicCard("test", "test", game, 2, false);
        ec.activateEffect(null);
        assertEquals(13, ship0.getCrewNumber());
        assertEquals(5, ship1.getCrewNumber());
    }
}