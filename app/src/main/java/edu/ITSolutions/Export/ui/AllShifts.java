package edu.ITSolutions.Export.ui;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import edu.ITSolutions.Export.App;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.App.appContext;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class AllShifts{

    private static VBox vbox;
    private static HBox hbox;
    private static HBox buttonContainer;
    private static AllShiftShower allShiftShower;
    private static ProfilesUtil profilesUtil;
    private static MainUI mainUI;
    private static App app;

    //buttons
    private Button confirmButton;
    private Button cancelButton;
    private Button doneButton;

    public AllShifts(){
        try {
            allShiftShower = new AllShiftShower();
            app = new App();
            profilesUtil = new ProfilesUtil();
            mainUI = new MainUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createAllShiftsLayout(){
        vbox = new VBox();
        hbox = new HBox();
        buttonContainer = new HBox();
        allShiftShower.setAllShiftList(getAllShifts());//list of shifts found
        hbox.getChildren().setAll(allShiftShower);//displays every shift in list
        allShiftShower.refreshTables();
        hbox.setAlignment(Pos.CENTER);

        //-----------------------buttons--------------------------//
        confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("allShiftTabButtons");

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("allShiftTabButtons");

        doneButton = new Button("Done");
        doneButton.getStyleClass().add("allShiftTabButtons");
        //--------------------------------------------------------//

        //---------------------button actions---------------------//
        confirmButton.setOnAction(e -> {
            mainUI.generateShiftsForAllMembers();
            showDoneButton();
            appContext.setConfirmed(true);

        });

        cancelButton.setOnAction(e -> {
            app.switchToMainTab(appContext.getTabPane(),appContext.getAllShiftTab());
        });
        
        doneButton.setOnAction(e -> {
            app.switchToMainTab(appContext.getTabPane(), appContext.getAllShiftTab());
            showConfirmCancelButtons();
        });
        //-------------------------------------------------------//

        buttonContainer.getChildren().setAll(confirmButton,cancelButton);//storing in button container
        buttonContainer.setSpacing(50);

        buttonContainer.setAlignment(Pos.CENTER);

        Insets insetVbox = new Insets(20,20,20,20);
        vbox.getChildren().setAll(hbox,buttonContainer);
        vbox.setPadding(insetVbox);
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.TOP_CENTER);
        return vbox;
    }

    //displays confirm and cancel buttons
    public void showConfirmCancelButtons(){
        buttonContainer.getChildren().setAll(confirmButton,cancelButton);
    }

    //displays done button
    public void showDoneButton(){
        buttonContainer.getChildren().setAll(doneButton);
    }

    //recieves existing list to update the table
    public ObservableList<Shift> getAllShifts(){
        Sheet memberShiftSheet = profilesUtil.getMemeberSheet();
        ObservableList<String> allMemberNameList = FXCollections.observableArrayList();
        ObservableList<Shift> allMemberShiftList = FXCollections.observableArrayList();

        for(Row row : memberShiftSheet){
            if (row.getRowNum() == 0) continue; // Skip header row
            String memberName = row.getCell(0).getStringCellValue(); //get the memberName
            if(!allMemberNameList.contains(memberName)){
                allMemberNameList.add(memberName);
            }
        }
        
        for(String name : allMemberNameList){
            allMemberShiftList.addAll(profilesUtil.getProfileShifts(name));
        }

        allShiftShower.setAllShiftList(allMemberShiftList);
        hbox.getChildren().setAll(allShiftShower);

        return allMemberShiftList;
    }
}
