package it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

public class EpidemicCard extends AdventureCard implements Serializable {

    public EpidemicCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    @Override
    public void activateEffect(InputCommand command) {
        for(String player : game.getPlayerList()){
            Shipboard shipboard = game.getShipboards().get(player);
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 7; j++) {
                    AtomicBoolean temp = new AtomicBoolean(false);
                    shipboard.getComponentTileFromGrid(i, j).ifPresent(ct -> temp.set(ct.isCabin()));
                    if(temp.get() && shipboard.isConnectedToCabin(i, j)) {
                        shipboard.getComponentTileFromGrid(i, j).ifPresent(ComponentTile::removeCrewMember);
                    }
                }
            }
        }
        game.manageInvalidPlayers();
        game.setCurrPlayerToLeader();
        game.setCurrCard(null);
        observableModel.updateAllCurrPlayer(game.getCurrPlayer());
        observableModel.updateAllCurrCard(game.getCurrCard());
        observableModel.updateAllFlightboard(game.getFlightboard());
        observableModel.updateAllShipboardList(game.getShipboards());
    }

    @Override
    public String getStateName() {
        return "";
    }
}
