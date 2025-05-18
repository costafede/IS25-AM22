package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

import java.util.List;

/**
 * The Command interface represents an abstract contract for defining game commands.
 * Each command can be validated, executed, and configured based on its specific requirements within the game flow.
 */
public interface Command {
    String getName();   //returns the input name
    boolean isApplicable(ClientModel model);    //check if the command is valid in the game phase
    int getInputLength();   //return the number of inputs needed by the command, 0 if the command needs no input
    void execute(ClientModel model);    //executes the command by calling the proper method from VirtualServer
    boolean isInputValid(ClientModel model); //check if the inputs are valid from the local copy of the model called ClientModel
    void setInput(List<String> input);
}
