package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.Bank;
import it.polimi.ingsw.is25am22new.Model.Shipboards.Shipboard;
import it.polimi.ingsw.is25am22new.Model.CardPile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level2Game extends Game {
    private List<CardPile> cardPiles;

    public Level2Game(List<String> nicknames) {
        super(nicknames);
        this.cardPiles = new ArrayList<CardPile>();
    }

    public Map<String, Integer> endGame() {
        Map<String, Integer> scores = new HashMap<>();
        String nickname = super.betterShipboard();

        //Calculate the partial scores : Score = Sold Goods - Discard Tiles
        for(String player : playerList) {
            scores.put(player, shipboards.get(player).getScore());
        }

        //Adding scores for position in the flightboard
        List<String> orderedRockets = flightboard.getOrderedRockets();
        for(int i = 0; i < orderedRockets.size(); i++) {
            scores.put(orderedRockets.get(i), scores.get(orderedRockets.get(i)) + 2*(4 - orderedRockets.indexOf(orderedRockets.get(i))));
        }

        //Adding scores for the best shipboard
        scores.put(betterShipboard(), scores.get(betterShipboard()) + 4);

        return scores;
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
