package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class MemberShiftShower extends HBox {
    private final TableView<Shift> tableView;
    private final ObservableList<Shift> shiftList;

    public MemberShiftShower() {
        tableView = new TableView<>();
        shiftList = FXCollections.observableArrayList();

        // Columns for the Shift Table
        TableColumn<Shift, String> memberColumn = new TableColumn<>("Member");
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("member"));

        TableColumn<Shift, String> weekdayColumn = new TableColumn<>("Week Day");
        weekdayColumn.setCellValueFactory(new PropertyValueFactory<>("weekDay"));

        TableColumn<Shift, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Shift, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Shift, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Shift, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<Shift, String> groupColumn = new TableColumn<>("Group");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));

        TableColumn<Shift, String> themeColorColumn = new TableColumn<>("Theme Color");
        themeColorColumn.setCellValueFactory(new PropertyValueFactory<>("themeColor"));

        @SuppressWarnings("unchecked")
        TableColumn<Shift, String>[] columns = new TableColumn[] {
            memberColumn, weekdayColumn, startDateColumn, startTimeColumn, endDateColumn, endTimeColumn, groupColumn, themeColorColumn
        };
        tableView.getColumns().addAll(columns);
        tableView.setItems(shiftList);

        this.getChildren().add(tableView);
    }

    public void setShiftList(ObservableList<Shift> shiftList) {
        this.shiftList.setAll(shiftList);
    }

    public void refreshTables() {
        tableView.refresh();
    }
}
