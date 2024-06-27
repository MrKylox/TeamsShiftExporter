package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private ColorThemePicker themeColorField;
    private GroupPicker groupField;
    private DayOfWeekUI dayOfWeekUI;
    private SeasonUI seasonUI;

    public VBox createMainLayout() {
        VBox vbox = new VBox();
        GridPane gridPane = new GridPane();

        Label importLabel = new Label("Import Excel File:");
        Button importButton = new Button("Import");

        memberShiftShower = new MemberShiftShower();
        dayOfWeekUI = new DayOfWeekUI();

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

        gridPane.add(importLabel, 0, 0);
        gridPane.add(importButton, 1, 0);

        memberChoiceBox = new ComboBox<>(memberList);
        memberChoiceBox.setOnAction(e -> filterShiftsByMember());

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        seasonUI = new SeasonUI();

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
                shiftList.setAll(excelUtil.getShifts(newValue));
                if (profilesUtil != null) {
                    shiftList.setAll(profilesUtil.getProfileShifts(newValue.getName(), startDatePicker.getValue(), endDatePicker.getValue()));
                }
                memberShiftShower.setShiftList(shiftList);
                memberShiftShower.refreshTables();
            } else {
                shiftList.clear();
            }
        });

        HBox datesBox = new HBox(new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker);

        startTimePicker = new CustomTimePicker();
        endTimePicker = new CustomTimePicker();
        themeColorField = new ColorThemePicker();
        groupField = new GroupPicker();

        HBox inputBox = new HBox(new Label("Start Time:"), startTimePicker, new Label("End Time:"), endTimePicker,
                new Label("Group:"), groupField, new Label("Theme Color:"), themeColorField);

        memberShiftShower.setShiftList(shiftList);

        Button generateShiftsButton = new Button("Generate Shifts");
        generateShiftsButton.setOnAction(e -> {
            generateShiftsForSelectedDays();
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                saveScheduleForMember(selectedMember);
            }
        });

        Button SaveProfileButton = new Button("Save Profile");
        SaveProfileButton.setOnAction(e -> {
            try {
                profilesUtil = new ProfilesUtil();
                System.out.println("Profile save initated");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                String selectedDay = dayOfWeekUI.getSelectedDay();
                if (startDate != null && endDate != null && selectedDay != null) {
                    profilesUtil.saveProfile(selectedMember.getName(), selectedDay, startTimePicker.getTime(), endTimePicker.getTime(), startDate, endDate, groupField.getGroup(), themeColorField.getColor(), seasonUI.getSeason());
                    System.out.println("Profile saved");
                }
            }
        });

        Button applyProfilesButton = new Button("Apply Profiles");
        applyProfilesButton.setOnAction(e -> {
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                if (startDate != null && endDate != null && profilesUtil != null) {
                    List<Shift> profileShifts = profilesUtil.getProfileShifts(selectedMember.getName(), startDate, endDate);
                    excelUtil.applyProfileShifts(selectedMember.getName(), profileShifts);
                    try {
                        excelUtil.save();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        vbox.getChildren().addAll(gridPane, memberChoiceBox, dayOfWeekUI, datesBox, inputBox, memberShiftShower, generateShiftsButton, SaveProfileButton, applyProfilesButton);

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
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            ObservableList<Shift> filteredShifts = shiftList.filtered(shift -> shift.getMember().equals(selectedMember.getName()));
            memberShiftShower.setShiftList(filteredShifts);
            memberShiftShower.refreshTables();
        }
    }

    private void generateShiftsForSelectedDays() {
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String startTime = startTimePicker.getTime();
            String endTime = endTimePicker.getTime();
            String group = groupField.getGroup();
            String themeColor = themeColorField.getColor();
            String selectedDay = dayOfWeekUI.getSelectedDay();

            if (startDate != null && endDate != null && !startDate.isAfter(endDate) &&
                    startTime != null && !startTime.isEmpty() &&
                    endTime != null && !endTime.isEmpty() &&
                    group != null && !group.isEmpty() &&
                    themeColor != null && !themeColor.isEmpty()) {

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (selectedDay != null && selectedDay.equals(date.getDayOfWeek().toString())) {
                        Shift newShift = new Shift(selectedMember.getName(), selectedDay, date.toString(), startTime, date.toString(), endTime, group, themeColor);
                        shiftList.add(newShift);
                    }
                }
                filterShiftsByMember();
            }
        }
    }
}
