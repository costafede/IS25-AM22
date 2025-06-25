package it.polimi.ingsw.is25am22new.Model.Games;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.CardPile;
import it.polimi.ingsw.is25am22new.Model.Flightboards.Level2FlightBoard;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Dices;
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

    /**
     * Constructs a new instance of Level2Game, a specialized implementation for Level 2 gameplay.
     * Initializes the game components, including player nicknames, observers, an empty list of card piles,
     * and a Level2FlightBoard configured specifically for this game level.
     *
     * @param nicknames a list of nicknames representing the players participating in the game.
     *                  Each nickname is unique and identifies a player.
     * @param observers a list of observers that will be registered to monitor and
     *                  update game-related actions and states, or null if no observers are provided.
     */
    public Level2Game(List<String> nicknames, List<ObserverModel> observers) {
        super(nicknames, observers);
        this.cardPiles = new ArrayList<>();
        this.flightboard = new Level2FlightBoard(this);
    }

    public Level2Game(List<String> playerList, List<ObserverModel> observers, List<String> coveredComponentTilesNames, List<String> deckCardsNames, int randomSeed, List<List<String>> cardPilesNames) {
        super(playerList,observers);
        this.dices = new Dices(randomSeed);
        this.cardPiles = new ArrayList<>();
        this.flightboard = new Level2FlightBoard(this);
        initGame();
        Map<String, Integer> position = new HashMap<>();
        for(int i = 0; i < coveredComponentTilesNames.size(); i++) {
            position.put(coveredComponentTilesNames.get(i), i);
        }
        coveredComponentTiles.sort(Comparator.comparingInt(t -> position.get(t.getPngName())));

        position.clear();
        for(int i = 0; i < deckCardsNames.size(); i++) {
            position.put(deckCardsNames.get(i), i);
        }
        this.deck = this.getCardArchive().stream()
                .filter(t -> position.containsKey(t.getPngName()))
                .sorted(Comparator.comparingInt(t -> position.get(t.getPngName())))
                .collect(Collectors.toList());
        for(int i = 0; i < cardPilesNames.size(); i++) {
            position.clear();
            for (int j = 0; j < cardPilesNames.get(i).size(); j++) {
                position.put(cardPilesNames.get(i).get(j), j);
            }
            this.cardPiles.set(i, new CardPile(this.getCardArchive().stream()
                    .filter(t -> position.containsKey(t.getPngName()))
                    .sorted(Comparator.comparingInt(t -> position.get(t.getPngName())))
                    .collect(Collectors.toList())));
        }
    }

    /**
     * Ends the current game session and calculates the final scores for all players.
     *
     * The final scores are determined based on the following criteria:
     * 1. Partial scores: Calculated as the difference between the value of sold goods
     *    and penalty points from discarded tiles for each player.
     * 2. Flightboard positions: Players receive additional points depending on the
     *    position of their rockets on the flightboard.
     * 3. Best shipboard bonus: The player with the best shipboard receives a bonus
     *    score.
     *
     * The method organizes the final scores in descending order, updates the leaderboard
     * for all observers, and returns the sorted score map.
     *
     * @return a map containing player nicknames as keys and their respective
     *         final scores as values, sorted in descending order of scores.
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
     * Sorts the given map in descending order based on its values.
     *
     * @param scores a map containing player names as keys and their respective scores as values
     * @return a new map that contains the same entries as the input map, sorted in descending order by value
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


    /**
     * Initializes the game for the Level 2 phase by performing the following tasks:
     *
     * - Calls the superclass method to set up foundational components and transitions.
     * - Initializes the deck by selecting and organizing the appropriate adventure cards,
     *   which are categorized into level 1 and level 2 cards.
     * - Updates the state of the game and synchronizes it with all registered observers
     *   to ensure consistency and notify them of the current game state.
     */
    @Override
    public void initGame() {
        ObjectMapper objectMapper = new ObjectMapper();
        super.initGame();
        this.initDeck();
        updateAllGame(this);
    }

    /**
     * Initializes the deck for the level 2 game by selecting and shuffling a specific set of cards.
     *
     * This method retrieves all available adventure cards from the game's card archive
     * and filters them based on their difficulty level. It chooses 4 level 1 cards and
     * 8 level 2 cards, shuffles these subsets to randomize the order, and then combines
     * them into a deck. Additionally, it prepares the card piles by calling the `initCardPiles`
     * method with the selected cards.
     *
     * The method logic includes:
     * - Filtering level 1 and level 2 cards from the archive.
     * - Randomly shuffling the filtered cards to ensure variability in gameplay.
     * - Selecting a predefined number of cards (4 level 1 cards and 8 level 2 cards).
     * - Initializing the card piles with the selected cards.
     * - Adding the selected cards to the deck.
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
        Collections.shuffle(deck);
    }

    /**
     * Initializes the card piles required for the game by shuffling and organizing
     * two levels of AdventureCards into groups. Each card pile is constructed using
     * specific combinations of cards from the given level1 and level2 lists.
     *
     * @param level1 the list of AdventureCards corresponding to the first difficulty level
     * @param level2 the list of AdventureCards corresponding to the second difficulty level
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

    /**
     * Retrieves the list of CardPiles associated with the game.
     *
     * @return a List of CardPile objects representing the card piles in the current game.
     */
    public List<CardPile> getCardPiles() {
        return cardPiles;
    }

    /**
     * Flips the hourglass to indicate progression in the game and manages the
     * associated state updates and observer notifications. This method ensures
     * that the hourglass flipping process adheres to the rules of the current
     * game phase and invokes the necessary callbacks when the hourglass state changes.
     *
     * @param callbackMethod the method to be executed as a callback when the hourglass
     *                       flipping process is completed. This action depends on the
     *                       state of the game and the number of times the hourglass
     *                       has been flipped.
     *
     * @throws IllegalStateException if the current game phase does not allow flipping the hourglass.
     * @throws IllegalArgumentException if flipping the hourglass exceeds the allowed number of times.
     */
    public void flipHourglass(Runnable callbackMethod) {//dobbiamo rimuovere il callback method dalla signature, non serve
        if(!gamePhase.getPhaseType().equals(PhaseType.BUILDING))
            throw new IllegalStateException("Cannot flip the hourglass now");
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
