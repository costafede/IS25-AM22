package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyBackground;
import it.polimi.ingsw.is25am22new.Client.View.GUI.GalaxyTruckerGUI;
import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketServerHandler;
import it.polimi.ingsw.is25am22new.Network.VirtualServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectToServerController extends FXMLController implements Initializable {


    @FXML
    private Canvas galaxyBackground;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField portField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button connectButton;

    private GalaxyBackground animatedBackground;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set default values
        ipAddressField.setText("localhost");
        portField.setText("1234");

        // Initialize the animated background
        if (galaxyBackground != null) {
            // Force the canvas to be full scene size
            galaxyBackground.setWidth(1280);
            galaxyBackground.setHeight(720);

            // Create background with correct dimensions
            animatedBackground = new GalaxyBackground(1280, 720);

            // Set parent style if available
            if (galaxyBackground.getParent() != null) {
                galaxyBackground.getParent().setStyle("-fx-background-color: black;");
            }

            // Use a scene property listener instead of direct access
            galaxyBackground.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.setFill(javafx.scene.paint.Color.BLACK);
                }
            });

            // Replace the canvas with our animated one
            if (galaxyBackground.getParent() != null) {
                int index = galaxyBackground.getParent().getChildrenUnmodifiable().indexOf(galaxyBackground);
                ((javafx.scene.layout.StackPane) galaxyBackground.getParent()).getChildren().set(index, animatedBackground);
            }
        }

        // Add action listener to the connect button
        connectButton.setOnAction(this::connectToServer);

        // Apply CSS styles
        connectButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/it/polimi/ingsw/is25am22new/styles.css").toExternalForm());
            }
        });
    }

    @FXML
    public void connectToServer(ActionEvent event){
        // Validate inputs
        if (usernameField.getText().trim().isEmpty()) {
            errorLabel.setText("Username cannot be empty");
            return;
        }

        if (ipAddressField.getText().trim().isEmpty()) {
            errorLabel.setText("IP Address cannot be empty");
            return;
        }

        if (portField.getText().trim().isEmpty()) {
            errorLabel.setText("Port cannot be empty");
            return;
        }

        try {
            int port = Integer.parseInt(portField.getText().trim());
            if (port < 1024 || port > 65535) {
                errorLabel.setText("Port must be between 1024 and 65535");
                return;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Port must be a valid number");
            return;
        }

        // Attempt connection
        String username = usernameField.getText().trim();
        String ipAddress = ipAddressField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());
        galaxyTruckerGUI.setPlayerName(username);

        // Show connecting message
        errorLabel.setText("Connecting to server...");

        if(galaxyTruckerGUI.getParameters().getRaw().getFirst().equals("socket")) {
            try {
                // For Socket clients, try to connect first before switching scenes
                // This ensures we don't see an empty lobby when the host is configuring
                VirtualServer s = SocketServerHandler.connectToServerGUI(ipAddress, port + 1, username, GalaxyTruckerGUI.getClientModel(), galaxyTruckerGUI);
                if (s != null) {
                    // Only if connection was successful, set the virtual server
                    galaxyTruckerGUI.setVirtualServer(s);
                    // Scene switching is handled by the SocketServerHandler via displayNicknameResult
                } else {
                    // Connection failed, but error message is already shown by SocketServerHandler
                    // No need to switch scenes
                }
            } catch (InterruptedException e) {
                System.out.println("Error in ConnectToServer: " + e.getMessage());
                errorLabel.setText("Error connecting to server: " + e.getMessage());
            }
        } else if(galaxyTruckerGUI.getParameters().getRaw().getFirst().equals("rmi")) {
            // For RMI, we need to switch to the Lobby scene first to ensure controllers are initialized
            // before RMI callbacks can happen
            galaxyTruckerGUI.switchToScene("/it/polimi/ingsw/is25am22new/Lobby.fxml");

            try {
                VirtualServer s = RmiClient.connectToServerRMI_GUI(ipAddress, port, username, GalaxyTruckerGUI.getClientModel(), galaxyTruckerGUI);
                if (s != null) {
                    galaxyTruckerGUI.setVirtualServer(s);
                } else {
                    // If connection fails, go back to connection screen
                    galaxyTruckerGUI.switchToScene("/it/polimi/ingsw/is25am22new/ConnectToServer.fxml");
                    errorLabel.setText("Failed to connect to server");
                }
            } catch (InterruptedException e) {
                System.out.println("Error in ConnectToServer: " + e.getMessage());
                errorLabel.setText("Error connecting to server: " + e.getMessage());
            }
        } else {
            errorLabel.setText("Missing connection type parameter");
        }
    }

    @FXML
    public void showError(String message) {
        errorLabel.setText(message);
    }
}

