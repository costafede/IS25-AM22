package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Bank;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.CardPile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Level2Game extends Game {
    private List<CardPile> cardPiles;

    public Level2Game(List<String> nicknames) {
        super(nicknames);
        this.cardPiles = new ArrayList<CardPile>();
    }

    @Override
    public void initGame() {
        ObjectMapper objectMapper = new ObjectMapper();
        super.initGame();
        this.initDeck(objectMapper);
        this.initCardPiles();
    }

    @Override
    public void initDeck(ObjectMapper objectMapper) {
        // Reads 12 cards from json file and adds them to the deck
    }

    private void initCardPiles() {

    }
}
