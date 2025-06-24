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

    /**
     * Constructs a CardPile with the given list of AdventureCards.
     *
     * @param cards the list of AdventureCards to be contained within the CardPile
     */
    public CardPile(List<AdventureCard> cards) {
        this.cards = cards;
    }

    /**
     * Retrieves the list of AdventureCards contained within the CardPile.
     *
     * @return a List of AdventureCard objects representing the cards in the pile.
     */
    public List<AdventureCard> getCards() {
        return cards;
    }
}


