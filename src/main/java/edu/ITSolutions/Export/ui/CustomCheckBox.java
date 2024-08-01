package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CustomCheckBox {
    private CheckBox select;

    public CustomCheckBox() {
        this.select = new CheckBox();
    }

    public CheckBox getSelect() {
        return select;
    }

    //chages the selected status of check boxes
    public void setSelected(CheckBox select) {
        this.select = select;
    }

    //will acitvate the check box upon clicking it
    public void handleCheckBoxClick(TableView<Shift> tableView, TableColumn<Shift,?> selectedColumn) {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                int columnIndex = getColumnIndex(tableView, event.getX());
                
                //will select the checkbox if only when 0th column is clicked
                if (tableView.getSelectionModel().getSelectedItem() != null && columnIndex == 0) {
                    System.out.println("Shift selected at column:" + columnIndex);
                    Shift shift = tableView.getSelectionModel().getSelectedItem();
                    shift.setSelected(!shift.isSelected());
                }
            }
        });
    }

    //returns the column index
    public int getColumnIndex(TableView<Shift> tableView, double x){
        double offsetX = 0.0;
        ObservableList<? extends TableColumn<?,?>> columns = tableView.getColumns();

        for(int i = 0; i < columns.size(); i++){
            TableColumn<?,?> column = columns.get(i);
            offsetX += column.getWidth();
            
            if(x < offsetX){
                return i;//column index
            }
        }

        return -1;
    }

    //returns all the selected shifts in the shiftlist
    public ObservableList<Shift> getSelectedShifts(ObservableList<Shift> shiftList){
        ObservableList<Shift> selectedShifts = FXCollections.observableArrayList();

        for(int i = 0; i < shiftList.size(); i++){
            if(shiftList.get(i).isSelected()){
                selectedShifts.add(shiftList.get(i));
            }
        }

        return selectedShifts;
    }

}
