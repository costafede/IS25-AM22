package it.polimi.ingsw.is25am22new.Model;

import java.util.ArrayList;
import java.util.List;

public class CardPile {
    private List<String> cards;

    public CardPile() {
        this.cards = new ArrayList<String>();
    }

    public List<String> getCards() {
        return cards;
    }
}


