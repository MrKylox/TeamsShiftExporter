package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class ColorThemePicker extends HBox{
    private final ComboBox<String> colorComboBox;

    public ColorThemePicker(){
        colorComboBox = new ComboBox<>();

        colorComboBox.getItems().add("Green");
        colorComboBox.getItems().add("Yellow");
        colorComboBox.getItems().add("Dark Yellow");

        this.getChildren().addAll(colorComboBox);
    }

    public String getTime() {
        return colorComboBox.getValue();
    }
}
