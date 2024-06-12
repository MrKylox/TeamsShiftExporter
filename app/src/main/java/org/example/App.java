package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CSV Exporter");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Button exportButton = new Button("Export to CSV");
        exportButton.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                exportToCSV(file);
            }
        });

        // primaryStage.initStyle(StageStyle.DECORATED);
        // VBox vBox = new VBox(exportButton);
        // Scene scene = new Scene(vBox, 600, 600);
        // primaryStage.setScene(scene);
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {System.out.println("Closing Stage");});

        primaryStage.show();

        
    }

    private Parent createContent() {
        ValidatingTextField field = new ValidatingTextField(input -> input.contains("a"));
        field.setFont(Font.font(22));
        Button btn = new Button("Submit");
        btn.setFont(Font.font(22));
        btn.disableProperty().bind(field.isValidProperty().not());


        VBox vBox = new VBox(10, field, btn);
        return vBox;
    }



    private void exportToCSV(File file) {
        String[] headers = { "ID", "Name", "Email" };
        String[][] data = {
                { "1", "John Doe", "john.doe@example.com" },
                { "2", "Jane Smith", "jane.smith@example.com" },
                { "3", "Mike Johnson", "mike.johnson@example.com" }
        };

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(headers).build();

        try (FileWriter writer = new FileWriter(file);
             CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {

            for (String[] record : data) {
                csvPrinter.printRecord((Object[]) record);
            }

            csvPrinter.flush();
            System.out.println("CSV file created successfully!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
