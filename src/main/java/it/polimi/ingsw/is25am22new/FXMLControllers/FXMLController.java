package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import javafx.stage.Stage;

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
