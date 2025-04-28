package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.CardPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

public class PickCardCommand extends AbstractCommand {

    public PickCardCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "PickCard";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.CARD) &&
                model.getCurrPlayer().equals(model.getPlayerName()) &&
                model.getCurrCard() == null &&
                !model.getDeck().isEmpty();
    }

    @Override
    public void execute(ClientModel model) {
        try {
            virtualServer.pickCard();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
