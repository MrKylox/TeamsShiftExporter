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
    private static final String[] MEMBER_HEADERS = {"Member", "WeekDay", "Start Time", "End Time", "Position", "Season"};
    private static final String[] SEASON_HEADERS = {"Season", "Start Date", "End Date"};
    private final Workbook workbook;
    private final File profilesFile;
    private static final String[] seasonList = {"Fall", "Winter", "Spring", "Summer"};
    private int receivedSeason = -1;
    private String startEndDate;

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

            for (int i = 0; i < seasonList.length; i++){
                Row seasonRow = seasonSheet.createRow(i+1);
                Cell season = seasonRow.createCell(0);
                Cell start = seasonRow.createCell(1);
                Cell end = seasonRow.createCell(2);
                season.setCellValue(seasonList[i]);
                start.setCellValue("Not Set");
                end.setCellValue("Not Set");
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

    public void saveProfile(String member, String day, String startTime, String endTime, String position, String season) {
        Sheet memberSheet = workbook.getSheet(MEMBER_PROFILES_SHEET);
        int lastRowNum = memberSheet.getLastRowNum();
        Row row = memberSheet.createRow(lastRowNum + 1);
        row.createCell(0).setCellValue(member);
        row.createCell(1).setCellValue(day);
        row.createCell(2).setCellValue(startTime);
        row.createCell(3).setCellValue(endTime);
        row.createCell(4).setCellValue(position);
        row.createCell(5).setCellValue(season);
        save();
    }

    public void saveSeason(String season, LocalDate startDate, LocalDate endDate){
        Sheet seasonSheet = workbook.getSheet(SEASON_PROFILES_SHEET);
        for (int i = 0; i < seasonList.length; i++) {
            if (seasonList[i].equals(season)) {
                receivedSeason = i + 1;
                break;
            }
        }
        if (receivedSeason != -1) {
            Row seasonRow = seasonSheet.getRow(receivedSeason);
            seasonRow.getCell(0).setCellValue(season);
            seasonRow.getCell(1).setCellValue(startDate.toString());
            seasonRow.getCell(2).setCellValue(endDate.toString());
        }
        save();
    }


    public List<String> getSeasonDates(String season) {
        Sheet seasonSheet = workbook.getSheet(SEASON_PROFILES_SHEET);
        List<String> seasonDate = new ArrayList<>();
        for (int i = 0; i < seasonList.length; i++) {
            if (seasonList[i].equals(season)) {
                receivedSeason = i + 1;
                break;
            }
        }
        if (receivedSeason == -1) {
            return seasonDate;  // Return empty if season not found
        }
        Row dateRow = seasonSheet.getRow(receivedSeason);
        if (dateRow != null) {
            String start = dateRow.getCell(1).getStringCellValue();
            String end = dateRow.getCell(2).getStringCellValue();
            seasonDate.add(start);
            seasonDate.add(end);
        }
        return seasonDate;
    }

    public List<Shift> getProfileShifts(String member) {
        List<Shift> profileShifts = new ArrayList<>(); //Create a list of object Shift
        Sheet memberSheet = workbook.getSheet(MEMBER_PROFILES_SHEET); // Get the sheet called member profile
        if (memberSheet != null) {
            for (Row row : memberSheet) { // for each row in memberSheet
                if (row.getRowNum() == 0) continue; // Skip header row
                String memberName = row.getCell(0).getStringCellValue(); //get the memberName 
                DayOfWeek day = DayOfWeek.valueOf(row.getCell(1).getStringCellValue().toUpperCase()); // get day
                String startTime = row.getCell(2).getStringCellValue(); // get start time
                String endTime = row.getCell(3).getStringCellValue();// get endtime
                String position = row.getCell(4).getStringCellValue(); // get position
                String season = row.getCell(5).getStringCellValue();

                if (memberName.equals(member)) { //Makes sure we're updating the correct member
                    profileShifts.add(new Shift(member, day.toString(), startTime, endTime, position, season)); //add to shift
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

    // private String getStartEndDate(String season){

    //     return 
    // }

}
