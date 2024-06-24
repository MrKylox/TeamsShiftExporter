package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class ColorThemePicker extends HBox{
    private final ComboBox<String> colorComboBox;

    public ColorThemePicker(){
        colorComboBox = new ComboBox<>();

        colorComboBox.getItems().add("3. Green");
        colorComboBox.getItems().add("6. Yellow");
        colorComboBox.getItems().add("12. Dark Yellow");

        this.getChildren().addAll(colorComboBox);
    }

    public String getColor() {
        return colorComboBox.getValue();
    }
}
