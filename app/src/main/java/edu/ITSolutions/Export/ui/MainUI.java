package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;

import edu.ITSolutions.Export.Controller.ScheduleController;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.util.ExcelUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainUI {

    private ExcelUtil excelUtil;
    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final ObservableList<Shift> shiftList = FXCollections.observableArrayList();
    private MemberShiftShower memberShiftShower;
    private ComboBox<Member> memberChoiceBox;

    public VBox createMainLayout() {
        VBox vbox = new VBox();
        GridPane gridPane = new GridPane();

        Label importLabel = new Label("Import Excel File:");
        Button importButton = new Button("Import");

        memberShiftShower = new MemberShiftShower();

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    excelUtil = new ExcelUtil(selectedFile);
                    memberList.setAll(excelUtil.getMembers());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        gridPane.add(importLabel, 0, 0);
        gridPane.add(importButton, 1, 0);

        memberChoiceBox = new ComboBox<>(memberList);
        memberChoiceBox.setOnAction(e -> filterShiftsByMember());
        memberChoiceBox.setConverter(new javafx.util.StringConverter<Member>(){
            @Override
            public String toString(Member member) {
                return member != null ? member.getName() : "";
            }

            @Override
            public Member fromString(String string) {
                return null; // Not needed
            }
        });

        memberChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update shiftList based on the selected member
                shiftList.setAll(excelUtil.getShifts(newValue));
                filterShiftsByMember();

            } else {
                shiftList.clear();
            }
        });


        // Add schedule creation fields
        TextField startDateField = new TextField();
        startDateField.setPromptText("Start Date (YYYY-MM-DD)");

        CustomTimePicker startTimePicker = new CustomTimePicker();

        TextField endDateField = new TextField();
        endDateField.setPromptText("End Date (YYYY-MM-DD)");

        CustomTimePicker endTimePicker = new CustomTimePicker();

        TextField groupField = new TextField();
        groupField.setPromptText("Group");

        TextField themeColorField = new TextField();
        themeColorField.setPromptText("Theme Color");

        Button addShiftButton = new Button("Add Shift");
        addShiftButton.setOnAction(e -> {
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                Shift newShift = new Shift(startDateField.getText(), startTimePicker.getTime(),
                    endDateField.getText(), endTimePicker.getTime(), groupField.getText(), themeColorField.getText(), selectedMember.getName());
                shiftList.add(newShift);
                memberShiftShower.setShiftList(shiftList);
                filterShiftsByMember();
            }
        });

        Button saveScheduleButton = new Button("Save Schedule");
        saveScheduleButton.setOnAction(e -> {
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                saveScheduleForMember(selectedMember);
            }
        });

        gridPane.add(new Label("Start Date:"), 0, 1);
        gridPane.add(startDateField, 1, 1);
        gridPane.add(new Label("Start Time:"), 0, 2);
        gridPane.add(startTimePicker, 1, 2);
        gridPane.add(new Label("End Date:"), 0, 3);
        gridPane.add(endDateField, 1, 3);
        gridPane.add(new Label("End Time:"), 0, 4);
        gridPane.add(endTimePicker, 1, 4);
        gridPane.add(new Label("Group:"), 0, 5);
        gridPane.add(groupField, 1, 5);
        gridPane.add(new Label("Theme Color:"), 0, 6);
        gridPane.add(themeColorField, 1, 6);
        gridPane.add(addShiftButton, 1, 7);
        gridPane.add(saveScheduleButton, 1, 8);

        memberShiftShower.setShiftList(shiftList);

        vbox.getChildren().addAll(gridPane, memberChoiceBox, memberShiftShower);
        return vbox;
    }

    private void saveScheduleForMember(Member member) {
        if (excelUtil != null) {
            ScheduleController scheduleController = new ScheduleController(excelUtil.getWorkbook());
            for (Shift shift : shiftList) {
                scheduleController.addSchedule(member.getName(), member.getEmail(), shift.getGroup(), 
                    shift.getStartDate(), shift.getStartTime(), shift.getEndDate(), shift.getEndTime(), shift.getThemeColor());
            }
            try {
                excelUtil.save(); // Call save() without arguments
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterShiftsByMember() {
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            ObservableList<Shift> filteredShifts = shiftList.filtered(shift -> shift.getMember().equals(selectedMember.getName()));
            memberShiftShower.setShiftList(filteredShifts);
            memberShiftShower.refreshTable();
        }
    }
}
