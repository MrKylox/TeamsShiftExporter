package edu.ITSolutions.Export.Controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ScheduleController {

    private final Workbook workbook;

    public ScheduleController(Workbook workbook) {
        this.workbook = workbook;
    }

    public void addSchedule(String member, String email, String group, String startDate, String startTime, String endDate, String endTime, String color) {
        Sheet sheet = workbook.getSheet("Shifts");
        if (sheet == null) {
            sheet = workbook.createSheet("Shifts");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Member");
            headerRow.createCell(1).setCellValue("Work Email");
            headerRow.createCell(2).setCellValue("Group");
            headerRow.createCell(3).setCellValue("Start Date");
            headerRow.createCell(4).setCellValue("Start Time");
            headerRow.createCell(5).setCellValue("End Date");
            headerRow.createCell(6).setCellValue("End Time");
            headerRow.createCell(7).setCellValue("Theme Color");
        }

        // Find the first empty row
        int rowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue(member);
        row.createCell(1).setCellValue(email);
        row.createCell(2).setCellValue(group);
        row.createCell(3).setCellValue(startDate);
        row.createCell(4).setCellValue(startTime);
        row.createCell(5).setCellValue(endDate);
        row.createCell(6).setCellValue(endTime);
        row.createCell(7).setCellValue(color);
        
    }
}
