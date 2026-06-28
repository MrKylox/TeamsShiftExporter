package edu.ITSolutions.Export.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ITSolutions.Export.App;
import edu.ITSolutions.Export.Member;
import edu.ITSolutions.Export.util.ExcelUtil;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GuiWorkflowSmoke {
    private static final Path SMOKE_HOME = Path.of(System.getProperty("user.home"));
    private static final Path IMPORT_FILE = SMOKE_HOME.resolve("Team Members Import.xlsx");

    public static void main(String[] args) throws Exception {
        resetSmokeHome();
        createImportWorkbook();
        seedProfilesWorkbook();

        CountDownLatch completed = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Platform.startup(() -> {
            try {
                runWorkflow();
            } catch (Throwable throwable) {
                failure.set(throwable);
            } finally {
                completed.countDown();
            }
        });

        if (!completed.await(30, TimeUnit.SECONDS)) {
            Platform.exit();
            throw new IllegalStateException("Timed out waiting for JavaFX GUI smoke workflow.");
        }
        Platform.exit();
        if (failure.get() != null) {
            throw new RuntimeException("JavaFX GUI smoke workflow failed.", failure.get());
        }

        verifyExportedShifts();
        System.out.println("GUI workflow smoke test passed: imported members, saved profile shifts, generated Shifts sheet.");
    }

    private static void runWorkflow() throws Exception {
        MainUI mainUI = new MainUI();
        Parent mainLayout = mainUI.createMainLayout();

        TabPane tabPane = new TabPane();
        Tab mainTab = new Tab("Main", mainLayout);
        Tab allShiftTab = new Tab("All Shifts");
        Tab groupShiftTab = new Tab("Group Shifts");
        tabPane.getTabs().add(mainTab);

        App.appContext.setTabPane(tabPane);
        App.appContext.setMainTab(mainTab);
        App.appContext.setAllShiftTab(allShiftTab);
        App.appContext.setGroupShiftTab(groupShiftTab);

        Stage stage = new Stage();
        stage.setScene(new Scene(tabPane, 1200, 800));
        stage.show();

        ExcelUtil excelUtil = ExcelUtil.initalize(IMPORT_FILE.toFile());
        setField(mainUI, "excelUtil", excelUtil);
        @SuppressWarnings("unchecked")
        ObservableList<Member> memberList = (ObservableList<Member>) getField(mainUI, "memberList");
        memberList.setAll(excelUtil.getMembers());
        ((Node) getField(mainUI, "vbox")).setVisible(true);
        ((Node) getField(mainUI, "importVBox")).setVisible(false);

        @SuppressWarnings("unchecked")
        ComboBox<Member> memberChoiceBox = (ComboBox<Member>) getField(mainUI, "memberChoiceBox");
        SeasonUI seasonUI = (SeasonUI) getField(mainUI, "seasonUI");
        DayOfWeekUI dayOfWeekUI = (DayOfWeekUI) getField(mainUI, "dayOfWeekUI");
        CustomTimePicker startTimePicker = (CustomTimePicker) getField(mainUI, "startTimePicker");
        CustomTimePicker endTimePicker = (CustomTimePicker) getField(mainUI, "endTimePicker");
        PositionUI positionUI = (PositionUI) getField(mainUI, "positionUI");

        Button saveShift = findButton(mainLayout, "Save Shift");
        Button generateAll = findButton(mainLayout, "Generate Shifts For All Members");

        List<String[]> assignments = List.of(
                new String[] {"Alex Johnson", "Fall", "Monday", "9:00", "am", "12:00", "pm", "Student Consultant"},
                new String[] {"Brianna Lee", "Fall", "Tuesday", "10:00", "am", "2:00", "pm", "Senior Student Consultant"},
                new String[] {"Carlos Rivera", "Fall", "Friday", "9:00", "am", "1:00", "pm", "Lead Student Consultant"},
                new String[] {"Dana Patel", "Fall", "Wednesday", "10:00", "am", "1:00", "pm", "Student Consultant"},
                new String[] {"Evan Smith", "Fall", "Thursday", "11:00", "am", "2:00", "pm", "Student Consultant"},
                new String[] {"Fatima Ahmed", "Fall", "Wednesday", "4:00", "pm", "7:00", "pm", "Senior Student Consultant"});

        for (String[] assignment : assignments) {
            selectMember(memberChoiceBox, assignment[0]);
            seasonUI.getSeasonComboBox().setValue(assignment[1]);
            comboIn(dayOfWeekUI, 0).setValue(assignment[2]);
            comboIn(startTimePicker, 0).setValue(assignment[3]);
            comboIn(startTimePicker, 1).setValue(assignment[4]);
            comboIn(endTimePicker, 0).setValue(assignment[5]);
            comboIn(endTimePicker, 1).setValue(assignment[6]);
            comboIn(positionUI, 0).setValue(assignment[7]);
            saveShift.fire();
        }

        allShiftTab.setContent(new AllShifts().createAllShiftsLayout());
        generateAll.fire();
        Button confirm = findButton(allShiftTab.getContent(), "Confirm");
        confirm.fire();

        stage.close();
    }

    private static void resetSmokeHome() throws IOException {
        if (Files.exists(SMOKE_HOME)) {
            try (var paths = Files.walk(SMOKE_HOME)) {
                paths.sorted(Comparator.reverseOrder())
                        .filter(path -> !path.equals(SMOKE_HOME))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException exception) {
                                throw new RuntimeException(exception);
                            }
                        });
            }
        }
        Files.createDirectories(SMOKE_HOME);
    }

    private static void createImportWorkbook() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet members = workbook.createSheet("Members");
            Row header = members.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Email");

            String[][] rows = {
                    {"Alex Johnson", "alex.johnson@example.edu"},
                    {"Brianna Lee", "brianna.lee@example.edu"},
                    {"Carlos Rivera", "carlos.rivera@example.edu"},
                    {"Dana Patel", "dana.patel@example.edu"},
                    {"Evan Smith", "evan.smith@example.edu"},
                    {"Fatima Ahmed", "fatima.ahmed@example.edu"}};

            for (int i = 0; i < rows.length; i++) {
                Row row = members.createRow(i + 1);
                row.createCell(0).setCellValue(rows[i][0]);
                row.createCell(1).setCellValue(rows[i][1]);
            }

            try (OutputStream outputStream = Files.newOutputStream(IMPORT_FILE)) {
                workbook.write(outputStream);
            }
        }
    }

    private static void seedProfilesWorkbook() throws IOException {
        ProfilesUtil.createDirectoryIfNotExists();
        ProfilesUtil.createExcelFile();
        ProfilesUtil profilesUtil = new ProfilesUtil();
        profilesUtil.saveSeason("Fall", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 14));
        profilesUtil.close();
    }

    private static void verifyExportedShifts() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(IMPORT_FILE))) {
            Sheet shifts = workbook.getSheet("Shifts");
            if (shifts == null) {
                throw new IllegalStateException("Generated workbook is missing the Shifts sheet.");
            }
            int generatedRows = shifts.getLastRowNum();
            if (generatedRows != 12) {
                throw new IllegalStateException("Expected 12 generated shift rows, found " + generatedRows + ".");
            }
            Row firstShift = shifts.getRow(1);
            if (!"Alex Johnson".equals(firstShift.getCell(0).getStringCellValue())) {
                throw new IllegalStateException("First generated shift did not belong to Alex Johnson.");
            }
        }
    }

    private static void selectMember(ComboBox<Member> memberChoiceBox, String name) {
        for (Member member : memberChoiceBox.getItems()) {
            if (member.getName().equals(name)) {
                memberChoiceBox.setValue(member);
                return;
            }
        }
        throw new IllegalArgumentException("Member not found in imported list: " + name);
    }

    @SuppressWarnings("unchecked")
    private static ComboBox<String> comboIn(Pane pane, int index) {
        return (ComboBox<String>) pane.getChildren().stream()
                .filter(ComboBox.class::isInstance)
                .skip(index)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected combo box at index " + index + " in " + pane.getClass().getSimpleName()));
    }

    private static Button findButton(Node root, String text) {
        if (root instanceof Button button && text.equals(button.getText())) {
            return button;
        }
        if (root instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                try {
                    return findButton(child, text);
                } catch (IllegalStateException ignored) {
                    // Keep looking in sibling branches.
                }
            }
        }
        throw new IllegalStateException("Could not find button: " + text);
    }

    private static Object getField(Object target, String name) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void setField(Object target, String name, Object value) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
