package it.polimi.ingsw.is25am22new;

import it.polimi.ingsw.is25am22new.FXMLControllers.GalaxyBackground;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GalaxyTruckerGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create a modified version of the FXML loader that doesn't try to use custom components
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));

        // Load the FXML content
        Parent root = loader.load();

        // If the FXML loading was successful
        if (root instanceof StackPane) {
            StackPane stackPane = (StackPane) root;

            // Create and add the galaxy background programmatically as the first child
            GalaxyBackground galaxyBackground = new GalaxyBackground(1280, 720);
            stackPane.getChildren().add(0, galaxyBackground);

            // Get the controller and set the background reference
            try {
                it.polimi.ingsw.is25am22new.FXMLControllers.StartMenuController controller =
                        loader.getController();
                controller.setGalaxyBackground(galaxyBackground);
            } catch (Exception e) {
                System.out.println("Warning: Could not set galaxy background in controller");
            }
        }

        Scene scene = new Scene(root, 1280, 720);
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/is25am22new/Graphics/Icon.png")).toString());
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}