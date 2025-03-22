package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        return tiles;
    }

    private List<Shipboard> initializeShipboards(List<String> nicknames){
        List<ComponentTile> tiles = initializeTiles();
        List<Shipboard> shipboards = new ArrayList<>();

        //Shouldnt lose crewmembers
        Shipboard ship1 = new Shipboard("yellow", "Federico", null);
        ship1.weldComponentTile(tiles.get(4),2, 2);
        ship1.weldComponentTile(tiles.get(4),2, 4);
        ship1.weldComponentTile(tiles.get(2),1, 2);
        ship1.weldComponentTile(tiles.get(3),1, 4);
        ship1.weldComponentTile(tiles.get(9),2, 1);
        ship1.weldComponentTile(tiles.get(9),2, 5);
        ship1.weldComponentTile(tiles.get(0),3, 2);
        ship1.weldComponentTile(tiles.get(0),3, 4);
        ship1.getComponentTileFromGrid(2,1).ifPresent(ct -> ct.putAstronauts());
        ship1.getComponentTileFromGrid(2,5).ifPresent(ct -> ct.putAstronauts());


        // Should lose 3 crewmembers
        Shipboard ship2 = new Shipboard("green", "Tommaso", null);
        ship2.weldComponentTile(tiles.get(2),1, 3);
        ship2.weldComponentTile(tiles.get(9),3, 3);
        ship2.weldComponentTile(tiles.get(9),3, 4);
        ship2.weldComponentTile(tiles.get(9),2, 4);

        // Should lose 1 astronaut and one alien
        Shipboard ship3 = new Shipboard("blue", "Emanuele", null);
        ship3.weldComponentTile(tiles.get(4),2, 2);
        ship3.weldComponentTile(tiles.get(4),2, 4);
        ship3.weldComponentTile(tiles.get(9),3, 2);
        ship3.weldComponentTile(tiles.get(8),3, 3);
        ship3.getComponentTileFromGrid(3,2).ifPresent(ct -> ct.putAlien("brown"));
        ship3.weldComponentTile(tiles.get(9),3, 1);

        // shouldnt lose crewmembers
        Shipboard ship4 = new Shipboard("red", "Anatoly", null);

        shipboards.add(ship1);
        shipboards.add(ship2);
        shipboards.add(ship3);
        shipboards.add(ship4);

        return shipboards;
    }
    @Test
    void RemovesCrewFromConnectedCabins(){
        List<ComponentTile> tiles = initializeTiles();

        List<String> nicknames = new ArrayList<>();
        nicknames.add("Federico");
        nicknames.add("Tommaso");
        nicknames.add("Emanuele");
        nicknames.add("Anatoly");

        List<Shipboard> shipboards = initializeShipboards(nicknames);
        Level2Game l2g = new Level2Game(nicknames);

        //Shipboard ship0 = new Shipboard("yellow", "Federico", null);
        //ship0.weldComponentTile(tiles.get(9), 2, 2);
        //ship0.weldComponentTile(tiles.get(9), 2, 1);
        //ship0.getComponentTileFromGrid(2,2).ifPresent(ct -> ct.putAstronauts());
        //ship0.getComponentTileFromGrid(2,3).ifPresent(ct -> ct.putAstronauts());
        //ship0.getComponentTileFromGrid(2,2).ifPresent(ct -> ct.putAstronauts());

        EpidemicCard ec = new EpidemicCard( "ec1", "Epidemia", l2g, 2, false);
        InputCommand input = new InputCommand();
        input = null;
        ec.activateEffect(input);

        assertEquals(4, shipboards.get(0).getCrewNumber());
        //assertEquals(4, shipboards.get(0).getCrewNumber());
        //assertEquals(3, shipboards.get(1).getCrewNumber());
        //assertEquals(2, shipboards.get(2).getCrewNumber());
        //assertEquals(0, shipboards.get(3).getCrewNumber());

    }
}