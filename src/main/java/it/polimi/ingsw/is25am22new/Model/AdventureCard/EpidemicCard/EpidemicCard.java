package it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.util.Optional;

public class EpidemicCard extends AdventureCard {

    public EpidemicCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    public void activateEffect(InputCommand command) {
        for(String player : game.getPlayerList()){
            Shipboard shipboard = game.getShipboards().get(player);
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 7; j++) {
                    if(shipboard.getComponentTileFromGrid(i, j).isCabin() && shipboard.isConnectedToCabin(i, j)) {
                        shipboard.getComponentTileFromGrid(i, j).removeCrewMember();
                    }
                }
            }
        }
    }
}
