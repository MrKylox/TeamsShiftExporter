package edu.ITSolutions.Export.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import edu.ITSolutions.Export.Controller.ScheduleController;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.util.ExcelUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainUI {

    private ExcelUtil excelUtil;
    private final ObservableList<Member> memberList = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    public VBox createMainLayout() {
        VBox vbox = new VBox();
        GridPane gridPane = new GridPane();

        Label importLabel = new Label("Import Excel File:");
        Button importButton = new Button("Import");

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    excelUtil = new ExcelUtil(selectedFile);
                    memberList.setAll(excelUtil.getMembers());
                } catch (IOException ioException) {
                    // ioException.printStackTrace();
                }
            }
        });

        gridPane.add(importLabel, 0, 0);
        gridPane.add(importButton, 1, 0);

        TableView<Member> tableView = new TableView<>();
        TableColumn<Member, String> nameColumn = new TableColumn<>("Member");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Member, String> emailColumn = new TableColumn<>("Work Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableView.setItems(memberList);
        tableView.getColumns().addAll(nameColumn, emailColumn);

        // Add schedule creation fields
        DatePicker startDatePicker = new DatePicker();
        CustomTimePicker startTimePicker = new CustomTimePicker();
        DatePicker endDatePicker = new DatePicker();
        CustomTimePicker endTimePicker = new CustomTimePicker();
        TextField groupField = new TextField();
        groupField.setPromptText("Group");
        TextField themeColorField = new TextField();
        themeColorField.setPromptText("Theme Color");

        Button addScheduleButton = new Button("Add Schedule");
        addScheduleButton.setOnAction(e -> {
            Member selectedMember = tableView.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                addScheduleForMember(selectedMember, groupField.getText(),
                    startDatePicker.getValue(), startTimePicker.getTime(),
                    endDatePicker.getValue(), endTimePicker.getTime(), themeColorField.getText());
            }
        });

        gridPane.add(new Label("Start Date:"), 0, 1);
        gridPane.add(startDatePicker, 1, 1);
        gridPane.add(new Label("Start Time:"), 0, 2);
        gridPane.add(startTimePicker, 1, 2);
        gridPane.add(new Label("End Date:"), 0, 3);
        gridPane.add(endDatePicker, 1, 3);
        gridPane.add(new Label("End Time:"), 0, 4);
        gridPane.add(endTimePicker, 1, 4);
        gridPane.add(new Label("Group:"), 0, 5);
        gridPane.add(groupField, 1, 5);
        gridPane.add(new Label("Theme Color:"), 0, 6);
        gridPane.add(themeColorField, 1, 6);
        gridPane.add(addScheduleButton, 1, 7);

        vbox.getChildren().addAll(gridPane, tableView);
        return vbox;
    }

    private void addScheduleForMember(Member member, String group, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String color) {
        if (excelUtil != null) {
            ScheduleController scheduleController = new ScheduleController(excelUtil.getWorkbook());
            scheduleController.addSchedule(member.getName(), member.getEmail(), group, startDate, startTime, endDate, endTime, color);
            FileChooser fileChooser = new FileChooser();
            File saveFile = fileChooser.showSaveDialog(new Stage());
            if (saveFile != null) {
                try {
                    excelUtil.save(saveFile);
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
        }
    }
}
