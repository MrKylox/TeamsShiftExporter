package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ITSolutions.Export.App;
import edu.ITSolutions.Export.App.appContext;
import edu.ITSolutions.Export.Controller.ProfileController;
import edu.ITSolutions.Export.Controller.ScheduleController;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.Shift;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
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
    private CustomTimePicker startTimePicker;
    private CustomTimePicker endTimePicker;
    private DayOfWeekUI dayOfWeekUI;
    private SeasonUI seasonUI;
    private PositionUI positionUI;
    private ProfileController profileController;
    private CustomCheckBox customCheckBox;
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
            allShiftShower = new AllShiftShower();
            app = new App();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    

    public VBox createMainLayout() {
        vbox = new VBox();
        importVBox = new VBox();
        VBox mainInputVbox = new VBox();

        Label importLabel = new Label("Import Excel File");
        Button importButton = new Button("Import");
        Button SaveProfileButton = new Button("Save Shift");
        Button deleteShiftButton = new Button("Delete Shift");
        Button generateIndividualShiftButton = new Button("Generate Shift For Selected Member");
        Button generateGroupShiftButton = new Button("Generate Shifts For Selected Group");
        Button generateAllShiftsButton = new Button("Generate Shifts For All Members");
        Button editSeasonsButton = new Button("Edit Season");

        memberShiftShower = new MemberShiftShower();
        dayOfWeekUI = new DayOfWeekUI();

        memberShiftShower.setShiftList(shiftList);
        memberChoiceBox = new ComboBox<>(memberList);
        memberChoiceBox.setOnAction(e -> updateShiftList());

        seasonUI = new SeasonUI();
        positionUI = new PositionUI();
        startTimePicker = new CustomTimePicker();
        endTimePicker = new CustomTimePicker();

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
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you would like to generate for the following member:");
                alert.setContentText(selectedMember.getName());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        generateShiftForSelectedMember(selectedMember);
                        System.out.println("Schedule Generated");
                    }
                });
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No member Selected");
                alert.setHeaderText("Please make sure a member is selected");
                // alert.setContentText("NOTE: Check if the checkboxes are marked");
                alert.showAndWait();
            }
        });

        generateGroupShiftButton.setOnAction(e -> {
            if(appContext.getTabPane() != null){
                // generateShiftsForAllMembers();
                app.switchToGroupShiftsTab(appContext.getTabPane(),appContext.getGroupShiftTab());
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


        HBox profileBox = new HBox(SaveProfileButton, deleteShiftButton); // HBox that contains the save and delete profiles
        HBox startTimeBox = new HBox(new Label("Start Time: "), startTimePicker); // Choose the start time Hbox
        HBox endTimeBox = new HBox(new Label("End Time: "), endTimePicker); // Choose the end time Hbox
        HBox datesBox = new HBox(new Label("Season: "), seasonUI, editSeasonsButton); //Choose the dates Hbox
        HBox positionBox = new HBox(new Label("Position: "), positionUI); // Choose the Position Hbox
        HBox dOWBox = new HBox(new Label("Day Of Week: "), dayOfWeekUI); // Choose the day of week Hbox
        HBox memberBox = new HBox(new Label("Member: "), memberChoiceBox); // Choose the member Hbox

        VBox generateVbox = new VBox(generateIndividualShiftButton, generateGroupShiftButton, generateAllShiftsButton);
        generateVbox.setSpacing(10);
        generateVbox.setAlignment(Pos.BOTTOM_CENTER);

        // GridPane -----------------------------------------------------------------------------------------
        GridPane gridPane = new GridPane();
        // Column Constraints for column 0
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setPercentWidth(20);
        column0.setHgrow(Priority.ALWAYS);

        // Column Constraints for column 1
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(70);
        column1.setHgrow(Priority.ALWAYS);

        // Add column constraints to the grid
        gridPane.getColumnConstraints().addAll(column0, column1);
        gridPane.setVgap(16);

        //Profile Box
        gridPane.add(seasonStartAndEnd, 0, 0);
        gridPane.add(seasonUI, 0, 1);
        gridPane.add(editSeasonsButton, 1, 1);
        //---------

        //Day Of Week Box
        gridPane.add(new Label("Day Of Week: "), 0, 2);
        gridPane.add(dayOfWeekUI, 1, 2);
        //---------------

        //Start Time and End Time
        gridPane.add(new Label("Start Time: "), 0, 3);
        gridPane.add(startTimePicker, 1, 3);

        gridPane.add(new Label("End Time: "), 0, 4);
        gridPane.add(endTimePicker, 1, 4);
        //-----------------------

        //Position Box 
        gridPane.add(new Label("Choose Position: "), 0, 5);
        gridPane.add(positionUI, 1, 5);
        //------------

        //Save and Delete buttons
        gridPane.add(SaveProfileButton, 0, 6);
        gridPane.add(deleteShiftButton, 0, 7);
        //-----------------------

        // Generating Buttons
        gridPane.add(generateIndividualShiftButton, 0, 8, 2, 1);
        gridPane.add(generateGroupShiftButton, 0, 9,2, 1);
        gridPane.add(generateAllShiftsButton, 0, 10,2, 1);
        gridPane.setAlignment(Pos.CENTER);
        // gridPane.setGridLinesVisible(true); // DELETE This Line WHEN DONE ---------------------------------

        //mainRightVBox will contain all of the above
        mainInputVbox.getChildren().addAll(seasonStartAndEnd, datesBox, dOWBox, startTimeBox, endTimeBox,
         positionBox, profileBox, generateVbox);
        mainInputVbox.setSpacing(10.0);
        mainInputVbox.setAlignment(Pos.CENTER);

        HBox.setHgrow(memberShiftShower, Priority.ALWAYS);
        VBox memberPickerShower = new VBox(memberBox, memberShiftShower);
        memberPickerShower.setSpacing(16);

        HBox mainUIVbox = new HBox(20, memberPickerShower, gridPane);  //Replace gridPane with mainInputVbox if want to change

        // Center alignment for HBox and VBox elements
        memberBox.setAlignment(Pos.CENTER);
        mainUIVbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(mainUIVbox);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        // Import VBox -------------------------
        HBox labelImport = new HBox(importLabel);
        HBox buttonImport = new HBox(importButton);
        labelImport.setAlignment(Pos.TOP_CENTER);
        buttonImport.setAlignment(Pos.TOP_CENTER);

        importVBox.getChildren().addAll(labelImport, buttonImport);
        importVBox.setAlignment(Pos.CENTER);

        importVBox.setOnDragOver(this::handleDragOver);
        importVBox.setOnDragDropped(this::handleDragDropped);

        // End Of Import VBox -------------------

        StackPane UIViewPane = new StackPane(importVBox, vbox);
        UIViewPane.setAlignment(Pos.CENTER);

        VBox mainVBox = new VBox(UIViewPane);
        mainVBox.setAlignment(Pos.CENTER);

        // Setting grow priority for main containers
        VBox.setVgrow(UIViewPane, Priority.ALWAYS);
        HBox.setHgrow(UIViewPane, Priority.ALWAYS);

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
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                try{
                    excelUtil = new ExcelUtil(file);
                }
                catch(IOException e){
                    System.out.println("Import error: "+e);
                }
                memberList.setAll(excelUtil.getMembers());
                vbox.setVisible(true);
                importVBox.setVisible(false);
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
            System.out.println("Members: "+ allMembers);

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

    public void generateShiftForSelectedMember(Member selectedMember) {
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

    public void generateShiftForGroup(List<String> selectedGroup) {
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
        for (Member member : excelUtil.getMembers()) {
            if (selectedGroup.contains(member.getName())) {
                if (scheduleController != null) {
                    receivedSchedule = profilesUtil.getSchedule(member.getName());
                    System.out.println("Member selected: "+member.getName());
                    
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
                                        scheduleController.addSchedule(member.getName(), member.getEmail(), shift.getGroup(), date.toString(), startTime, date.toString(), endTime, shift.getColor());
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
    }

    public ObservableList<Member> getMembers(){
        ObservableList<Member> members = FXCollections.observableArrayList();
        excelUtil = ExcelUtil.getInstance();
        members.setAll(excelUtil.getMembers());
        return members;
    }
}
