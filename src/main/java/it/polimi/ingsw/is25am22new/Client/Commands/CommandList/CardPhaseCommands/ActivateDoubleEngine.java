package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceCard;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.OpenSpaceCard.OpenSpaceState;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class ActivateDoubleEngine extends AbstractCommand {
    public ActivateDoubleEngine(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ActivateDoubleEngine";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return true;
    }

    @Override
    public void execute(ClientModel model) {

    }
}
