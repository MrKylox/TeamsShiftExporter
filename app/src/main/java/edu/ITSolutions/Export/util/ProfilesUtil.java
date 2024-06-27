package edu.ITSolutions.Export.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ITSolutions.Export.Shift;

public class ProfilesUtil {
    private static final String DIRECTORY_PATH = System.getProperty("user.home") + "\\ShiftExporter";
    private static final String FILE_NAME = DIRECTORY_PATH + "\\MemberProfiles.xlsx";
    private static final String MEMBER_PROFILES_SHEET = "MemberProfiles";
    private static final String SEASON_PROFILES_SHEET = "SeasonProfiles";
    private static final String[] MEMBER_HEADERS = {"Member", "WeekDay", "Start Time", "End Time", "Group", "Color"};
    private static final String[] SEASON_HEADERS = {"Season", "Start Date", "End Date"};
    private final Workbook workbook;
    private final File profilesFile;

    public ProfilesUtil() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        this.workbook = new XSSFWorkbook(fileInputStream);
        this.profilesFile = new File(FILE_NAME);
    }

    public static void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public Workbook getWorkbook() {
        return workbook;
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

    public void saveProfile(String member, String day, String startTime, String endTime, LocalDate startDate, LocalDate endDate, String group, String color, String season) {
        Sheet memberSheet = workbook.getSheet(MEMBER_PROFILES_SHEET);
        int lastRowNum = memberSheet.getLastRowNum();
        Row row = memberSheet.createRow(lastRowNum + 1);
        row.createCell(0).setCellValue(member);
        row.createCell(1).setCellValue(day);
        row.createCell(2).setCellValue(startTime);
        row.createCell(3).setCellValue(endTime);
        row.createCell(4).setCellValue(group);
        row.createCell(5).setCellValue(color);
        
        Sheet seasonSheet = workbook.getSheet(SEASON_PROFILES_SHEET);
        Row seasonRow = seasonSheet.createRow(lastRowNum + 1);
        seasonRow.createCell(0).setCellValue(season);
        seasonRow.createCell(1).setCellValue(startDate.toString());
        seasonRow.createCell(2).setCellValue(endDate.toString());
        
        save();
    }

    public List<Shift> getProfileShifts(String member, LocalDate startDate, LocalDate endDate) {
        List<Shift> profileShifts = new ArrayList<>();
        Sheet memberSheet = workbook.getSheet(MEMBER_PROFILES_SHEET);
        if (memberSheet != null) {
            for (Row row : memberSheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String memberName = row.getCell(0).getStringCellValue();
                DayOfWeek day = DayOfWeek.valueOf(row.getCell(1).getStringCellValue());
                String startTime = row.getCell(2).getStringCellValue();
                String endTime = row.getCell(3).getStringCellValue();
                String group = row.getCell(4).getStringCellValue();
                String color = row.getCell(5).getStringCellValue();

                if (memberName.equals(member)) {
                    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                        if (date.getDayOfWeek() == day) {
                            profileShifts.add(new Shift(member, day.toString(), date.toString(), startTime, date.toString(), endTime, group, color));
                        }
                    }
                }
            }
        }
        return profileShifts;
    }

    public void save() {
        try (FileOutputStream fileOut = new FileOutputStream(profilesFile)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        workbook.close();
    }
}
