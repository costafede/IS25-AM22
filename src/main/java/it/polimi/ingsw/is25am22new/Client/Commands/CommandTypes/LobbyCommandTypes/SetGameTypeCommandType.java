package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.LobbyCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.LobbyCommands.SetGameTypeCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class SetGameTypeCommandType extends AbstractCommandType {
    public SetGameTypeCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Set Game Type";
    }

    @Override
    public boolean isApplicable(ClientModel model) {    /* TO DO */
        return true;  //Bisogna mettere la lobby nel model per verificarne l'applicabilità: fase di lobby, player name = lobby creator
    }

    @Override
    public int getInputLength() {
        return 1;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new SetGameTypeCommand(virtualServer, clientModel, input);
    }

    @Override
    public String getInputRequest() {
        return "Enter 0 for a tutorial game, 1 for a level 2 game:";
    }
}
