package edu.ITSolutions.Export;

import edu.ITSolutions.Export.ui.AllShiftShower;
import edu.ITSolutions.Export.ui.AllShifts;
import edu.ITSolutions.Export.ui.MainUI;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App extends Application {
    private TabPane tabPane;
    private Tab allShiftTab;
    private Tab mainTab;

    public App(TabPane tabPane){
        this.tabPane = tabPane;
    }

    public App(){}
    @Override
    public void start(Stage primaryStage) {
// Create a TabPane
        tabPane = new TabPane();

        // Create a new tab
        mainTab = new Tab();
        
        Label mainTabLabel = new Label("Main");
        mainTabLabel.setMinWidth(150);
        mainTab.setGraphic(mainTabLabel);
        
        // Create an instance of MainUI
        MainUI mainUI = new MainUI(tabPane);
        AllShifts allShifts = new AllShifts();
        AllShiftShower allShiftShower = new AllShiftShower();

        // Set the MainUI layout as the content of the tab
        mainTab.setContent(mainUI.createMainLayout());
        mainTab.setClosable(false); // Make the tab non-closable
        
        // Set the MainUI layout as the content of the tab
        mainTab.setContent(mainUI.createMainLayout());
        
        // Add the tab to the TabPane
        
        allShiftTab = new Tab();

        Label allShiftDisplayLabel = new Label("All Shifts");
        allShiftDisplayLabel.setMinWidth(150);
        allShiftTab.setGraphic(allShiftDisplayLabel);

        allShiftTab.setContent(allShifts.createAllShiftsLayout());
        allShiftTab.setClosable(false);

        tabPane.getTabs().addAll(mainTab,allShiftTab);


        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab){
                if (newTab == allShiftTab){
                    System.out.println("STab: ");//debugging
                    allShiftShower.refreshTables();
                }
            }
        });

        // Optionally, add other tabs if needed
        // Tab anotherTab = new Tab("Another Tab");
        // anotherTab.setContent(new AnotherUI().createLayout());
        // tabPane.getTabs().add(anotherTab);

        Scene scene = new Scene(tabPane, 1200, 800);
        primaryStage.setTitle("Schedule Manager");

        // Load the CSS file and apply it to the scene
        String css = getClass().getResource("/styles.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.out.println("CSS file not found.");
        }

        primaryStage.setScene(scene);

        // primaryStage.setFullScreen(true); -- Uncomment before pushing
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

    public void switchToAllShiftsTab(TabPane tab){
        tab.getSelectionModel().select(1);
    }

    public void switchToMainTab(TabPane tab){
        tab.getSelectionModel().select(0);
    }
}
