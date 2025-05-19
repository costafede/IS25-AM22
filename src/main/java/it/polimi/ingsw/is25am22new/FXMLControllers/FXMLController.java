package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import javafx.stage.Stage;

/**
 * The FXMLController class serves as an abstract base class for creating controllers
 * in the Galaxy Trucker application that interact with the JavaFX FXML framework.
 * It provides common functionality and shared attributes for its subclasses, enabling
 * easy management of the application state, GUI integration, and interactions with
 * the client model. Subclasses are expected to implement specific logic aligned with
 * their functionality while leveraging this class's shared resource management.
 *
 * The FXMLController includes references to key components necessary for its operation:
 * - A reference to the GalaxyTruckerGUI instance for scene and GUI management.
 * - A reference to the ClientModel for accessing and manipulating the game state.
 * - A reference to the VirtualServer for server interactions.
 * - A reference to the primary Stage for scene setup and navigation.
 */
public abstract class FXMLController {
    protected GalaxyTruckerGUI galaxyTruckerGUI;
    protected ClientModel model;
    protected VirtualServer virtualServer;
    protected Stage primaryStage;

    public void setup(GalaxyTruckerGUI galaxyTruckerGUI, ClientModel model, Stage primaryStage, VirtualServer virtualServer) {
        this.galaxyTruckerGUI = galaxyTruckerGUI;
        this.model = model;
        this.primaryStage = primaryStage;
        this.virtualServer = virtualServer;
    }

    public ClientModel getModel() {
        return model;
    }

    public void setModel(ClientModel model) {
        this.model = model;
    }

    public VirtualServer getVirtualServer() {
        return virtualServer;
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
