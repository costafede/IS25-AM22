package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TutorialGame extends Game {
    public TutorialGame() {
        super();
    }

    @Override
    public void initGame() {
        ObjectMapper objectMapper = new ObjectMapper();
        super.initGame();
        this.initDeck(objectMapper);
    }

    @Override
    public void initDeck(ObjectMapper objectMapper) {
        // Reads 8 cards from json file and adds them to the deck
    }
}
