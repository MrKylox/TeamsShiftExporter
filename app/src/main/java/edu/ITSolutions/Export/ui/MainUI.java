package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ITSolutions.Export.Controller.ProfileController;
import edu.ITSolutions.Export.Controller.ScheduleController;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.util.ExcelUtil;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainUI {
    private ExcelUtil excelUtil;
    private ProfilesUtil profilesUtil;
    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final ObservableList<Shift> shiftList = FXCollections.observableArrayList();
    private MemberShiftShower memberShiftShower;
    private ComboBox<Member> memberChoiceBox;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private CustomTimePicker startTimePicker;
    private CustomTimePicker endTimePicker;
    private DayOfWeekUI dayOfWeekUI;
    private SeasonUI seasonUI;
    private PositionUI positionUI;
    private ProfileController profileController;


    public MainUI() {
        try {
            profilesUtil = new ProfilesUtil();
            profileController = new ProfileController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createMainLayout() {
        VBox vbox = new VBox();
        VBox rVBox = new VBox();
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();

        Label importLabel = new Label("Import Excel File:");

        Button importButton = new Button("Import");
        Button SaveProfileButton = new Button("Save Shift");
        Button deleteShiftButton = new Button("Delete Shift");
        Button generateIndividualShiftButton = new Button("Generate Shift For Selected Member");
        Button generateGroupShiftButton = new Button("Generate Shifts For Selected Group");
        Button generateAllShiftsButton = new Button("Generate Shifts For All Members");
        Button editSeasonsButton = new Button("Edit Season");
        Button saveSeasonButton = new Button("Save Season");

        memberShiftShower = new MemberShiftShower();
        dayOfWeekUI = new DayOfWeekUI();

        gridPane.add(importLabel, 0, 0);
        gridPane.add(importButton, 1, 0);

        memberShiftShower.setShiftList(shiftList);
        memberChoiceBox = new ComboBox<>(memberList);
        memberChoiceBox.setOnAction(e -> updateShiftList());

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        seasonUI = new SeasonUI();
        positionUI = new PositionUI();
        startTimePicker = new CustomTimePicker();
        endTimePicker = new CustomTimePicker();
        Label startDateLabel = new Label("Start Date:");
        Label endDateLabel = new Label("End Date:");

        saveSeasonButton.setVisible(false);
        startDatePicker.setVisible(false);
        endDatePicker.setVisible(false);
        startDateLabel.setVisible(false);
        endDateLabel.setVisible(false);

        Label seasonStartAndEnd = new Label();

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    excelUtil = new ExcelUtil(selectedFile);
                    memberList.setAll(excelUtil.getMembers());
                    System.out.println("File imported");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        memberChoiceBox.setConverter(new javafx.util.StringConverter<Member>() {
            @Override
            public String toString(Member member) {
                return member != null ? member.getName() : "";
            }

            @Override
            public Member fromString(String string) {
                return null;
            }
        });

        memberChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (profilesUtil != null) {
                    updateShiftList();
                }
 
            } else {
                shiftList.clear();
            }
        });

        seasonUI.getSeasonComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<LocalDate> receivedDates = profilesUtil.getSeasonDates(newValue);
                if (!receivedDates.isEmpty()) {
                    seasonStartAndEnd.setText(receivedDates.get(0) + " - " + receivedDates.get(1));
                }

                // Handle season selection change if needed
                System.out.println("Season selected: " + newValue);
            }
        });

        editSeasonsButton.setOnAction(e -> {
            saveSeasonButton.setVisible(true);
            startDatePicker.setVisible(true);
            endDatePicker.setVisible(true);
            startDateLabel.setVisible(true);
            endDateLabel.setVisible(true);
            editSeasonsButton.setVisible(false);
            seasonStartAndEnd.setVisible(false);
        });

        saveSeasonButton.setOnAction(e -> {
            String selectedSeason = seasonUI.getSeasonComboBox().getSelectionModel().getSelectedItem();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (selectedSeason != null && startDate != null && endDate != null) {
                profilesUtil.saveSeason(selectedSeason, startDate, endDate);
                List<LocalDate> receivedDates = profilesUtil.getSeasonDates(selectedSeason);
                if (!receivedDates.isEmpty()) {
                    seasonStartAndEnd.setText(receivedDates.get(0) + " - " + receivedDates.get(1));
                }
            }
            startDatePicker.setVisible(false);
            endDatePicker.setVisible(false);
            startDateLabel.setVisible(false);
            endDateLabel.setVisible(false);
            saveSeasonButton.setVisible(false);
            editSeasonsButton.setVisible(true);
            seasonStartAndEnd.setVisible(true);
        });

        SaveProfileButton.setOnAction(e -> {
            try {
                profilesUtil = new ProfilesUtil();
                System.out.println("Profile save initated");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                String selectedDay = dayOfWeekUI.getSelectedDay();
                if (selectedDay != null) {
                    profilesUtil.saveProfile(selectedMember.getName(), selectedDay, startTimePicker.getTime(),
                                            endTimePicker.getTime(), positionUI.getPosition(),
                                            seasonUI.getSeason());
                    System.out.println("Profile saved");
                }
            }
            updateShiftList();
        });
        
        deleteShiftButton.setOnAction(e -> {
            Shift selectedShift = memberShiftShower.getSelectedShift();
            if (selectedShift != null) {
                profilesUtil.deleteShift(selectedShift);
                updateShiftList();
                System.out.println("Shift deleted");
            } else {
                System.out.println("No shift selected");
            }
        });
        
        generateIndividualShiftButton.setOnAction(e -> {
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                generateShiftForSelectedMember(selectedMember);
            }
        });

        generateGroupShiftButton.setOnAction(e -> {
            //Still need to implement 
        });

        generateAllShiftsButton.setOnAction(e -> {
            generateShiftsForAllMembers();
        });

        // StackPane to overlay buttons
        StackPane toggleButtonsPane = new StackPane(editSeasonsButton, saveSeasonButton);

        HBox profileBox = new HBox(SaveProfileButton, deleteShiftButton); 

        HBox inputBox = new HBox(new Label("Start Time:"), startTimePicker, new Label("End Time:"), endTimePicker,
        new Label("Position:"), positionUI );

        HBox datesBox = new HBox(new Label("Season"), seasonUI, toggleButtonsPane, startDateLabel, startDatePicker, endDateLabel, endDatePicker);

        vbox.getChildren().addAll(gridPane, memberChoiceBox, dayOfWeekUI, seasonStartAndEnd, datesBox, inputBox, profileBox, memberShiftShower, generateIndividualShiftButton, generateGroupShiftButton, generateAllShiftsButton);

        return vbox;
    }

    private void updateShiftList() {
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedMember != null && profilesUtil != null) {
            shiftList.setAll(profilesUtil.getProfileShifts(selectedMember.getName()));
            
            ObservableList<Shift> newShifts = profileController.sortList(shiftList);
            memberShiftShower.setShiftList(newShifts);
            memberShiftShower.refreshTables();
            // System.out.println("Refreshed list"); debugging to check if refreshing the table worked
        }
    }

    // private void generateShiftsForSelectedGroup(){

    // }

    private void generateShiftsForAllMembers() {
        List<Member> allMembers = memberList;  // Use the member list from your class
        ScheduleController scheduleController = null;
        
        try {
            profilesUtil = new ProfilesUtil();
            scheduleController = new ScheduleController(excelUtil.getWorkbook());
            excelUtil.clearSheetExceptHeader();
            System.out.println("Schedule Creation Started");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (scheduleController != null) {
            for (Member member : allMembers) {
                List<LocalDate> receivedDates = new ArrayList<>();
                List<Shift> receivedSchedule = profilesUtil.getSchedule(member.getName());

                for (Shift shift : receivedSchedule) {
                    receivedDates = profilesUtil.getSeasonDates(shift.getSeason());
                    String startTime = shift.getStartTime();
                    String endTime = shift.getEndTime();
                    String position = shift.getPosition();
                    String selectedDay = shift.getWeekDay();

                    if (receivedDates.size() >= 2) {  // Ensure we have both start and end dates
                        System.out.println("Received dates is greater than 2 for member: " + member.getName());
                        LocalDate startDate = receivedDates.get(0);
                        LocalDate endDate = receivedDates.get(1);

                        if (!startDate.isAfter(endDate) &&
                                startTime != null && !startTime.isEmpty() &&
                                endTime != null && !endTime.isEmpty() &&
                                position != null && !position.isEmpty()) {

                            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                                if (selectedDay != null && selectedDay.equalsIgnoreCase(date.getDayOfWeek().toString())) {
                                    System.out.println("Adding shifts to schedule: " + selectedDay + " for member: " + member.getName());
                                    scheduleController.addSchedule(member.getName(), member.getEmail(), shift.getGroup(), date.toString(), startTime, date.toString(), endTime, shift.getColor());
                                }
                            }
                        }
                    }
                }
            }
            try {
                excelUtil.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void generateShiftForSelectedMember(Member selectedMember) {
        List<LocalDate> receivedDates = new ArrayList<>();
        List<Shift> receivedSchedule = new ArrayList<>();
        // Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        ScheduleController scheduleController = null;
        try {
            profilesUtil = new ProfilesUtil();
            scheduleController = new ScheduleController(excelUtil.getWorkbook());
            excelUtil.clearSheetExceptHeader();
            System.out.println("Schedule Creation Started");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (selectedMember != null && scheduleController != null) {
            receivedSchedule = profilesUtil.getSchedule(selectedMember.getName());
            for (Shift shift : receivedSchedule) {
                receivedDates = profilesUtil.getSeasonDates(shift.getSeason());
                String startTime = shift.getStartTime();
                String endTime = shift.getEndTime();
                String position = shift.getPosition();
                String selectedDay = shift.getWeekDay();
    
                if (receivedDates.size() >= 2) {  // Ensure we have both start and end dates
                    System.out.println("Received dates is greater than 2");
                    LocalDate startDate = receivedDates.get(0);
                    LocalDate endDate = receivedDates.get(1);

                    // print
                    if (!startDate.isAfter(endDate) &&
                            startTime != null && !startTime.isEmpty() &&
                            endTime != null && !endTime.isEmpty() &&
                            position != null && !position.isEmpty()) {
    
                        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                            if (selectedDay != null && selectedDay.equalsIgnoreCase(date.getDayOfWeek().toString())) {
                                System.out.println("Adding shifts to schedule: " + selectedDay);
                                scheduleController.addSchedule(selectedMember.getName(), selectedMember.getEmail(), shift.getGroup(), date.toString(), startTime, date.toString(), endTime, shift.getColor());
                                try {
                                    excelUtil.save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
}
