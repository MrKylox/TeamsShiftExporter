package edu.ITSolutions.Export.ui;

import java.time.LocalTime;
import java.util.stream.IntStream;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class CustomTimePicker extends HBox {

    private ComboBox<Integer> hourComboBox;
    private ComboBox<Integer> minuteComboBox;

    public CustomTimePicker() {
        hourComboBox = new ComboBox<>();
        minuteComboBox = new ComboBox<>();

        IntStream.range(00, 24).forEach(hour -> hourComboBox.getItems().add(hour));
        IntStream.range(00, 60).forEach(minute -> minuteComboBox.getItems().add(minute));

        hourComboBox.getSelectionModel().select(LocalTime.now().getHour());
        minuteComboBox.getSelectionModel().select(LocalTime.now().getMinute());

        this.getChildren().addAll(hourComboBox, minuteComboBox);
    }

    public LocalTime getTime() {
        return LocalTime.of(hourComboBox.getValue(), minuteComboBox.getValue());
    }
}
