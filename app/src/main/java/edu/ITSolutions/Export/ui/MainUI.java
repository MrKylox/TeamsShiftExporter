package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ITSolutions.Export.Controller.ScheduleController;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.util.ExcelUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainUI {
    // ---------------------------Variable Declarer -------------------------------------
    private ExcelUtil excelUtil;
    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final ObservableList<Shift> shiftList = FXCollections.observableArrayList();
    private MemberShiftShower memberShiftShower;
    private ComboBox<Member> memberChoiceBox;
    private final CheckBox[] dayCheckBoxes = new CheckBox[7];
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    CustomTimePicker startTimePicker;
    CustomTimePicker endTimePicker; 
    private TextField themeColorField = new TextField();
    private TextField groupField = new TextField();

    //---------------------------VBox Creator ------------------------------------------
    public VBox createMainLayout() {
        // instantiate vbox and Gridpane
        VBox vbox = new VBox();
        GridPane gridPane = new GridPane();

        // Creating Excel Labels and import button
        Label importLabel = new Label("Import Excel File:");
        Button importButton = new Button("Import");

        // Instantiating memberShiftShower
        memberShiftShower = new MemberShiftShower();

        // Setting action on import button in order to select a file
        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser(); // instantiating file chooser
            File selectedFile = fileChooser.showOpenDialog(new Stage());// choose a file to import and get the file
            if (selectedFile != null) {
                try {
                    excelUtil = new ExcelUtil(selectedFile); // Use the selected file to utilize the imported file
                    memberList.setAll(excelUtil.getMembers()); // Retrieve the members from the excel file
                    System.out.printf("File imported");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        gridPane.add(importLabel, 0, 0); // Adds the label to the grid Pane
        gridPane.add(importButton, 1, 0); // Adds the import button to the grid pane

        memberChoiceBox = new ComboBox<>(memberList); // assign the members List to the combo box
        memberChoiceBox.setOnAction(e -> filterShiftsByMember()); //filter shifts by the selected member

        // Day selection checkboxes
        dayCheckBoxes[0] = new CheckBox("Monday");
        dayCheckBoxes[1] = new CheckBox("Tuesday");
        dayCheckBoxes[2] = new CheckBox("Wednesday");
        dayCheckBoxes[3] = new CheckBox("Thursday");
        dayCheckBoxes[4] = new CheckBox("Friday");
        dayCheckBoxes[5] = new CheckBox("Saturday");
        dayCheckBoxes[6] = new CheckBox("Sunday");

        

        // Date pickers
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        memberChoiceBox.setConverter(new javafx.util.StringConverter<Member>(){ 
            @Override
            public String toString(Member member) { // retrieves the member is selected
                return member != null ? member.getName() : ""; // sets the name ofmember to a string
            }

            @Override
            public Member fromString(String string) {
                return null; // Not needed
            }
        });

        memberChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                shiftList.setAll(excelUtil.getShifts(newValue)); //sets the shifts list to the new shifts list
                filterShiftsByMember(); // then filter the shift by the selected member

            } else {
                shiftList.clear();
            }
        });


        HBox daysBox = new HBox(dayCheckBoxes);
        HBox datesBox = new HBox(new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker);

        // New input fields using CustomTimePicker
        startTimePicker = new CustomTimePicker();
        endTimePicker = new CustomTimePicker();
        
        themeColorField = new TextField();
        themeColorField.setPromptText("Theme Color");

        groupField = new TextField();
        groupField.setPromptText("Group");

        HBox inputBox = new HBox(new Label("Start Time:"), startTimePicker, new Label("End Time:"), endTimePicker, 
                                 new Label("Group:"), groupField, new Label("Theme Color:"), themeColorField);

        memberShiftShower.setShiftList(shiftList);

        Button generateShiftsButton = new Button("Generate Shifts");
        generateShiftsButton.setOnAction(e -> {
            generateShiftsForSelectedDays();
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                saveScheduleForMember(selectedMember); // Save shifts to the Excel file
            }
        });

        vbox.getChildren().addAll(gridPane, memberChoiceBox, daysBox, datesBox, inputBox, memberShiftShower, generateShiftsButton);



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
                excelUtil.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterShiftsByMember() {
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem(); // Get the selected member from the combo box
        if (selectedMember != null) {
            ObservableList<Shift> filteredShifts = shiftList.filtered(shift -> shift.getMember().equals(selectedMember.getName())); // go through the shifts and get the selected member shifts
            memberShiftShower.setShiftList(filteredShifts); // Clear the current shift list and we assign it to filtered shifts
            memberShiftShower.refreshTable(); // refresh the table viewer of the shifts
        }
    }

        private void generateShiftsForSelectedDays() {
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String startTime = startTimePicker.getTime();
            String endTime = endTimePicker.getTime();
            String group = groupField.getText();
            String themeColor = themeColorField.getText();

            if (startDate != null && endDate != null && !startDate.isAfter(endDate) &&
            startTime != null && !startTime.isEmpty() &&
            endTime != null && !endTime.isEmpty() &&
            group != null && !group.isEmpty() &&
            themeColor != null && !themeColor.isEmpty()) {
                List<DayOfWeek> selectedDays = new ArrayList<>();
                if (dayCheckBoxes[0].isSelected()) selectedDays.add(DayOfWeek.MONDAY);
                if (dayCheckBoxes[1].isSelected()) selectedDays.add(DayOfWeek.TUESDAY);
                if (dayCheckBoxes[2].isSelected()) selectedDays.add(DayOfWeek.WEDNESDAY);
                if (dayCheckBoxes[3].isSelected()) selectedDays.add(DayOfWeek.THURSDAY);
                if (dayCheckBoxes[4].isSelected()) selectedDays.add(DayOfWeek.FRIDAY);
                if (dayCheckBoxes[5].isSelected()) selectedDays.add(DayOfWeek.SATURDAY);
                if (dayCheckBoxes[6].isSelected()) selectedDays.add(DayOfWeek.SUNDAY);

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (selectedDays.contains(date.getDayOfWeek())) {
                        Shift newShift = new Shift(date.toString(), startTime, date.toString(), endTime, group, themeColor, selectedMember.getName());
                        shiftList.add(newShift);
                    }
                }
                filterShiftsByMember();
            }
        }
    }
}
