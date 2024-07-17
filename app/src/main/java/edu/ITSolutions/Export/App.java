package edu.ITSolutions.Export;

import edu.ITSolutions.Export.ui.MainUI;
import edu.ITSolutions.Export.util.ProfilesUtil;
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
        
        // Load the CSS file and apply it to the scene
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); 
        primaryStage.setScene(scene);

        // primaryStage.setFullScreen(true); -- UNcomment before pushing
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        ProfilesUtil.createDirectoryIfNotExists();

        if (!ProfilesUtil.doesFileExist()) {
            ProfilesUtil.createExcelFile();
            System.out.println("Excel file created.");
        } else {
            System.out.println("Excel file already exists. Reading data...");
        }
        launch(args);
    }
}
