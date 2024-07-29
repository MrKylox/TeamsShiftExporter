package edu.ITSolutions.Export.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ITSolutions.Export.App;
import edu.ITSolutions.Export.App.appContext;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GroupShift {
    private static VBox vbox;
    private static HBox hbox;
    private static HBox hboxLeft;
    private static HBox groupSelectBox;
    private static AllShiftShower allShiftShower;
    private static ProfilesUtil profilesUtil;
    private ObservableList<Shift> selectedShifts = FXCollections.observableArrayList();
    private static MainUI mainUI;
    private static App app;
    private static MemberSelectionUI memberSelectionUI;


public GroupShift() { // Modify constructor to accept File parameter
        try {
            allShiftShower = new AllShiftShower();
            app = new App();
            profilesUtil = new ProfilesUtil();
            mainUI = new MainUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createGroupShiftLayout() {
        vbox = new VBox();
        hbox = new HBox();
        hboxLeft = new HBox();
        groupSelectBox = new HBox();
        List<String> selectedGroup = new ArrayList<>(); // initiate selected group list
        
        Button selectMembers = new Button("Select Members");

        selectMembers.setOnAction(e -> {
            memberSelectionUI = new MemberSelectionUI(mainUI.getMembers()); //initiate member selection table importing memberList
            System.out.println("Received Members: "+mainUI.getMembers());
            groupSelectBox = memberSelectionUI.createMemberSelectionLayout(); // create the boxes 
        });
         

        // memberSelectionUI.getSelectedMembers().forEach(selectedGroup::add); // get the selected members and add it to the selected Group list

        hbox.setAlignment(Pos.CENTER); // hbox being centered

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("allShiftTabButtons");

        confirmButton.setOnAction(e -> {
            mainUI.generateShiftForGroup(selectedGroup, mainUI.getMembers());
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("allShiftTabButtons");

        cancelButton.setOnAction(e -> {
            app.switchToMainTab(appContext.getTabPane(),appContext.getMainTab());
        });
        
        hboxLeft.getChildren().setAll(confirmButton,cancelButton);
        hboxLeft.setSpacing(50);

        hboxLeft.setAlignment(Pos.CENTER);

        Insets insetVbox = new Insets(20,20,20,20);
        vbox.setPadding(insetVbox);
        vbox.setSpacing(50);

        hbox.getChildren().addAll(groupSelectBox);
        vbox.getChildren().setAll(selectMembers,hbox,hboxLeft);
        vbox.setPadding(insetVbox);
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.TOP_CENTER);


        return vbox;
    }

    public ObservableList<Shift> getSelectedMemberShifts(List<String> selectedMembers) {
        selectedShifts.clear();

        for (String name : selectedMembers) {
            selectedShifts.addAll(profilesUtil.getProfileShifts(name));
        }

        allShiftShower.setAllShiftList(selectedShifts);
        hbox.getChildren().setAll(allShiftShower);

        return selectedShifts;
    }


    private void generateShiftForGroup(List<String> selectedGroup, ObservableList<Member> memberList) {
        for (Member member : memberList) {
            if (selectedGroup.contains(member.getName())) {
                // Implement the method to generate shifts for each member
            }
        }
    }
}

/*
    List<String> selectedGroup = new ArrayList<>();
    memberSelectionUI.getSelectedMembers().forEach(selectedGroup::add);

    if (!selectedGroup.isEmpty()) {
        String selectedMembers = selectedGroup.stream()
                .collect(Collectors.joining(", "));

        // Create the confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Generation");
        alert.setHeaderText("Selected Members for Shift Generation");
        alert.setContentText("Are you sure you want to generate shifts for the following members?\n" + selectedMembers);

        // Set up the confirmation dialog buttons
        ButtonType confirmButton = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Show the dialog and wait for response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            generateShiftForGroup(selectedGroup, memberList);
            System.out.println("Shifts generated for selected members.");
        } else {
            System.out.println("Shift generation canceled.");
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Members Selected");
        alert.setHeaderText("Please select at least one member");
        alert.showAndWait();
    } 

 */