package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Bank;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Level2Shipboard;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.Shipboards.TutorialShipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TutorialGame extends Game {
    public TutorialGame(List<String> nicknames) {
        super(nicknames);
    }

    @Override
    public void initShipboard(Map<String, Shipboard> shipboards, List<String> playerList, Bank bank) {
        List<String> colors = List.of("red", "green", "blue", "yellow");
        for(String nickname : playerList) {
            shipboards.put(nickname, new TutorialShipboard(colors.removeFirst(), nickname, bank));
        }
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
