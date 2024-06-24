package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class GroupPicker extends HBox{

    private final ComboBox<String> groupComboBox;

    public GroupPicker(){
        groupComboBox = new ComboBox<>();

        groupComboBox.getItems().add("Lead Consultants");
        groupComboBox.getItems().add("Senior and Student Consultants");

        this.getChildren().addAll(groupComboBox);
    }

    public String getGroup() {
        return groupComboBox.getValue();
    }



}
