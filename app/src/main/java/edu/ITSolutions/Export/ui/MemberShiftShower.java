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
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("Member"));

        TableColumn<Shift, String> weekdayColumn = new TableColumn<>("Week Day");
        weekdayColumn.setCellValueFactory(new PropertyValueFactory<>("WeekDay"));

        TableColumn<Shift, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Shift, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<Shift, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Shift, String> seasonColumn = new TableColumn<>("Season");
        seasonColumn.setCellValueFactory(new PropertyValueFactory<>("season"));



        @SuppressWarnings("unchecked")
        TableColumn<Shift, String>[] columns = new TableColumn[] {
            memberColumn, weekdayColumn, startTimeColumn, endTimeColumn, positionColumn, seasonColumn
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

    public Shift getSelectedShift() {
        return tableView.getSelectionModel().getSelectedItem();
    }
}
