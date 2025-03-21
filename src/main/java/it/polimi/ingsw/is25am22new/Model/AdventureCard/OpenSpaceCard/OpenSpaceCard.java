package it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

import java.util.ArrayList;
import java.util.List;

public class OpenSpaceCard extends AdventureCard {

    private OpenSpaceState openSpaceState;

    private List<String> orderedPlayersBeforeEffect;

    public OpenSpaceCard(String pngName, String name, Game game, int level, boolean tutorial) {
        super(pngName, name, game, level, tutorial);
        openSpaceState = new OpenSpaceState_1(this);
        orderedPlayersBeforeEffect = new ArrayList<>(game.getFlightboard().getOrderedRockets());

    }

    @Override
    public void activateEffect(InputCommand inputCommand) {
        openSpaceState.activateEffect(inputCommand);
    }

    public void setOpenSpaceState(OpenSpaceState openSpaceState) {
        this.openSpaceState = openSpaceState;
    }

    public List<String> getOrderedPlayersBeforeEffect() {
        return orderedPlayersBeforeEffect;
    }
}
