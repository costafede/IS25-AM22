package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;

import java.util.List;

public interface CommandType {
    String getName();   //returns the input name
    boolean isApplicable(ClientModel model);    //check if the command is valid in the game phase
    int getInputLength();   //return the number of inputs needed by the command, 0 if the command needs no input
    ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter);    //creates the actual command with the execute method
    String getInputRequest();   //returns the string asking for the input
}
