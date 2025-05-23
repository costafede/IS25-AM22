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

/**
 * Represents a level 2 game, extending the functionality of the base {@link Game} class.
 * Implements advanced game mechanisms including specialized card piles, scoring,
 * and time-based gameplay.
 * This class handles initialization, game progression, and scoring logic in
 * accordance with Level 2 gameplay rules.
 * Implements {@link Serializable} to facilitate game state persistence.
 */
public class Level2Game extends Game implements Serializable {
    private final List<CardPile> cardPiles;

    public Level2Game(List<String> nicknames, List<ObserverModel> observers) {
        super(nicknames, observers);
        this.cardPiles = new ArrayList<>();
        this.flightboard = new Level2FlightBoard(this);
    }

    /**
     * Ends the game and calculates the final scores for all players by:
     * 1. Computing partial scores based on sold goods and discarded tiles for each player's shipboard.
     * 2. Adding bonus scores depending on the ranks of players' rockets in the flightboard.
     * 3. Awarding additional points for the player with the best shipboard configuration.
     * The scores are then sorted in descending order before being returned.
     *
     * @return A map where each key represents a player's nickname and the corresponding value is their final score,
     * sorted in descending order of scores.
     */
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

        updateAllLeaderboard(scores);

        return scores;
    }

    /**
     * Sorts the given map of scores in descending order of their values.
     *
     * @param scores a map where each key represents a player's nickname, and the corresponding value
     *               is their score.
     * @return a new map sorted in descending order based on the values (scores).
     */
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

    /**
     * Initializes the game's adventure card deck and card piles. This process involves the following steps:
     *
     * 1. Retrieves the card archive, which contains all available adventure cards.
     * 2. Filters the cards into two separate lists based on their level (level 1 and level 2).
     * 3. Randomizes the order of cards in each list to ensure variety in gameplay.
     * 4. Selects a predefined number of cards:
     *      - 4 cards from level 1
     *      - 8 cards from level 2
     * 5. Initializes the card piles using selected level 1 and level 2 cards.
     *      - Each card pile consists of 3 cards: 1 card from level 1 and 2 cards from level 2.
     * 6. Adds the selected cards to the deck, which will be used during the game.
     */
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

    /**
     * Initializes the card piles for the game by shuffling the given adventure cards and organizing them into piles.
     * Each pile consists of one card from the level 1 list and two cards from the level 2 list.
     * The card piles are then stored in the game's internal data structure.
     *
     * @param level1 a list of level 1 adventure cards to be used in the game.
     * @param level2 a list of level 2 adventure cards to be used in the game.
     */
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
                updateAllShipboardList(shipboards);
                if(gamePhase.getPhaseType().equals(PhaseType.BUILDING))
                    updateAllStopHourglass();
            };
        }
        else{
            callbackMethod = this::updateAllStopHourglass;
        }
        hourglass.startTimer(callbackMethod);
        updateAllStartHourglass(hourglassSpot);
    }

}
