package it.polimi.ingsw.is25am22new.Client.Commands.CommandList.EndPhaseCommands;

import it.polimi.ingsw.is25am22new.Client.Commands.AbstractCommand;
import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.GamePhase.PhaseType;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

/**
 * ShowLeaderboardCommand is responsible for displaying the leaderboard to the player
 * at the end of the game. This command is only applicable during the END phase of the game.
 * It extends the AbstractCommand class and provides specific behavior for showing
 * the leaderboard through the ViewAdapter.
 */
public class ShowLeaderboardCommand extends AbstractCommand {
    public ShowLeaderboardCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        super(virtualServer, viewAdapter);
    }

    @Override
    public String getName() {
        return "ShowLeaderboard";
    }

    @Override
    public boolean isApplicable(ClientModel model) {
        return model.getGamePhase().getPhaseType().equals(PhaseType.END);
    }

    @Override
    public void execute(ClientModel model) {
        viewAdapter.showLeaderboard(model);
    }
}
