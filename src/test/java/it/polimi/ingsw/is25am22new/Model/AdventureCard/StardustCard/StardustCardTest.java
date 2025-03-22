package it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StardustCardTest {
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

        // should lose 16 days in flight
        Shipboard ship1 = new Shipboard("yellow", "Federico", null);
        ship1.weldComponentTile(tiles.get(4),2, 2);
        ship1.weldComponentTile(tiles.get(4),2, 4);
        ship1.weldComponentTile(tiles.get(2),1, 2);
        ship1.weldComponentTile(tiles.get(3),1, 4);
        ship1.weldComponentTile(tiles.get(9),2, 1);
        ship1.weldComponentTile(tiles.get(9),2, 5);
        ship1.weldComponentTile(tiles.get(0),3, 2);
        ship1.weldComponentTile(tiles.get(0),3, 4);


        Shipboard ship2 = new Shipboard("green", "Tommaso", null);
        ship2.weldComponentTile(tiles.get(2),1, 3);
        ship2.weldComponentTile(tiles.get(9),3, 3);
        ship2.weldComponentTile(tiles.get(9),3, 4);
        ship2.weldComponentTile(tiles.get(9),2, 4);

        //Shipboard ship3 = new Shipboard("blue", "Emanuele", null);
        //ship3.weldComponentTile(tiles.get(4),2, 2);
        //ship3.weldComponentTile(tiles.get(4),2, 4);
        //ship3.weldComponentTile(tiles.get(9),3, 2);
        //ship3.weldComponentTile(tiles.get(8),3, 3);
        //ship3.getComponentTileFromGrid(3,2).ifPresent(ct -> ct.putAlien("brown"));
        //ship3.weldComponentTile(tiles.get(9),3, 1);

        // Shipboard ship4 = new Shipboard("red", "Anatoly", null);

        //shipboards.add(ship1);
        //shipboards.add(ship2);
        //shipboards.add(ship3);
        // shipboards.add(ship4);

        return shipboards;
    }
    @Test
    void movesCorrectly2Ships(){


    }
}