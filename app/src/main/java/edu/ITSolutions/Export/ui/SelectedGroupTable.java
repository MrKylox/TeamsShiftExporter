package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class SelectedGroupTable extends HBox{
    private final TableView<Member> tableView;
    private final ObservableList<Member> shiftList;

    public SelectedGroupTable() {
        tableView = new TableView<>();
        shiftList = FXCollections.observableArrayList();

        // Columns for member
        
        TableColumn<Member, String> memberColumn = new TableColumn<>("Member");
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("Member"));
        

        @SuppressWarnings("unchecked")
        TableColumn<Member, String>[] columns = new TableColumn[] {
            memberColumn
        };
        tableView.getColumns().addAll(columns);
        tableView.setItems(shiftList);

        this.getChildren().add(tableView);
    }

    public void setMemberList(ObservableList<Member> shiftList) {
        this.shiftList.setAll(shiftList);
    }

    public ObservableList<Member> getMemberList(){
        return shiftList;
    }

    public void refreshTables() {
        tableView.refresh();
    }

    public Member getSelectedShift() {
        return tableView.getSelectionModel().getSelectedItem();
    }

}