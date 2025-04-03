package it.polimi.ingsw.is25am22new.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.is25am22new.Model.Games.Game;
import it.polimi.ingsw.is25am22new.Model.Games.Level2Game;
import it.polimi.ingsw.is25am22new.Model.Games.TutorialGame;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;
import it.polimi.ingsw.is25am22new.Model.Miscellaneous.GoodBlock;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Gsontest {

    @Test
    void test_3() throws IOException {
        List<String> players = List.of("A", "B", "C");

        Game game = new Level2Game(players);
        game.initGame();
        Bank bank = game.getBank();

        String path = "src/main/java/it/polimi/ingsw/is25am22new/Model/JSONfiles/Saves/Bank.json";
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), bank);
    }
}
