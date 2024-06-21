package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class MemberShiftShower extends HBox{
        private final TableView<Shift> tableView;
        private final ObservableList<Shift> shiftList;

        public MemberShiftShower(){
            tableView = new TableView<>();
            shiftList = FXCollections.observableArrayList();

            //startDate, startTime, endDate, endTime, group, themeColor
            TableColumn<Shift, String> startTimeColumn = new TableColumn<>("Starting time");
            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

            TableColumn<Shift, String> endTimeColumn = new TableColumn<>("Ending time");
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

            TableColumn<Shift, String> startDateColumn = new TableColumn<>("Starting Date");
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

            TableColumn<Shift, String> endDateColumn = new TableColumn<>("Ending Date");
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

            TableColumn<Shift, String> groupColumn = new TableColumn<>("Group");
            groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));

            TableColumn<Shift, String> themeColorColumn = new TableColumn<>("Theme Color");
            themeColorColumn.setCellValueFactory(new PropertyValueFactory<>("themeColor"));

            @SuppressWarnings("unchecked")
            TableColumn<Shift, String>[] columns = new TableColumn[] {
                startTimeColumn, endTimeColumn, startDateColumn, endDateColumn, groupColumn, themeColorColumn
            };

            tableView.getColumns().addAll(columns);
            tableView.setItems(shiftList);
            this.getChildren().add(tableView);

        }

        public void setShiftList(ObservableList<Shift> shiftList) {
            this.shiftList.setAll(shiftList);
        }

        public void refreshTable() {
            tableView.refresh();
        }
 

}
