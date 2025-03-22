package it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard.EpidemicCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
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


    @Test
    void movesCorrectly2Ships(){
        List<ComponentTile> tiles = initializeTiles();
        List<String> players = List.of("Federico", "Tommaso", "Emanuele", "Anatoly");
        Game game = new Level2Game(players);

        Shipboard ship0 = game.getShipboards().get(players.get(0));
        ship0.weldComponentTile(tiles.get(9),2, 2);

        Shipboard ship1 = game.getShipboards().get(players.get(1));
        ship1.weldComponentTile(tiles.get(2),1, 3);
        ship1.weldComponentTile(tiles.get(9),1, 2);


        Shipboard ship2 = game.getShipboards().get(players.get(2));
        ship2.weldComponentTile(tiles.get(2),1, 3);
        ship2.weldComponentTile(tiles.get(9),1, 4);
        ship2.weldComponentTile(tiles.get(9),1, 2);


        StardustCard ec = new StardustCard("test", "test", game, 2, false);
        ec.activateEffect(null);

    }
}