package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.LobbyCommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes.AbstractCommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.LobbyCommands.SetPlayerReadyCommand;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

public class SetPlayerReadyCommandType extends AbstractCommandType {
    public SetPlayerReadyCommandType(VirtualServer virtualServer) {
        super(virtualServer);
    }

    @Override
    public String getName() {
        return "Set Player Ready";
    }

    @Override
    public boolean isApplicable(ClientModel model) {    /* TO DO */
        return true;  //Bisogna mettere la lobby nel model per verificarne l'applicabilità: fase di lobby
    }

    @Override
    public int getInputLength() {
        return 0;
    }

    @Override
    public ParametrizedCommand createWithInput(ClientModel clientModel, List<Integer> input, ViewAdapter viewAdapter) {
        return new SetPlayerReadyCommand(virtualServer, clientModel);
    }
}
