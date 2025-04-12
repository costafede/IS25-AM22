package it.polimi.ingsw.is25am22new.Client.Commands.CommandTypes;

import it.polimi.ingsw.is25am22new.Client.Commands.CommandType;
import it.polimi.ingsw.is25am22new.Client.Commands.InputDescriptor;
import it.polimi.ingsw.is25am22new.Client.Commands.ParametrizedCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Model.GamePhase.GamePhase;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;

public class RemovePlayerCommand implements CommandType {

    @Override
    public String getName() {
        return "RemovePlayerCommand";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase() == PhaseType.LOBBY && model.getPlayerName()  //Bisogna mettere la lobby nel model
    }

    @Override
    public InputDescriptor getInputDescriptor() {
        return null;
    }

    @Override
    public ParametrizedCommand createWithInput(Object... args) {
        return null;
    }
}
