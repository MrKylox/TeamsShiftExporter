package edu.ITSolutions.Export;

import edu.ITSolutions.Export.ui.AllShiftShower;
import edu.ITSolutions.Export.ui.AllShifts;
import edu.ITSolutions.Export.ui.MainUI;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App extends Application {
    private TabPane tabPane;
    private Tab allShiftTab;
    private Tab mainTab;

    @Override
    public void start(Stage primaryStage) {
// Create a TabPane
        tabPane = new TabPane();

        // Create a new tab
        mainTab = new Tab();
        
        Label mainTabLabel = new Label("Main");
        mainTabLabel.setMinWidth(150);
        mainTab.setGraphic(mainTabLabel);
        

        AllShifts allShifts = new AllShifts();
        AllShiftShower allShiftShower = new AllShiftShower();

        // Add the tab to the TabPane
        
        allShiftTab = new Tab();

        Label allShiftDisplayLabel = new Label("All Shifts");
        allShiftDisplayLabel.setMinWidth(150);
        allShiftTab.setGraphic(allShiftDisplayLabel);

        allShiftTab.setContent(allShifts.createAllShiftsLayout());
        allShiftTab.setClosable(false);

        // Create an instance of MainUI
        MainUI mainUI = new MainUI();

        // Set the MainUI layout as the content of the tab
        mainTab.setContent(mainUI.createMainLayout());
        mainTab.setClosable(false); // Make the tab non-closable
        
        // Set the MainUI layout as the content of the tab
        mainTab.setContent(mainUI.createMainLayout());

        appContext.setTabPane(tabPane);
        appContext.setAllShiftTab(allShiftTab);
        appContext.setMainTab(mainTab);

        tabPane.getTabs().addAll(mainTab);


        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab){

                if (newTab == mainTab){
                    System.out.println("MainTab clicked");

                    if(appContext.getConfirmed()){
                        System.out.println("Confirmed");
                        appContext.setConfirmed(false);
                        // switchToMainTab(appContext.getTabPane(), appContext.getAllShiftTab());
                        return;
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("Generated Shifts:");

                        alert.showAndWait().ifPresent(response -> {
                            if(response == ButtonType.OK){
                                // appContext.setConfirmed(true);
                                System.out.println("Ok Clicked");
                                appContext.setConfirmed(false);
                            } 
                            else{
                                appContext.getTabPane().getSelectionModel().select(appContext.getAllShiftTab());
                                appContext.setConfirmed(false);
                            }
                        });
                    }
                    
                    // switchToMainTab(appContext.getTabPane(), appContext.getAllShiftTab());
                }
            

                // tabPane.getSelectionModel().selectedItemProperty().addListener(this);
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
            System.out.println("added css file");
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

    public static class appContext{
        private static TabPane tabPane; 
        private static Tab allShiftTab;
        private static Tab main;
        private static boolean confirmed = false;

        public static void setTabPane(TabPane tabPane){
            appContext.tabPane = tabPane;
        }

        public static TabPane getTabPane(){
            return tabPane;
        }

        public static void setAllShiftTab(Tab newTab){
            appContext.allShiftTab = newTab;
        }

        public static Tab getAllShiftTab(){
            return allShiftTab;
        }

        public static void setMainTab(Tab newTab){
            appContext.main = newTab;
        }

        public static Tab getMainTab(){
            return main;
        }

        public static void setConfirmed(Boolean confirmed) {
            appContext.confirmed = confirmed;
        }

        public static Boolean getConfirmed(){
            return confirmed;
        }

    }

    public void switchToAllShiftsTab(TabPane tabPane, Tab allShiftTab){
        if(tabPane == null ){
            System.err.println("TabPane is null");
            return;
        }

        if(allShiftTab == null){
            System.err.println(" allshift tab is null");
            return; 
        }
        
        if(!tabPane.getTabs().contains(allShiftTab)){
            tabPane.getTabs().add(allShiftTab);
        }

        tabPane.getSelectionModel().clearAndSelect(1);

    }

    public void switchToMainTab(TabPane tabPane, Tab mainTab){
        if(tabPane == null ){
            System.err.println("TabPane is null");
            return;
        }

        if(mainTab == null){
            System.err.println(" mainTab tab is null");
            return; 
        }
        
        if(!tabPane.getTabs().contains(mainTab)){
            tabPane.getTabs().add(mainTab);
        }

        removeTab(tabPane, appContext.getAllShiftTab());
    }
    public void removeTab(TabPane tabPane, Tab tab){
        if(tabPane.getTabs().contains(tab)){
            tabPane.getTabs().remove(tab);
        }
    }
    
}
