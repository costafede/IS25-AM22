package it.polimi.ingsw.is25am22new.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.AdventureCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ComponentTile;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import it.polimi.ingsw.is25am22new.Network.ObserverModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSaver {

    private static final String fileName = "C:\\Users\\Emanuele\\Desktop\\Uni\\ProgettoSoftEng\\Save.json"; //temporaneo
    private static final ObjectMapper mapper = new ObjectMapper();
    private static BufferedWriter writer;

    public static void clearFile() {
        try {
            FileWriter fileClearer = new FileWriter(fileName, false);
            fileClearer.write("");
            fileClearer.close();
            writer = new BufferedWriter(new FileWriter(fileName));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static void savePlaceAstronauts(String nickname, int i, int j) {
        saveCommand(new SavedCommand("placeAstronauts", nickname, i, j));
    }

    public static void savePlaceBrownAlien(String nickname, int i, int j) {
        saveCommand(new SavedCommand("placeBrownAlien", nickname, i, j));
    }

    public static void savePlacePurpleAlien(String nickname, int i, int j) {
        saveCommand(new SavedCommand("placePurpleAlien", nickname, i, j));
    }

    public static void savePickCoveredTile(String nickname) {
        saveCommand(new SavedCommand("pickCoveredTile", nickname));
    }

    public static void savePickUncoveredTile(String nickname, String tilePngName) {
        saveCommand(new SavedCommand("pickUncoveredTile", nickname, tilePngName));
    }

    public static void saveRotateClockwise(String nickname) {
        saveCommand(new SavedCommand("rotateClockwise", nickname));
    }

    public static void saveRotateCounterClockwise(String nickname) {
        saveCommand(new SavedCommand("rotateCounterClockwise", nickname));
    }

    public static void saveWeldComponentTile(String nickname, int i, int j) {
        saveCommand(new SavedCommand("weldComponentTile", nickname, i, j));
    }

    public static void saveStandbyComponentTile(String nickname) {
        saveCommand(new SavedCommand("standbyComponentTile", nickname));
    }

    public static void savePickStandByComponentTile(String nickname, int index) {
        saveCommand(new SavedCommand("pickStandByComponentTile", nickname, index));
    }

    public static void saveDiscardComponentTile(String nickname) {
        saveCommand(new SavedCommand("discardComponentTile", nickname));
    }

    public static void saveFinishBuilding(String nickname, int pos) {
        saveCommand(new SavedCommand("finishBuilding", nickname, pos));
    }

    public static void saveFlipHourglass() {
        saveCommand(new SavedCommand("flipHourglass"));
    }

    public static void savePickCard() {
        saveCommand(new SavedCommand("pickCard"));
    }

    public static void savePlayerAbandons(String nickname) {
        saveCommand(new SavedCommand("playerAbandons", nickname));
    }

    public static void saveDestroyTile(String nickname, int i, int j) {
        saveCommand(new SavedCommand("destroyTile", nickname, i, j));
    }

    public static void saveActivateCard(InputCommand inputCommand) {
        saveCommand(new SavedCommand("activateCard", inputCommand));
    }

    public static Game loadGame(List<ObserverModel> observers) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String GameType = mapper.readValue(reader.readLine(), String.class);
            List<String> playerList = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            List<String> coveredComponentTiles = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            List<String> deck = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            int randomSeed = Integer.parseInt(mapper.readValue(reader.readLine(), new TypeReference<>() {}));
            List<List<String>> cardPiles = mapper.readValue(reader.readLine(), new TypeReference<>() {});
            Game game = null;
            if(GameType.equals("tutorial")) {
                game =  new TutorialGame(playerList, observers, coveredComponentTiles, deck, randomSeed);
            }
            else if(GameType.equals("level2")) {
                game =  new Level2Game(playerList, observers, coveredComponentTiles, deck, randomSeed, cardPiles);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                SavedCommand cmd = mapper.readValue(line, SavedCommand.class);
                applyCommand(cmd, game);
            }
            return game;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public static void main(String[] args) {
        /*clearFile();
        Game game = new Level2Game(new ArrayList<>(List.of("Emanuele","De","Simone")),new ArrayList<>());
        game.initGame();
        saveLevel2Game(game);
        InputCommand inputCommand = new InputCommand();
        inputCommand.setChoice(true);
        inputCommand.setGoodBlock(GoodBlock.YELLOWBLOCK);
        inputCommand.setRow(2);
        inputCommand.setCol(4);
        saveActivateCard(inputCommand);
        saveDestroyTile("Emanuele",1,2);
        savePlayerAbandons("Carlo");
        savePickCard();
        saveFlipHourglass();
        saveFinishBuilding("Consuelo",23);
        saveDiscardComponentTile("Franco");
        savePickStandByComponentTile("Armando",2);
        saveStandbyComponentTile("Quintiliano");
        saveWeldComponentTile("Domiziano",1,2);
        saveRotateCounterClockwise("Catone");
        saveRotateClockwise("Doge");
        savePickUncoveredTile("Pino","x");
        savePickCoveredTile("Daniele");
        savePlacePurpleAlien("Tony",2,3);
        savePlaceBrownAlien("Carmela",4,5);
        savePlaceAstronauts("Paolo",1,2);*/
        Game game1 = loadGame(null);
        System.exit(0);
    }
}
