package edu.ITSolutions.Export.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProfilesUtil {
    private static final String DIRECTORY_PATH = System.getProperty("user.home") + "\\ShiftExporter";
    private static final String FILE_NAME = DIRECTORY_PATH + "\\MemberProfiles.xlsx";
    private static final String MEMBER_PROFILES_SHEET = "MemberProfiles";
    private static final String SEASON_PROFILES_SHEET = "SeasonProfiles";
    private static final String[] MEMBER_HEADERS = {"Member", "WeekDay", "Start Time", "End Time"};
    private static final String[] SEASON_HEADERS = {"Season", "Start Date", "End Date"};

    public static void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static boolean doesFileExist() {
        File file = new File(FILE_NAME);
        return file.exists() && !file.isDirectory();
    }

    public static void createExcelFile() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet memberSheet = workbook.createSheet(MEMBER_PROFILES_SHEET);
            Sheet seasonSheet = workbook.createSheet(SEASON_PROFILES_SHEET);

            Row memberHeaderRow = memberSheet.createRow(0);
            for (int i = 0; i < MEMBER_HEADERS.length; i++) {
                Cell cell = memberHeaderRow.createCell(i);
                cell.setCellValue(MEMBER_HEADERS[i]);
            }

            Row seasonHeaderRow = seasonSheet.createRow(0);
            for (int i = 0; i < SEASON_HEADERS.length; i++) {
                Cell cell = seasonHeaderRow.createCell(i);
                cell.setCellValue(SEASON_HEADERS[i]);
            }

            try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace(); // Print the stack trace for debugging
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public static void readExcelFile() {
        try (FileInputStream fileIn = new FileInputStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(fileIn)) {

            Sheet memberSheet = workbook.getSheet(MEMBER_PROFILES_SHEET);
            System.out.println("Reading " + MEMBER_PROFILES_SHEET + " sheet:");
            readSheet(memberSheet);

            Sheet seasonSheet = workbook.getSheet(SEASON_PROFILES_SHEET);
            System.out.println("Reading " + SEASON_PROFILES_SHEET + " sheet:");
            readSheet(seasonSheet);

        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    private static void readSheet(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                    case NUMERIC -> System.out.print(cell.getNumericCellValue() + "\t");
                    default -> {
                    }
                }
            }
            System.out.println();
        }
    }
}
