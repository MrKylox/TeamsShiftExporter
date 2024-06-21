package edu.ITSolutions.Export.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import edu.ITSolutions.Export.Shift;

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

    public List<Shift> getShifts(Member member){
        List<Shift> shifts = new ArrayList<>();
        Sheet sheet = workbook.getSheet("Shifts");
        if(sheet != null) {
            for (Row row: sheet){
                if(row.getRowNum() == 0) continue; //Skip header row
                String memberReceived = row.getCell(0).getStringCellValue();
                if (memberReceived.equals(member.getName())){
                    String email = row.getCell(1).getStringCellValue();
                    String group = row.getCell(2).getStringCellValue();
                    String startDate = row.getCell(3).getStringCellValue();
                    String startTime = row.getCell(4).getStringCellValue();
                    String endDate = row.getCell(5).getStringCellValue();
                    String endTime = row.getCell(6).getStringCellValue();
                    String themeColor = row.getCell(7).getStringCellValue();
                    shifts.add(new Shift(startDate, startTime, endDate, endTime, group, themeColor, memberReceived));
                }
            }
        }
        return shifts;
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
}
