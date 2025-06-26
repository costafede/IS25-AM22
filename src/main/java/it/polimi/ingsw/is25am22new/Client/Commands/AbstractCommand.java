package it.polimi.ingsw.is25am22new.Client.Commands;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.TUI.ViewAdapter;
import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;

import java.util.List;

/**
 * AbstractCommand provides a base implementation for commands in the game.
 * It serves as an abstract class adhering to the Command interface, encapsulating
 * common functionalities for command execution, input validation, and interaction
 * with the VirtualServer and ViewAdapter. Concrete command implementations should
 * extend this class and provide specific behavior for their defined use case.
 */
public abstract class AbstractCommand implements Command {
    protected VirtualServer virtualServer;
    protected List<String> input;
    protected ViewAdapter viewAdapter;

    public AbstractCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        this.virtualServer = virtualServer;
        this.viewAdapter = viewAdapter;
    }

    @Override
    public void setInput(List<String> input) {
        this.input = input;
    }

    /**
     * I assume there is no input by default
     */
    @Override
    public boolean isInputValid(ClientModel model) {
        return input.size() == getInputLength();
    }

    @Override
    public int getInputLength() {
        return 0;   //no input
    }

    protected void activateCard(InputCommand inputCommand) {
        try{
            virtualServer.activateCard(inputCommand);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
