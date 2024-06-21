package edu.ITSolutions.Export.ui;

import java.time.LocalTime;
import java.util.stream.IntStream;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class CustomTimePicker extends HBox {

    private ComboBox<String> hourComboBox;
    private ComboBox<String> minuteComboBox;

    public CustomTimePicker() {
        hourComboBox = new ComboBox<>();
        minuteComboBox = new ComboBox<>();
        IntStream.range(0, 24).forEach(hour -> hourComboBox.getItems().add(String.format("%02d", hour)));
        IntStream.range(0, 60).forEach(minute -> minuteComboBox.getItems().add(String.format("%02d", minute)));

        hourComboBox.getSelectionModel().select(String.format("%02d", LocalTime.now().getHour()));
        minuteComboBox.getSelectionModel().select(String.format("%02d", LocalTime.now().getMinute()));

        this.getChildren().addAll(hourComboBox, minuteComboBox);
    }

    public String getTime() {
        return hourComboBox.getValue() + ":" + minuteComboBox.getValue();
    }
}
