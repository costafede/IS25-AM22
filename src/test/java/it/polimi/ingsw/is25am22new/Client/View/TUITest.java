package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TUITest {
    @Test
    void should_parse_strings_correctly() {
        String inputString = "command1()\n" + "command2(,)\n" + "command3(in1, in2, in3)\n" + "()\n" + "command4(in1, ,in2)\n" + "command5(in1,,in2)\n" + "command6(,,,,)\n" + "command7())\n" + "command8(in)\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(inputString.getBytes()));
        TUI tui = new TUI(new CommandManager(), null);

        assertTrue(tui.askCommand(scanner));
        assertEquals(tui.getCommandName(), "command1");
        assertTrue(tui.getInput().isEmpty());

        assertFalse(tui.askCommand(scanner));

        assertTrue(tui.askCommand(scanner));
        assertEquals(tui.getCommandName(), "command3");
        assertEquals(tui.getInput().getFirst(), "in1");
        assertEquals(tui.getInput().get(1), "in2");
        assertEquals(tui.getInput().get(2), "in3");

        assertFalse(tui.askCommand(scanner));

        assertFalse(tui.askCommand(scanner));

        assertFalse(tui.askCommand(scanner));

        assertFalse(tui.askCommand(scanner));

        assertFalse(tui.askCommand(scanner));

        assertTrue(tui.askCommand(scanner));
        assertEquals(tui.getCommandName(), "command8");
        assertEquals(tui.getInput().getFirst(), "in");
    }
}