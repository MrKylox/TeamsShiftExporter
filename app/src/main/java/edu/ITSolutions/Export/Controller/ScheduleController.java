package edu.ITSolutions.Export.Controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ScheduleController {

    private final Workbook workbook;
    boolean updated = false;


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

        // int rowCount = sheet.getLastRowNum();
        // Row row = sheet.createRow(++rowCount);
        for(int i = 1; i < sheet.getLastRowNum(); i++){
            Row rowItem = sheet.getRow(i);
            if(rowItem != null){
                Cell rowMember = rowItem.getCell(0);
                // Cell rowEmail = rowItem.getCell(1);
                // Cell rowGroup = rowItem.getCell(2);
                Cell rowStartDate = rowItem.getCell(3);
                Cell rowStartTime = rowItem.getCell(4);
                // Cell rowEndDate = rowItem.getCell(5);
                Cell rowEndTime = rowItem.getCell(6);
                // Cell rowColor = rowItem.getCell(7);

                if(rowMember != null && rowMember.getStringCellValue().equals(member) &&
                    rowStartDate != null && rowStartDate.getStringCellValue().equals(startDate)
                    //rowStartTime != null && rowStartDate.getStringCellValue().equals(startTime)
                    // rowEndDate != null && rowEndDate.getStringCellValue().equals(endDate) 
                    // &&
                    // rowEndTime != null && rowEndTime.getStringCellValue().equals(endTime)
                    ){
                    updateRow(rowItem,member,email,group,startDate,startTime,endDate,endTime,color);
                    System.out.println("updated is now true");
                    System.out.printf("These is time:",rowStartDate,"string start",rowStartDate.getStringCellValue(),rowStartTime.getStringCellValue());
                    updated = true;
                    break;
                }
            }
        }

        if(!updated){
            Row rowItem = sheet.createRow(sheet.getLastRowNum()+1);
            updateRow(rowItem,member,email,group,startDate,startTime,endDate,endTime,color);
            System.out.println("New row created!");

            
        }
    }

    private void updateRow(Row row, String member, String email, String group, String startDate, String startTime, String endDate, String endTime, String color ){
        row.createCell(0).setCellValue(member);
        row.createCell(1).setCellValue(email);
        row.createCell(2).setCellValue(group);
        row.createCell(3).setCellValue(startDate);
        row.createCell(4).setCellValue(startTime.toString());
        row.createCell(5).setCellValue(endDate);
        row.createCell(6).setCellValue(endTime.toString());
        row.createCell(7).setCellValue(color);
        System.out.println("Updated!");
    }
}

