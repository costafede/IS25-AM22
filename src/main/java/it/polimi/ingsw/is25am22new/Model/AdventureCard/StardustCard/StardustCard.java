package it.polimi.ingsw.is25am22new.Model.AdventureCard.StardustCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.List;

public class StardustCard extends AdventureCard {

    public StardustCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
    }

    public void activateEffect(InputCommand command) {
        List<String> orderedPlayers = game.getFlightboard().getOrderedRockets();
        for (int i = orderedPlayers.size() - 1; i >= 0; i--) {
            String nickname = orderedPlayers.get(i);
            int stepsBackwards = game.getShipboards().get(nickname).countExposedConnectors();
            game.getFlightboard().shiftRocket(game.getShipboards(), nickname, stepsBackwards);
        }
    }
}
