package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

import java.util.List;

/**
 * The Command interface represents an abstract contract for defining game commands.
 * Each command can be validated, executed, and configured based on its specific requirements within the game flow.
 */
public interface Command {
    /**
     * Returns the name of the command.
     *
     * @return the input name of the command
     */
    String getName();

    /**
     * Checks if the command is applicable in the current game phase.
     *
     * @param model the current client model
     * @return true if the command is valid in the current game phase, false otherwise
     */
    boolean isApplicable(ClientModel model);

    /**
     * Returns the number of inputs needed by the command.
     *
     * @return the number of required inputs, or 0 if no inputs are needed
     */
    int getInputLength();

    /**
     * Executes the command by invoking the appropriate method on the VirtualServer.
     *
     * @param model the current client model
     */
    void execute(ClientModel model);

    /**
     * Checks if the inputs set for this command are valid based on the current client model.
     *
     * @param model the current client model
     * @return true if the inputs are valid, false otherwise
     */
    boolean isInputValid(ClientModel model);

    /**
     * Sets the inputs needed to execute the command.
     *
     * @param input the list of input strings for the command
     */
    void setInput(List<String> input);
}
