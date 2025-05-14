package it.polimi.ingsw.is25am22new.FXMLControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StartMenu {
    @FXML
    private Button playButton;

    @FXML
    protected void onPlayButtonClick() {
        System.out.println("onPlayButtonClick");
    }
}