package edu.ITSolutions.Export.ui;

import java.io.IOException;
import java.util.List;

import edu.ITSolutions.Export.App;
import edu.ITSolutions.Export.App.appContext;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GroupShift {
    private static VBox vbox;
    private static HBox hbox;
    private static HBox buttonContainer;
    private static VBox groupSelectBox;
    private static VBox shiftDisplayer;
    private static AllShiftShower allShiftShower;
    private static ProfilesUtil profilesUtil;
    private static ObservableList<Shift> selectedShifts = FXCollections.observableArrayList();
    private static MainUI mainUI;
    private static App app;
    private static MemberSelectionUI memberSelectionUI;
    private static List<String> selectedGroup;


public GroupShift() { // Modify constructor to accept File parameter
        try {
            allShiftShower = new AllShiftShower();
            app = new App();
            profilesUtil = new ProfilesUtil();
            mainUI = new MainUI();
            memberSelectionUI = new MemberSelectionUI(mainUI.getMembers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createGroupShiftLayout() {
        vbox = new VBox();
        hbox = new HBox();
        buttonContainer = new HBox();
        groupSelectBox = new VBox();
        shiftDisplayer = new VBox();

        Label membersTitle = new Label("Members List");
        Label selectedShiftsTitle = new Label("Selected Member Shifts");

        selectedGroup = memberSelectionUI.getSelectedMembers(); // initiate selected group list

        allShiftShower.setAllShiftList(getSelectedMemberShifts(selectedGroup));

        groupSelectBox.getChildren().addAll(membersTitle, memberSelectionUI);
        groupSelectBox.setAlignment(Pos.CENTER);
        groupSelectBox.setSpacing(16);

        shiftDisplayer.getChildren().addAll(selectedShiftsTitle, allShiftShower);
        shiftDisplayer.setAlignment(Pos.CENTER);
        shiftDisplayer.setSpacing(16);


        hbox.getChildren().setAll(groupSelectBox,shiftDisplayer);
        hbox.setSpacing(30);
        hbox.setAlignment(Pos.TOP_CENTER); // hbox being centered

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("allShiftTabButtons");

        confirmButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("This will overwrite any shifts in imported file");
            alert.setContentText("Would you like to proceed?");

            alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.OK){
                    mainUI.generateShiftForGroup(selectedGroup);
                    app.switchToMainTab(appContext.getTabPane(), appContext.getMainTab(), appContext.getGroupShiftTab());
                } 
            });
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("allShiftTabButtons");

        cancelButton.setOnAction(e -> {
            app.switchToMainTab(appContext.getTabPane(), appContext.getMainTab(), appContext.getGroupShiftTab());
        });
        
        buttonContainer.getChildren().setAll(confirmButton,cancelButton);
        buttonContainer.setSpacing(50);

        buttonContainer.setAlignment(Pos.CENTER);

        Insets insetVbox = new Insets(20,20,20,20);
        vbox.setPadding(insetVbox);
        vbox.setSpacing(50);

        vbox.getChildren().setAll(hbox,buttonContainer);
        vbox.setPadding(insetVbox);
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.BASELINE_CENTER);


        return vbox;
    }

    public static ObservableList<Shift> getSelectedMemberShifts(List<String> selectedMembers) {
        selectedShifts.clear();

        for (String name : selectedMembers) {
            selectedShifts.addAll(profilesUtil.getProfileShifts(name));
        }

        return selectedShifts;
    }

    public static void refreshMemberList(){
        selectedGroup = memberSelectionUI.getSelectedMembers();
        selectedShifts = getSelectedMemberShifts(selectedGroup);
        allShiftShower.setAllShiftList(getSelectedMemberShifts(selectedGroup));
        allShiftShower.refreshTables();
    }
}
