package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class AllShiftShower extends HBox {
    private final TableView<Shift> tableView;
    private ObservableList<Shift> shiftList;

    //looks familiar? Yeah check MemberShiftShower.java
    public AllShiftShower() {
        tableView = new TableView<>();
        shiftList = FXCollections.observableArrayList();

        tableView.setMinWidth(800);
        tableView.setMinHeight(500);

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

        memberColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        weekdayColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        startTimeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        endTimeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        positionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        seasonColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));

        @SuppressWarnings("unchecked")
        TableColumn<Shift, String>[] columns = new TableColumn[] {
            memberColumn, weekdayColumn, startTimeColumn, endTimeColumn, positionColumn, seasonColumn
        };
        tableView.getColumns().addAll(columns);
        tableView.setItems(shiftList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        this.getChildren().add(tableView);
    }

    public void setAllShiftList(ObservableList<Shift> list) {
        
        this.shiftList.setAll(list);
        refreshTables();
    }

    // public ObservableList<Shift> getAllShiftList(){
    //     return shiftList;
    // }

    public void refreshTables() {
        tableView.refresh();
    }

}
