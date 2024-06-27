package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class DayOfWeekUI extends HBox {
    private final ComboBox<String> weekdayComboBox;

    public DayOfWeekUI() {
        weekdayComboBox = new ComboBox<>();

        weekdayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        this.getChildren().addAll(weekdayComboBox);
    }

    public String getSelectedDay() {
        return weekdayComboBox.getValue();
    }
}
