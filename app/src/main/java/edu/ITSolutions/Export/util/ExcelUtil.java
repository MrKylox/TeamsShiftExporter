package edu.ITSolutions.Export.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream; // Missing import
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ITSolutions.Export.Member;
// import edu.ITSolutions.Export.Shift;

public class ExcelUtil {

    private final Workbook workbook;
    private final Map<String, Sheet> sheets = new HashMap<>();
    private final File originalFile;

    public ExcelUtil(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
        for (Sheet sheet : workbook) {
            sheets.put(sheet.getSheetName(), sheet);
        }
        originalFile = file;
    }

    public List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        Sheet sheet = workbook.getSheet("Members");
        if (sheet != null) {
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                members.add(new Member(name, email));
            }
        }
        return members;
    }

    public String getEmail(Row row){
        return row.getCell(1).getStringCellValue();
    }

    public Sheet getSheet(String name) {
        return sheets.get(name);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void save() throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(originalFile)) {
            workbook.write(fileOutputStream);
        }
    }

    public void close() throws IOException {
        workbook.close();
    }

    // public void applyProfileShifts(String member, List<Shift> shifts) {
    //     Sheet sheet = workbook.getSheet("Shifts");
    //     if (sheet == null) {
    //         sheet = workbook.createSheet("Shifts");
    //         Row headerRow = sheet.createRow(0);
    //         headerRow.createCell(0).setCellValue("Member");
    //         headerRow.createCell(1).setCellValue("WeekDay");
    //         headerRow.createCell(2).setCellValue("Start Date");
    //         headerRow.createCell(3).setCellValue("Start Time");
    //         headerRow.createCell(4).setCellValue("End Date");
    //         headerRow.createCell(5).setCellValue("End Time");
    //         headerRow.createCell(6).setCellValue("Group");
    //         headerRow.createCell(7).setCellValue("Theme Color");
    //     }

    //     for (Shift shift : shifts) {
    //         Row row = sheet.createRow(sheet.getLastRowNum() + 1);
    //         row.createCell(0).setCellValue(shift.getMember());
    //         row.createCell(1).setCellValue(shift.getWeekDay());
    //         row.createCell(2).setCellValue(shift.getStartDate());
    //         row.createCell(3).setCellValue(shift.getStartTime());
    //         row.createCell(4).setCellValue(shift.getEndDate());
    //         row.createCell(5).setCellValue(shift.getEndTime());
    //         row.createCell(6).setCellValue(shift.getGroup());
    //         row.createCell(7).setCellValue(shift.getColor());
    //     }
    // }

    public void clearSheetExceptHeader() {
        Sheet sheet = workbook.getSheet("Shifts");
        if (sheet != null) {
            // Start from the second row (index 1) to keep the header
            for (int i = sheet.getLastRowNum(); i > 0; i--) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    sheet.removeRow(row);
                }
            }
        }
    }
}
