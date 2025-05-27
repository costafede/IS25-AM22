package it.polimi.ingsw.is25am22new.Model.AdventureCard.EpidemicCard;

import it.polimi.ingsw.is25am22new.Client.View.TUI.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Client.View.GUI.ViewableCard;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The EpidemicCard class represents a specific type of AdventureCard that initiates
 * an epidemic event in the game. This card affects the state of player ships and
 * triggers necessary updates to the game model and observable elements.
 *
 * EpidemicCard is a subclass of AdventureCard and implements the ViewableCard interface.
 * It provides functionality for applying card effects and rendering the card view in
 * different game phases.
 */
public class EpidemicCard extends AdventureCard implements Serializable, ViewableCard {

    public EpidemicCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    /**
     * Applies the epidemic effect as defined by this card to all players' shipboards.
     * The effect removes crew members from certain tiles based on specific conditions
     * and updates the game state and observable model accordingly.
     *
     * @param command an InputCommand object that may influence the behavior of the effect
     *                depending on its properties or state.
     */
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
        observableModel.updateAllFlightboard(game.getFlightboard());
        observableModel.updateAllShipboardList(game.getShipboards());
    }

    @Override
    public String getStateName() {
        return "";
    }

    @Override
    public void show(AdventureCardViewTUI view, ClientModel model){
        if (model.getGamePhase().getClass().getSimpleName().equals("CardPhase")){
            view.showEpidemicCardInGame(this);
        }
        else{
            view.showEpidemicCard(this);
        }
    }
}
