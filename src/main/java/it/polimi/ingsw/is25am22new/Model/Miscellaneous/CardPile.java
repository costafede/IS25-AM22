package it.polimi.ingsw.is25am22new.Model.Miscellaneous;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a collection or pile of AdventureCards within the game system.
 * The CardPile serves as a container for a list of AdventureCards, allowing
 * operations such as retrieving the cards it holds.
 */
public class CardPile implements Serializable {
    private final List<AdventureCard> cards;

    public CardPile(List<AdventureCard> cards) {
        this.cards = cards;
    }

    public List<AdventureCard> getCards() {
        return cards;
    }
}


