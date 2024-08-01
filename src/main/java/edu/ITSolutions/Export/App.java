package edu.ITSolutions.Export;
import edu.ITSolutions.Export.ui.AllShifts;
import edu.ITSolutions.Export.ui.GroupShift;
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
    private Tab groupShiftTab;

    @Override
    public void start(Stage primaryStage) {
// Create a TabPane
        tabPane = new TabPane();

        // Create a new tab
        mainTab = new Tab();
        allShiftTab = new Tab();
        groupShiftTab = new Tab();

        // Create an instance of UI
        MainUI mainUI = new MainUI();
        AllShifts allShifts = new AllShifts();

        // AllShiftShower allShiftShower = new AllShiftShower();
        // AllShiftShower allShiftShower = new AllShiftShower();

        // Add the tab to the TabPane
        
        Label mainTabLabel = new Label("Main"); // create label called Main
        mainTabLabel.setMinWidth(150);
        mainTab.setGraphic(mainTabLabel); // set the name of the tab

        Label allShiftDisplayLabel = new Label("All Shifts"); // create label called All Shifts
        allShiftDisplayLabel.setMinWidth(150);
        allShiftTab.setGraphic(allShiftDisplayLabel); // Set the name of the tab

        Label groupShiftDisplayLabel = new Label("Group Shifts"); // create label called Group Shifts
        groupShiftDisplayLabel.setMinWidth(150);
        groupShiftTab.setGraphic(groupShiftDisplayLabel); // Set the name of the tab

        // Set the UI layout as the content of the tab
        allShiftTab.setContent(allShifts.createAllShiftsLayout());
        allShiftTab.setClosable(false); // Make the tab non-closable

        mainTab.setContent(mainUI.createMainLayout());
        mainTab.setClosable(false); // Make the tab non-closable

        // Initialize GroupShift only after MainUI creates sharedFile

        groupShiftTab.setClosable(false);

        appContext.setTabPane(tabPane);
        appContext.setAllShiftTab(allShiftTab);
        appContext.setMainTab(mainTab);
        appContext.setGroupShiftTab(groupShiftTab);

        // Add the tab to the TabPane
        tabPane.getTabs().addAll(mainTab);

        //detects when tab options are clicked
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab){
                //if main tab is clicked
                if (newTab == mainTab){
                    System.out.println("MainTab clicked");
                    //if confirmed then close the all shifts
                    if(appContext.getConfirmed()){
                        System.out.println("Confirm clicked and now closing current tab...");
                        appContext.setConfirmed(false);
                        removeTab(tabPane,oldTab);//removes the old tab(all shifts tab) from view
                        allShifts.showConfirmCancelButtons();//resets the button positions from previous tab
                        return;
                    }
                    else{
                        //sends an alert message to confirm whether user wants to proceed with their actions
                        //cancels the action of generating and saving shifts for all members
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("This will cancel generating shifts!!");
                        alert.setContentText("Would you like to proceed?");

                        //wait until response is received
                        alert.showAndWait().ifPresent(response -> {
                            //if response is ok, go back to main tab 
                            if(response == ButtonType.OK){
                                // appContext.setConfirmed(true);
                                System.out.println("Ok Clicked");
                                appContext.setConfirmed(false);
                                removeTab(tabPane,oldTab);
                            } 
                            //cancels action
                            else{
                                appContext.getTabPane().getSelectionModel().select(oldTab);
                                // removeTab(tabPane,oldTab);
                                appContext.setConfirmed(false);
                            }
                        });
                    }
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
        private static Tab groupShiftTab;
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

        public static void setGroupShiftTab(Tab newTab){
            appContext.groupShiftTab = newTab;
        }

        public static Tab getGroupShiftTab(){
            return groupShiftTab;
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

    public void switchToGroupShiftsTab(TabPane tabPane, Tab groupShiftTab){
        if(tabPane == null ){
            System.err.println("TabPane is null");
            return;
        }

        if(groupShiftTab == null){
            System.err.println(" Groupshift tab is null");
            return; 
        }
        
        GroupShift groupShift = new GroupShift(); // Initialize with shared file
        groupShiftTab.setContent(groupShift.createGroupShiftLayout());
        
        if(!tabPane.getTabs().contains(groupShiftTab)){
            System.out.println("Switched to group shift tab");
            tabPane.getTabs().add(groupShiftTab);
        }

        tabPane.getSelectionModel().clearAndSelect(1);

    }

    public void switchToMainTab(TabPane tabPane, Tab mainTab, Tab currentTab){
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
        appContext.setConfirmed(true);
        removeTab(tabPane, currentTab);
    }
    
    public void removeTab(TabPane tabPane, Tab tab){
        if(tabPane.getTabs().contains(tab)){
            tabPane.getTabs().remove(tab);
        }
    }
    
}
