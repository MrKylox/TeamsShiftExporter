package edu.ITSolutions.Export.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class SeasonUI extends HBox{

    private final ComboBox<String> seasonComboBox;

    public SeasonUI(){
        seasonComboBox = new ComboBox<>();

        seasonComboBox.getItems().add("Fall");
        seasonComboBox.getItems().add("Winter");
        seasonComboBox.getItems().add("Spring");
        seasonComboBox.getItems().add("Summer");

        this.getChildren().addAll(seasonComboBox);
    }

    public String getSeason() {
        return seasonComboBox.getValue();
    }

    public ComboBox<String> getSeasonComboBox() {
        return seasonComboBox;
    }
}