package edu.ITSolutions.Export;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a root node (empty container)
        StackPane root = new StackPane();

        // Create a scene with the root node
        Scene scene = new Scene(root, 800, 600);

        // Set the stage title
        primaryStage.setTitle("Full Screen Window");

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Set the stage to full screen
        primaryStage.setFullScreen(true);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
