package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Level2FlightBoard;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Level2Game extends Game implements Serializable {
    private final List<CardPile> cardPiles;

    public Level2Game(List<String> nicknames, List<ObserverModel> observers) {
        super(nicknames, observers);
        this.cardPiles = new ArrayList<>();
        this.flightboard = new Level2FlightBoard(this);
    }

    public Map<String, Integer> endGame() {
        Map<String, Integer> scores = new HashMap<>();

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
        scores.put(super.betterShipboard(), scores.get(betterShipboard()) + 4);

        scores = sortDesc(scores);

        return scores;
    }

    protected Map<String, Integer> sortDesc(Map<String, Integer> scores) {
        //sorting the map
        scores = scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );
        return scores;
    }


    @Override
    public void initGame() {
        ObjectMapper objectMapper = new ObjectMapper();
        super.initGame();
        this.initDeck();
        updateAllGame(this);
    }

    @Override
    public void initDeck() {
        List<AdventureCard> cardArchive = getCardArchive();

        // Filter level 1 and level 2 cards
        List<AdventureCard> level1Cards = cardArchive.stream()
                .filter(card -> card.getLevel() == 1)
                .collect(Collectors.toList());

        List<AdventureCard> level2Cards = cardArchive.stream()
                .filter(card -> card.getLevel() == 2)
                .collect(Collectors.toList());

        // Shuffle the lists to randomize the selection
        Collections.shuffle(level1Cards);
        Collections.shuffle(level2Cards);

        // Select 4 level 1 cards and 8 level 2 cards
        List<AdventureCard> selectedCards = level1Cards.stream().limit(4).collect(Collectors.toList());
        selectedCards.addAll(level2Cards.stream().limit(8).toList());

        // initialize the Piles
        initCardPiles(level1Cards, level2Cards);

        // Add the selected cards to the deck
        deck.addAll(selectedCards);
    }

    //cardPiles are 4 made of 2 level 2 cards and 1 level 1 card
    private void initCardPiles(List<AdventureCard> level1, List<AdventureCard> level2) {
        Collections.shuffle(level1);
        Collections.shuffle(level2);
        for(int i = 0; i < 4; i++) {
            List<AdventureCard> cards = new ArrayList<>();
            cards.add(level1.get(i));
            cards.add(level2.get(i));
            cards.add(level2.get(i+4)); //magic number 4
            cardPiles.add(new CardPile(cards));
        }
    }

    public List<CardPile> getCardPiles() {
        return cardPiles;
    }

    public void flipHourglass(Runnable callbackMethod) {    //dobbiamo rimuovere il callback method dalla signature, non serve
        if(hourglassSpot == 2)
            throw new IllegalArgumentException("Cannot flip the hourglass three times");
        hourglassSpot++;
        if(hourglassSpot == 2) {
            callbackMethod = () -> {
                for (String p : playerList) {
                    shipboards.get(p).setFinishedShipboard(true);
                }
                gamePhase.trySwitchToNextPhase();
                if(gamePhase.getPhaseType().equals(PhaseType.BUILDING))
                    updateAllStopHourglass();
                updateAllGamePhase(gamePhase);
            };
        }
        else{
            callbackMethod = this::updateAllStopHourglass;
        }
        hourglass.startTimer(callbackMethod);
        updateAllStartHourglass(hourglassSpot);
    }

}
