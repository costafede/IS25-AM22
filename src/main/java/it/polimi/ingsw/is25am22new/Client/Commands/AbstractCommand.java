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

    /**
     * Constructs a new command with references to the virtual server and the view adapter.
     *
     * @param virtualServer the virtual server interface used to send commands to the game logic
     * @param viewAdapter   the view adapter used to update the user interface
     */
    public AbstractCommand(VirtualServer virtualServer, ViewAdapter viewAdapter) {
        this.virtualServer = virtualServer;
        this.viewAdapter = viewAdapter;
    }

    /**
     * Sets the input parameters for this command.
     *
     * @param input the list of input strings to process
     */
    @Override
    public void setInput(List<String> input) {
        this.input = input;
    }

    /**
     * Validates the input parameters for this command.
     *
     * By default, this method checks that the number of input parameters
     * matches the expected input length, which is zero unless overridden.
     *
     * @param model the client model, can be used for context-aware validation
     * @return {@code true} if the input is valid; {@code false} otherwise
     */
    @Override
    public boolean isInputValid(ClientModel model) {
        return input.size() == getInputLength();
    }

    /**
     * Returns the expected length of the input list for this command.
     *
     * The default implementation returns 0, indicating no input is required.
     * Subclasses should override this method if they expect input parameters.
     *
     * @return the expected number of input parameters (default is 0)
     */
    @Override
    public int getInputLength() {
        return 0;   //no input
    }

    /**
     * Activates a card on the virtual server using the given input command.
     *
     * This method catches any exceptions thrown by the server activation call
     * and prints the exception message to the standard output.
     *
     * @param inputCommand the input command encapsulating the activation parameters
     */
    protected void activateCard(InputCommand inputCommand) {
        try{
            virtualServer.activateCard(inputCommand);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
