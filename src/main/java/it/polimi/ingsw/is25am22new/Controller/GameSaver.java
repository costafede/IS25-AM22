package it.polimi.ingsw.is25am22new.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class GameSaver {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static BufferedWriter writer;

    private static final Path saveFilePath = Paths.get("saves", "Save.json");

    static {
        try {
            Files.createDirectories(saveFilePath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the current save file by creating a new buffered writer.
     * If the file already exists, its contents are truncated.
     * Any exception during this operation is printed to the stack trace.
     */
    public static void clearFile() {
        try {
            writer = Files.newBufferedWriter(saveFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes a {@link SavedCommand} object to JSON and appends it to the save file.
     * Adds a newline after the JSON string and flushes the writer.
     *
     * @param savedCommand the command to serialize and save
     */
    private static void saveCommand(SavedCommand savedCommand) {
        try {
            String json = mapper.writeValueAsString(savedCommand);
            writer.write(json);
            writer.newLine();
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the initial game data to the save file, including:
     * - Player list
     * - Covered component tiles
     * - Deck of adventure cards
     * - Random seed for the dice generator
     *
     * All data is serialized in JSON format, written on separate lines, and flushed immediately.
     *
     * @param game the game instance whose starting data should be saved
     */
    private static void saveStartingData(Game game) {
        try {
            List<String> playerList = new ArrayList<>(game.getPlayerList());
            List<String> deck = new ArrayList<>(game.getDeck().stream().map(AdventureCard::getPngName).toList());
            List<String> coveredComponentTiles = new ArrayList<>(game.getCoveredComponentTiles().stream().map(ComponentTile::getPngName).toList());
            String jsonPlayerList = mapper.writeValueAsString(playerList);
            writer.write(jsonPlayerList);
            writer.newLine();
            writer.flush();
            String jsonCoveredComponentTiles = mapper.writeValueAsString(coveredComponentTiles);
            writer.write(jsonCoveredComponentTiles);
            writer.newLine();
            writer.flush();
            String jsonDeck = mapper.writeValueAsString(deck);
            writer.write(jsonDeck);
            writer.newLine();
            writer.flush();
            String jsonDicesRandomSeed = mapper.writeValueAsString(game.getDices().getRandomSeed());
            writer.write(jsonDicesRandomSeed);
            writer.newLine();
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the identifier for a tutorial game and its starting data to the save file.
     *
     * @param game the tutorial game instance to save
     */
    public static void saveTutorialGame(Game game) {
        try {
            String json = mapper.writeValueAsString("tutorial");
            writer.write(json);
            writer.newLine();
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        saveStartingData(game);
    }

    /**
     * Saves the identifier for a level 2 game, its starting data, and
     * the serialized card piles to the save file.
     *
     * @param game the level 2 game instance to save
     */
    public static void saveLevel2Game(Game game) {
        try {
            String json = mapper.writeValueAsString("level2");
            writer.write(json);
            writer.newLine();
            writer.flush();
            saveStartingData(game);
            List<List<String>> cardPiles = new ArrayList<>(game.getCardPiles().stream().map(x -> x.getCards().stream().map(AdventureCard::getPngName).toList()).toList());
            String jsonCardPiles = mapper.writeValueAsString(cardPiles);
            writer.write(jsonCardPiles);
            writer.newLine();
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the "placeAstronauts" command with the specified parameters to the save file.
     *
     * @param nickname the player performing the action
     * @param i the row index
     * @param j the column index
     */
    public static void savePlaceAstronauts(String nickname, int i, int j) {
        saveCommand(new SavedCommand("placeAstronauts", nickname, i, j));
    }

    /**
     * Saves the "placeBrownAlien" command with the specified coordinates for the given player.
     *
     * @param nickname the player placing the purple alien
     * @param i the row index on the ship grid
     * @param j the column index on the ship grid
     */
    public static void savePlaceBrownAlien(String nickname, int i, int j) {
        saveCommand(new SavedCommand("placeBrownAlien", nickname, i, j));
    }

    /**
     * Saves the "placePurpleAlien" command with the specified coordinates for the given player.
     *
     * @param nickname the player placing the purple alien
     * @param i the row index on the ship grid
     * @param j the column index on the ship grid
     */
    public static void savePlacePurpleAlien(String nickname, int i, int j) {
        saveCommand(new SavedCommand("placePurpleAlien", nickname, i, j));
    }

    /**
     * Saves the "pickCoveredTile" command for the given player to the save file.
     *
     * @param nickname the player performing the action
     */
    public static void savePickCoveredTile(String nickname) {
        saveCommand(new SavedCommand("pickCoveredTile", nickname));
    }

    /**
     * Saves the "pickUncoveredTile" command for the specified player,
     * indicating that they selected a visible component tile.
     *
     * @param nickname the player picking the tile
     * @param tilePngName the PNG name of the chosen tile
     */
    public static void savePickUncoveredTile(String nickname, String tilePngName) {
        saveCommand(new SavedCommand("pickUncoveredTile", nickname, tilePngName));
    }

    /**
     * Saves the "rotateClockwise" command for the specified player,
     * indicating that they rotated the currently held tile clockwise.
     *
     * @param nickname the player performing the rotation
     */
    public static void saveRotateClockwise(String nickname) {
        saveCommand(new SavedCommand("rotateClockwise", nickname));
    }

    /**
     * Saves the "rotateCounterClockwise" command for the specified player,
     * indicating that they rotated the currently held tile counterclockwise.
     *
     * @param nickname the player performing the rotation
     */
    public static void saveRotateCounterClockwise(String nickname) {
        saveCommand(new SavedCommand("rotateCounterClockwise", nickname));
    }

    /**
     * Saves the "weldComponentTile" command for the specified player,
     * indicating that they placed the tile at the given coordinates on their ship.
     *
     * @param nickname the player placing the tile
     * @param i the row index on the ship grid
     * @param j the column index on the ship grid
     */
    public static void saveWeldComponentTile(String nickname, int i, int j) {
        saveCommand(new SavedCommand("weldComponentTile", nickname, i, j));
    }

    /**
     * Saves the "standbyComponentTile" command for the specified player,
     * indicating that they set aside the currently held tile into standby mode.
     *
     * @param nickname the player putting the tile on standby
     */
    public static void saveStandbyComponentTile(String nickname) {
        saveCommand(new SavedCommand("standbyComponentTile", nickname));
    }

    /**
     * Saves the "pickStandByComponentTile" command for the specified player,
     * indicating that they picked a tile from their standby list.
     *
     * @param nickname the player picking the tile
     * @param index the index of the tile in the standby list
     */
    public static void savePickStandByComponentTile(String nickname, int index) {
        saveCommand(new SavedCommand("pickStandByComponentTile", nickname, index));
    }

    /**
     * Saves the "discardComponentTile" command for the specified player,
     * indicating that they discarded the currently held tile.
     *
     * @param nickname the player discarding the tile
     */
    public static void saveDiscardComponentTile(String nickname) {
        saveCommand(new SavedCommand("discardComponentTile", nickname));
    }

    /**
     * Saves the "finishBuilding" command for the specified player,
     * indicating that they completed the ship building phase.
     *
     * @param nickname the player finishing their ship
     * @param pos the player's position in the order of completion
     */
    public static void saveFinishBuilding(String nickname, int pos) {
        saveCommand(new SavedCommand("finishBuilding", nickname, pos));
    }

    /**
     * Saves the "flipHourglass" command,
     * indicating that the hourglass was flipped during the building phase.
     */
    public static void saveFlipHourglass() {
        saveCommand(new SavedCommand("flipHourglass"));
    }

    /**
     * Saves the "pickCard" command,
     * indicating that a new adventure card was drawn.
     */
    public static void savePickCard() {
        saveCommand(new SavedCommand("pickCard"));
    }

    /**
     * Saves the "playerAbandons" command for the specified player,
     * indicating that the player abandoned the game.
     *
     * @param nickname the player who abandoned the game
     */
    public static void savePlayerAbandons(String nickname) {
        saveCommand(new SavedCommand("playerAbandons", nickname));
    }

    /**
     * Saves the "destroyTile" command for the specified player,
     * indicating that a tile was destroyed on their ship at the given coordinates.
     *
     * @param nickname the affected player
     * @param i the row index of the destroyed tile
     * @param j the column index of the destroyed tile
     */
    public static void saveDestroyTile(String nickname, int i, int j) {
        saveCommand(new SavedCommand("destroyTile", nickname, i, j));
    }

    /**
     * Saves the "activateCard" command,
     * indicating that an adventure card was activated with the given input.
     *
     * @param inputCommand the input used to activate the card
     */
    public static void saveActivateCard(InputCommand inputCommand) {
        saveCommand(new SavedCommand("activateCard", inputCommand));
    }

    /**
     * Loads a previously saved game from the save file.
     *
     * It reads the game type (e.g., "tutorial", "level2"), reconstructs the corresponding
     * {@link Game} object with its initial state (players, deck, covered tiles, etc.),
     * and then applies all saved commands to bring the game back to its latest state.
     *
     *
     * @return the reconstructed {@link Game} instance, or {@code null} if an error occurs during loading
     */
    public static Game loadGame() {
        try(BufferedReader reader =  Files.newBufferedReader(saveFilePath)) {
            String GameType = mapper.readValue(reader.readLine(), String.class);
            List<String> playerList = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            List<String> coveredComponentTiles = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            List<String> deck = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            int randomSeed = Integer.parseInt(mapper.readValue(reader.readLine(), new TypeReference<>() {}));
            Game game = null;
            if(GameType.equals("tutorial")) {
                game =  new TutorialGame(playerList, null, coveredComponentTiles, deck, randomSeed);
            }
            else if(GameType.equals("level2")) {
                List<List<String>> cardPiles = mapper.readValue(reader.readLine(), new TypeReference<>() {});
                game =  new Level2Game(playerList, null, coveredComponentTiles, deck, randomSeed, cardPiles);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                SavedCommand cmd = mapper.readValue(line, SavedCommand.class);
                applyCommand(cmd, game);
            }
            writer = Files.newBufferedWriter(saveFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return game;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Applies a previously saved command to the given game instance.
     *
     * This method is used during game loading to re-execute game actions
     * in the order they were saved. It dispatches based on the command type
     * and calls the appropriate method on the {@link Game} object.
     *
     *
     * @param cmd  the {@link SavedCommand} to apply
     * @param game the {@link Game} instance to which the command should be applied
     */
    private static void applyCommand(SavedCommand cmd, Game game) {
        switch(cmd.getType()) {
            case "placeAstronauts" -> game.placeAstronauts(cmd.getNickname(), cmd.getI(), cmd.getJ());
            case "placeBrownAlien" -> game.placeBrownAlien(cmd.getNickname(), cmd.getI(), cmd.getJ());
            case "placePurpleAlien" -> game.placePurpleAlien(cmd.getNickname(), cmd.getI(), cmd.getJ());
            case "pickCoveredTile" -> game.pickCoveredTile(cmd.getNickname());
            case "pickUncoveredTile" -> game.pickUncoveredTile(cmd.getNickname(), cmd.getTilePngName());
            case "rotateClockwise" -> game.rotateClockwise(cmd.getNickname());
            case "rotateCounterClockwise" -> game.rotateCounterClockwise(cmd.getNickname());
            case "weldComponentTile" -> game.weldComponentTile(cmd.getNickname(), cmd.getI(), cmd.getJ());
            case "standbyComponentTile" -> game.standbyComponentTile(cmd.getNickname());
            case "pickStandByComponentTile" -> game.pickStandByComponentTile(cmd.getNickname(), cmd.getI());
            case "discardComponentTile" -> game.discardComponentTile(cmd.getNickname());
            case "finishBuilding" -> game.finishBuilding(cmd.getNickname(), cmd.getI());
            case "flipHourglass" -> game.flipHourglass(null);
            case "pickCard" -> game.pickCard();
            case "playerAbandons" -> game.playerAbandons(cmd.getNickname());
            case "destroyTile" -> game.destroyTile(cmd.getNickname(), cmd.getI(), cmd.getJ());
            case "activateCard" -> game.activateCard(cmd.getInputCommand());
        }
    }
}
