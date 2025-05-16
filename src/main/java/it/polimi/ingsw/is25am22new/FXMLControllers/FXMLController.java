package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.ClientModel;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import javafx.stage.Stage;

public abstract class FXMLController {
    protected ClientModel model;
    protected VirtualServer virtualServer;
    protected Stage primaryStage;

    public FXMLController(VirtualServer virtualServer, ClientModel model, Stage primaryStage) {
        this.virtualServer = virtualServer;
        this.model = model;
        this.primaryStage = primaryStage;
    }

}
