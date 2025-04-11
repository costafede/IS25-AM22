package it.polimi.ingsw.is25am22new.Model.Miscellaneous;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

import java.io.Serializable;
import java.util.List;

public class CardPile implements Serializable {
    private final List<AdventureCard> cards;

    public CardPile(List<AdventureCard> cards) {
        this.cards = cards;
    }

    public List<AdventureCard> getCards() {
        return cards;
    }
}


