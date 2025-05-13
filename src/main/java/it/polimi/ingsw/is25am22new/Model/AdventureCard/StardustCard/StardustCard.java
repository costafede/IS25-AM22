package it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.List;

public class StardustCard extends AdventureCard {

    public StardustCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    @Override
    public void activateEffect(InputCommand command) {
        List<String> orderedPlayers = game.getFlightboard().getOrderedRockets();
        for (int i = orderedPlayers.size() - 1; i >= 0; i--) {
            String nickname = orderedPlayers.get(i);
            int stepsBackwards = game.getShipboards().get(nickname).countExposedConnectors();
            game.getFlightboard().shiftRocket(nickname, stepsBackwards);
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
