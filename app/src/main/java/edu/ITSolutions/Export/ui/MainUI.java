package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ITSolutions.Export.Controller.ProfileController;
import edu.ITSolutions.Export.Controller.ScheduleController;
import edu.ITSolutions.Export.App;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.App.appContext;
import edu.ITSolutions.Export.util.ExcelUtil;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

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
    private CustomCheckBox customCheckBox;
    private ComboBox<GroupSelector<String>> cb = new ComboBox<>();
    private SelectedGroupTable selectedGroupTable;
    private AllShiftShower allShiftShower;
    private App app;

    // Declare importVBox as a class member variable
    private VBox importVBox;
    private VBox vbox;

    public MainUI() {
        try {
            profilesUtil = new ProfilesUtil();
            profileController = new ProfileController();
            customCheckBox = new CustomCheckBox();
            memberShiftShower = new MemberShiftShower();
            selectedGroupTable = new SelectedGroupTable();
            allShiftShower = new AllShiftShower();
            app = new App();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    

    public VBox createMainLayout() {
        vbox = new VBox();
        importVBox = new VBox();
        VBox mVBox = new VBox();

        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();

        Label importLabel = new Label("Import Excel File");

        ObservableList<GroupSelector<String>> options = FXCollections.observableArrayList();

        Button importButton = new Button("Import");
        Button SaveProfileButton = new Button("Save Shift");
        Button deleteShiftButton = new Button("Delete Shift");
        Button generateIndividualShiftButton = new Button("Generate Shift For Selected Member");
        Button generateGroupShiftButton = new Button("Generate Shifts For Selected Group");
        Button generateAllShiftsButton = new Button("Generate Shifts For All Members");
        Button editSeasonsButton = new Button("Edit Season");
        Button saveSeasonButton = new Button("Save Season");
        Button cancelSeasonButton = new Button("Cancel");

        memberShiftShower = new MemberShiftShower();

        dayOfWeekUI = new DayOfWeekUI();

        gridPane.add(importLabel, 0, 0);
        gridPane.add(importButton, 0, 1);

        gridPane.setAlignment(Pos.CENTER);

        // Centering elements within the GridPane
        GridPane.setHalignment(importLabel, HPos.CENTER);
        GridPane.setHalignment(importButton, HPos.CENTER);

        GridPane.setValignment(importLabel, VPos.CENTER);
        GridPane.setValignment(importButton, VPos.CENTER);

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
        startDateLabel.getStyleClass().add("label-blue");
        Label endDateLabel = new Label("End Date:");
        endDateLabel.getStyleClass().add("label-blue");

        // -- Editing Season dates -------------------------------------------
        saveSeasonButton.setVisible(false);
        startDatePicker.setVisible(false);
        endDatePicker.setVisible(false);
        startDateLabel.setVisible(false);
        endDateLabel.setVisible(false);
        cancelSeasonButton.setVisible(false);

        // -- False visibility if there's no file imported in the beginning ----------------
        vbox.setVisible(false);

        Label seasonStartAndEnd = new Label();

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    excelUtil = ExcelUtil.initalize(selectedFile);
                    memberList.setAll(excelUtil.getMembers());
                    vbox.setVisible(true);
                    importVBox.setVisible(false);
                    System.out.println("File imported");
                    for (Member member : memberList) {
                        options.add(new GroupSelector<>(member.getName()));
                    }
                    cb.setItems(options);
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

                System.out.println("Season selected: " + newValue);
            }
        });
        
        cb.setCellFactory(c -> {
            ListCell<GroupSelector<String>> cell = new ListCell<>() {
                @Override
                protected void updateItem(GroupSelector<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        final CheckBox cb = new CheckBox(item.toString());
                        cb.selectedProperty().bindBidirectional(item.checkProperty());
                        setGraphic(cb);
                    } else {
                        setGraphic(null);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                if (!cell.isEmpty()) {
                    GroupSelector<String> item = cell.getItem();
                    item.setCheck(!item.getCheck());
                    StringBuilder sb = new StringBuilder();
                    cb.getItems().filtered(f -> f.getCheck()).forEach(p -> sb.append(";").append(p.getItem()));
                    String text = sb.length() > 0 ? sb.substring(1) : "";
                    cb.setPromptText(text);
                    List<GroupSelector<String>> selectedGroup = cb.getItems().filtered(GroupSelector::getCheck);
                    updateMemberList(selectedGroup, memberList);

                }
            });

            return cell;
        });

        editSeasonsButton.setOnAction(e -> {
            String selectedSeason = seasonUI.getSeasonComboBox().getSelectionModel().getSelectedItem();
            if (selectedSeason != null) {
                Optional<Pair<LocalDate, LocalDate>> result = EditSeasonDialog.showEditSeasonDialog();
                result.ifPresent(dates -> {
                    LocalDate startDate = dates.getKey();
                    LocalDate endDate = dates.getValue();
                    if (startDate != null && endDate != null) {
                        profilesUtil.saveSeason(selectedSeason, startDate, endDate);
                        List<LocalDate> receivedDates = profilesUtil.getSeasonDates(selectedSeason);
                        if (!receivedDates.isEmpty()) {
                            seasonStartAndEnd.setText(receivedDates.get(0) + " - " + receivedDates.get(1));
                        }
                    }
                });
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No Season Selected");
                alert.setHeaderText("Please make sure a season is selected");
                alert.showAndWait();
            }
        });

        SaveProfileButton.setOnAction(e -> {
            try {
                profilesUtil = new ProfilesUtil();
                System.out.println("Profile save initiated");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                String selectedDay = dayOfWeekUI.getSelectedDay();
                boolean overlaps = false;

                if (selectedDay != null) {
                    for (Shift shift : shiftList) {
                        if (profileController.compareTime(shift.getWeekDay(), shift.getStartTime(), shift.getEndTime(), selectedDay, startTimePicker.getTime(), endTimePicker.getTime())) {
                            overlaps = true;
                            break;
                        }
                    }

                    if (!overlaps) {
                        profilesUtil.saveProfile(selectedMember.getName(), selectedDay, startTimePicker.getTime(),
                                endTimePicker.getTime(), positionUI.getPosition(),
                                seasonUI.getSeason());
                        System.out.println("Profile saved");
                    } else {
                        System.out.println("Shift overlaps with an existing shift."); 
                    }
                }
            }
            updateShiftList();
        });

        deleteShiftButton.setOnAction(e -> {
            ObservableList<Shift> selectedShifts = customCheckBox.getSelectedShifts(shiftList);
            if (!selectedShifts.isEmpty()) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you would like to delete the following shifts:");
                GridPane shiftDetailsGrid = new GridPane();
                shiftDetailsGrid.setPadding(new Insets(10));
                shiftDetailsGrid.setVgap(10);
                shiftDetailsGrid.setHgap(10);

                shiftDetailsGrid.addRow(0,
                        new Label("Member"),
                        new Label("Week Day"),
                        new Label("Start Time"),
                        new Label("End Time"),
                        new Label("Position"),
                        new Label("Season"));

                int row = 1;
                for (Shift shift : selectedShifts) {
                    shiftDetailsGrid.addRow(row++,
                            new Label(shift.getMember()),
                            new Label(shift.getWeekDay()),
                            new Label(shift.getStartTime()),
                            new Label(shift.getEndTime()),
                            new Label(shift.getPosition()),
                            new Label(shift.getSeason()));
                }

                alert.getDialogPane().setContent(shiftDetailsGrid);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        profilesUtil.deleteSelectedShifts(selectedShifts);
                        updateShiftList();
                        System.out.println("Shifts deleted");
                    }
                });
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No Shift Selected");
                alert.setHeaderText("Please make sure a shift is selected");
                alert.setContentText("NOTE: Check if the checkboxes are marked");
                alert.showAndWait();
            }
        });

        generateIndividualShiftButton.setOnAction(e -> {
            Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                generateShiftForSelectedMember(selectedMember);
            }
        });

        generateGroupShiftButton.setOnAction(e -> {
            List<String> selectedGroup = new ArrayList<>();
            cb.getItems().filtered(GroupSelector::getCheck).forEach(item -> selectedGroup.add(item.getItem()));
            generateShiftForGroup(selectedGroup, memberList);
        });

        generateAllShiftsButton.setOnAction(e -> {
            if(appContext.getTabPane() != null){
                // generateShiftsForAllMembers();
                app.switchToAllShiftsTab(appContext.getTabPane(),appContext.getAllShiftTab());
            }
            else{
                System.err.println("Tab pane is null");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Failed to generate shifts for all member");
                alert.setContentText("Program failure occured, please restart the app");
                alert.showAndWait();
            }
        });

        StackPane toggleButtonsPane = new StackPane(editSeasonsButton, saveSeasonButton);

        HBox profileBox = new HBox(SaveProfileButton, deleteShiftButton);

        HBox startTimeBox = new HBox(new Label("Start Time: "), startTimePicker);
        HBox endTimeBox = new HBox(new Label("End Time: "), endTimePicker);

        HBox datesBox = new HBox(new Label("Season: "), seasonUI, toggleButtonsPane, cancelSeasonButton, startDateLabel, startDatePicker, endDateLabel, endDatePicker);
        HBox positionBox = new HBox(new Label("Position: "), positionUI);
        HBox dOWBox = new HBox(new Label("Day Of Week: "), dayOfWeekUI);
        HBox memberBox = new HBox(new Label("Member: "), memberChoiceBox);

        VBox test = new VBox(seasonStartAndEnd, datesBox);
        mVBox.getChildren().addAll(dOWBox, startTimeBox, endTimeBox, positionBox, profileBox);
        mVBox.setSpacing(10.0);

        borderPane.setTop(test);
        BorderPane.setMargin(mVBox, new Insets(0, 0, 10, 10));
        BorderPane.setMargin(test, new Insets(0, 0, 10, 10));
        borderPane.setCenter(mVBox);

        HBox generateShiftBox = new HBox(generateIndividualShiftButton, generateAllShiftsButton);
        HBox generateGroupBox = new HBox(generateGroupShiftButton, cb);
        HBox memberShiftControls = new HBox(memberShiftShower, borderPane);
        HBox.setHgrow(memberShiftControls, Priority.ALWAYS);

        vbox.getChildren().addAll(memberBox, memberShiftControls, generateShiftBox, generateGroupBox, selectedGroupTable);
        importVBox.getChildren().addAll(gridPane);
        importVBox.setAlignment(Pos.CENTER);

        importVBox.setOnDragOver(this::handleDragOver);
        importVBox.setOnDragDropped(this::handleDragDropped);

        StackPane UIViewPane = new StackPane(importVBox, vbox);
        UIViewPane.setAlignment(Pos.CENTER);

        VBox mainVBox = new VBox(UIViewPane);

        return mainVBox;
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != importVBox && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        ObservableList<GroupSelector<String>> options = FXCollections.observableArrayList();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                ExcelUtil.getInstance();
                memberList.setAll(excelUtil.getMembers());
                vbox.setVisible(true);
                importVBox.setVisible(false);
                for (Member member : memberList) {
                    options.add(new GroupSelector<>(member.getName()));
                }
                cb.setItems(options);
                System.out.println("File imported via drag-and-drop");
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void updateShiftList() {
        Member selectedMember = memberChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedMember != null && profilesUtil != null) {
            shiftList.setAll(profilesUtil.getProfileShifts(selectedMember.getName()));

            ObservableList<Shift> newShifts = profileController.sortList(shiftList);
            memberShiftShower.setShiftList(newShifts);
            memberShiftShower.refreshTables();
            AllShifts allShifts = new AllShifts();
            ObservableList<Shift> allNewShifts = allShifts.getAllShifts();
            allShiftShower.setAllShiftList(allNewShifts);
            allShiftShower.refreshTables();
        }
    }

    public void generateShiftsForAllMembers() {
        List<Member> allMembers = memberList;
        ScheduleController scheduleController = null;   

        try {
            profilesUtil = new ProfilesUtil();
            excelUtil = ExcelUtil.getInstance();
            scheduleController = new ScheduleController(excelUtil.getWorkbook());
            memberList.setAll(excelUtil.getMembers());

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

                    if (receivedDates.size() >= 2) {
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
                System.out.println("Saving file...");
                excelUtil.save();
                System.out.println("Saved successfully!");
            } catch (IOException e) {
                System.err.println("Error while generating shfit for all members: " + e);
            }
        }
    }

    private void generateShiftForSelectedMember(Member selectedMember) {
        List<LocalDate> receivedDates = new ArrayList<>();
        List<Shift> receivedSchedule = new ArrayList<>();
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

                if (receivedDates.size() >= 2) {
                    System.out.println("Received dates is greater than 2");
                    LocalDate startDate = receivedDates.get(0);
                    LocalDate endDate = receivedDates.get(1);

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

    private void generateShiftForGroup(List<String> selectedGroup, ObservableList<Member> memberList) {
        for (Member member : memberList) {
            if (selectedGroup.contains(member.getName())) {
                generateShiftForSelectedMember(member);
            }
        }
    }

    private void updateMemberList(List<GroupSelector<String>> selectedGroup, ObservableList<Member> memberList) {
        ObservableList<Member> members = FXCollections.observableArrayList();
        for (GroupSelector<String> groupSelector : selectedGroup) {
            // members.add(groupSelector.getItem());
        }
        selectedGroupTable.setMemberList(members);
    }
    

    private void showGeneratedShiftsConfirmationDialog(List<Shift> generatedShifts) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Generated Shifts:");

        GridPane shiftDetailsGrid = new GridPane();
        shiftDetailsGrid.setPadding(new Insets(10));
        shiftDetailsGrid.setVgap(10);
        shiftDetailsGrid.setHgap(10);

        shiftDetailsGrid.addRow(0,
                new Label("Member"),
                new Label("Week Day"),
                new Label("Start Time"),
                new Label("End Time"),
                new Label("Position"),
                new Label("Season"));

        int row = 1;
        for (Shift shift : generatedShifts) {
            shiftDetailsGrid.addRow(row++,
                    new Label(shift.getMember()),
                    new Label(shift.getWeekDay()),
                    new Label(shift.getStartTime()),
                    new Label(shift.getEndTime()),
                    new Label(shift.getPosition()),
                    new Label(shift.getSeason()));
        }

        alert.getDialogPane().setContent(shiftDetailsGrid);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                for (Shift shift : generatedShifts) {
                    profilesUtil.saveProfile(shift.getMember(), shift.getWeekDay(), shift.getStartTime(), shift.getEndTime(), shift.getPosition(), shift.getSeason());
                }
                updateShiftList();
                System.out.println("Shifts generated and saved");
            }
        });
    }
}
