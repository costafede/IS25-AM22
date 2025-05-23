package it.polimi.ingsw.is25am22new.Model.AdventureCard;

import it.polimi.ingsw.is25am22new.Client.View.AdventureCardViewTUI;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

/**
 * Represents a contract for cards that can be displayed in the game's user interface.
 * Classes implementing this interface must define how the card should be rendered
 * based on the current game state and user interaction.
 */
public interface ViewableCard {
    void show(AdventureCardViewTUI view, ClientModel model);
}
