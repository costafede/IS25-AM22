package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandManager;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class, TUITest, is designed to verify the functionality of a Text-based User Interface (TUI) system.
 * It includes unit tests that assess the ability of the TUI to correctly parse commands and their inputs from
 * an input stream, while also performing validations on invalid commands.
 *
 * Tests a variety of cases including:
 * - Correct parsing of commands and their input arguments.
 * - Handling of empty or invalid commands.
 * - Verification of command name and extracted inputs against expected results.
 *
 * The test leverages:
 * - Predefined test cases with various command formats.
 * - A Scanner to simulate user input via a ByteArrayInputStream.
 *
 * Assertions validate:
 * - Whether the command name matches expectations.
 * - Whether the input arguments are parsed and stored correctly.
 * - The ability to reject improperly formatted or invalid commands.
 */
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