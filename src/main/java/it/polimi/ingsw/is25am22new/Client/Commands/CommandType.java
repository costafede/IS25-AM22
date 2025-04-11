package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;

public interface CommandType {
    String getName();
    boolean isApplicable(ClientModel model);
    InputDescriptor getInputDescriptor();
    ParametrizedCommand createWithInput(Object... args);

}
