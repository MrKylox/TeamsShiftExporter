package edu.ITSolutions.Export.ui;

import java.time.LocalDate;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class EditSeasonDialog extends Dialog<Pair<LocalDate, LocalDate>> {

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public EditSeasonDialog() {
        setTitle("Edit Season Dates");
        setHeaderText("Please select the start and end dates for the season.");

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        GridPane grid = new GridPane();
        grid.addRow(0, new javafx.scene.control.Label("Start Date:"), startDatePicker);
        grid.addRow(1, new javafx.scene.control.Label("End Date:"), endDatePicker);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonType.OK.getButtonData());
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter((ButtonType buttonType) -> {
            if (buttonType == saveButtonType) {
                return new Pair<>(startDatePicker.getValue(), endDatePicker.getValue());
            }
            return null;
        });
    }

    public static Optional<Pair<LocalDate, LocalDate>> showEditSeasonDialog() {
        EditSeasonDialog dialog = new EditSeasonDialog();
        return dialog.showAndWait();
    }
}