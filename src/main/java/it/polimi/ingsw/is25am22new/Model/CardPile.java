package it.polimi.ingsw.is25am22new.Model;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public class CardPile {
    private List<AdventureCard> cards;


    public CardPile(List<AdventureCard> cards) {
        this.cards = cards;
    }

    public List<AdventureCard> getCards() {
        return cards;
    }
}


