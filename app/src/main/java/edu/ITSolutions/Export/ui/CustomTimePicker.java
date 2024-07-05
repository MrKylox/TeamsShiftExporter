package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class CustomTimePicker extends HBox {
    private final ComboBox<String> hrminComboBox;
    private final ComboBox<String> ampmComboBox;
    private String hour;
    private final String minute = "00";
    private final String minute2 = "30";
    private String time;
    private String time2;


    public CustomTimePicker() {
        hrminComboBox = new ComboBox<>();
        ampmComboBox = new ComboBox<>();
        for(int i = 1; i<13;i++){
            hour = Integer.toString(i);
            time = hour + ":" +  minute;
            time2 = hour + ":"+ minute2;
            hrminComboBox.getItems().addAll(time, time2);
        }
        ampmComboBox.getItems().addAll("am", "pm");

        this.getChildren().addAll(hrminComboBox, ampmComboBox);
    }

    public String getTime() {
        return hrminComboBox.getValue() + ampmComboBox.getValue();
    }
    
}
