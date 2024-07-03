package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class PositionUI extends HBox{

    private final ComboBox<String> positionComboBox;

    public PositionUI(){
        positionComboBox = new ComboBox<>();

        positionComboBox.getItems().add("Student Consultant");
        positionComboBox.getItems().add("Senior Student Consultant");
        positionComboBox.getItems().add("Lead Student Consultant");

        this.getChildren().addAll(positionComboBox);
    }

    public String getPosition() {
        return positionComboBox.getValue();
    }


}