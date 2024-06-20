package edu.ITSolutions.Export;

import edu.ITSolutions.Export.ui.MainUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        MainUI mainUI = new MainUI();
        root.getChildren().add(mainUI.createMainLayout());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Schedule Manager");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
