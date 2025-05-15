package it.polimi.ingsw.is25am22new.FXMLControllers;

import it.polimi.ingsw.is25am22new.FXMLControllers.GalaxyBackground;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PreviewStartMenu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a modified version of the FXML loader that doesn't try to use custom components
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/is25am22new/StartMenu.fxml"));

        // Load the FXML content
        Parent root = loader.load();

        // If the FXML loading was successful
        if (root instanceof StackPane) {
            StackPane stackPane = (StackPane) root;

            // Create and add the galaxy background programmatically as the first child
            GalaxyBackground galaxyBackground = new GalaxyBackground(854, 480);
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

        Scene scene = new Scene(root, 854, 480);
        primaryStage.setTitle("Preview - StartMenu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}