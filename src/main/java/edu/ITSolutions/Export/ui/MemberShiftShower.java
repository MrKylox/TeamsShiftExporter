package edu.ITSolutions.Export.ui;

import java.util.List;

import edu.ITSolutions.Export.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class MemberShiftShower extends HBox {
    private final TableView<Shift> tableView;
    private final ObservableList<Shift> shiftList;
    private CustomCheckBox customCheckBox;

    public MemberShiftShower() {
        tableView = new TableView<>();
        shiftList = FXCollections.observableArrayList();
        customCheckBox = new CustomCheckBox();

        tableView.setMinWidth(800);
        tableView.setMinHeight(500);

        // Columns for the Shift Table
        TableColumn<Shift, Boolean> checkBoxColumn = new TableColumn<>("Select");
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));
        customCheckBox.handleCheckBoxClick(tableView,checkBoxColumn);
        
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

        memberColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        weekdayColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        startTimeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        endTimeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        positionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        seasonColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));

        @SuppressWarnings("unchecked")
        TableColumn<Shift, String>[] columns = new TableColumn[] {
            checkBoxColumn, memberColumn, weekdayColumn, startTimeColumn, endTimeColumn, positionColumn, seasonColumn
        };
        tableView.getColumns().addAll(columns);
        tableView.setItems(shiftList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        this.getChildren().add(tableView);
    }

    public void setShiftList(List<Shift> list) {
        this.shiftList.setAll(list);
    }

    public ObservableList<Shift> getShiftList(){
        return shiftList;
    }

    public void refreshTables() {
        tableView.refresh();
    }

    public Shift getSelectedShift() {
        return tableView.getSelectionModel().getSelectedItem();
    }

}
