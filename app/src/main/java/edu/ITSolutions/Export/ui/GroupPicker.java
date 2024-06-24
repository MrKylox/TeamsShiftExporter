package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class GroupPicker extends HBox{

    private final ComboBox<String> groupComboBox;

    public GroupPicker(){
        groupComboBox = new ComboBox<>();

        groupComboBox.getItems().add("Green");
        groupComboBox.getItems().add("Yellow");
        groupComboBox.getItems().add("Dark Yellow");

        this.getChildren().addAll(groupComboBox);

    }

    public String getTime() {
        return groupComboBox.getValue();
    }



}
