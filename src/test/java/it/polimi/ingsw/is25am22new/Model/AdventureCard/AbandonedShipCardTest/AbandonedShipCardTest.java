package it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCardTest;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AbandonedShipCard.AbandonedShipCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.*;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Cella{
    int row;
    int col;

    public Cella(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

class AbandonedShipCardTest {

    List<String> players = List.of("Federico", "Tommaso", "Emanuele", "Anatoly");

    Game game = new Level2Game(players);

    List<Cella> cellList = new ArrayList<Cella>();


    @Test
    void abandoned_ship_card_effect_should_remove_astronauts_or_aliens_properly(){
        game.initGame();
        int done2 = 0, done3 = 0, done5 = 0;

        //Fimchè ci sono carte di tipo abandoned ship nel mazzo
        while(game.getCardArchive().stream().filter(c -> c instanceof AbandonedShipCard).count() > 0){
            System.out.print("carta con numero di astronauti: ");
            AbandonedShipCard card;
            //Pick an abandoned ship card from card archive
            card = game.getCardArchive().stream()
                    .filter(c -> c instanceof AbandonedShipCard)
                    .map(c -> (AbandonedShipCard) c)
                    .findFirst()
                    .orElse(null);

            System.out.println(card.getLostAstronauts());

            assertNotNull(card);

            game.getCardArchive().remove(card);

            if (card.getLostAstronauts() >= 2 && done2 == 0) {
                cellList.add(new Cella(1, 4));
                done2 = 1;
            }

            if(card.getLostAstronauts() >= 3 && done3 == 0) {
                cellList.add(new Cella(3, 0));
                done3 = 1;
            }

            if(card.getLostAstronauts() >= 5 && done5 == 0) {
                cellList.add(new Cella(3, 2));
                done5 = 1;
            }

            for(int i = 0; i < cellList.size(); i++){
                System.out.println(cellList.get(i).row + " " + cellList.get(i).col);
            }

            activate_card_effect(card, cellList);

        }

    }

    private void activate_card_effect(AbandonedShipCard card, List<Cella> cellList) {
        System.out.println(card.getLostAstronauts());

        game.setCurrCard(card);

        game.getFlightboard().placeRocket("Federico", 0);
        game.getFlightboard().placeRocket("Tommaso", 1);
        game.getFlightboard().placeRocket("Emanuele", 2);
        game.getFlightboard().placeRocket("Anatoly", 3);

        game.getFlightboard().setOrderedRocketsAndDaysOnFlight(game.getShipboards());

        for (int i = 0; i < 4; i++) {
            System.out.println(game.getFlightboard().getOrderedRockets().get(i));
        }

        game.getShipboards().get("Federico").weldComponentTile(new Cannon("1", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH), 0, 2);
        game.getShipboards().get("Federico").weldComponentTile(new Cannon("2", Side.SMOOTH, Side.ONEPIPE, Side.TWOPIPES, Side.SMOOTH), 0, 4);
        game.getShipboards().get("Federico").weldComponentTile(new DoubleCannon("3", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH), 1, 1);
        game.getShipboards().get("Federico").weldComponentTile(new BatteryComponent("4", Side.UNIVERSALPIPE, Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, 2), 1, 2);
        game.getShipboards().get("Federico").weldComponentTile(new DoubleCannon("5", Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, Side.TWOPIPES), 1, 3);
        RegularCabin rc3 = new RegularCabin("6", Side.ONEPIPE, Side.ONEPIPE, Side.UNIVERSALPIPE, Side.SMOOTH);
        rc3.putAstronauts();
        game.getShipboards().get("Federico").weldComponentTile(rc3, 1, 4);
        game.getShipboards().get("Federico").weldComponentTile(new Cannon("7", Side.SMOOTH, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE), 2, 0);
        game.getShipboards().get("Federico").weldComponentTile(new StorageCompartment("8", Side.TWOPIPES, Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE, 2), 2, 1);
        game.getShipboards().get("Federico").weldComponentTile(new SpecialStorageCompartment("9", Side.SMOOTH, Side.ONEPIPE, Side.UNIVERSALPIPE, Side.TWOPIPES, 1), 2, 2);
        StartingCabin sc = new StartingCabin("10", Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, "Green");
        sc.putAstronauts();
        game.getShipboards().get("Federico").weldComponentTile(sc, 2, 3);
        game.getShipboards().get("Federico").weldComponentTile(new ShieldGenerator("11", Side.ONEPIPE, Side.SMOOTH, Side.ONEPIPE, Side.ONEPIPE), 2, 4);
        game.getShipboards().get("Federico").weldComponentTile(new AlienAddon("12", Side.SMOOTH, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.SMOOTH, "Purple"), 2, 5);
        game.getShipboards().get("Federico").weldComponentTile(new SpecialStorageCompartment("13", Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH, Side.SMOOTH, 2), 2, 6);
        RegularCabin rc4 = new RegularCabin("14", Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, Side.UNIVERSALPIPE);
        rc4.putAstronauts();
        game.getShipboards().get("Federico").weldComponentTile(rc4, 3, 0);
        game.getShipboards().get("Federico").weldComponentTile(new AlienAddon("15", Side.SMOOTH, Side.ONEPIPE, Side.ONEPIPE, Side.ONEPIPE, "brown"), 3, 1);
        RegularCabin rc = new RegularCabin("16", Side.ONEPIPE, Side.TWOPIPES, Side.UNIVERSALPIPE, Side.SMOOTH);
        rc.putAlien("brown");
        game.getShipboards().get("Federico").weldComponentTile(rc, 3, 2);
        game.getShipboards().get("Federico").weldComponentTile(new SpecialStorageCompartment("17", Side.UNIVERSALPIPE, Side.ONEPIPE, Side.SMOOTH, Side.TWOPIPES, 1), 3, 3);
        game.getShipboards().get("Federico").weldComponentTile(new StorageCompartment("18", Side.SMOOTH, Side.UNIVERSALPIPE, Side.UNIVERSALPIPE, Side.SMOOTH, 2), 3, 4);
        RegularCabin rc2 = new RegularCabin("19", Side.TWOPIPES, Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE);
        rc.putAlien("purple");
        game.getShipboards().get("Federico").weldComponentTile(rc2, 3, 5);
        game.getShipboards().get("Federico").weldComponentTile(new BatteryComponent("20", Side.UNIVERSALPIPE, Side.SMOOTH, Side.ONEPIPE, Side.SMOOTH, 2), 3, 6);
        game.getShipboards().get("Federico").weldComponentTile(new DoubleEngine("21", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 4, 0);
        game.getShipboards().get("Federico").weldComponentTile(new ShieldGenerator("23", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.UNIVERSALPIPE), 4, 1);
        game.getShipboards().get("Federico").weldComponentTile(new DoubleEngine("24", Side.TWOPIPES, Side.SMOOTH, Side.UNIVERSALPIPE, Side.SMOOTH), 4, 2);
        game.getShipboards().get("Federico").weldComponentTile(new Engine("25", Side.ONEPIPE, Side.SMOOTH, Side.SMOOTH, Side.SMOOTH), 4, 4);
        game.getShipboards().get("Federico").weldComponentTile(new BatteryComponent("26", Side.TWOPIPES, Side.ONEPIPE, Side.SMOOTH, Side.TWOPIPES, 3), 4, 5);
        game.getShipboards().get("Federico").weldComponentTile(new Cannon("27", Side.SMOOTH, Side.SMOOTH, Side.TWOPIPES, Side.SMOOTH), 4, 6);

        game.setCurrPlayer(game.getFlightboard().getOrderedRockets().getFirst());


        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);

        card.activateEffect(inputCommand);

        //Deve essere eseguito lo stato 1 della carta che impone di spostare il razzo

        assertEquals(6 - card.getFlightdaysLost(), game.getFlightboard().getPositions().get(game.getCurrPlayer()));

        //System.out.println("LA cella 3 2 ha " + game.getShipboards().get("Federico").getComponentTileFromGrid(3, 2).get().getCrewNumber() + " crew members (contiene un alieno)");

        int i = 0;
        int iteration = 0, expected = 2;

        while (game.getCurrCard() != null) {
            System.out.println("Entro nel while con iteration: " + iteration);

            if (iteration % 2 == 0 && iteration != 0)
                i++;

            inputCommand.setRow(cellList.get(i).row);
            inputCommand.setCol(cellList.get(i).col);

            card.activateEffect(inputCommand);
            System.out.println("Tolto da cella: " + cellList.get(i).row + " " + cellList.get(i).col);

            if (iteration == 0) {
                expected = 1;
            } else if (iteration == 1) {
                expected = 0;
            } else if (iteration == 2){
                expected = 1;
            } else if (iteration == 3){
                expected = 0;
            } else
                expected = 0;

            System.out.println("Crew member: " + game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().getCrewNumber());
            System.out.println("Purple alien presence: " + game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().isPurpleAlienPresent());
            System.out.println("Brown Alien presence: " + game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().isBrownAlienPresent());

            if(game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().isAlienPresent("purple"))
                assertFalse(game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().isAlienPresent("purple"));
            else if(game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().isAlienPresent("brown"))
                assertFalse(game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().isAlienPresent("brown"));
            else
                assertEquals(expected, game.getShipboards().get("Federico").getComponentTileFromGrid(cellList.get(i).row, cellList.get(i).col).get().getCrewNumber());

            iteration++;
        }

    }


}