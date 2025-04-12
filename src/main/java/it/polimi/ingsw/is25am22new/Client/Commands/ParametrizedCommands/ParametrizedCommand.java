package it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

public interface ParametrizedCommand {
    void execute();    //executes the command by calling the proper method from VirtualServer
    boolean isValid(ClientModel model); //check if the inputs are valid from the local copy of the model called ClientModel
}
